package com.disbrain.akkatemplate.messages;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 30/12/13
 * Time: 11.24
 * To change this template use File | Settings | File Templates.
 */
public class DummyOutputReply {
    public static final int out_columns_num = 1;
    public static final int out_lines_num = Integer.MAX_VALUE;
    public Object[]    output;
    public DummyOutputReply(Object[] values)
    {
        output = values;
    }
}
