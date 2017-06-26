package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by marufei
 * time on 2017/6/27
 */

public class ShareDataBean implements Serializable {

    /**
     * data : {"id":"1","title":"送你3天被动加粉，我的邀请码：","content":"填写我的邀请码，你我各获得3天被动加粉，半小时置顶，最低被加100人。","qqone":"1123456789","qqtwo":"8765432"}
     * errcode : 1
     * errmsg : 获取分享配置！
     */

    private DataBean data;
    private int errcode;
    private String errmsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * id : 1
         * title : 送你3天被动加粉，我的邀请码：
         * content : 填写我的邀请码，你我各获得3天被动加粉，半小时置顶，最低被加100人。
         * qqone : 1123456789
         * qqtwo : 8765432
         */

        private String id;
        private String title;
        private String content;
        private String qqone;
        private String qqtwo;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getQqone() {
            return qqone;
        }

        public void setQqone(String qqone) {
            this.qqone = qqone;
        }

        public String getQqtwo() {
            return qqtwo;
        }

        public void setQqtwo(String qqtwo) {
            this.qqtwo = qqtwo;
        }
    }
}
