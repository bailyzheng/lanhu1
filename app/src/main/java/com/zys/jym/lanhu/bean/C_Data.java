package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/18.
 */

public class C_Data implements Serializable {

    public String id;
    public String name;
    public String province_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    @Override
    public String toString() {
        return "C_Data{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", province_id='" + province_id + '\'' +
                '}';
    }
}
