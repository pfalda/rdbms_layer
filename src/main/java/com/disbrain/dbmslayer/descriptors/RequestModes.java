package com.disbrain.dbmslayer.descriptors;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.14
 * To change this template use File | Settings | File Templates.
 */
public class RequestModes {
    public enum RequestTypology { READ_ONLY, READ_WRITE, WRITE, ASYNC_READ_ONLY, ASYNC_READ_WRITE, ASYNC_WRITE };
    public enum RequestBehaviour { RESOURCE_GETTER, RESOURCE_RELEASER, RESOURCE_WHATEVER };
    public RequestTypology typology;
    public RequestBehaviour behaviour = RequestBehaviour.RESOURCE_WHATEVER;

    public RequestModes(RequestTypology typo)
    {
        typology = typo;
    }

    public RequestModes(RequestTypology typo, RequestBehaviour behaviour)
    {
        typology = typo;
        this.behaviour = behaviour;
    }

    public RequestModeDescription getProperty()
    {
        RequestModeDescription properties = null;

        switch(typology)
        {
            case ASYNC_READ_ONLY:
            case ASYNC_READ_WRITE:
            case READ_ONLY:
            case READ_WRITE:
                properties = new RequestModeDescription(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                break;
            case ASYNC_WRITE:
            case WRITE:
                properties = new RequestModeDescription(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                break;

        }
        return (properties);

    }
}
