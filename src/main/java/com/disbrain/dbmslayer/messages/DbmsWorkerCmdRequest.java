package com.disbrain.dbmslayer.messages;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 16.48
 * To change this template use File | Settings | File Templates.
 */
public class DbmsWorkerCmdRequest {
    public static enum Command { COMMIT, ROLLBACK, CLOSE_STMT };
    public Command request;
    public DbmsWorkerCmdRequest(Command request)
    {
        this.request = request;
    }
}
