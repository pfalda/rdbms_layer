package com.disbrain.dbmslayer.actors.brokers;

import akka.actor.UntypedActor;
import com.disbrain.dbmslayer.exceptions.DbmsConnectionPoolError;
import com.disbrain.dbmslayer.messages.CloseDbmsConnectionRequest;
import com.disbrain.dbmslayer.messages.GetDbmsConnectionReply;
import com.disbrain.dbmslayer.messages.GetDbmsConnectionRequest;
import com.disbrain.dbmslayer.net.DbmsConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 12.58
 * To change this template use File | Settings | File Templates.
 */

public class ConnectionsBroker extends UntypedActor {

    private volatile DbmsConnectionPool connection_pool = null;

    private static volatile AtomicInteger connections_taken = new AtomicInteger(0);

    private DbmsConnectionPoolError error = new DbmsConnectionPoolError();

    public static int getBrokerStats()
    {
        return connections_taken.get();
    }

    public ConnectionsBroker(DbmsConnectionPool pool)
    {
        connection_pool = pool;
    }

    public ConnectionsBroker()
    {

    }

    @Override
    public void onReceive(Object message)
    {
        do {
            if (message instanceof GetDbmsConnectionRequest)
            {

                Object output = null;

                try {
                    output = new GetDbmsConnectionReply(connection_pool.getConnection());
                    connections_taken.incrementAndGet();
                }
                catch(SQLException exc)
                {
                    output = new DbmsConnectionPoolError(exc,connection_pool.getStatistics());
                }
                getSender().tell(output, getSelf());

                break;
            }

            if (message instanceof CloseDbmsConnectionRequest)
            {

                Connection close_this = ((CloseDbmsConnectionRequest)message).connection;

                try {
                    if (close_this.isClosed())
                    {
                        System.err.println("CANNOT CLOSE OR ROLLBACK A CLOSED CONNECTION!");
                        return;
                    }
                }catch(SQLException exc)
                {
                    System.err.println("ERROR CHECKING CONNECTION STATUS, TRYING TO KEEP GOING: "+exc);
                }
                try {
                    if(close_this.getAutoCommit() == false)
                        close_this.rollback();
                }catch (SQLException exc)
                {
                    System.err.println("ERROR ROLLBACKING A CONNECTION, TRYING TO KEEP GOING: "+exc);
                }
                try {
                    close_this.close();
                    connections_taken.decrementAndGet();
                }catch(SQLException exc)
                {
                    System.err.println("CRITICAL ERROR CLOSING A CONNECTION!: "+exc);
                }
                break;
            }

            getSender().tell(new DbmsConnectionPoolError("Unkown message: "+message.getClass()),getSelf());

        }while(false);


    }
}
