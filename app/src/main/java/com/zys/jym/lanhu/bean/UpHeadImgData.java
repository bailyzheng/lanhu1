package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/18.
 */
public class UpHeadImgData implements Serializable{
    public int errcode;
    public String errmsg;
    public HeadImgData data;

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

    public HeadImgData getData() {
        return data;
    }

    public void setData(HeadImgData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UpHeadImgData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
