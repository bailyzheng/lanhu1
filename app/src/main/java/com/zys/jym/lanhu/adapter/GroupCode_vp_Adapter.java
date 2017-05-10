package com.zys.jym.lanhu.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import java.util.List;

/**
 * Created by Admin on 2016/6/16.
 */
public class GroupCode_vp_Adapter extends PagerAdapter {
    private Activity activity;
    List<TopData> list;
    OnLongListener listener;
    public GroupCode_vp_Adapter(Activity activity, List<TopData> list,OnLongListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener=listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView iv = new ImageView(activity);

//        iv.setMaxHeight(300);
//        iv.setMaxWidth(400);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(activity).load(LHHttpUrl.IMG_URL + list.get(position).getImgurl()).into(iv);

        container.addView(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyUtils.Loge("TAG","长按二维码");
                if (listener!=null){
                    listener.onLongC();
                }
                return true;
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    public static abstract class OnLongListener {

        public abstract void onLongC();


    }
}