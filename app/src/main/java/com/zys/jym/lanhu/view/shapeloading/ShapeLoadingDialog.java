package com.zys.jym.lanhu.view.shapeloading;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.zys.jym.lanhu.R;


/**
 * Created by zzz40500 on 15/6/15.
 */
public class ShapeLoadingDialog {



    private Context mContext;
    private Dialog mDialog;
    private LoadingView mLoadingView;
    private View mDialogContentView;


    public ShapeLoadingDialog(Context context) {
        this.mContext=context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        mDialogContentView= LayoutInflater.from(mContext).inflate(R.layout.layout_dialog,null);


        mLoadingView= (LoadingView) mDialogContentView.findViewById(R.id.loadView);
        mDialog.setContentView(mDialogContentView);
    }

    public void setBackground(int color){
        GradientDrawable gradientDrawable= (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence){
        mLoadingView.setLoadingText(charSequence);
    }

    public void show(){
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public Dialog getDialog(){
        return  mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        mDialog.setCanceledOnTouchOutside(cancel);
    }

	public boolean isShowing() {
		return mDialog.isShowing();
	}
}
