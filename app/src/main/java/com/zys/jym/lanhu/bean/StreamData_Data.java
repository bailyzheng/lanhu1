package com.zys.jym.lanhu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */
public class StreamData_Data implements Serializable{
    private List<HB_ZDStream> logList;
    private String count;
    private int pageSize;
    private int p;

    public List<HB_ZDStream> getLogList() {
        return logList;
    }

    public void setLogList(List<HB_ZDStream> logList) {
        this.logList = logList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "StreamData_Data{" +
                "logList=" + logList +
                ", count='" + count + '\'' +
                ", pageSize=" + pageSize +
                ", p=" + p +
                '}';
    }
}
