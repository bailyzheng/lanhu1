package com.zys.jym.lanhu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.zys.jym.lanhu.R;


/**
 * Created by simaben on 2/12/15.
 */
public class LoginEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {
    private static final int MODE_CLEAN = 0;
    private static final int MODE_PWD = 1;
    /**
     * 删除按钮的引用
     */
    private Drawable clearImg;
    private int defaultMode;
    /**
     * 密码按钮的引用
     */
    private Drawable pwdImg;

    private Drawable pwdShow;
    private Drawable pwdHide;

    private boolean isShow = true;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    public LoginEditText(Context context) {
        this(context, null);
    }

    public LoginEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public LoginEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText, 0, 0);
        try {
            defaultMode = a.getInt(R.styleable.ClearEditText_mode, MODE_CLEAN);
            setMode();
        } finally {
            a.recycle();
        }


    }

    private void setMode() {
        if (defaultMode == MODE_CLEAN) {
            clearImg = getCompoundDrawables()[2];
            if (clearImg == null) {
                clearImg = getResources().getDrawable(R.drawable.clear_edittext_img);
            }
            clearImg.setBounds(0, 0, clearImg.getIntrinsicWidth(), clearImg.getIntrinsicHeight());

            //默认设置隐藏图标
            setClearIconVisible(false);
            //设置焦点改变的监听
            setOnFocusChangeListener(this);
            //设置输入框里面内容发生改变的监听
            addTextChangedListener(this);
        } else if (defaultMode == MODE_PWD) {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            pwdHide = getResources().getDrawable(R.mipmap.user_ic_pass_invisible);
            pwdShow = getResources().getDrawable(R.mipmap.uc_ic_pass_visible);

            pwdImg = pwdShow;

            setPwdDrawable();


        }
    }

    private void setPwdDrawable() {
        pwdImg.setBounds(0, 0, pwdImg.getIntrinsicWidth(), pwdImg.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], pwdImg, getCompoundDrawables()[3]);
    }


    /**
     * 按下的位置模拟点击事件，在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {

                    if (defaultMode == MODE_CLEAN) {
                        this.setText("");
                    } else if (defaultMode == MODE_PWD) {
                        if (isShow) {
                            pwdImg = pwdHide;
                            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            pwdImg = pwdShow;
                            setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        isShow = !isShow;
                        setPwdDrawable();

                        if (getText() != null && getText().equals("")) {
                            setSelection(getText().length());

                        }

                    }
                }


            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当EditText焦点变化时，判断字符串长度设置清除图标的显示与隐藏
     *
     * @param view
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            if (defaultMode == MODE_CLEAN) {
                setClearIconVisible(getText().length() > 0);
            }
        } else {
            if (defaultMode == MODE_CLEAN) {
                setClearIconVisible(false);
            }
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? clearImg : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调
     *
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (hasFoucs) {
            if (defaultMode == MODE_CLEAN) {
                setClearIconVisible(text.length() > 0);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
