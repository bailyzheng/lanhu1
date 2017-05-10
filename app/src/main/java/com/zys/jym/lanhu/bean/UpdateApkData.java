package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/8.
 */
public class UpdateApkData implements Serializable{
    private int errcode;
    private String errmsg;
    private NewApkData data;

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

    public NewApkData getData() {
        return data;
    }

    public void setData(NewApkData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UpdateApkData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
