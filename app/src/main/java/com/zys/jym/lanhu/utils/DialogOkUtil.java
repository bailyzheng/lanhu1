package com.zys.jym.lanhu.utils;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zys.jym.lanhu.R;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DialogOkUtil {


    /**
     * 帶確定和取消按鈕的对话框
     * @param msg
     */
    public static Dialog show_OK_NO_Dialog(Activity activity, String msg, final On_OK_N0_ClickListener listener,String title) {
        Dialog dialog=null;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.view_delete_dialog, null);// 得到加载view
        final Button btn_ok= (Button) v.findViewById(R.id.btn_ok);
        final Button btn_no= (Button) v.findViewById(R.id.btn_no);
        final TextView tv_msg= (TextView) v.findViewById(R.id.tv_msg);
        final TextView tv_dialog_title=(TextView)v.findViewById(R.id.tv_dialog_title);//标题
        dialog = new Dialog(activity, R.style.custom_dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(v);
        if (!TextUtils.isEmpty(msg)){
            tv_msg.setText(msg);// 设置加载信息
        }
        final Dialog finalDialog = dialog;
        if(!TextUtils.isEmpty(title)){
            tv_dialog_title.setText(title);
        }else{
            tv_dialog_title.setText("温馨提示");
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onOk();
                    }
                    finalDialog.dismiss();

                }
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onNo();
                    }
                    finalDialog.dismiss();
                }
            }
        });
        return dialog;
    }


    /**
     * 带确定按钮的对话框
     * @param activity
     * @param msg
     * @param listener
     */
    public static Dialog show_Ok_Dialog(Activity activity, String msg, final On_OK_ClickListener listener,String title) {
        Dialog dialog = null;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.view_dialog, null);// 得到加载view
        final Button btn_ok= (Button) v.findViewById(R.id.btn_ok);
        final TextView tv_msg= (TextView) v.findViewById(R.id.tv_msg);
        final TextView tv_finsh= (TextView) v.findViewById(R.id.tv_finsh);
        final TextView tv_dialog2_title=(TextView)v.findViewById(R.id.tv_dialog2_title);
        dialog = new Dialog(activity, R.style.custom_dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(v);
        if (!TextUtils.isEmpty(msg)){
            tv_msg.setText(msg);// 设置加载信息
        }

        final Dialog finalDialog = dialog;
        if(!TextUtils.isEmpty(title)){
            tv_dialog2_title.setText(title);
        }else {
            tv_dialog2_title.setText("温馨提示");
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onOk();
                    }
                    finalDialog.dismiss();

                }
            }
        });
        tv_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    finalDialog.dismiss();
                }
            }
        });
        return dialog;
    }

    /**
     * 带finsh按钮的对话框
     * @param activity
     */
    public static Dialog show_finsh_Dialog(Activity activity) {
        Dialog dialog = null;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.view_havecode_dialog, null);// 得到加载view
        final TextView tv_finsh= (TextView) v.findViewById(R.id.tv_finsh);

        dialog = new Dialog(activity, R.style.custom_dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(v);
        final Dialog finalDialog = dialog;
        tv_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });
        return finalDialog;
    }



    /**
     * 选择支付方式的对话框
     */
    public static Dialog show_PayType_Dialog(Activity activity,final On_PayType_ClickListener listener) {
        Dialog dialog=null;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.view_paytype_dialog, null);// 得到加载view
        final Button btn_wx= (Button) v.findViewById(R.id.btn_wx);
        final Button btn_zfb= (Button) v.findViewById(R.id.btn_zfb);
        final Button btn_ye= (Button) v.findViewById(R.id.btn_ye);
        final TextView tv_finsh= (TextView) v.findViewById(R.id.tv_finsh);
        dialog = new Dialog(activity, R.style.custom_dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(v);

        final Dialog finalDialog = dialog;
        tv_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    finalDialog.dismiss();
                }
            }
        });
        btn_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onWxPay();
                    }
                    finalDialog.dismiss();
                }
            }
        });
        btn_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onAliPay();
                    }
                    finalDialog.dismiss();
                }
            }
        });
        btn_ye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onHuBiPay();
                    }
                    finalDialog.dismiss();

                }
            }
        });
        return finalDialog;
    }



    /**
     * 发现新版本提示框
     * @param activity
     */
    public static Dialog show_update_Dialog(Activity activity,String version, String msg, final On_Update_ClickListener listener) {
        Dialog dialog = null;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.view_update_dialog, null);// 得到加载view
        final TextView tv_msg= (TextView) v.findViewById(R.id.tv_msg);
        TextView btn_cancle= (TextView) v.findViewById(R.id.btn_cancle);
        TextView btn_down= (TextView) v.findViewById(R.id.btn_down);
        TextView tv_code=(TextView) v.findViewById(R.id.tv_code);
        if (!TextUtils.isEmpty(version)){
            tv_code.setText("版本更新 v"+version);
        }
        if (!TextUtils.isEmpty(msg)){
            tv_msg.setText(msg);// 设置加载信息
        }
        dialog = new Dialog(activity, R.style.custom_dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(v);
        final Dialog finalDialog = dialog;
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onCancle();
                    }
                    finalDialog.dismiss();
                }
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()){
                    if (listener!=null){
                        listener.onDownload();
                    }
                    finalDialog.dismiss();
                }
            }
        });
        return finalDialog;
    }



    public static abstract class On_OK_N0_ClickListener {
        /**
         * 确定
         */
        public abstract void onOk();

        /**
         * 取消
         */
        public abstract void onNo();
    }

    public static abstract class On_OK_ClickListener {
        /**
         * 确定
         */
        public abstract void onOk();


    }
    public static abstract class On_PayType_ClickListener {
        /**
         * 微信支付
         */
        public abstract void onWxPay();
        /**
         * 支付宝支付
         */
        public abstract void onAliPay();
        /**
         * 余额支付
         */
        public abstract void onHuBiPay();
    }
    public static abstract class On_Finish_ClickListener {
        /**
         * 关闭dialog
         */
        public abstract void onFinish();

    }

    public static abstract class On_Update_ClickListener {
        /**
         * 取消
         */
        public abstract void onCancle();
        /**
         * 下载
         */
        public abstract void onDownload();

    }
}
