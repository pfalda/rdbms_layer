package com.disbrain.dbmslayer.exceptions;

@SuppressWarnings("serial")
public class DbmsRemoteQueryError extends RuntimeException implements DbmsException {
    StackTraceElement trace[] = Thread.currentThread().getStackTrace();
    private Exception error;
    private final static int code = 2;
    private final static String description = "Error performing a query on remote db ";
    private String extraInfo = "";
    private String real_message = null;

    public DbmsRemoteQueryError() {
        error = null;
    }

    public DbmsRemoteQueryError(Exception ex) {
        trace = Thread.currentThread().getStackTrace();
        error = ex;
    }

    public DbmsRemoteQueryError(Exception ex, String extra_msg) {
        error = ex;
        extraInfo = extra_msg;
    }

    public DbmsRemoteQueryError(String extra_info) {
        error = null;
        extraInfo = extra_info;
    }

    public void setException(Exception ex) {
        error = ex;
    }

    public String getMessage() {
        if (error != null)
            extraInfo += String.format("\nClass: %s\nCause: %s\nMessage: %s\nStack Trace:\n", error.getClass(), error.getCause(), error.getMessage());
        for (StackTraceElement element : trace)
            extraInfo += element.toString() + "\n";
        return description + extraInfo;
    }

    public String getRealMessage() {
        if (error != null)
            real_message = error.getMessage();
        return real_message;
    }

    public int getErrorCode() {
        return code;
    }
}
