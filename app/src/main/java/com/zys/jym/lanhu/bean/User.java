package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 */
public class User implements Serializable{

    public String id;
    public String UID;
    public String phone;
    public String nickname;
    public String regtime;
    public String viptime;//会员到期时间戳<=0不是会员
    public String headurl;
    public String login_token;
    public String url;
    public String describe;
    public String regcode;
    public String ewmurl;//分享-下载app二维码
    public String appurl;//分享-下载app地址


    public String getEwmurl() {
        return ewmurl;
    }

    public void setEwmurl(String ewmurl) {
        this.ewmurl = ewmurl;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getRegcode() {
        return regcode;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", UID='" + UID + '\'' +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", regtime='" + regtime + '\'' +
                ", viptime='" + viptime + '\'' +
                ", headurl='" + headurl + '\'' +
                ", login_token='" + login_token + '\'' +
                ", url='" + url + '\'' +
                ", describe='" + describe + '\'' +
                ", regcode='" + regcode + '\'' +
                ", ewmurl='" + ewmurl + '\'' +
                ", appurl='" + appurl + '\'' +
                '}';
    }
}
