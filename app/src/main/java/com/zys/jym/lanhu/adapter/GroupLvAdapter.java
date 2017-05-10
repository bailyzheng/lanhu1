package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.GroupCodeActivity;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class GroupLvAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    List<TopData> list;
    TopData mdata;
    public GroupLvAdapter(Activity activity, List<TopData> list){
        this.activity = activity;
        inflater = this.activity.getLayoutInflater();
        this.list=list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        mdata=list.get(position);
        ViewHolder holder;
        if (convertView == null ) {
            convertView = inflater.inflate(R.layout.item_lv_group, null);
            holder = new ViewHolder();
            holder.tv_addgroup= (TextView) convertView.findViewById(R.id.tv_addgroup);
            holder.iv_head= (ImageView) convertView.findViewById(R.id.iv_head);
            holder.iv_vip= (ImageView) convertView.findViewById(R.id.iv_vip);
            holder.iv_zhiding= (ImageView) convertView.findViewById(R.id.iv_zhiding);
            holder.tv_describe= (TextView) convertView.findViewById(R.id.tv_describe);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mdata.getHeadurl()!=null){
            Picasso.with(activity).load(LHHttpUrl.IMG_URL+mdata.getHeadurl()).into(holder.iv_head);
        }else {
            holder.iv_head.setImageResource(R.mipmap.icon_head_normal);
        }
        holder.tv_title.setText(mdata.getTitle());
        holder.tv_describe.setText(mdata.getDescribe());
        if (MyUtils.Str2Int(mdata.getViprest())<0){
            holder.tv_title.setTextColor(activity.getResources().getColor(R.color.black));
            holder.iv_vip.setVisibility(View.GONE);
        }else {
            holder.tv_title.setTextColor(activity.getResources().getColor(R.color.red));
            holder.iv_vip.setVisibility(View.VISIBLE);
        }
        if (MyUtils.Str2Int(mdata.getToprest())<0){
            holder.iv_zhiding.setVisibility(View.GONE);
        }else {
            holder.iv_zhiding.setVisibility(View.VISIBLE);
        }

        holder.tv_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(activity, GroupCodeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("TopList", (Serializable)list);
                bundle.putInt("p",position);
                in.putExtras(bundle);
                activity.startActivity(in);
            }
        });
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_head,iv_vip,iv_zhiding;
        TextView tv_addgroup;
        TextView tv_describe;
        TextView tv_title;
    }
}
