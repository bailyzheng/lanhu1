package com.zys.jym.lanhu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.zys.jym.lanhu.R;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ParallaxScrollView extends ScrollView {
    private ImageView mImageView;
    // 定义一个imageview的最大的拉伸的高度
    private int mDrawableMaxHeight = -1;
    // 定义ImageView 初始加载的高度
    private int mImageViewHeight = -1;
    // 默认的高度
    private int mDefaultImageViewHeight = 0;

    public ParallaxScrollView(Context context) {
        this(context, null);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDefaultImageViewHeight = (int) getResources().getDimension(
                R.dimen.size_default_height);
    }

    /**
     * 设置可拉伸的图片
     */
    public void setParallaxImageView(ImageView iv) {
        this.mImageView = iv;
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 设置缩放级别 -- 控制图片的最大拉伸度 在界面加载完毕的时候调用
     */
    public void setZoomRatio(double zoomRatio) {
        if (mImageViewHeight == -1) {
            mImageViewHeight = mImageView.getHeight();
            if (mImageViewHeight < 0) {
                mImageViewHeight = mDefaultImageViewHeight;
            }

            mDrawableMaxHeight = (int) (zoomRatio*mImageViewHeight);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 监听ListView的滑动
        // 如何控制图片减小的高度？---监听listView头部划出去的距离
        View header = (View) mImageView.getParent();
        // 头部划出去的距离 ---<0
        if (header.getTop() < 0 && mImageView.getHeight() > mImageViewHeight) {// 大于初始的高度
            mImageView.getLayoutParams().height = Math.max(mImageView.getHeight()
                    + header.getTop(), mImageViewHeight);
            // 调整ImageView所在的容器的高度
            header.layout(header.getLeft(), 0, header.getRight(),
                    header.getHeight());
            // 重新调整ImageView的高度
            mImageView.requestLayout();
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // ListView,ScrollView滑动过头的时候调用
        // 不断的控制ImageView的高度
        boolean isCollapse = resizeOverScrollBy(deltaX, deltaY, scrollX,
                scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
                maxOverScrollY, isTouchEvent);

        // return true 下拉到某一个地方的时候不再往下拉
        return isCollapse ? true : super.overScrollBy(deltaX, deltaY, scrollX,
                scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
                maxOverScrollY, isTouchEvent);
    }

    private boolean resizeOverScrollBy(int deltaX, int deltaY, int scrollX,
                                       int scrollY, int scrollRangeX, int scrollRangeY,
                                       int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // 下拉的过程当中，不断的控制ImageView的高度
        /**
         * deltaY 是在超出滑动的时候每秒滑动的距离 (- 往上拉 +往下拉) 大小根据用户滑动的速度决定的 一般滑动50~-50
         */
        if (deltaY < 0) {
            if(mDrawableMaxHeight != -1){
                if(mImageView.getLayoutParams().height>=mDrawableMaxHeight){
                    // 当不能再往下拉的时候开启一个放大动画
                    mImageView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start();
                    return true;
                }
            }

            mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
            // 重新调整ImageView的高度
            mImageView.requestLayout();
        } else {
            // 不松开往上拉的时候应该将图片的高度不断的减小
            if (mImageView.getHeight() > mImageViewHeight) {
                mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY > mImageViewHeight ? mImageView
                        .getHeight() - deltaY
                        : mImageViewHeight;
                mImageView.requestLayout();
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            // 手指松开--让拉伸的图片动画的减少高度
            // 自定义动画
            if (mImageViewHeight - 1 < mImageView.getHeight()) {
                ResetAnimimation animation = new ResetAnimimation(mImageView,
                        mImageViewHeight);
                animation.setDuration(300);
                mImageView.startAnimation(animation);
                mImageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(350).start();
            }
        }
        return super.onTouchEvent(ev);
    }

    public class ResetAnimimation extends Animation {
        int targetHeight;
        int originalHeight;
        int extraHeight;
        View mView;

        protected ResetAnimimation(View view, int targetHeight) {
            // 图片动画的减小高度
            this.mView = view;
            // 动画执行之后的高度
            this.targetHeight = targetHeight;
            // 动画执行之前的高度
            originalHeight = view.getHeight();
            // 高度差
            extraHeight = this.targetHeight - originalHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            /** originalHeight ~~~~ targetHeight
             *  0ms            ~~~~ 300ms
             *  150ms ---> originalHeight - extraHeight*1/2
             *  interpolatedTime 变化比例（0.0~1.0）
             */

            int newHeight = (int) (targetHeight - extraHeight
                    * (1 - interpolatedTime));
            mView.getLayoutParams().height = newHeight;
            mView.requestLayout();
        }
    }
}
