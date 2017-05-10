package com.zys.jym.lanhu.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/20.
 */

public class SavePicUtil {
    static String TAG="TAG--SavePicUtil";

    public static void SavePic(final Activity context, String url, final int position, final On_Save_ClickListener listener) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0x1);
            return;
        }
        OkHttpUtils
                .get()
                .url(LHHttpUrl.IMG_URL+url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath()+"/lh/CodeImg/",
                        TimeUtil.getTimeStamp()+"_LH_codeImg.png") {
                    @Override
                    public void inProgress(float progress, long l) {
//                        MyUtils.Loge(TAG,"progress="+progress+",l="+l);
                        if (listener!=null){
                            listener.onProgress(progress,l);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                        if (listener!=null){
                            listener.onFail(call, e,position+1);
                        }
                    }

                    @Override
                    public void onResponse(File file) {
                        if (listener!=null){
                            listener.onSuccess(file,position+1);
                        }
                        // 最后通知图库更新
                        Uri localUri = Uri.fromFile(file);
                        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                        context.sendBroadcast(localIntent);

                    }
                });
    }

    public static abstract class On_Save_ClickListener {
        /**
         * 确定
         */
        public abstract void onSuccess(File file,int nextPosition);

        /**
         * 取消
         */
        public abstract void onFail(Call call, Exception e,int nextPosition);

        public abstract void onProgress(float progress, long l);
    }
}
