package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */
public class CardDetailData implements Serializable{
    private int errcode;
    private String errmsg;
    private TopData data;

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

    public TopData getData() {
        return data;
    }

    public void setData(TopData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CardDetailData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
