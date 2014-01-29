package com.disbrain.dbmslayer.exceptions;

public interface DbmsException {
    public int getErrorCode();

    public String getMessage();

    // PN: remove this setter?
    // PF: I would keep it in order to mantain the interface more flexible
    public void setException(Exception ex);

    public String getRealMessage();
}
