package com.disbrain.dbmslayer.messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.18
 * To change this template use File | Settings | File Templates.
 */
public class GetDbmsPreparedStatementReply {
    public PreparedStatement p_stmt;
    public GetDbmsPreparedStatementReply(Connection conn, GetDbmsPreparedStatementRequest request) throws SQLException {
        if(request.properties == null)
            p_stmt = conn.prepareStatement(request.query);
        else
            p_stmt = conn.prepareStatement(request.query,request.properties.resultSetType,request.properties.resultSetConcurrency,request.properties.resultSetHoldability);
    }
}