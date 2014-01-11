package com.disbrain.dbmslayer.messages;

import com.disbrain.dbmslayer.descriptors.RequestModeDescription;
import com.disbrain.dbmslayer.descriptors.RequestModes;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.17
 * To change this template use File | Settings | File Templates.
 */
public class GetDbmsPreparedStatementRequest {
    public String query;
    public RequestModeDescription properties = null;

    public GetDbmsPreparedStatementRequest(String query, RequestModes props)
    {
            this.query = query;
            properties = props.getProperty();
    }
}
