package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.adapter.GroupCode_vp_Adapter;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.fragment.impl.GroupFragment;
import com.zys.jym.lanhu.fragment.impl.PersonalFragment;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LXRUtil;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SaveCodeUtil;
import com.zys.jym.lanhu.utils.SavePicUtil;
import com.zys.jym.lanhu.view.NoScrollViewPager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

import static com.zys.jym.lanhu.activity.Main2Activity.app;

/**
 * Created by Administrator on 2016/12/16.
 */

public class GroupCodeActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "TAG--GroupCodeActivity";
    ViewPager vp_code;
    static List<TopData> mlist;
    static int position;
    int fSun, sSum;
    RelativeLayout rl_lc;
    ImageView iv_n1, iv_n2, iv_n3;
    boolean save_one = false;
    private static GroupCodeActivity gca;
    private static StringBuilder sb = new StringBuilder();

    public static Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    MyUtils.dismssDialog();
                    String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
                    String hcDate = (String) MySharedPrefrencesUtil.getParam(gca, "CLEAN_DATE", "");
                    MySharedPrefrencesUtil.setParam(gca, "CLEAN_DATE", currentDate);
                    sb.append(mlist.get(position).getPhone() + "^");
                    MySharedPrefrencesUtil.setParam(gca, "phone_data", MySharedPrefrencesUtil.getParam(gca, "phone_data", "") + sb.toString());
                    sb.delete(0, sb.length());
                    MyUtils.showToast(gca, "添加成功");
                    break;
                case 2:
                    //添加通讯录拒绝权限回调
                    MyUtils.dismssDialog();
                    MyUtils.showToast(gca, "请打开手机设置，权限管理，允许蓝狐微商读取、写入和删除联系人信息后再使用立即加粉");
                    break;
            }
        }
    };
    private int length_phone;

    /**
     * 保存手机号
     */
    private void toSavePhone(final List<TopData> mTopDataList) {
        String str = MySharedPrefrencesUtil.getParam(gca, "phone_data", "").toString();
        String[] temp = str.split("\\^");
        if (Arrays.asList(temp).contains(mlist.get(position).getPhone())) {
            MyUtils.showToast(gca, "该手机号您已经添加过");
            return;
        }
        //判断是不是VIP
//        if (app.getUser().getViptime().length() > 1) {//如果是
        if (app.getPurseData() != null && Long.parseLong(app.getPurseData().getViprest()) > 0) {//如果是
            if (temp.length >= 120) {
                MyUtils.showToast(gca, "您今天已经添加了120条，请明天再添加吧");
            } else {
                if (mTopDataList.size() <= (120 - temp.length)) {
                    if(mTopDataList.size()>5){
                        length_phone=5;
                    }else {
                        length_phone = mTopDataList.size();
                    }
                    addMyList();
                } else {
//                    length_phone = 120 - temp.length;
                    if((120-temp.length)>5){
                        length_phone=5;
                    }else {
                        length_phone=120-temp.length;
                    }
                    addMyList();
                }
            }
        } else {
            if (temp.length >= 60) {
                MyUtils.showToast(gca, "您今天已经添加了60条，请明天再添加吧");
            } else {
                if (mTopDataList.size() <= (60 - temp.length)) {
//                    length_phone = mTopDataList.size();
                    if(mTopDataList.size()>5){
                        length_phone=5;
                    }else {
                        length_phone = mTopDataList.size();
                    }
                    addMyList();
                } else {
//                    length_phone = 60 - temp.length;
                    if((60-temp.length)>5){
                        length_phone=5;
                    }else {
                        length_phone=60-temp.length;
                    }
                    addMyList();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_groupcode);
        ActivityUtil.add(this);
        gca = this;
        initViews();
        initData();
    }

    private void initViews() {
        vp_code = (ViewPager) findViewById(R.id.vp_code);
        findViewById(R.id.btn_save_one).setOnClickListener(this);
        findViewById(R.id.ll_save_ten).setOnClickListener(this);
        findViewById(R.id.btn_next_c).setOnClickListener(this);
        rl_lc = (RelativeLayout) findViewById(R.id.rl_lc);
        iv_n1 = (ImageView) findViewById(R.id.iv_n1);
        iv_n2 = (ImageView) findViewById(R.id.iv_n2);
        iv_n3 = (ImageView) findViewById(R.id.iv_n3);
    }

    private void initData() {
        mlist = (List<TopData>) getIntent().getSerializableExtra("TopList");
        position = getIntent().getIntExtra("p", 0);
        vp_code.setAdapter(new GroupCode_vp_Adapter(GroupCodeActivity.this, mlist, new GroupCode_vp_Adapter.OnLongListener() {
            @Override
            public void onLongC() {
                rl_lc.setVisibility(View.VISIBLE);
                iv_n1.setVisibility(View.VISIBLE);
            }
        }));
        vp_code.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_one:
                //save_one = true;
                //toSave(false);
                toSavePhone(mlist);
                break;
            case R.id.ll_save_ten:
                save_one = false;
                toSave(false);
//                saveEwm();
                break;
            case R.id.btn_next_c:
                if (iv_n1.getVisibility() == View.VISIBLE) {
                    iv_n1.setVisibility(View.GONE);
                    iv_n2.setVisibility(View.VISIBLE);
                    return;
                }
                if (iv_n2.getVisibility() == View.VISIBLE) {
                    iv_n2.setVisibility(View.GONE);
                    iv_n3.setVisibility(View.VISIBLE);
                    return;
                }

                if (iv_n3.getVisibility() == View.VISIBLE) {
                    rl_lc.setVisibility(View.GONE);
                    iv_n3.setVisibility(View.GONE);
                    return;
                }
                break;

        }
    }

    private void saveEwm() {
        if (mlist != null && mlist.size() > 0) {
            SavePicUtil.SavePic(this, mlist.get(position).getImgurl(), 0, new SavePicUtil.On_Save_ClickListener() {
                @Override
                public void onSuccess(File file, int nextPosition) {
                    SaveCodeUtil.insertCode(GroupCodeActivity.this, mlist.get(position).getId());
                    MyUtils.showToast(GroupCodeActivity.this, "已保存至" +
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/lh/CodeImg/"
                            + file.getName());
                }

                @Override
                public void onFail(Call call, Exception e, int nextPosition) {
                    MyUtils.showToast(GroupCodeActivity.this, "保存失败");
                }

                @Override
                public void onProgress(float progress, long l) {

                }
            });
        }
    }

    /**
     * 添加到通讯录
     */
    private void addMyList() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        0x1);
                return;
            } else {
                MyUtils.showDialog(GroupCodeActivity.this, "正在添加通讯录...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyUtils.Loge(TAG, "选中的电话号码：" + mlist.get(position));
                            LXRUtil.addContacts(GroupCodeActivity.this, mlist.get(position).getNickname(), mlist.get(position).getPhone(), false, 1, 3);
                        } catch (Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.showToast(GroupCodeActivity.this, "打开手机设置，找到已安装的应用，蓝狐微商，允许读取联系人信息，允许写入联系人权限，即可使用");
                        }
                    }
                }).start();
            }
        } else {
            MyUtils.showDialog(GroupCodeActivity.this, "正在添加通讯录...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyUtils.Loge(TAG, "选中的电话号码：" + mlist.get(position));
                        LXRUtil.addContacts(GroupCodeActivity.this, mlist.get(position).getNickname(), mlist.get(position).getPhone(), false, 1, 3);
                    } catch (Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.showToast(GroupCodeActivity.this, "打开手机设置，找到已安装的应用，蓝狐微商，允许读取联系人信息，允许写入联系人权限，即可使用");
                    }
                }
            }).start();
        }
    }

    private void toSave(boolean toHaveP) {
        try {
            if (save_one) {
                SavePicUtil.SavePic(GroupCodeActivity.this, mlist.get(position).getImgurl(), 0, new SavePicUtil.On_Save_ClickListener() {
                    @Override
                    public void onSuccess(File file, int nextPosition) {
                        SaveCodeUtil.insertCode(GroupCodeActivity.this, mlist.get(position).getId());
                        MyUtils.showToast(GroupCodeActivity.this, "已保存至" +
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/lh/CodeImg/"
                                + file.getName());
                    }

                    @Override
                    public void onFail(Call call, Exception e, int nextPosition) {
                        MyUtils.showToast(GroupCodeActivity.this, "保存失败");
                    }

                    @Override
                    public void onProgress(float progress, long l) {

                    }
                });
            } else {
                if (toHaveP) {
                    sSum = 0;
                    fSun = 0;
                    if (position < 25) {
                        save(0, mlist.size());
                    }
                    if (position >= 10) {
                        save((position / 10) * 10, mlist.size());
                    }
                } else {
                    DialogOkUtil.show_OK_NO_Dialog(GroupCodeActivity.this, "是否批量保存？", new DialogOkUtil.On_OK_N0_ClickListener() {
                        @Override
                        public void onOk() {
                            sSum = 0;
                            fSun = 0;
                            if (position < 10) {
                                save(0, mlist.size());
                            }
                            if (position >= 10) {
                                save((position / 10) * 10, mlist.size());
                            }
                        }

                        @Override
                        public void onNo() {

                        }
                    },"").show();
                }

            }
        } catch (Exception e) {

        }

    }

    private void save(int position, final int size) {
        try {
            if (position > size - 1) {
                MyUtils.showToast(GroupCodeActivity.this, "二维码已保存");
                return;
            }
            if (SaveCodeUtil.selectCountFormCodeId(GroupCodeActivity.this, mlist.get(position).getId()) != 0) {
                position++;
                save(position, mlist.size());
                return;
            }
            String url = mlist.get(position).getImgurl();
            final int finalPosition = position;
            SavePicUtil.SavePic(GroupCodeActivity.this, url, position, new SavePicUtil.On_Save_ClickListener() {
                @Override
                public void onSuccess(File file, int nextPosition) {
//                MyUtils.Loge(TAG,"图片"+nextPosition+"保存成功");
                    SaveCodeUtil.insertCode(GroupCodeActivity.this, mlist.get(finalPosition).getId());
                    sSum++;
                    if ((nextPosition + 1) / 10 > (finalPosition / 10)) {
                        MyUtils.showToast(GroupCodeActivity.this, "批量保存成功");
                        MyUtils.Loge(TAG, "图片" + mlist.get(finalPosition).getId() + "保存成功" + sSum + "张");
                        return;
                    }
                    if ((nextPosition + 1) > size) {
                        MyUtils.showToast(GroupCodeActivity.this, "批量保存成功");
                        MyUtils.Loge(TAG, "图片" + mlist.get(finalPosition).getId() + "保存成功" + sSum + "张");
                        return;
                    }

                    save(nextPosition, size);

                }

                @Override
                public void onFail(Call call, Exception e, int nextPosition) {
                    MyUtils.Loge(TAG, "图片" + nextPosition + "保存失败" + "----e=" + e.toString());

                    fSun++;
                    if ((nextPosition + 1) / 10 > (finalPosition / 10)) {
                        MyUtils.showToast(GroupCodeActivity.this, "二维码保存失败");
                        return;
                    }
                    if ((nextPosition + 1) > size) {
                        MyUtils.showToast(GroupCodeActivity.this, "二维码保存失败");
                        return;
                    }
                    save(nextPosition, size);
                }

                @Override
                public void onProgress(float progress, long l) {
                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 0x1:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    toSave(true);
//                } else {
//                    MyUtils.showToast(GroupCodeActivity.this, "权限获取失败,不能保存二维码");
//                }
//                break;
//
//        }
        switch (requestCode) {
            case 0x1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.v("stones", "权限回调--获取权限失败");
                    Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    Log.v("stones", "权限回调--获取权限成功");
                    MyUtils.showDialog(GroupCodeActivity.this, "正在添加通讯录...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyUtils.Loge(TAG, "选中的电话号码：" + mlist.get(position));
                                LXRUtil.addContacts(GroupCodeActivity.this, mlist.get(position).getNickname(), mlist.get(position).getPhone(), false, 1, 3);
                            } catch (Exception e) {
                                MyUtils.dismssDialog();
                                MyUtils.showToast(GroupCodeActivity.this, "打开手机设置，找到已安装的应用，蓝狐微商，允许读取联系人信息，允许写入联系人权限，即可使用");
                            }
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }
}
