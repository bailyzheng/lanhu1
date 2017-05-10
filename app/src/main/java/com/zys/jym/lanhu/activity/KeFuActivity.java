package com.zys.jym.lanhu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.kefu.ImageUtil;
import com.zys.jym.lanhu.activity.kefu.ReWebChomeClient;
import com.zys.jym.lanhu.utils.ActivityUtil;

import java.io.File;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/18.
 */

public class KeFuActivity extends BaseActivity implements ReWebChomeClient.OpenFileChooserCallBack{
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    String TAG="TAG--KeFuActivity";
    Toolbar index_toolbar;
    WebView wv_kefu;
    KeFuActivity kfa;
    private float multiple;
    String url="";
    int currentapiVersion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_kefu);
        kfa=this;
        ActivityUtil.add(kfa);
        // 获取屏幕测量工具DisplayMetrics
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;//屏幕密度系数
        multiple = density / 2;

        initViews();
//        initToolBar();
        initData();
    }

//    private void initToolBar() {
//        index_toolbar= (Toolbar) findViewById(R.id.index_toolbar);
//        index_toolbar.setTitle("");
//        index_toolbar.setTitleTextColor(Color.WHITE);
//        index_toolbar.setNavigationIcon(R.mipmap.backimg);
//        setSupportActionBar(index_toolbar);
//        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }

    private void initViews() {
        currentapiVersion=android.os.Build.VERSION.SDK_INT;
        fixDirPath();
        wv_kefu= (WebView) findViewById(R.id.wv_kefu);
    }

    private void initData() {

        url=getApplicationContext().getUser().getUrl();
        if (!TextUtils.isEmpty(url)){
            // 加载需要显示的网页
            wv_kefu.loadUrl(url);

            wv_kefu.setWebViewClient(new KFWebViewClient());
//分别处理不同版本的手机
            if(currentapiVersion<21) {
                wv_kefu.getSettings().setJavaScriptEnabled(true);
                wv_kefu.setWebChromeClient(new ReWebChomeClient(this));
            }else{
                initWebView(wv_kefu);
            }
//            wv_kefu.setWebViewClient(new KFWebViewClient());
//            WebSettings settings = wv_kefu.getSettings();
//            settings.setJavaScriptEnabled(true);// 表示支持js
////        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
//            settings.setUseWideViewPort(true);// 支持双击缩放
        }

    }
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && wv_kefu.canGoBack()) {
                // goBack()表示返回WebView的上一页面
                wv_kefu.goBack();
                return true;
            } else {
                if (wv_kefu != null) {
                    wv_kefu.destroy();
                }
                kfa.finish();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if(mUploadMsg ==null){
                        return;
                    }
                    String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                    if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                        break;
                    }
                    Uri uri = Uri.fromFile(new File(sourcePath));
                    mUploadMsg.onReceiveValue(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case Controller.FILE_SELECTED:
                if (mUploadHandler != null) {
                    mUploadHandler.onResult(resultCode, data);
                }

        }
    }



    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("选择");//R.string.options
        String[] choices;
        choices = new String[2];
        choices[0] = "拍照"; // 拍照
        choices[1] = "相册"; // 从相册中选择
        alertDialog.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mSourceIntent = ImageUtil.choosePicture();
                            startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                        } else {
                            mSourceIntent = ImageUtil.takeBigPicture();
                            startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                        }
                    }
                }
        );
        alertDialog.show();
    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg!= null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
        }
    }



    private void initWebView(WebView webView) {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    UploadHandler mUploadHandler;

    class MyWebChromeClient extends WebChromeClient {
        public MyWebChromeClient() {

        }

        private String getTitleFromUrl(String url) {
            String title = url;
            try {
                URL urlObj = new URL(url);
                String host = urlObj.getHost();
                if (host != null && !host.isEmpty()) {
                    return urlObj.getProtocol() + "://" + host;
                }
                if (url.startsWith("file:")) {
                    String fileName = urlObj.getFile();
                    if (fileName != null && !fileName.isEmpty()) {
                        return fileName;
                    }
                }
            } catch (Exception e) {
            }
            return title;
        }
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            String newTitle = getTitleFromUrl(url);
            new AlertDialog.Builder(kfa).setTitle(newTitle).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false).create().show();
            return true;
        }
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            String newTitle = getTitleFromUrl(url);
            new AlertDialog.Builder(kfa).setTitle(newTitle).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).create().show();
            return true;
        }

        // Android 2.x
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, "", "filesystem");
        }

        // Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadHandler = new UploadHandler(new Controller());
            mUploadHandler.openFileChooser(uploadMsg, acceptType, capture);
        }

        // Android 4.4, 4.4.1, 4.4.2
        // openFileChooser function is not called on Android 4.4, 4.4.1, 4.4.2,
        // you may use your own java script interface or other hybrid framework.

        // Android 5.0.1
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {

            String acceptTypes[] = fileChooserParams.getAcceptTypes();

            String acceptType = "";
            for (int i = 0; i < acceptTypes.length; ++ i) {
                if (acceptTypes[i] != null && acceptTypes[i].length() != 0)
                    acceptType += acceptTypes[i] + ";";
            }
            if (acceptType.length() == 0)
                acceptType = "*/*";

            final ValueCallback<Uri[]> finalFilePathCallback = filePathCallback;

            ValueCallback<Uri> vc = new ValueCallback<Uri>() {

                @Override
                public void onReceiveValue(Uri value) {

                    Uri[] result;
                    if (value != null)
                        result = new Uri[]{value};
                    else
                        result = null;

                    finalFilePathCallback.onReceiveValue(result);

                }
            };

            openFileChooser(vc, acceptType, "filesystem");


            return true;
        }
    };

    class Controller {
        final static int FILE_SELECTED = 4;

        Activity getActivity() {
            return kfa;
        }
    }

    class UploadHandler {
        /*
         * The Object used to inform the WebView of the file to upload.
         */
        private ValueCallback<Uri> mUploadMessage;
        private String mCameraFilePath;
        private boolean mHandled;
        private boolean mCaughtActivityNotFoundException;
        private Controller mController;
        public UploadHandler(Controller controller) {
            mController = controller;
        }
        String getFilePath() {
            return mCameraFilePath;
        }
        boolean handled() {
            return mHandled;
        }
        void onResult(int resultCode, Intent intent) {
            if (resultCode == Activity.RESULT_CANCELED && mCaughtActivityNotFoundException) {
                // Couldn't resolve an activity, we are going to try again so skip
                // this result.
                mCaughtActivityNotFoundException = false;
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            // As we ask the camera to save the result of the user taking
            // a picture, the camera application does not return anything other
            // than RESULT_OK. So we need to check whether the file we expected
            // was written to disk in the in the case that we
            // did not get an intent returned but did get a RESULT_OK. If it was,
            // we assume that this result has came back from the camera.
            if (result == null && intent == null && resultCode == Activity.RESULT_OK) {
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile);
                    // Broadcast to the media scanner that we have a new photo
                    // so it will be added into the gallery for the user.
                    mController.getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
                }
            }
            mUploadMessage.onReceiveValue(result);
            mHandled = true;
            mCaughtActivityNotFoundException = false;
        }
        void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            final String imageMimeType = "image/*";
            final String videoMimeType = "video/*";
            final String audioMimeType = "audio/*";
            final String mediaSourceKey = "capture";
            final String mediaSourceValueCamera = "camera";
            final String mediaSourceValueFileSystem = "filesystem";
            final String mediaSourceValueCamcorder = "camcorder";
            final String mediaSourceValueMicrophone = "microphone";
            // According to the spec, media source can be 'filesystem' or 'camera' or 'camcorder'
            // or 'microphone' and the default value should be 'filesystem'.
            String mediaSource = mediaSourceValueFileSystem;
            if (mUploadMessage != null) {
                // Already a file picker operation in progress.
                return;
            }
            mUploadMessage = uploadMsg;
            // Parse the accept type.
            String params[] = acceptType.split(";");
            String mimeType = params[0];
            if (capture.length() > 0) {
                mediaSource = capture;
            }
            if (capture.equals(mediaSourceValueFileSystem)) {
                // To maintain backwards compatibility with the previous implementation
                // of the media capture API, if the value of the 'capture' attribute is
                // "filesystem", we should examine the accept-type for a MIME type that
                // may specify a different capture value.
                for (String p : params) {
                    String[] keyValue = p.split("=");
                    if (keyValue.length == 2) {
                        // Process key=value parameters.
                        if (mediaSourceKey.equals(keyValue[0])) {
                            mediaSource = keyValue[1];
                        }
                    }
                }
            }
            //Ensure it is not still set from a previous upload.
            mCameraFilePath = null;
            if (mimeType.equals(imageMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamera)) {
                    // Specified 'image/*' and requested the camera, so go ahead and launch the
                    // camera directly.
                    startActivity(createCameraIntent());
                    return;
                } else {
                    // Specified just 'image/*', capture=filesystem, or an invalid capture parameter.
                    // In all these cases we show a traditional picker filetered on accept type
                    // so launch an intent for both the Camera and image/* OPENABLE.
                    Intent chooser = createChooserIntent(createCameraIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(videoMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamcorder)) {
                    // Specified 'video/*' and requested the camcorder, so go ahead and launch the
                    // camcorder directly.
                    startActivity(createCamcorderIntent());
                    return;
                } else {
                    // Specified just 'video/*', capture=filesystem or an invalid capture parameter.
                    // In all these cases we show an intent for the traditional file picker, filtered
                    // on accept type so launch an intent for both camcorder and video/* OPENABLE.
                    Intent chooser = createChooserIntent(createCamcorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(audioMimeType)) {
                if (mediaSource.equals(mediaSourceValueMicrophone)) {
                    // Specified 'audio/*' and requested microphone, so go ahead and launch the sound
                    // recorder.
                    startActivity(createSoundRecorderIntent());
                    return;
                } else {
                    // Specified just 'audio/*',  capture=filesystem of an invalid capture parameter.
                    // In all these cases so go ahead and launch an intent for both the sound
                    // recorder and audio/* OPENABLE.
                    Intent chooser = createChooserIntent(createSoundRecorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
                    startActivity(chooser);
                    return;
                }
            }
            // No special handling based on the accept type was necessary, so trigger the default
            // file upload chooser.
            startActivity(createDefaultOpenableIntent());
        }
        private void startActivity(Intent intent) {
            try {
                mController.getActivity().startActivityForResult(intent, Controller.FILE_SELECTED);
            } catch (ActivityNotFoundException e) {
                // No installed app was able to handle the intent that
                // we sent, so fallback to the default file upload control.
                try {
                    mCaughtActivityNotFoundException = true;
                    mController.getActivity().startActivityForResult(createDefaultOpenableIntent(),
                            Controller.FILE_SELECTED);
                } catch (ActivityNotFoundException e2) {
                    // Nothing can return us a file, so file upload is effectively disabled.
                    Toast.makeText(mController.getActivity(), "选择失败",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        private Intent createDefaultOpenableIntent() {
            // Create and return a chooser with the default OPENABLE
            // actions including the camera, camcorder and sound
            // recorder where available.
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
                    createSoundRecorderIntent());
            chooser.putExtra(Intent.EXTRA_INTENT, i);
            return chooser;
        }
        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE,
                    "选择上传");//mController.getActivity().getResources().getString(R.string.choose_upload)
            return chooser;
        }
        private Intent createOpenableIntent(String type) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            return i;
        }
        private Intent createCameraIntent() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File externalDataDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
            File cameraDataDir = new File(externalDataDir.getAbsolutePath() +
                    File.separator + "browser-photos");
            cameraDataDir.mkdirs();
            mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator +
                    System.currentTimeMillis() + ".jpg";
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
            return cameraIntent;
        }
        private Intent createCamcorderIntent() {
            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }
        private Intent createSoundRecorderIntent() {
            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        }
    }
    private class TouchListenerImp implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
//            MyUtils.Loge(TAG, "点击位置x=" + event.getX() + "y=" + event.getY());
            if (event.getX() < (80*multiple) && event.getY() < (65*multiple)) {
//                MyUtils.Loge(TAG, "返回按钮");
                kfa.finish();
            }
            return false;
        }
    }
    class KFWebViewClient extends WebViewClient {
        @Override

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);

        }



        @Override

        public void onPageFinished(WebView view, String url) {
            wv_kefu.setOnTouchListener(new TouchListenerImp());
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(kfa);
    }
}
