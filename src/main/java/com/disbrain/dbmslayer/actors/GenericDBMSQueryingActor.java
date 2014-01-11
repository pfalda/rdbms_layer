package com.disbrain.dbmslayer.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.disbrain.dbmslayer.DbmsLayer;
import com.disbrain.dbmslayer.descriptors.QueryGenericArgument;
import com.disbrain.dbmslayer.descriptors.RequestModes;
import com.disbrain.dbmslayer.exceptions.DbmsException;
import com.disbrain.dbmslayer.exceptions.DbmsLayerError;
import com.disbrain.dbmslayer.exceptions.DbmsOutputFormatError;
import com.disbrain.dbmslayer.exceptions.DbmsRemoteDbError;
import com.disbrain.dbmslayer.messages.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 17.16
 * To change this template use File | Settings | File Templates.
 */
public class GenericDBMSQueryingActor extends UntypedActor {

    private static AtomicInteger replies = new AtomicInteger(0);
    private String query;
    private boolean prepared = false;
    private Vector<Object> values;
    private ActorRef dbms_actor;
    private ActorRef    real_requester;
    //private Statement   stmt = null;
    //private PreparedStatement p_stmt = null;
    private Class<?> rep_type;
    private RequestModes request_properties = null;
    private boolean autocommit = false;
    private DbmsLayer.DeathPolicy policy;

    private int rows_num;
    private int lines_num = 1;

    private void closeResult(ResultSet result)
    {
        try {
            result.close();
        }catch(SQLException ex)
        {
            System.err.println("Errore chiudendo il result?!");//TODO Loggare l'eccezione!
        }
    }

    public GenericDBMSQueryingActor(QueryGenericArgument gen_arg)
    {
        prepared = false;
        this.query = gen_arg.query;
        this.rep_type = gen_arg.reply_type;
        this.request_properties = gen_arg.request_properties;
        this.autocommit = gen_arg.autocommit;
        this.policy = gen_arg.deathPolicy;
        this.real_requester = gen_arg.real_requester;

        if (gen_arg.arg_array != null)
        {

            prepared = true;
            values = new Vector<Object>();
            for(Object value: gen_arg.arg_array)
            {
                values.add(value);
            }
        }

    }

    private void start_fsm()
    {
        Object request;

        //System.err.println("GOING TO EXECUTE: ["+query+"]");

        if(prepared == true)
            request = new GetDbmsPreparedStatementRequest(query, request_properties);
        else
            request = new GetDbmsStatementRequest(request_properties);

        dbms_actor.tell(request,getSelf());
    }

    private int fetch_reply_struct()
    {
        try {

            rows_num = rep_type.getField("out_columns_num").getInt(null);

        }catch(NoSuchFieldException | IllegalAccessException ex)
        {
            real_requester.tell(new DbmsLayerError(ex), ActorRef.noSender());
            return (-1);
        }

        try {
            lines_num = rep_type.getField("out_lines_num").getInt(null);
        }catch(NoSuchFieldException | IllegalAccessException ex)
        {

        }
        return (0);
    }

    @Override
    public void preStart()
    {

        if (fetch_reply_struct() == 0)
        {

            dbms_actor = getContext().actorOf(Props.create(DBMSWorker.class),"DBMSWORKER");
            //real_requester = getContext().parent();

            start_fsm();

        }
    }

    public void reincarnate(QueryGenericArgument gen_arg)
    {
        //closeStatement();
        dbms_actor.tell(new DbmsWorkerCmdRequest(DbmsWorkerCmdRequest.Command.CLOSE_STMT),getSelf());
        prepared = false;
        this.policy = gen_arg.deathPolicy;
        this.autocommit = gen_arg.autocommit;
        this.query = gen_arg.query;
        this.rep_type = gen_arg.reply_type;
        this.request_properties = gen_arg.request_properties;
        this.real_requester = gen_arg.real_requester;
        if (gen_arg.real_requester == null)
            this.real_requester = getContext().parent();

        if(gen_arg.async_request == true)
        {
            /* This is not a request born to be async, so ASYNC_WHATEVER dispatcher is ok */
            switch(this.request_properties.typology)
            {
                case READ_ONLY:
                    this.request_properties.typology = RequestModes.RequestTypology.ASYNC_READ_ONLY;
                    break;
                case READ_WRITE:
                    this.request_properties.typology = RequestModes.RequestTypology.ASYNC_READ_WRITE;
                    break;
                case WRITE:
                    this.request_properties.typology = RequestModes.RequestTypology.ASYNC_WRITE;
                    break;
            }
        }

        if (values != null)
            values.clear();
        if (gen_arg.arg_array != null)
        {
            prepared = true;
            values = new Vector<Object>();
            for(Object value: gen_arg.arg_array)
            {
                values.add(value);
            }
        }
        if (fetch_reply_struct() == 0)
            start_fsm();
    }

    @Override
    public void onReceive(Object message)
    {

        do {

            //System.err.println("GENERIC MESSAGE: "+message.getClass());

            if (message instanceof QueryGenericArgument)
            {
                reincarnate((QueryGenericArgument)message);
                break;
            }

            if(message instanceof GetDbmsStatementReply)
            {
                Statement stmt = ((GetDbmsStatementReply)message).stmt;
                getSender().tell(new DbmsExecuteStmtRequest(stmt, query, request_properties,autocommit), getSelf());
                break;
            }

            if(message instanceof GetDbmsPreparedStatementReply)
            {
                PreparedStatement p_stmt = ((GetDbmsPreparedStatementReply) message).p_stmt;

                try {

                    for(int param_idx = 0; param_idx < values.size() ; param_idx++)
                        p_stmt.setObject(param_idx+1,values.get(param_idx));

                    getSender().tell(new DbmsExecutePrepStmtRequest(p_stmt, request_properties,autocommit), getSelf());

                }catch(SQLException ex)
                {
                    //closeStatement();
                    real_requester.tell(new DbmsLayerError(ex), getSelf());
                    if(policy == DbmsLayer.DeathPolicy.SUICIDE)
                        getContext().stop(getSelf());
                }

                break;
            }

            if(message instanceof DBMSReply)
            {
                boolean has_result = false;
                DBMSReply dbms_result = (DBMSReply)message;
                ResultSet result = dbms_result.resultSet;
                Object  output_obj = null,
                        output_data= null;
                Vector<Object> result_vector = new Vector<>();


                //System.err.println("GOT " + (replies.incrementAndGet()) + " FROM THE WORKER");
                try {

                    switch(dbms_result.request_mode)
                    {
                        case WRITE:
                            output_data = dbms_result.ddl_retval;
                            break;
                        case READ_WRITE:
                        case READ_ONLY:
                            output_data = null;
                    }


                    if(result != null)
                    {

                        for(int cur_line = 0; cur_line < lines_num; cur_line++)
                        {
                            if(result.next())
                            {
                                for(int cur_row = 0; cur_row < rows_num; cur_row++)
                                    result_vector.add(result.getObject(cur_row+1));
                            }
                            else
                                break;
                        }

                    }

                    output_obj = rep_type.getConstructor(Object[].class).newInstance(new Object[] {result_vector.toArray()});


                }catch(SQLException ex)
                {
                    if(has_result == false)
                        output_obj = new DbmsRemoteDbError(ex);
                    else
                        output_obj = new DbmsOutputFormatError(ex);
                }catch(Exception ex)//(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex)
                {
                    output_obj = new DbmsLayerError(ex," INPUT CLASS: "+rep_type.getName()+" OUT_DATA ARG LEN: "+result_vector.size());
                }
                finally {
                    if(result != null)
                        closeResult(result);

                    real_requester.tell(output_obj, getSelf());
                    if(policy == DbmsLayer.DeathPolicy.SUICIDE)
                        getContext().stop(getSelf());

                    break;
                }
            }

            if(message instanceof DbmsWorkerCmdRequest)
            {
                dbms_actor.forward(message,getContext());
                //dbms_actor.tell(message,getSelf());
                break;
            }

            if(message instanceof DbmsWorkerCmdReply)
            {
                DbmsWorkerCmdReply reply = (DbmsWorkerCmdReply) message;

                if(reply.originalRequest != DbmsWorkerCmdRequest.Command.CLOSE_STMT)
                    System.err.println("WTF HERE!?!?!?!?!?!?!?!?!?");
                //real_requester.tell(message,getSelf());
                break;
            }

            if (message instanceof DbmsException)
            {
                real_requester.tell(message,getSelf());
                if(policy == DbmsLayer.DeathPolicy.SUICIDE)
                    getContext().stop(getSelf());
                break;
            }

            real_requester.tell(new DbmsLayerError(message.getClass().getName() + " UNHANDLED RECEIVED"),getSelf());
            if(policy == DbmsLayer.DeathPolicy.SUICIDE)
                getContext().stop(getSelf());

        }while(false);

    }

}
