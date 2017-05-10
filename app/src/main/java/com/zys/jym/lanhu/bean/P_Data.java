package com.zys.jym.lanhu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/18.
 */

public class P_Data implements Serializable {

    public String id;
    public String name;

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

    @Override
    public String toString() {
        return "P_Data{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public P_Data(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
