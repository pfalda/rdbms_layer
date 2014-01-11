package com.disbrain.dbmslayer.messages;

import com.disbrain.dbmslayer.descriptors.RequestModeDescription;
import com.disbrain.dbmslayer.descriptors.RequestModes;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.15
 * To change this template use File | Settings | File Templates.
 */
public class GetDbmsStatementRequest {

    public RequestModeDescription properties = null;

    public GetDbmsStatementRequest(RequestModes props)
    {
        properties = props.getProperty();

    }
}
