package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.HomeListData;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/28.
 */

public abstract class HomeListCallback extends Callback<HomeListData> {
    @Override
    public HomeListData parseNetworkResponse(Response response) throws Exception {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr = response.body().string();
        MyUtils.Loge("TAG--JSON","JSON="+jsonStr);
        HomeListData mData = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr,  HomeListData.class);
        return mData;
    }
}
