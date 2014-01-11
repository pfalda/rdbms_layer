package com.disbrain.dbmslayer.messages;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.15
 * To change this template use File | Settings | File Templates.
 */
public class GetDbmsStatementReply {
    public Statement stmt = null;

    public GetDbmsStatementReply(Connection conn, GetDbmsStatementRequest request) throws SQLException {
        if(request.properties == null) {
            this.stmt = conn.createStatement();
        }
        else {
            this.stmt = conn.createStatement(request.properties.resultSetType,request.properties.resultSetConcurrency,request.properties.resultSetHoldability);
        }
    }
}
