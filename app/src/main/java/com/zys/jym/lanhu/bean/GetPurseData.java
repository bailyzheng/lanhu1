package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */
public class GetPurseData implements Serializable{
    private int errcode;
    private String errmsg;
    private PurseData userDate;
    private User userInfo;

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

    public PurseData getUserDate() {
        return userDate;
    }

    public void setUserDate(PurseData userDate) {
        this.userDate = userDate;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "GetPurseData{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", userDate=" + userDate +
                ", userInfo=" + userInfo +
                '}';
    }
}
