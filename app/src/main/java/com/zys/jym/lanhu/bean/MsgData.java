package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/2.
 */
public class MsgData implements Serializable{
    private String id;
    private String title;
    private String userid;
    private String addtime;


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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "MsgData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", userid='" + userid + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
