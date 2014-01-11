package com.disbrain.dbmslayer.descriptors;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.12
 * To change this template use File | Settings | File Templates.
 */
public class RequestModeDescription {

    public int  resultSetType,
                resultSetConcurrency,
                resultSetHoldability;

    public RequestModeDescription(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    {
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
    }

}
