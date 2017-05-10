package com.zys.jym.lanhu.MVP;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.utils.LHHttpUrl;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/16.
 */

public class HPersonalHttp {
    private DataLoadListener listener;



    public void getData(int mPage, String cataId,String provinceId,String cityId){

        OkHttpUtils
                .post()
                .url(LHHttpUrl.TOPICLIST_URL)
                .addParams("p", mPage+"")
                .addParams("cataId",cataId)
                .addParams("provinceId",provinceId)
                .addParams("cityId",cityId)
                .build()
                .execute(new MyTopCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (listener!=null){
                            listener.error(e);
                        }

                    }

                    @Override
                    public void onResponse(MyTopData mData) {
                        if (listener!=null){
                            listener.success(mData);
                        }

                    }
                });

    }

    public interface DataLoadListener{
        void error(Exception e);
        void success(MyTopData mTopData);
    }

    public void setListener(DataLoadListener listener) {
        this.listener = listener;
    }
}
