package com.disbrain.akkatemplate.actors;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 02/01/14
 * Time: 11.34
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTestActor extends UntypedActor {

    @Override
    public void onReceive(Object message)
    {
        getContext().actorOf(Props.create(SimpleTestWorker.class,getSender(),message));
    }
}
