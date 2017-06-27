package com.zys.jym.lanhu.activity;

<<<<<<< HEAD
=======
import android.Manifest;
import android.content.ComponentName;
>>>>>>> github/master
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.provider.MediaStore;
>>>>>>> github/master
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
<<<<<<< HEAD
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
=======
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
>>>>>>> github/master
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.Constants;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
<<<<<<< HEAD
import cn.sharesdk.wechat.moments.WechatMoments;
=======
import cn.sharesdk.tencent.qzone.QZone;

import static com.umeng.socialize.utils.DeviceConfig.context;
>>>>>>> github/master


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
    private String contents;
<<<<<<< HEAD
    private Platform plat;
=======
>>>>>>> github/master
//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onStart(SHARE_MEDIA share_media) {
//
//        }
//
//        //        @Override
////        public void onStart(SHARE_MEDIA platform) {
////            //分享开始的回调
////        }
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//           MyUtils.Loge("plat","platform"+platform);
//
//            Toast.makeText(ShareActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(ShareActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
//            if(t!=null){
//               MyUtils.Loge("throw","throw:"+t.getMessage());
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(ShareActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//        }
//    };

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
        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getRegcode())) {
            tv_yqmcode.setText(getApplicationContext().getUser().getRegcode() + "");//二维码中包含的文本信息
            contents = getApplicationContext().getUser().getAppurl();
        }

        try {
            //调用方法createCode生成二维码
            Bitmap bm = createCode(contents, logo, BarcodeFormat.QR_CODE);
            //将二维码在界面中显示
            iv_xzcode.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.Loge("aaa","e:"+e.getMessage());
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ql:
//                share(2);
<<<<<<< HEAD
                plat = ShareSDK.getPlatform(QQ.NAME);
=======
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
>>>>>>> github/master
                shareQQ(plat.getName());
                break;
            case R.id.tv_pyq:
                plat = ShareSDK.getPlatform(WechatMoments.NAME);
                shareQQ(plat.getName());
//                share(1);

                break;
            case R.id.tv_fz:
                MyUtils.copyText(ShareActivity.this, tv_yqmcode.getText().toString());
                MyUtils.showToast(ShareActivity.this, "复制成功");
                break;
        }
    }

    /**
     * 分享到QQ
     */
    private void shareQQ(String platform) {
//        if(Build.VERSION.SDK_INT>=23){
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);
//        }
//        MyUtils.Loge("aaa", "友盟版本号:"+SocializeConstants.SDK_VERSION);


//        new ShareAction(ShareActivity.this).setPlatform(SHARE_MEDIA.QQ)
//                .withText("hello")
//                .setCallback(umShareListener)
//                .withMedia(new UMImage(ShareActivity.this,getApplicationContext().getUser().getAppurl()))
////                .setShareContent(new ShareContent().)
////                    .withTargetUrl(getApplicationContext().getUser().getAppurl())
//                .setShareContent(new ShareContent())
//                .share();
//        UMImage image1 = new UMImage(ShareActivity.this, R.mipmap.add_pic);//网络图片
//        UMImage thumb =  new UMImage(this, R.mipmap.add_pic);
////        image1.setThumb(thumb);
//        UMWeb web = new UMWeb(getApplicationContext().getUser().getAppurl());
//        web.setTitle("This is music title");//标题
//        web.setThumb(thumb);  //缩略图
//        web.setDescription("my description");//描述
//        new ShareAction(ShareActivity.this).withText("hello")
//                .setCallback(umShareListener)
//                .withMedia(web).share();
        OnekeyShare oks = new OnekeyShare();
        if (platform != null)
            oks.setPlatform(platform);
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle("分享");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
            oks.show(this);

    }

    private void share(int type) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        MyUtils.Loge("aaa","url:"+getApplicationContext().getUser().getAppurl());
        webpageObject.webpageUrl = getApplicationContext().getUser().getAppurl();  //http://58.216.6.12/imtt.dd.qq.com/16891/A070E8F02623900187C8A12EBAFC618A.apk?mkey=58f1f4ddad724b41&f=6d17&c=0&fsname=com.zys.jym.lanhu_2.2_10.apk&csr=1bbd&p=.apk
//        webpageObject.webpageUrl ="http://58.216.6.12/imtt.dd.qq.com/16891/A070E8F02623900187C8A12EBAFC618A.apk?mkey=58f1f4ddad724b41&f=6d17&c=0&fsname=com.zys.jym.lanhu_2.2_10.apk&csr=1bbd&p=.apk";
        WXMediaMessage message = new WXMediaMessage(webpageObject);
        message.title = "这个软件加粉不要钱，还能自动删除僵尸粉。我的邀请码：" + getApplicationContext().getUser().getRegcode();//微商必备神器免费爆粉软件，蓝狐微商。邀请码：
        if (type == 2) {
            message.description = "每天领取120个散客人脉，月领3600人，点击免费领取";//加人，加群，被加，微商必备神器！邀请好友免费送置顶，注册就可免费加粉
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
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
           MyUtils.Loge("plat", "platform" + platform);
            MyUtils.showToast(ShareActivity.this, "分享成功");

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            MyUtils.showToast(ShareActivity.this, "分享失败");
            if (t != null) {
               MyUtils.Loge("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            MyUtils.showToast(ShareActivity.this, "取消分享");
        }
    };
    


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

    /**
     * 分享回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 分享权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                for(int i=0;i<grantResults.length;i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.v("stones", "权限回调--获取权限失败");
                        Toast.makeText(ShareActivity.this, "请打开手机设置，权限管理，允许蓝狐微商读取、写入和删除联系人信息后再使用立即加粉", Toast.LENGTH_SHORT).show();

                    } else {
                        UMImage thumb = new UMImage(this, R.mipmap.add_pic);
//        image1.setThumb(thumb);
                        UMWeb web = new UMWeb(getApplicationContext().getUser().getAppurl());
                        web.setTitle("This is music title");//标题
                        web.setThumb(thumb);  //缩略图
                        web.setDescription("my description");//描述
                        new ShareAction(ShareActivity.this).withText("hello")
                                .setCallback(umShareListener)
                                .withMedia(web).share();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.delect(this);
        super.onDestroy();
    }
}
