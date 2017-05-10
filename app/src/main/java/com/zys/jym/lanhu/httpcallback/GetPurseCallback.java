package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.GetPurseData;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class GetPurseCallback extends Callback<GetPurseData> {
    @Override
    public GetPurseData parseNetworkResponse(Response response) throws IOException {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr = response.body().string();
        MyUtils.Loge("TAG--JSON","JSON="+jsonStr);
        GetPurseData mData = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr,  GetPurseData.class);
        return mData;
    }
}
