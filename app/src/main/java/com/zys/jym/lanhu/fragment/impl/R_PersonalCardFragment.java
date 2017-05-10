package com.zys.jym.lanhu.fragment.impl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;

import org.w3c.dom.Text;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Administrator on 2017/1/22.
 */

public class R_PersonalCardFragment extends BaseFragment implements View.OnClickListener {
    String TAG = "TAG--R_PersonalCardFragment";
    private ImageView iv_erweima;
    private static TextView selectTV;
    private EditText titleET, miaoshuET;
    private Button fabuBtn;
    static String p = "", p_id = "";
    static String c = "", c_id = "";

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_PC_ACTIVITY:
                    p = msg.getData().getString("p");
                    c = msg.getData().getString("c");
                    p_id = msg.getData().getString("p_id");
                    c_id = msg.getData().getString("c_id");
                    selectTV.setText(p + c);
                    break;
                case 0x101:
                    p = "";
                    c = "";
                    p_id = "";
                    c_id = "";
                    break;
            }
        }
    };

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_rpersonalcard, null);
        iv_erweima = (ImageView) view.findViewById(R.id.iv_erweima);
        iv_erweima.setOnClickListener(this);
        selectTV = (TextView) view.findViewById(R.id.personal_address_tv);
        selectTV.setOnClickListener(this);
        titleET = (EditText) view.findViewById(R.id.personal_title_ed);
        miaoshuET = (EditText) view.findViewById(R.id.personal_miaoshu_ed);
        fabuBtn = (Button) view.findViewById(R.id.sure_fabu_btn);
        fabuBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyUtils.Loge(TAG, "R_PersonalCardFragment--onResume");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_erweima:
                // 从相册中去获取
                MyUtils.Loge(TAG,"进入上传图像的按钮");
                if (ContextCompat.checkSelfPermission(mActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    EasyPermissions.requestPermissions(mActivity, "请允许申请读取图片权限，否则本应用无法进行图片上传发布",
                            0x13, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    MediaUtil.doPickPhotoFromGallery(mActivity);
                }
                break;
            case R.id.personal_address_tv:
                MyUtils.Loge(TAG,"进入地区选择界面");
                Intent in = new Intent(mActivity, SelectP2Activity.class);
                in.putExtra("inData", 6);
                mActivity.startActivity(in);
                break;
            case R.id.sure_fabu_btn:

                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case MediaUtil.SELECT_PHOTO_CODE:// 相册
                case MediaUtil.CAMERA_REQUEST_CODE:// 相机
                    MediaUtil.doCropPhotore(getActivity(),data);
                    ReleasePager.setImg(data);
//                    mdata=data;

                    break;
                case MediaUtil.PHOTO_CROP:// 剪裁
                    ReleasePager.setImg(data);
//                    mdata=data;
                    break;
            }
        }
    }
}
