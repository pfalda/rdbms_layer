package com.disbrain.dbmslayer.actors.brokers;

import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import com.disbrain.dbmslayer.exceptions.DbmsException;
import com.disbrain.dbmslayer.messages.CloseDbmsConnectionRequest;
import com.disbrain.dbmslayer.messages.GetDbmsConnectionReply;
import com.disbrain.dbmslayer.messages.GetDbmsPreparedStatementReply;
import com.disbrain.dbmslayer.messages.GetDbmsStatementReply;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.00
 * To change this template use File | Settings | File Templates.
 */
public class DeadLetterBroker extends UntypedActor {


        private volatile ActorRef connection_broker;

        public DeadLetterBroker(ActorRef connection_broker)
        {
            this.connection_broker = connection_broker;
        }

        @Override
        public void onReceive(Object message)
        {
            do {


                if(message instanceof DeadLetter)
                {
                    DeadLetter dead_msg = (DeadLetter)message;
                    Object real_msg;
                    real_msg = dead_msg.message();

                    if(real_msg instanceof GetDbmsConnectionReply)
                    {
                        //System.err.println("Fixing CP shit...");
                        connection_broker.tell(new CloseDbmsConnectionRequest(((GetDbmsConnectionReply) real_msg).connection), ActorRef.noSender());
                        return;
                    }

                    try {
                        if(real_msg instanceof GetDbmsStatementReply)
                        {
                            GetDbmsStatementReply reply = (GetDbmsStatementReply) real_msg;

                            reply.stmt.close();
                            return;
                        }
                        if(real_msg instanceof GetDbmsPreparedStatementReply)
                        {
                            GetDbmsPreparedStatementReply reply = (GetDbmsPreparedStatementReply) real_msg;
                            //System.err.println("Fixing CP PSTMT shit...");
                            reply.p_stmt.close();
                            return;
                        }
                    }catch(SQLException exc)
                    {
                        System.err.println("ERROR TRYING TO FIX CP SHIT: "+exc);
                        return;
                    }
                    //System.err.println("Unhandled "+real_msg.getClass());
                    break;
                }

                if(message instanceof DbmsException)
                {
                    DbmsException msg = (DbmsException)message;
                    System.err.println("ERROR IN DEAD LETTER DISPATCHER!!!\nError code: "+msg.getErrorCode()+"\nMessage: "+msg.getMessage());
                    break;
                }

            }while(false);
        }

}
