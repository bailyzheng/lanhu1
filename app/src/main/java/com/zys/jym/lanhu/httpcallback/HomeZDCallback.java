package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.HomeZDData;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */

public abstract class HomeZDCallback  extends Callback<HomeZDData> {

    @Override
    public HomeZDData parseNetworkResponse(Response response) throws Exception {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr = response.body().string();
        MyUtils.Loge("TAG--JSON","JSON="+jsonStr);
        HomeZDData mData = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr,  HomeZDData.class);
        return mData;
    }

}
