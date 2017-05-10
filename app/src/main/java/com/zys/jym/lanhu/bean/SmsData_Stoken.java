package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SmsData_Stoken implements Serializable{
    public String sms_token;

    public String getSms_token() {
        return sms_token;
    }

    public void setSms_token(String sms_token) {
        this.sms_token = sms_token;
    }

    @Override
    public String toString() {
        return "SmsData_Stoken{" +
                "sms_token='" + sms_token + '\'' +
                '}';
    }
}
