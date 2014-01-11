package com.disbrain.dbmslayer.messages;

import com.disbrain.dbmslayer.descriptors.RequestModes;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.57
 * To change this template use File | Settings | File Templates.
 */
public class DBMSReply  {
    public RequestModes.RequestTypology request_mode;

    public ResultSet resultSet = null;
    public int       ddl_retval = -1;

    public DBMSReply(ResultSet res,RequestModes.RequestTypology mode) {
        request_mode = mode;
        resultSet = res;;
    }
    public DBMSReply(int ret,RequestModes.RequestTypology mode)
    {
        request_mode = mode;
        ddl_retval = ret;
    }
}