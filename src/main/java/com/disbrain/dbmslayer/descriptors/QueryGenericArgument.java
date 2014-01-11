package com.disbrain.dbmslayer.descriptors;

import akka.actor.ActorRef;
import com.disbrain.dbmslayer.DbmsLayer;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 17.32
 * To change this template use File | Settings | File Templates.
 */
public class QueryGenericArgument {


    public String       query;
    public RequestModes request_properties;
    public Class<?>     reply_type;
    public Object       arg_array[] = null;
    public boolean      autocommit = false;
    public ActorRef     real_requester = null;
    public boolean      async_request = false;
    public DbmsLayer.DeathPolicy deathPolicy = DbmsLayer.DeathPolicy.SURVIVE;

    public QueryGenericArgument(ActorRef father,DbmsLayer.DeathPolicy policy, String query, RequestModes request_properties, boolean autocommit, Class<?> reply_type, Object[] arg_array)
    {
        this.query = query;
        this.deathPolicy = policy;
        this.request_properties = request_properties;
        this.reply_type = reply_type;
        this.arg_array = arg_array;
        this.autocommit = autocommit;
        this.real_requester = father;
    }


    public QueryGenericArgument(ActorRef father, String query, RequestModes request_properties, boolean autocommit, Class<?> reply_type, Object[] arg_array)
    {
        this.query = query;
        this.request_properties = request_properties;
        this.reply_type = reply_type;
        this.arg_array = arg_array;
        this.autocommit = autocommit;
        this.real_requester = father;
    }

    public QueryGenericArgument(ActorRef father, String query, RequestModes request_properties, boolean autocommit, Class<?> reply_type)
    {
        this.query = query;
        this.request_properties = request_properties;
        this.reply_type = reply_type;

        this.autocommit = autocommit;
        this.real_requester = father;
    }

    public QueryGenericArgument(ActorRef father, String query, RequestModes request_properties,Class<?> reply_type, Object[] arg_array)
    {
        this.query = query;
        this.request_properties = request_properties;
        this.reply_type = reply_type;
        this.arg_array = arg_array;
        this.real_requester = father;
    }

    public QueryGenericArgument(ActorRef father, String query, RequestModes request_properties,Class<?> reply_type)
    {
        this.query = query;
        this.request_properties = request_properties;
        this.reply_type = reply_type;

        this.real_requester = father;
    }



}