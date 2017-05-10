package com.zys.jym.lanhu.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.activity.OpenVipActivity;
import com.zys.jym.lanhu.activity.RechargeActivity;
import com.zys.jym.lanhu.activity.ZDActivity;
import com.zys.jym.lanhu.utils.Constants;
import com.zys.jym.lanhu.utils.MyUtils;

/**
 * Created by Administrator on 2016/12/21.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    String TAG="TAG--WXPayEntryActivity";
    private IWXAPI api;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (App) getApplicationContext();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            MyUtils.Loge(TAG,"onPayFinish,errCode="+baseResp.errCode);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            switch (baseResp.errCode){
                case 0://成功
                    MyUtils.Loge(TAG,"支付成功");

                    switch (app.getPayActivity()){
                        case 0://充值狐币页面
                            Main2Activity.getPurseData(false);
                            MyUtils.showToast(app,"充值成功");
                            RechargeActivity.ra.finish();
                            break;
                        case 1://兑换置顶页面
                            MyUtils.showToast(app,"兑换成功");
                            Intent in =new Intent(WXPayEntryActivity.this,ZDActivity.class);
                            startActivity(in);
                            Main2Activity.getPurseData(true);
                            break;
                        case 2://开通VIP页面
                            Main2Activity.getPurseData(false);
                            MyUtils.showToast(app,"开通成功");
                            OpenVipActivity.ova.finish();
                            break;
                    }
                    finish();
                break;
                case -1://失败
                    MyUtils.Loge(TAG,"支付失败");
                    Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT);
                    MyUtils.showToast(WXPayEntryActivity.this, "支付失败");
                    finish();
                    break;
                case -2://取消
                    MyUtils.Loge(TAG,"支付取消");
                    MyUtils.showToast(WXPayEntryActivity.this, "支付取消");
//                    Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT);
                    finish();
                    break;
            }
        }
    }
}
