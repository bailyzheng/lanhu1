package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */
public class StreamData implements Serializable{
    private int errcode;
    private String errmsg;
    private StreamData_Data data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public StreamData_Data getData() {
        return data;
    }

    public void setData(StreamData_Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StreamData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
