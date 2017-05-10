package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/19.
 */
public class MyTopData implements Serializable{
    private int errcode;
    private String errmsg;
    private MyTopData_data data;

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

    public MyTopData_data getData() {
        return data;
    }

    public void setData(MyTopData_data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyTopData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
