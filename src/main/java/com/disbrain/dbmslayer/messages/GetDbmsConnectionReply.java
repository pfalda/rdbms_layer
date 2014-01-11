package com.disbrain.dbmslayer.messages;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 14.39
 * To change this template use File | Settings | File Templates.
 */
public class GetDbmsConnectionReply {
    public Connection connection;
    public GetDbmsConnectionReply(Connection conn)
    {
            connection = conn;
    }
}
