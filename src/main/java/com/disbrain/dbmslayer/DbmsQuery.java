package com.disbrain.dbmslayer;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.disbrain.dbmslayer.actors.GenericDBMSQueryingActor;
import com.disbrain.dbmslayer.descriptors.QueryGenericArgument;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 17.48
 * To change this template use File | Settings | File Templates.
 */
public class DbmsQuery {

    public static ActorRef create_generic_fsm(ActorContext ctx, QueryGenericArgument request, String description)
    {
        return ctx.actorOf(Props.create(GenericDBMSQueryingActor.class, request),description);

    }

    public static ActorRef create_generic_fsm(ActorContext ctx, QueryGenericArgument request)
    {
        return ctx.actorOf(Props.create(GenericDBMSQueryingActor.class, request));

    }

    public static ActorRef reuse_fsm(ActorRef target,QueryGenericArgument new_command)
    {
        target.tell(new_command,ActorRef.noSender());
        return (target);
    }

    public static ActorRef async_reuse_fsm(ActorRef target,QueryGenericArgument new_command)
    {
        new_command.async_request = true;
        target.tell(new_command,ActorRef.noSender());
        return (target);
    }



}
