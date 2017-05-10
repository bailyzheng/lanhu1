package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/21.
 */
public class ToPayData_Data implements Serializable{
    private WXPayData wxdata;
    private String alidata;

    public String getAlidata() {
        return alidata;
    }

    public void setAlidata(String alidata) {
        this.alidata = alidata;
    }

    public WXPayData getWxdata() {
        return wxdata;
    }

    public void setWxdata(WXPayData wxdata) {
        this.wxdata = wxdata;
    }

    @Override
    public String toString() {
        return "ToPayData_Data{" +
                "wxdata=" + wxdata +
                ", alidata='" + alidata + '\'' +
                '}';
    }
}
