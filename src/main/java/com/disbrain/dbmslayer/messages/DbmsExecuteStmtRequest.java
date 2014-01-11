package com.disbrain.dbmslayer.messages;

import com.disbrain.dbmslayer.descriptors.RequestModes;

import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.34
 * To change this template use File | Settings | File Templates.
 */
public class DbmsExecuteStmtRequest {
    public Statement stmt;
    public String    query;
    public RequestModes request_opts;
    public boolean autocommit = false;

    public DbmsExecuteStmtRequest(Statement stmt, String query, RequestModes opts)
    {
        this.stmt = stmt;
        this.query = query;
        this.request_opts = opts;

    }
    public DbmsExecuteStmtRequest(Statement stmt, String query, RequestModes opts, boolean autocommit)
    {
        this.stmt = stmt;
        this.query = query;
        this.request_opts = opts;
        this.autocommit = autocommit;

    }
}
