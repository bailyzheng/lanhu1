package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/21.
 */
public class ToPayData implements Serializable{
    private int errcode;
    private String errmsg;
    private ToPayData_Data data;


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

    public ToPayData_Data getData() {
        return data;
    }

    public void setData(ToPayData_Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ToPayData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
