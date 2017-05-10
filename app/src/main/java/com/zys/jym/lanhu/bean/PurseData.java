package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */
public class PurseData implements Serializable{
    private String id;
    private String pursebalance;//狐币余额（单位分）
    private String topbalance;//剩余置顶次数
    private String viprest;  //vip剩余时间

    public String getViprest() {
        return viprest;
    }

    public void setViprest(String viprest) {
        this.viprest = viprest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPursebalance() {
        return pursebalance;
    }

    public void setPursebalance(String pursebalance) {
        this.pursebalance = pursebalance;
    }

    public String getTopbalance() {
        return topbalance;
    }

    public void setTopbalance(String topbalance) {
        this.topbalance = topbalance;
    }

    @Override
    public String toString() {
        return "PurseData{" +
                "id='" + id + '\'' +
                ", pursebalance='" + pursebalance + '\'' +
                ", topbalance='" + topbalance + '\'' +
                ", viprest='" + viprest + '\'' +
                '}';
    }
}
