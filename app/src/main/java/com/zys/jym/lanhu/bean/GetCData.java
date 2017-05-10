package com.zys.jym.lanhu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */

public class GetCData implements Serializable {

    public int errcode;
    public String errmsg;
    public List<C_Data> data;

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

    public List<C_Data> getData() {
        return data;
    }

    public void setData(List<C_Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetPData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
