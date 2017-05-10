package com.zys.jym.lanhu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zys.jym.lanhu.R;

/**
 * Created by Administrator on 2017/1/2.
 */

public class LoweImageView extends ImageView {

//    <com.zys.jym.lanhu.view.LoweImageView
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    android:id="@+id/iv_ewmcode"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:layout_marginRight="50dp"
//    android:layout_marginLeft="50dp"
//    android:scaleType="fitXY"
//    app:ratio="0.75"/>

    /**
     * 图片比例. 比例=宽/高
     */
    private float mRatio;

    public LoweImageView(Context context) {
        this(context, null);
    }

    public LoweImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoweImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     *            上下文
     * @param attrs
     *            属性
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LoweImageView);
        mRatio = typedArray.getFloat(R.styleable.LoweImageView_ratio, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 宽大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 高大小
        int heightSize;
        // 只有宽的值是精确的才对高做精确的比例校对
        if (widthMode == MeasureSpec.EXACTLY && mRatio > 0) {
            heightSize = (int) (widthSize / mRatio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
