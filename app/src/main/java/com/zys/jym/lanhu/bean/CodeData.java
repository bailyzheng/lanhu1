package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/18.
 */
public class CodeData implements Serializable{
    public String cardId;
    public String imgtext;
    public String imgurl;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getImgtext() {
        return imgtext;
    }

    public void setImgtext(String imgtext) {
        this.imgtext = imgtext;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "CodeData{" +
                "cardId='" + cardId + '\'' +
                ", imgtext='" + imgtext + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
