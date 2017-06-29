package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.HB_ZDStream;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class StreamLvAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    List<HB_ZDStream> list;
    HB_ZDStream mdata;
    int type;
    public StreamLvAdapter(Activity activity, List<HB_ZDStream> list,int type){
        this.activity = activity;
        inflater = this.activity.getLayoutInflater();
        this.list=list;
        this.type=type;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mdata=list.get(position);
        ViewHolder holder;
        if (convertView == null ) {
            convertView = inflater.inflate(R.layout.item_lv_stream, null);
            holder = new ViewHolder();
            holder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_hhmm= (TextView) convertView.findViewById(R.id.tv_hhmm);
            holder.tv_yyyymmdd= (TextView) convertView.findViewById(R.id.tv_yyyymmdd);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        String time= TimeUtil.timeStamp2Date(mdata.getAddtime(),null);
        holder.tv_hhmm.setText(MyUtils.Intercept_HMS(time));
        holder.tv_yyyymmdd.setText(MyUtils.Intercept_YMD(time));

        if (type==1){//狐币流水
            holder.tv_content.setText(mdata.getRemark());
            holder.tv_num.setText("+"+MyUtils.mul(Double.valueOf(mdata.getMoney()),0.01));
            switch (MyUtils.Str2Int(mdata.getType())){
                case 1:
//                    holder.tv_content.setText("充值狐币");
                    //MyUtils.Str2Int(MyUtils.Intercept_Int_Point(mdata.getMoney()))*0.01
//                    holder.tv_num.setText("+"+MyUtils.Intercept_Int_Point(MyUtils.Str2Double(mdata.getMoney())*0.01+""));
                    holder.tv_num.setText("+"+MyUtils.mul(Double.valueOf(mdata.getMoney()),0.01));
                    break;
                case 2:
//                    holder.tv_content.setText("开通会员");
//                    holder.tv_num.setText("-"+ MyUtils.Intercept_Int_Point(MyUtils.Str2Double(mdata.getMoney())*0.01+""));
                    holder.tv_num.setText("-"+MyUtils.mul(Double.valueOf(mdata.getMoney()),0.01));
                    break;
                case 3:
//                    holder.tv_content.setText("购买置顶");
//                    holder.tv_num.setText("-"+ MyUtils.Intercept_Int_Point(MyUtils.Str2Double(mdata.getMoney())*0.01+""));
                    holder.tv_num.setText("-"+MyUtils.mul(Double.valueOf(mdata.getMoney()),0.01));
                    break;
                case 4:
//                    holder.tv_content.setText("分享奖励");
//                    holder.tv_num.setText("+"+ MyUtils.Intercept_Int_Point(MyUtils.Str2Double(mdata.getMoney())*0.01+""));
                    holder.tv_num.setText("+"+MyUtils.mul(Double.valueOf(mdata.getMoney()),0.01));
                    break;
            }
        }
        if (type==2){
            switch (MyUtils.Str2Int(mdata.getType())){
                case 1:
                    holder.tv_content.setText("置顶消耗");
                    holder.tv_num.setText("-"+ mdata.getNum());
                    break;
                case 2:
                    holder.tv_content.setText("购买置顶");
                    holder.tv_num.setText("+"+ mdata.getNum());
                    break;
                case 3:
                    holder.tv_content.setText("推荐奖励");
                    holder.tv_num.setText("+"+ mdata.getNum());
                    break;
                case 4:
                    holder.tv_content.setText("兑换置顶");
                    holder.tv_num.setText("+"+ mdata.getNum());
                    break;
                case 5:
                    holder.tv_content.setText("邀请好友");
                    holder.tv_num.setText("+"+ mdata.getNum());
                    break;
            }
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tv_content,tv_num,tv_hhmm,tv_yyyymmdd;
    }
}
