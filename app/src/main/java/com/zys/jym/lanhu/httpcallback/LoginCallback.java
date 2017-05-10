package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.LoginData;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class LoginCallback extends Callback<LoginData> {
    @Override
    public LoginData parseNetworkResponse(Response response) throws IOException {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr=response.body().string();
        MyUtils.Loge("TAG--JSON","JSON="+jsonStr);
        LoginData mData =  new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr, LoginData.class);
        return mData;
    }
}
