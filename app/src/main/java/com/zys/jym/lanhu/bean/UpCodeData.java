package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/18.
 */
public class UpCodeData implements Serializable{
    public int errcode;
    public String errmsg;
    public CodeData data;

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

    public CodeData getData() {
        return data;
    }

    public void setData(CodeData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UpCodeData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
