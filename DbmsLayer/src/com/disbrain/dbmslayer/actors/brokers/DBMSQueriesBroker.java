package com.disbrain.dbmslayer.actors.brokers;

import akka.actor.UntypedActor;
import com.disbrain.dbmslayer.DbmsLayerProvider;
import com.disbrain.dbmslayer.DbmsQuery;
import com.disbrain.dbmslayer.descriptors.QueryGenericArgument;
import com.disbrain.dbmslayer.messages.QueryRequest;


public class DBMSQueriesBroker extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof QueryRequest) {
            QueryRequest request = (QueryRequest) message;
            DbmsQuery.create_generic_fsm(getContext(),
                    new QueryGenericArgument(getSender(),
                            DbmsLayerProvider.DeathPolicy.SUICIDE,
                            request.query,
                            request.modes,
                            request.autocommit,
                            request.reply_type,
                            request.args)
            );
        }
    }
}
