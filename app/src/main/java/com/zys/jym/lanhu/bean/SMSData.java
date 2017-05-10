package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 */
public class SMSData implements Serializable{
    public int errcode;
    public String errmsg;
    public SmsData_Stoken data;


    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public SmsData_Stoken getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SMSData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }

    public void setData(SmsData_Stoken data) {
        this.data = data;
    }
}
