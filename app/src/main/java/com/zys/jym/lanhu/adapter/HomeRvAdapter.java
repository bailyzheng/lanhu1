package com.zys.jym.lanhu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.GroupCodeActivity;
import com.zys.jym.lanhu.bean.HomeListData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.MyViewHolder> {
    private static final String TAG = "GroupRvAdapter";
    private List<HomeListData.DataBean.ContactListBean> list;
    private LayoutInflater inflater;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private   int type;
//    private int coose_type=
    private View mHeaderView;
    Context context;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
/**
    public HomeRvAdapter(Context context, List<HomeListData.DataBean.ContactListBean> list) {
        this.context = context;
        String str=MySharedPrefrencesUtil.getParam(context,"phone_data","").toString();
        String [] temp=str.split("\\^");
        for(int i=0;i<temp.length;i++){
              for(int j=0;j<list.size();j++){
                  if((list.get(j).getPhone()).equals(temp[i])){
                    list.get(j).setType(3);
                }
            }
        }
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
 **/

public HomeRvAdapter(Context context) {
    this.context = context;
    inflater = LayoutInflater.from(context);
}

    public void setData(List<HomeListData.DataBean.ContactListBean> list){
//        String str=MySharedPrefrencesUtil.getParam(context,"phone_data","").toString();
//        String [] temp=str.split("\\^");
//        for(int i=0;i<temp.length;i++){
//            for(int j=0;j<list.size();j++){
//                if((list.get(j).getPhone()).equals(temp[i])){
//                    list.remove(j);
//                }
//            }
//        }
         this.list = list;
        this.notifyDataSetChanged();
     }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new MyViewHolder(mHeaderView);
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.item_rcv_home, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
//        final TopData data = list.get(pos);
        HomeListData.DataBean.ContactListBean data=new HomeListData.DataBean.ContactListBean();
        data=list.get(pos);
        if (holder instanceof MyViewHolder) {
            //判断是否设置了监听器
            if (mOnItemClickListener != null) {
                //为ItemView设置监听器
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, pos); // 2
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemLongClickListener.onItemLongClick(holder.itemView, pos);
                        //返回true 表示消耗了事件 事件不会继续传递
                        return true;
                    }
                });
            }
            if (data.getHeadurl() != null) {
                Picasso.with(context).load(LHHttpUrl.IMG_URL + data.getHeadurl()).into(holder.iv_head);
            } else {
                holder.iv_head.setImageResource(R.mipmap.icon_head_normal);
            }

            holder.tv_title.setText(data.getNickname());

            if (Long.parseLong(list.get(pos).getViprest())<=0){
                holder.tv_title.setTextColor(context.getResources().getColor(R.color.black));
                holder.iv_vip.setVisibility(View.GONE);
            }else {
                holder.tv_title.setTextColor(context.getResources().getColor(R.color.red));
                holder.iv_vip.setVisibility(View.VISIBLE);
            }
            if (Long.parseLong(list.get(pos).getToprest())<=0){
                holder.iv_zhiding.setVisibility(View.GONE);
            }else {
                holder.iv_zhiding.setVisibility(View.VISIBLE);
            }


            switch (list.get(position-1).getType()){
                case 1:
                    holder.iv_home_choose.setEnabled(true);
                    holder.iv_home_choose.setVisibility(View.VISIBLE);
                    holder.tv_home_add.setVisibility(View.GONE);
                    holder.iv_home_choose.setImageResource(R.mipmap.choose);
                    break;
                case 2:
                    holder.iv_home_choose.setEnabled(true);
                    holder.iv_home_choose.setVisibility(View.VISIBLE);
                    holder.tv_home_add.setVisibility(View.GONE);
                    holder.iv_home_choose.setImageResource(R.mipmap.choose_light);
                    break;
                case 3:
                    holder.iv_home_choose.setVisibility(View.GONE);
                    holder.tv_home_add.setVisibility(View.VISIBLE);
                    holder.iv_home_choose.setEnabled(false);
                    break;
                default:
                    break;
            }

        }


    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? list.size() : list.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_home_add;
        ImageView iv_head, iv_vip, iv_zhiding, iv_home_choose;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            iv_home_choose = (ImageView) itemView.findViewById(R.id.iv_home_choose);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_home_head);
            iv_vip = (ImageView) itemView.findViewById(R.id.iv_home_vip);
            iv_zhiding = (ImageView) itemView.findViewById(R.id.iv_home_zhiding);
            tv_title = (TextView) itemView.findViewById(R.id.tv_home_title);
            tv_home_add=(TextView)itemView.findViewById(R.id.tv_home_add);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
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
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

}
