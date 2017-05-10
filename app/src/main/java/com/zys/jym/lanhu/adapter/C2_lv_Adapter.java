package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.C_Data;

import java.util.List;


/**
 * Created by Administrator on 2016/3/11.
 */
public class C2_lv_Adapter extends BaseAdapter {
    Activity activity;
    private LayoutInflater inflater;
    List<C_Data> list;
    C_Data mdata;
    public C2_lv_Adapter(Activity activity, List<C_Data> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        mdata=list.get(position);
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.item_lv_p,
                    null);
            mHolder.tv_p = (TextView) convertView.findViewById(R.id.tv_p);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.tv_p.setText(mdata.getName());
        return convertView;
    }

    class ViewHolder {
        TextView tv_p;
    }

}
