package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.pager.TopPager;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class TopLvAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    List<TopData> list;
    TopData mdata;
    public TopLvAdapter(Activity activity, List<TopData> list){
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
            convertView = inflater.inflate(R.layout.item_lv_top, null);
            holder = new ViewHolder();
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_describe= (TextView) convertView.findViewById(R.id.tv_describe);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_state= (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_scan= (TextView) convertView.findViewById(R.id.tv_scan);
            holder.tv_zd=(TextView) convertView.findViewById(R.id.tv_zd);
            holder.tv_zdsx=(TextView) convertView.findViewById(R.id.tv_zdsx);
            holder.tv_sdsx=(TextView) convertView.findViewById(R.id.tv_sdsx);
            holder.tv_delete=(TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(mdata.getTitle());
        holder.tv_describe.setText(mdata.getDescribe());
        holder.tv_time.setText("发布时间："+TimeUtil.timeStamp2Date(mdata.getAddtime(),null));
        holder.tv_scan.setText("扫描："+mdata.getScannum()+"次");
        if (MyUtils.Str2Int(mdata.getResttop())<0){
            holder.tv_state.setText("发布状态：未置顶");
        }else {
            holder.tv_state.setText("发布状态：置顶剩余"+mdata.getResttop()+"秒");
        }
        holder.tv_zd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopPager.itemZD(list.get(position).getId());
            }
        });
        holder.tv_zdsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopPager.itemZDSX(list.get(position).getId());
            }
        });
        holder.tv_sdsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopPager.itemSDSX(list.get(position).getId());
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopPager.itemDelete(list.get(position).getId());
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView tv_title;
        TextView tv_describe;
        TextView tv_time,tv_state,tv_scan,tv_zd,tv_zdsx,tv_sdsx,tv_delete;
    }


}
