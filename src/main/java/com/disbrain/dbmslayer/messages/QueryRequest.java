package com.disbrain.dbmslayer.messages;

import com.disbrain.dbmslayer.descriptors.RequestModes;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 30/12/13
 * Time: 15.07
 * To change this template use File | Settings | File Templates.
 */
public class QueryRequest {
    public String query;
    public RequestModes modes;
    public Class<?> reply_type;
    public boolean autocommit;
    public Object[] args = null;

    public QueryRequest(String query, RequestModes modes, Class<?> reply_type, boolean autocommit, Object[] args)
    {
        this.query = query;
        this.modes = modes;
        this.reply_type = reply_type;
        this.autocommit = autocommit;
        this.args = args;
    }

    public QueryRequest(String query, RequestModes modes, Class<?> reply_type, boolean autocommit)
    {
        this.query = query;
        this.modes = modes;
        this.reply_type = reply_type;
        this.autocommit = autocommit;
    }

}
