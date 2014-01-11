package com.disbrain.dbmslayer.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 14.42
 * To change this template use File | Settings | File Templates.
 */
public interface DbmsException {
    public int getErrorCode();
    public String getMessage();
    public void setException(Exception ex);
    public String getRealMessage();
}
