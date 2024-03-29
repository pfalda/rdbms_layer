import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.disbrain.akkatemplate.messages.DummyOutputReply;
import com.disbrain.dbmslayer.DbmsLayer;
import com.disbrain.dbmslayer.DbmsLayerProvider;
import com.disbrain.dbmslayer.descriptors.RequestModes;
import com.disbrain.dbmslayer.exceptions.DbmsException;
import com.disbrain.dbmslayer.messages.QueryRequest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;

public class MainTest {

    public static abstract class ResultCheck {
        Future<Object> arg;

        public ResultCheck(Future<Object> arg) {
            this.arg = arg;
        }

        public abstract void validate(Object obj);

    }

    private static ActorSystem your_app_actorsystem = null;

    @BeforeClass
    public static void setup() throws Exception {
        Config user_cfg = ConfigFactory.load();


        your_app_actorsystem = akka.actor.ActorSystem.create("YourAppAS", user_cfg);


    }

    @AfterClass
    public static void teardown() {
        if (your_app_actorsystem != null)
            your_app_actorsystem.shutdown();
    }

    @Test
    public void testQueries() throws Exception {

        Future<Object> generic_reply = null;
        Object generic_response = null;
        ArrayList<ResultCheck> output_storage = new ArrayList<ResultCheck>();
        int storage_size = 0;
        DbmsLayerProvider db_layer = DbmsLayer.DbmsLayerProvider.get(your_app_actorsystem);

        //ActorRef    actor = your_app_actorsystem.actorOf(Props.create(SimpleTestActor.class));

        /* Here we try a simple request to the dbs trough a future */

        generic_reply = Patterns.ask(db_layer.getQueriesBroker(),
                new QueryRequest("INSERT INTO Activities(Activity_Name) VALUES (?);", //SQL query
                        new RequestModes(RequestModes.RequestTypology.WRITE),
                        DummyOutputReply.class,
                        true,
                        new Object[]{"Now is: " + Calendar.getInstance().getTimeInMillis()}
                ),
                4096000);

        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply)
                    return;
                fail();
            }
        }); // generic_reply);


        generic_reply = Patterns.ask(db_layer.getQueriesBroker(), //is outside from an actor context, we use this broker to communicate with dbms querying system
                new QueryRequest("SELECT COUNT(*) FROM Activities;", //SQL query
                        new RequestModes(RequestModes.RequestTypology.READ_ONLY), //Query typology
                        DummyOutputReply.class, //Output object. We use its constructor to properly decode output
                        true //autocommit?
                ),
                4096000);
        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply) {
                    DummyOutputReply res = (DummyOutputReply) obj;
                    if ((res.output != null) && (res.output.length > 0)) {
                        assertTrue(((Long) res.output[0]).longValue() > 0);
                        return;
                    }
                }
                fail();
            }
        });

        generic_reply = Patterns.ask(db_layer.getQueriesBroker(),
                new QueryRequest("INSERT INTO Tags(Tag_Name) VALUES (?);", //SQL query
                        new RequestModes(RequestModes.RequestTypology.WRITE),
                        DummyOutputReply.class,
                        true,
                        new Object[]{"Computer Science"}
                ),
                4096000);
        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply)
                    return;
                if (obj instanceof DbmsException) {
                    DbmsException error = (DbmsException) obj;
                    if (error.getMessage().contains("uplicate"))
                        return;
                }
                fail();
            }
        });

        generic_reply = Patterns.ask(db_layer.getQueriesBroker(),
                new QueryRequest("INSERT INTO Languages(Lang_Name) VALUES (\"DE\");", //SQL query
                        new RequestModes(RequestModes.RequestTypology.WRITE),
                        DummyOutputReply.class,
                        false //automatic rollback on close wo commit
                ),
                4096000);
        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply)
                    return;
                fail();
            }
        });

        generic_reply = Patterns.ask(db_layer.getQueriesBroker(),
                new QueryRequest("INSERT INTO Languages(Lang_Name) VALUES (\"IT\");", //SQL query
                        new RequestModes(RequestModes.RequestTypology.WRITE),
                        DummyOutputReply.class,
                        true),
                4096000);
        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply)
                    return;
                if (obj instanceof DbmsException) {
                    DbmsException error = (DbmsException) obj;
                    if (error.getMessage().contains("uplicate"))
                        return;
                }
                fail();
            }
        });

        generic_reply = Patterns.ask(db_layer.getQueriesBroker(),
                new QueryRequest("SELECT COUNT(Lang_Id) FROM Languages;", //SQL query
                        new RequestModes(RequestModes.RequestTypology.READ_ONLY),
                        DummyOutputReply.class,
                        true),
                4096000);
        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply) {
                    DummyOutputReply res = (DummyOutputReply) obj;
                    if ((res.output != null) && (res.output.length > 0)) {
                        assertEquals(((Long) res.output[0]).longValue(), 1);
                        return;
                    }
                }
                fail();
            }
        });

        generic_reply = Patterns.ask(db_layer.getQueriesBroker(),
                new QueryRequest("SELECT COUNT(Tag_Id) FROM Tags;", //SQL query
                        new RequestModes(RequestModes.RequestTypology.READ_ONLY),
                        DummyOutputReply.class,
                        true),
                4096000);
        output_storage.add(new ResultCheck(generic_reply) {
            public void validate(Object obj) {
                if (obj instanceof DummyOutputReply) {
                    DummyOutputReply res = (DummyOutputReply) obj;
                    if ((res.output != null) && (res.output.length > 0)) {
                        assertEquals(((Long) res.output[0]).longValue(), 1);
                        return;
                    }
                }
                fail();
            }
        });



        /*
        generic_reply = Patterns.ask(   db_layer.getQueriesBroker(),
                                        new QueryRequest("SELECT GET_LOCK(321,-1);", //SQL query
                                                         new RequestModes(RequestModes.RequestTypology.READ_WRITE,
                                                                          RequestModes.RequestBehaviour.RESOURCE_GETTER //locking operation, use dedicate dispatcher
                                                         ),
                                                         DummyOutputReply.class,
                                                         false
                                        ),
                                        4096000);
        output_storage.add(generic_reply);

        generic_reply = Patterns.ask(   db_layer.getQueriesBroker(),
                                        new QueryRequest("SELECT RELEASE_LOCK(321);", //SQL query
                                                         new RequestModes(RequestModes.RequestTypology.READ_WRITE,
                                                                          RequestModes.RequestBehaviour.RESOURCE_RELEASER //unlocking operation, use resource releasing dedicate dispatcher
                                                         ),
                                                         DummyOutputReply.class,
                                                         false
                                        ),
                                        4096000);
        output_storage.add(generic_reply);


        // To exploit the full power and flexibility of the dbms layer we must use if from within an actor
        generic_reply = Patterns.ask(actor,Messages.TestRequest.newBuilder().setMessage("Who's there?").build(),4096000);

        output_storage.add(generic_reply);

        */


        storage_size = output_storage.size();

        for (int cur_elem = 0; cur_elem < storage_size; cur_elem++) {
            ResultCheck obj = output_storage.remove(0);

            obj.validate(Await.result(obj.arg, (new Timeout(Duration.create(4096, "seconds"))).duration()));

            /*
            if(generic_response instanceof Messages.TestReply)
            {
                Messages.TestReply reply = (Messages.TestReply)generic_response;
                System.out.println("Return code:"+reply.getReturnCode());
                if(reply.getReturnCode() == 0)
                    for(Long element : reply.getOutDataList())
                    System.out.println(String.format("\t%d",element.longValue()));
                else
                    System.out.println("Error found: "+reply.getReturnMsg());
            }
            */
        }

        System.out.println("Simple test finished!");

    }


}
