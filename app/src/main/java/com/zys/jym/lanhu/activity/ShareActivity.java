package com.zys.jym.lanhu.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.Constants;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;


/**
 * 推荐有礼
 * Created by Administrator on 2016/12/18.
 */

public class ShareActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "TAG--ShareActivity";
    Toolbar index_toolbar;
    UMShareAPI mShareAPI;
    UMImage image;
    ImageView iv_xzcode;
    TextView tv_yqmcode, tv_fz;
    private Bitmap logo;
    private IWXAPI api;
    private static final int IMAGE_HALFWIDTH = 40;//宽度值，影响中间图片大小

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.ac_share);
        ActivityUtil.add(this);
        initToolBar();
        initViews();
        initData();
    }

    private void initToolBar() {
        index_toolbar = (Toolbar) findViewById(R.id.index_toolbar);
        index_toolbar.setTitle("");
        index_toolbar.setTitleTextColor(Color.WHITE);
        index_toolbar.setNavigationIcon(R.mipmap.backimg);
        setSupportActionBar(index_toolbar);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        tv_yqmcode = (TextView) findViewById(R.id.tv_yqmcode);
        tv_fz = (TextView) findViewById(R.id.tv_fz);
        iv_xzcode = (ImageView) findViewById(R.id.iv_xzcode);
        findViewById(R.id.tv_pyq).setOnClickListener(this);
        findViewById(R.id.tv_ql).setOnClickListener(this);
        tv_fz.setOnClickListener(this);

    }

    private void initData() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        logo = BitmapFactory.decodeResource(super.getResources(), R.mipmap.logo);
        mShareAPI = UMShareAPI.get(ShareActivity.this);
        image = new UMImage(ShareActivity.this, R.mipmap.ic_share_logo);//资源文件
        tv_yqmcode.setText(getApplicationContext().getUser().getRegcode() + "");
        //二维码中包含的文本信息
        String contents = getApplicationContext().getUser().getAppurl();
        try {
            //调用方法createCode生成二维码
            Bitmap bm = createCode(contents, logo, BarcodeFormat.QR_CODE);
            //将二维码在界面中显示
            iv_xzcode.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ql:
                share(2);
                break;
            case R.id.tv_pyq:
                share(1);
                break;
            case R.id.tv_fz:
                MyUtils.copyText(ShareActivity.this, tv_yqmcode.getText().toString());
                MyUtils.showToast(ShareActivity.this, "复制成功");
                break;
        }
    }

    private void share(int type) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        MyUtils.Loge("aaa","url:"+getApplicationContext().getUser().getAppurl());
        webpageObject.webpageUrl = getApplicationContext().getUser().getAppurl();  //http://58.216.6.12/imtt.dd.qq.com/16891/A070E8F02623900187C8A12EBAFC618A.apk?mkey=58f1f4ddad724b41&f=6d17&c=0&fsname=com.zys.jym.lanhu_2.2_10.apk&csr=1bbd&p=.apk
//        webpageObject.webpageUrl ="http://58.216.6.12/imtt.dd.qq.com/16891/A070E8F02623900187C8A12EBAFC618A.apk?mkey=58f1f4ddad724b41&f=6d17&c=0&fsname=com.zys.jym.lanhu_2.2_10.apk&csr=1bbd&p=.apk";
        WXMediaMessage message = new WXMediaMessage(webpageObject);
        message.title = "这个软件加粉不要钱，还免费检测僵尸粉哦！我的邀请码：" + getApplicationContext().getUser().getRegcode();//微商必备神器免费爆粉软件，蓝狐微商。邀请码：
        if (type == 2) {
            message.description = "每天送120个全国散客，月加3600人，点通过点到手软，点击免费下载";//加人，加群，被加，微商必备神器！邀请好友免费送置顶，注册就可免费加粉
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_share_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
        bitmap.recycle();
        message.thumbData = bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = message;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        api.sendReq(req);

    }

    private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            Log.d("plat", "platform" + platform);
//            MyUtils.showToast(ShareActivity.this, "分享成功");
//
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            MyUtils.showToast(ShareActivity.this, "分享失败");
//            if (t != null) {
//                Log.d("throw", "throw:" + t.getMessage());
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            MyUtils.showToast(ShareActivity.this, "取消分享");
//        }
//    };


    /**
     * 　　* 生成二维码
     * 　　* @param string 二维码中包含的文本信息
     * 　　* @param mBitmap logo图片
     * 　　* @param format 编码格式
     * 　　* @return Bitmap 位图
     * 　　* @throws WriterException
     */
    public Bitmap createCode(String string, Bitmap mBitmap, BarcodeFormat format)
            throws Exception {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable hst = new Hashtable();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 600, 600, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
                    //记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.delect(this);
        super.onDestroy();
    }
}
