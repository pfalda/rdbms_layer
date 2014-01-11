package com.disbrain.dbmslayer.messages;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 16.50
 * To change this template use File | Settings | File Templates.
 */
public class DbmsWorkerCmdReply {
    public DbmsWorkerCmdRequest.Command originalRequest;
    public DbmsWorkerCmdReply(DbmsWorkerCmdRequest.Command request)
    {
        originalRequest = request;
    }
}
