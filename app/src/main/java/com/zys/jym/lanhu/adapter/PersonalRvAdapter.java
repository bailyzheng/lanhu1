package com.zys.jym.lanhu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Administrator on 2017/2/14.
 */

public class PersonalRvAdapter extends RecyclerView.Adapter <PersonalRvAdapter.MyViewHolder>{
    private static final String TAG = "GroupRvAdapter";
    private List<TopData> list;
    private LayoutInflater inflater;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;
    Context context;
    public PersonalRvAdapter(Context context, List<TopData> list) {
        this.context=context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new MyViewHolder(mHeaderView);
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.item_rcv_personal, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        final TopData data = list.get(pos);
        if(holder instanceof MyViewHolder) {
            //判断是否设置了监听器
            if(mOnItemClickListener != null){
                //为ItemView设置监听器
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView,pos); // 2
                    }
                });
            }
            if(mOnItemLongClickListener != null){
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemLongClickListener.onItemLongClick(holder.itemView,pos);
                        //返回true 表示消耗了事件 事件不会继续传递
                        return true;
                    }
                });
            }
            if (data.getHeadurl()!=null){
                Picasso.with(context).load(LHHttpUrl.IMG_URL+data.getHeadurl()).into(holder.iv_head);
            }else {
                holder.iv_head.setImageResource(R.mipmap.icon_head_normal);
            }

            holder.tv_title.setText(data.getTitle());
            holder.tv_describe.setText(data.getDescribe());
            if (!TextUtils.isEmpty(data.getViprest())&&MyUtils.Str2Int(data.getViprest())<0){
                holder.tv_title.setTextColor(context.getResources().getColor(R.color.black));
                holder.iv_vip.setVisibility(View.GONE);
            }else {
                holder.tv_title.setTextColor(context.getResources().getColor(R.color.red));
                holder.iv_vip.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(data.getToprest())&&MyUtils.Str2Int(data.getToprest())<0){
                holder.iv_zhiding.setVisibility(View.GONE);
            }else {
                holder.iv_zhiding.setVisibility(View.VISIBLE);
            }

            holder.tv_addgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in =new Intent(context, GroupCodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TopList", (Serializable)list);
                    bundle.putInt("p",pos);
                    in.putExtras(bundle);
                    context.startActivity(in);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? list.size() : list.size() + 1;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_addgroup,tv_describe,tv_title;
        ImageView iv_head,iv_vip,iv_zhiding;

        public MyViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            tv_addgroup= (TextView) itemView.findViewById(R.id.tv_addgroup);
            iv_head= (ImageView) itemView.findViewById(R.id.iv_head);
            iv_vip= (ImageView) itemView.findViewById(R.id.iv_vip);
            iv_zhiding= (ImageView) itemView.findViewById(R.id.iv_zhiding);
            tv_describe= (TextView) itemView.findViewById(R.id.tv_describe);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);

        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

}
