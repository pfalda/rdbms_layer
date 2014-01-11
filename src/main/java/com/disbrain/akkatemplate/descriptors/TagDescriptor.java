package com.disbrain.akkatemplate.descriptors;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: angel
 * Date: 02/01/14
 * Time: 13.25
 * To change this template use File | Settings | File Templates.
 */
public class TagDescriptor {
    public long tag_id;
    public String tag_name;

    public TagDescriptor(Object id, Object name)
    {
        this.tag_id = ((BigInteger)id).longValue();
        this.tag_name = (String)name;
    }
}
