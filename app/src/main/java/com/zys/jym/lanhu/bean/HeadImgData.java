package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/18.
 */
public class HeadImgData implements Serializable{
    public String headurl;

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    @Override
    public String toString() {
        return "HeadImgData{" +
                "headurl='" + headurl + '\'' +
                '}';
    }
}
