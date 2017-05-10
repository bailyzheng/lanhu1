package com.zys.jym.lanhu.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.zys.jym.lanhu.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/12/19.
 */

public class SavePicUtils {
    Context context;
    public SavePicUtils(Context context){
        this.context=context;
    }

    private class SaveImageTask extends AsyncTask<Bitmap, Void, String> {
        File file=null;
        @Override
        protected String doInBackground(Bitmap... params) {
            String result = context.getResources().getString(R.string.save_picture_failed);
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();
                file = new File(sdcard + "/LHIMG/CodeImg");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File imageFile = new File(file.getAbsolutePath(),TimeUtil.timeStamp()+"_LH_CodeImg.jpg");
                FileOutputStream outStream = null;
                outStream = new FileOutputStream(imageFile);
                Bitmap image = params[0];
                image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
//                result = context.getResources().getString(R.string.save_picture_success,  file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            MyUtils.showToast(context, result);

            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + file.getAbsolutePath())));
//            iv_mycode.setDrawingCacheEnabled(false);
        }
    }
}
