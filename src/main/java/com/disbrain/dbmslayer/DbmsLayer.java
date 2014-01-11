package com.disbrain.dbmslayer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import com.disbrain.dbmslayer.actors.brokers.AsyncTasksBroker;
import com.disbrain.dbmslayer.actors.brokers.ConnectionsBroker;
import com.disbrain.dbmslayer.actors.brokers.DBMSQueriesBroker;
import com.disbrain.dbmslayer.actors.brokers.DeadLetterBroker;
import com.disbrain.dbmslayer.net.DbmsConnectionPool;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 19/12/13
 * Time: 15.36
 * To change this template use File | Settings | File Templates.
 */
public class DbmsLayer {
    private static ActorSystem  system = null;
    private static ActorRef     async_get_tasks_broker = null;
    private static ActorRef     async_release_tasks_broker = null;
    private static ActorRef     async_whatever_tasks_broker = null;
    private static ActorRef     connections_broker = null;
    private static ActorRef     close_connections_broker = null;
    private static ActorRef     dead_letter_handler = null;
    private static ActorRef     dbms_queries_broker = null;

    public enum DeathPolicy { SUICIDE, SURVIVE };

    private DbmsLayer()
    {

    }

    public static ActorRef getAsyncGetTasksBroker()
    {
        return async_get_tasks_broker;
    }

    public static ActorRef getAsyncReleaseTasksBroker()
    {
        return async_release_tasks_broker;
    }

    public static ActorRef getAsyncWhateverTasksBroker()
    {
        return async_whatever_tasks_broker;
    }

    public static ActorRef getConnectionsBroker()
    {
        return connections_broker;
    }

    public static ActorRef getCloseConnectionsBroker()
    {
        return close_connections_broker;
    }

    public static ActorRef getQueriesBroker()
    {
        return dbms_queries_broker;
    }

    public static synchronized ActorSystem getActorSystem(DbmsConnectionPool pool)
    {
        if (system == null)
        {
            Config config = ConfigFactory.load();
            system = akka.actor.ActorSystem.create("DbmsLayer",config.getConfig("DbmsLayer").withFallback(config) );
            async_get_tasks_broker = system.actorOf(Props.create(AsyncTasksBroker.class).withDispatcher("akka.actor.async-get-broker-dispatcher"), "ASYNC_GET_TASK_BROKER");
            async_release_tasks_broker = system.actorOf(Props.create(AsyncTasksBroker.class).withDispatcher("akka.actor.async-release-broker-dispatcher"),"ASYNC_RELEASE_TASK_BROKER");
            async_whatever_tasks_broker = system.actorOf(Props.create(AsyncTasksBroker.class).withDispatcher("akka.actor.async-whatever-broker-dispatcher"),"ASYNC_WHATEVER_TASK_BROKER");
            connections_broker = system.actorOf(Props.create(ConnectionsBroker.class,pool).withDispatcher("akka.actor.connections-dispatcher"), "CONNECTIONS_BROKER");
            close_connections_broker = system.actorOf(Props.create(ConnectionsBroker.class).withDispatcher("akka.actor.close-connections-dispatcher"),"CLOSE_CONNECTIONS_BROKER");
            dbms_queries_broker = system.actorOf(Props.create(DBMSQueriesBroker.class).withDispatcher("akka.actor.dbms-queries-dispatcher"),"QUERIES_BROKER");
            dead_letter_handler = system.actorOf(Props.create(DeadLetterBroker.class,getCloseConnectionsBroker()).withDispatcher("akka.actor.deadletters-dispatcher"),"DEADLETTER_HANDLER");
            system.eventStream().subscribe(dead_letter_handler, DeadLetter.class);
        }
        return system;
    }

}
