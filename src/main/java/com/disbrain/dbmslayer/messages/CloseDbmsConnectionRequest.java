package com.disbrain.dbmslayer.messages;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 14.40
 * To change this template use File | Settings | File Templates.
 */
public class CloseDbmsConnectionRequest {
    public Connection connection;
    public CloseDbmsConnectionRequest(Connection conn)
    {
        connection = conn;
    }
}
