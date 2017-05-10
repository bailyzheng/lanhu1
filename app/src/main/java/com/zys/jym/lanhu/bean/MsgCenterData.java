package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/2.
 */
public class MsgCenterData implements Serializable{
    private int errcode;
    private String errmsg;
    private MsgListData data;

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

    public MsgListData getData() {
        return data;
    }

    public void setData(MsgListData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgCenterData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
