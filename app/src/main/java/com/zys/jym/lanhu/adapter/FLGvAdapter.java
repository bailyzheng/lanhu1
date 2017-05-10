package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zys.jym.lanhu.R;

/**
 * Created by Administrator on 2016/12/14.
 */

public class FLGvAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    String[] names=new String[]{"人脉推广","宝妈互动","经验分享","小白学习","兼职信息","代理产品","微商团队"};
    int[] imgs=new int[]{R.mipmap.tg,R.mipmap.hd,R.mipmap.fx,R.mipmap.xx,R.mipmap.jz,R.mipmap.dl,R.mipmap.td};
     public FLGvAdapter(Activity activity){
        this.activity = activity;
        inflater = this.activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        mdata=list.get(position);
        ViewHolder holder;
        if (convertView == null ) {
            convertView = inflater.inflate(R.layout.item_gv_fl, null);
            holder = new ViewHolder();
            holder.iv_img= (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_img.setImageResource(imgs[position]);
        holder.tv_name.setText(names[position]);
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_img;
        TextView tv_name;

    }
}
