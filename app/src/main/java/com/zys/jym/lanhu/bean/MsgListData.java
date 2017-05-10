package com.zys.jym.lanhu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/2.
 */
public class MsgListData implements Serializable{
    private List<MsgData> logList;
    private int p;
    private String count;
    private String pageSize;


    public List<MsgData> getLogList() {
        return logList;
    }

    public void setLogList(List<MsgData> logList) {
        this.logList = logList;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "MsgListData{" +
                "logList=" + logList +
                ", p=" + p +
                ", count='" + count + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
