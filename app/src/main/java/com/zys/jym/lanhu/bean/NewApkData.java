package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/8.
 */
public class NewApkData implements Serializable{
    private String id;
    private String version;
    private String url;
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewApkData{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
