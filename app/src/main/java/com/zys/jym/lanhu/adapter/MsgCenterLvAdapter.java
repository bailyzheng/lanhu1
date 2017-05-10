package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.MsgData;
import com.zys.jym.lanhu.utils.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class MsgCenterLvAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    List<MsgData> list;
    MsgData mdata;
    public MsgCenterLvAdapter(Activity activity, List<MsgData> list){
        this.activity = activity;
        this.list=list;
        inflater = this.activity.getLayoutInflater();
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
            convertView = inflater.inflate(R.layout.item_lv_msgcenter, null);
            holder = new ViewHolder();
            holder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(mdata.getTitle());
        holder.tv_time.setText(TimeUtil.timeStamp2Date(mdata.getAddtime(),null));
        return convertView;
    }

    static class ViewHolder{
        TextView tv_content;
        TextView tv_time;
    }
}
