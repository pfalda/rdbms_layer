package com.disbrain.dbmslayer.messages;

import com.disbrain.dbmslayer.descriptors.RequestModes;

import java.sql.PreparedStatement;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.32
 * To change this template use File | Settings | File Templates.
 */
public class DbmsExecutePrepStmtRequest {
    public PreparedStatement stmt;
    public RequestModes request_opts;
    public boolean autocommit = false;

    public DbmsExecutePrepStmtRequest(PreparedStatement stmt, RequestModes opts, boolean autocommit)
    {
        this.stmt = stmt;
        this.request_opts = opts;
        this.autocommit = autocommit;

    }
}
