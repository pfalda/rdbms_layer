package com.disbrain.dbmslayer.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 20/12/13
 * Time: 15.58
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class DbmsLayerError extends Exception implements DbmsException {

    StackTraceElement   trace[] = Thread.currentThread().getStackTrace();
    private Exception error;
    private final static int code = 4;
    private final static String description = "The following error has occurred executing an api call: ";
    private String extraInfo = "";
    private String real_message = null;

    public DbmsLayerError()
    {
        error = null;
    }

    public DbmsLayerError(Exception ex)
    {
        trace = Thread.currentThread().getStackTrace();
        error = ex;
    }

    public DbmsLayerError(Exception ex, String extra_msg)
    {
        error = ex;
        extraInfo = extra_msg;
    }

    public DbmsLayerError(String extra_info)
    {
        error = null;
        extraInfo = extra_info;
    }

    public void setException(Exception ex)
    {
        error = ex;
    }

    public String getMessage()
    {
        if(error != null)
            extraInfo += String.format("\nClass: %s\nCause: %s\nMessage: %s\nStack Trace:\n",error.getClass(),error.getCause(),error.getMessage());
        for (StackTraceElement element : trace)
            extraInfo += element.toString()+"\n";
        return description + extraInfo;
    }

    public String getRealMessage()
    {
        if(error != null)
            real_message = error.getMessage();
        return real_message;
    }

    public int getErrorCode()
    {
        return code;
    }

}
