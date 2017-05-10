package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/19.
 */
public class TopData implements Serializable{
    private String id;
    private String title;
    private String describe;
    private String addtime;
    private String scannum;
    private String resttop;//剩余置顶时间小于0没有置顶大于表示剩余时间秒
    private String cataid;
    private String nickname;
    private String headurl;
    private String viprest;
    private String toprest;
    private String imgurl;//二维码地址
    private String provincename;
    private String cityname;
    private String udescribe;//用户描述
    private String phone;//手机号

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUdescribe() {
        return udescribe;
    }

    public void setUdescribe(String udescribe) {
        this.udescribe = udescribe;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getScannum() {
        return scannum;
    }

    public void setScannum(String scannum) {
        this.scannum = scannum;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getResttop() {
        return resttop;
    }

    public void setResttop(String resttop) {
        this.resttop = resttop;
    }


    public String getCataid() {
        return cataid;
    }

    public void setCataid(String cataid) {
        this.cataid = cataid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getViprest() {
        return viprest;
    }

    public void setViprest(String viprest) {
        this.viprest = viprest;
    }

    public String getToprest() {
        return toprest;
    }

    public void setToprest(String toprest) {
        this.toprest = toprest;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "TopData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", addtime='" + addtime + '\'' +
                ", scannum='" + scannum + '\'' +
                ", resttop='" + resttop + '\'' +
                ", cataid='" + cataid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headurl='" + headurl + '\'' +
                ", viprest='" + viprest + '\'' +
                ", toprest='" + toprest + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", provincename='" + provincename + '\'' +
                ", cityname='" + cityname + '\'' +
                ", udescribe='" + udescribe + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
