package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */
public class HB_ZDStream implements Serializable{
    private String id;
    private String userid;
    private String money;//狐币数
    private String type;
    private String addtime;
    private String remark;
    private String num;//置顶数


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "HB_ZDStream{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", money='" + money + '\'' +
                ", type='" + type + '\'' +
                ", addtime='" + addtime + '\'' +
                ", remark='" + remark + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
