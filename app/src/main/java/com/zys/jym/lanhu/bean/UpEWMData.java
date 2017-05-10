package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/11.
 */

public class UpEWMData implements Serializable {

    /**
     * errcode : 1
     * errmsg : 上传二维码成功!
     * data : {"cardId":"4","imgtext":"http://weixin.qq.com/g/AeHB0b4c-tQW34F3","imgurl":"http://139.224.194.108/lh/uploads/cards/2016-12-17/58553c8170a1c.JPG"}
     */

    private int errcode;
    private String errmsg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cardId : 4
         * imgtext : http://weixin.qq.com/g/AeHB0b4c-tQW34F3
         * imgurl : http://139.224.194.108/lh/uploads/cards/2016-12-17/58553c8170a1c.JPG
         */

        private String cardId;
        private String imgtext;
        private String imgurl;

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
    }
}
