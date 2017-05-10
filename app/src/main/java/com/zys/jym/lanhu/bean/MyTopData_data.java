package com.zys.jym.lanhu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */
public class MyTopData_data implements Serializable{
    private int p;
    private int pageSize;
    private String count;
    private List<TopData> topicList;

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<TopData> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopData> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String toString() {
        return "MyTopData_data{" +
                "p=" + p +
                ", pageSize=" + pageSize +
                ", count='" + count + '\'' +
                ", topicList=" + topicList +
                '}';
    }
}
