package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.activity.pager.release.R_PersonalCardPager;
import com.zys.jym.lanhu.adapter.MainFragmentPagerAdapter;
import com.zys.jym.lanhu.bean.GetPurseData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.bean.UpdateApkData;
import com.zys.jym.lanhu.fragment.FragmentContainer;
import com.zys.jym.lanhu.fragment.impl.GroupFragment;
import com.zys.jym.lanhu.fragment.impl.HomeFragment;
import com.zys.jym.lanhu.fragment.impl.MineFragment;
import com.zys.jym.lanhu.fragment.impl.PersonalFragment;
import com.zys.jym.lanhu.fragment.impl.ReleaseFragment;
import com.zys.jym.lanhu.httpcallback.GetPurseCallback;
import com.zys.jym.lanhu.httpcallback.UpdateApkCallback;
import com.zys.jym.lanhu.receiver.NetWorkChangeReceiver;
import com.zys.jym.lanhu.service.UpdateService;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.LXRUtil;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;
import com.zys.jym.lanhu.utils.SaveCodeUtil;
import com.zys.jym.lanhu.utils.SavePicUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/1/22.
 */

public class Main2Activity extends BaseActivity implements View.OnClickListener {
    static String TAG = "TAG--Main2Activity";
    protected Stack<FragmentContainer> fragmentStack = new Stack<FragmentContainer>();
    protected FragmentContainer currentContainer;
    private RadioGroup rg_main;
    public RadioButton rb_home, rb_personal, rb_release, rb_group, rb_mine;
    private List<Fragment> mFragments;
    protected FragmentManager fragmentManager;
    private MainFragmentPagerAdapter mMainFragmentPagerAdapter;
    private long exitTime = 0;
    private LinearLayout mian_bottom_linear, main_scewm_linear, main_sctxl_linear;
    static App app;
    static Main2Activity ma;
    private static int selectedId = 0;
    public static Intent mdata = null;
    private static StringBuilder sb = new StringBuilder();
    protected static Map<String, FragmentContainer> fragmentClassMap = new HashMap<String, FragmentContainer>();
    List<Fragment> fragments = new ArrayList<>();

    static {
        fragmentClassMap.put("home", new FragmentContainer(R.id.rb_home, HomeFragment.class));
        fragmentClassMap.put("personal", new FragmentContainer(R.id.rb_personal, PersonalFragment.class));
        fragmentClassMap.put("release", new FragmentContainer(R.id.rb_release, ReleaseFragment.class));
        fragmentClassMap.put("group", new FragmentContainer(R.id.rb_group, GroupFragment.class));
        fragmentClassMap.put("mine", new FragmentContainer(R.id.rb_mine, MineFragment.class));
    }

    public static Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    MyUtils.dismssDialog();
                    String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
                    String hcDate = (String) MySharedPrefrencesUtil.getParam(ma, "CLEAN_DATE", "");
                    MySharedPrefrencesUtil.setParam(ma, "CLEAN_DATE", currentDate);
                    if (selectedId == R.id.rb_personal) {
                        for (int i = 0; i < length_phone; i++) {
                            sb.append(PersonalFragment.mTopDataList.get(i).getPhone() + "^");
                            MyUtils.Loge(TAG, "添加的手机号：" + PersonalFragment.mTopDataList.get(i).getPhone());
                            MySharedPrefrencesUtil.setParam(ma, "phone_data", MySharedPrefrencesUtil.getParam(ma, "phone_data", "") + sb.toString());
                            sb.delete(0, sb.length());
                        }
                        MyUtils.Loge(TAG, "缓存中的电话号码：" + MySharedPrefrencesUtil.getParam(ma, "phone_data", ""));
                    }
                    if (selectedId == R.id.rb_group) {
                        for (int i = 0; i < length_phone; i++) {
                            sb.append(GroupFragment.mTopDataList.get(i).getPhone() + "^");
                            MyUtils.Loge(TAG, "添加的手机号：" + GroupFragment.mTopDataList.get(i).getPhone());
                            MySharedPrefrencesUtil.setParam(ma, "phone_data", MySharedPrefrencesUtil.getParam(ma, "phone_data", "") + sb.toString());
                            sb.delete(0, sb.length());
                        }
                        MyUtils.Loge(TAG, "缓存中的电话号码：" + MySharedPrefrencesUtil.getParam(ma, "phone_data", ""));
                    }
                    MyUtils.showToast(ma, "添加成功");
                    break;
                case 2:
                    //添加通讯录拒绝权限回调
                    MyUtils.dismssDialog();
                    MyUtils.showToast(ma, "请打开手机设置，权限管理，允许蓝狐微商读取、写入和删除联系人信息后再使用立即加粉");
                    break;
            }
        }
    };
    private static int length_phone;
    private int length_phone1;
    private List<TopData> mTopDataList1 = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        ActivityUtil.add(this);
        ma = this;
        app = getApplicationContext();
        initView();
        initViewPager();
        initData(savedInstanceState);
        getPurseData(false);
        getApkVersion();
        isNextDay();
        MySharedPrefrencesUtil.setParam(ma, "my_phone_type", "1");//设置第一页是否标记30条
    }

    /**
     * 判断是否是第二天
     */
    private void isNextDay() {
        //String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(new Date());
        String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        String hcDate = (String) MySharedPrefrencesUtil.getParam(this, "CLEAN_DATE", "");
        if (!currentDate.equals(hcDate)) {
            MySharedPrefrencesUtil.setParam(this, "phone_data", "");
        }
    }

    private void initView() {
        mian_bottom_linear = (LinearLayout) findViewById(R.id.mian_bottom_linear);
        main_scewm_linear = (LinearLayout) findViewById(R.id.main_scewm_linear);
        main_scewm_linear.setOnClickListener(this);
        main_sctxl_linear = (LinearLayout) findViewById(R.id.main_sctxl_linear);
        main_sctxl_linear.setOnClickListener(this);

        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_personal = (RadioButton) findViewById(R.id.rb_personal);
        rb_group = (RadioButton) findViewById(R.id.rb_group);
        rb_release = (RadioButton) findViewById(R.id.rb_release);
        rb_mine = (RadioButton) findViewById(R.id.rb_mine);
    }

    //改变底部布局
    public void changeBottomView(int type) {
        if (selectedId == R.id.rb_personal || selectedId == R.id.rb_group) {
            switch (type) {
                case 1:
                    rg_main.setVisibility(View.GONE);
                    mian_bottom_linear.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    rg_main.setVisibility(View.VISIBLE);
                    mian_bottom_linear.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_scewm_linear:
                if (selectedId == R.id.rb_personal) {
                    toSave(PersonalFragment.mTopDataList);
                }
                if (selectedId == R.id.rb_group) {
                    toSave(GroupFragment.mTopDataList);
                }
                break;
            case R.id.main_sctxl_linear:

                if (selectedId == R.id.rb_personal) {
                    toSavePhone(PersonalFragment.mTopDataList);
                }
                if (selectedId == R.id.rb_group) {
                    toSavePhone(GroupFragment.mTopDataList);
                }
                break;
        }
    }

    /**
     * 保存手机号
     */
    private void toSavePhone(final List<TopData> mTopDataList) {
        String str = MySharedPrefrencesUtil.getParam(ma, "phone_data", "").toString();
        String[] temp = str.split("\\^");
        //判断是不是VIP
//        if (Long.parseLong(app.getUser().getViptime()) > 0) {//如果是
//        MyUtils.Loge("AAAAA","app:"+app);
//        MyUtils.Loge("AAAAA","app.getPurseData():"+app.getPurseData());
//        MyUtils.Loge("AAAAA","app.getPurseData().getViprest():"+app.getPurseData().getViprest());
        if (app.getPurseData() != null && Long.parseLong(app.getPurseData().getViprest()) > 0) {//如果是
            if (temp.length >= 120) {
                MyUtils.showToast(ma, "您今天已经添加了120条，请明天再添加吧");
            } else {
                if (mTopDataList.size() <= (120 - temp.length)) {
                    length_phone = mTopDataList.size();
                    addMyList(mTopDataList.size(), mTopDataList);
                } else {
                    length_phone = 120 - temp.length;
                    addMyList(length_phone, mTopDataList);
                }
            }
        } else {
            if (temp.length >= 60) {
                MyUtils.showToast(ma, "您今天已经添加了60条，请明天再添加吧");
            } else {
                if (mTopDataList.size() <= (60 - temp.length)) {
                    length_phone = mTopDataList.size();
                    addMyList(mTopDataList.size(), mTopDataList);
                } else {
                    length_phone = 60 - temp.length;
                    addMyList(length_phone, mTopDataList);
                }
            }
        }
    }

    /**
     * 添加到通讯录
     */
    private void addMyList(final int length_phone, final List<TopData> mTopDataList) {
        length_phone1 = length_phone;
        mTopDataList1 = mTopDataList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ma,
                    Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ma,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        0x1);
                return;
            } else {
                MyUtils.showDialog(Main2Activity.this, "正在添加通讯录...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < length_phone1; i++) {
                            MyUtils.Loge(TAG, "选中的电话号码：" + mTopDataList1.get(i).getPhone());
                            LXRUtil.addContacts(ma, mTopDataList1.get(i).getNickname(), mTopDataList1.get(i).getPhone(), false, length_phone1, 2);
                        }
                    }
                }).start();
            }
        } else {
            MyUtils.showDialog(Main2Activity.this, "正在添加通讯录...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < length_phone1; i++) {
                        MyUtils.Loge(TAG, "选中的电话号码：" + mTopDataList1.get(i).getPhone());
                        LXRUtil.addContacts(ma, mTopDataList1.get(i).getNickname(), mTopDataList1.get(i).getPhone(), false, length_phone1, 2);
                    }
                }
            }).start();
        }
    }

    //
    private int length;
    private int position;

    /**
     * 批量保存二维码
     *
     * @param mTopDataList
     */
    private void toSave(final List<TopData> mTopDataList) {
        if (mTopDataList.size() <= 25) {
            length = mTopDataList.size();
        } else {
            length = 25;
        }
        for (position = 0; position < length; position++) {
            SavePicUtil.SavePic(this, mTopDataList.get(position).getImgurl(), 0, new SavePicUtil.On_Save_ClickListener() {
                @Override
                public void onSuccess(File file, int nextPosition) {
                    SaveCodeUtil.insertCode(Main2Activity.this, mTopDataList.get(position - 1).getId());
                    MyUtils.showToast(Main2Activity.this, "已保存至" +
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/lh/CodeImg/"
                            + file.getName());
                }

                @Override
                public void onFail(Call call, Exception e, int nextPosition) {
                    MyUtils.showToast(Main2Activity.this, "保存失败");
                }

                @Override
                public void onProgress(float progress, long l) {

                }
            });
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        fragmentManager = getSupportFragmentManager();
        mFragments = new LinkedList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new PersonalFragment());
        mFragments.add(new ReleaseFragment());
        mFragments.add(new GroupFragment());
        mFragments.add(new MineFragment());
        /**
         * 设置主页面viewpager的适配器
         */
        mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(fragmentManager, mFragments);
    }

    private void initData(Bundle savedInstanceState) {
        //设置主页第一个分页被选中
        rb_home.setChecked(true);
        rb_home.setSelected(true);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            rb_home.setChecked(true);
            currentContainer = new FragmentContainer();
            currentContainer.setId(R.id.rb_home);
            switchTab(R.id.rb_home);
        }
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton currentRB = (RadioButton) findViewById(currentContainer.getId());
                selectedId = checkedId;
                RadioButton rb = (RadioButton) findViewById(checkedId);
                boolean buttonChecked = rb.isChecked();
                if (buttonChecked) {//用户点击触发,或后退键出发
                    if (getApplicationContext().getIsLogin()) {

                        if (currentContainer != null && !TextUtils.isEmpty(String.valueOf(currentContainer.getId()))) {
                            if (checkedId != currentContainer.getId()) {
                                unSelectCurrentTab();
                                switchTab(checkedId);
                            }
                            if (checkedId != R.id.rb_home) {
                                RadioButton tabRB = (RadioButton) findViewById(R.id.rb_home);
                                tabRB.setSelected(false);
                                tabRB.setChecked(false);
                            }
                        }
                    } else {
                        rb_home.setChecked(true);
                        Intent in = new Intent(Main2Activity.this, LoginActivity.class);
                        startActivity(in);
                    }

                }
            }
        });
    }

    /**
     * 取消当前tab的选中状态
     */
    public void unSelectCurrentTab() {
        RadioButton button = (RadioButton) this.findViewById(currentContainer.getId());
        button.setSelected(false);
        button.setChecked(false);
    }

    public void switchTab(int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                fragmentStack.clear();
                this.switchFragment("home");
                break;
            case R.id.rb_personal:
                this.switchFragment("personal");
                break;
            case R.id.rb_release:
                this.switchFragment("release");
                break;
            case R.id.rb_group:
                this.switchFragment("group");
                break;
            case R.id.rb_mine:
                this.switchFragment("mine");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent=getIntent();
//        intent.getStringExtra()
    }

    public void switchFragment(String tag) {
        FragmentContainer container = fragmentClassMap.get(tag);
        try {
            Fragment fragment = container.getFragment();
            if (fragment == null) {
                fragment = (Fragment) (container.getFragmentClass().newInstance());
            }
            switchContent(fragment, true);
            container.setFragment(fragment);
            currentContainer.setFragment(fragment);
            currentContainer.setId(container.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param to 需要显示的fragment
     */
    public void switchContent(Fragment to, boolean addToStack) {
        if (currentContainer.getFragment() != to) {
            FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (currentContainer.getFragment() != null) {
                transaction.hide(currentContainer.getFragment());
                if (addToStack) {
                    FragmentContainer container = new FragmentContainer(currentContainer.getId(), currentContainer.getFragment());
                    fragmentStack.add(container);
                }
            }
            for (FragmentContainer f : fragmentStack) {
                transaction.hide(f.getFragment());
            }
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.add(R.id.content_frame, to); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.show(to); // 隐藏当前的fragment，显示下一个
            }
            transaction.commitAllowingStateLoss();
            currentContainer.setFragment(to);
        }
    }

    /**
     * 返回键的监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                ActivityUtil.exitAll();
                finish();
                System.exit(0);
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 获取账户余额信息
     */
    public static void getPurseData(final boolean dhzd) {
        if (app != null && !app.getIsLogin()) {
            return;
        }
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.FLUSH_URL)
//                .addParams("login_token", app.getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(ma,"TOKEN",""))
                .build()
                .execute(new GetPurseCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                    }

                    @Override
                    public void onResponse(GetPurseData mData) {
                        MyUtils.Loge(TAG, "请求成功：mData1=" + mData.toString());
                        if (mData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(ma);
                            return;
                        }
                        if (mData.getErrcode() == 1) {
                            MyUtils.Loge(TAG, "user=" + mData.getUserDate() + "，账户信息=" + mData.getUserInfo());
                            app.setPurseData(mData.getUserDate());
                            app.getUser().setViptime(mData.getUserInfo().getViptime());
                            app.getPurseData().setViprest(mData.getUserDate().getViprest());
//                            app.getUser().set
                            if (MineFragment.isResume) {
                                MineFragment.setPData(mData.getUserDate());
                            }
                            if (dhzd) {
                                ZDActivity.setZDNum();
                            }
                        }
                    }
                });
//        }

    }

    private void getApkVersion() {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.GETVERSION_URL)
                .addParams("type", 1 + "")
                .build()
                .execute(new UpdateApkCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                    }

                    @Override
                    public void onResponse(UpdateApkData mData) {
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(Main2Activity.this);
                            return;
                        }
                        if (mData.getErrcode() == 1) {
                            // 先判断是否有新版本 有弹提示框
                            if (!TextUtils.equals(mData.getData().getVersion(), getAppVersionName())) {
                                app.setmNewApkData(mData.getData());
                                String msg = "";
                                if (TextUtils.isEmpty(mData.getData().getMsg())) {
                                    msg = "有新版本啦";
                                } else {
                                    msg = MyUtils.Replace_hh_Str(mData.getData().getMsg());
                                }
                                DialogOkUtil.show_update_Dialog(Main2Activity.this, mData.getData().getVersion(), msg, new DialogOkUtil.On_Update_ClickListener() {
                                    @Override
                                    public void onCancle() {

                                    }

                                    @Override
                                    public void onDownload() {
                                        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(Main2Activity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    0x14);
                                            return;
                                        }
                                        downLoad();
                                    }
                                }).show();
                            }

                        }
                    }
                });
    }

    private void downLoad() {
        if (NetWorkChangeReceiver.NET_WORK_WIFI_TYPE) {
            startService(new Intent(Main2Activity.this, UpdateService.class));
        } else if (!NetWorkChangeReceiver.NET_WORK_TYPE) {
            DialogOkUtil.show_Ok_Dialog(Main2Activity.this, "请打开网络连接", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            }).show();
        } else {
            DialogOkUtil.show_OK_NO_Dialog(Main2Activity.this, "当前网络不是Wifi，是否用手机流量下载？", new DialogOkUtil.On_OK_N0_ClickListener() {
                @Override
                public void onOk() {
                    startService(new Intent(Main2Activity.this, UpdateService.class));
                }

                @Override
                public void onNo() {

                }
            }).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            MyUtils.Loge(TAG, "data.getData():" + data.getData());
            mFragments.get(2).onActivityResult(requestCode, resultCode, data);
        }
    }


    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Fragment fragment = getVisibleFragment();
        if (fragment != null) {
            if (fragment instanceof PersonalFragment || fragment instanceof GroupFragment) {
                for (MyOnTouchListener listener : onTouchListeners) {
                    listener.onTouch(ev);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = Main2Activity.this.getSupportFragmentManager();
        fragments = fragmentManager.getFragments();
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0x1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.v("stones", "权限回调--获取权限失败");
                    Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    Log.v("stones", "权限回调--获取权限成功");
                    MyUtils.showDialog(this, "正在添加通讯录...");
                    if (length_phone1 > 0 && mTopDataList1 != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < length_phone1; i++) {
                                    MyUtils.Loge(TAG, "选中的电话号码：" + mTopDataList1.get(i).getPhone());
                                    LXRUtil.addContacts(ma, mTopDataList1.get(i).getNickname(), mTopDataList1.get(i).getPhone(), false, length_phone1, 2);
                                }
                            }
                        }).start();
                    }
                }
                break;
            default:
                break;
        }
    }
}


