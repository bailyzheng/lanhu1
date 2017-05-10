package com.zys.jym.lanhu.MVP.MVPPresenter.impl;

import com.zys.jym.lanhu.MVP.HPersonalContract;
import com.zys.jym.lanhu.MVP.HPersonalHttp;
import com.zys.jym.lanhu.bean.MyTopData;

/**
 * Created by Administrator on 2017/2/16.
 */

public class HPersonalPresenter implements HPersonalContract.Presenter,HPersonalHttp.DataLoadListener  {

    HPersonalHttp mHttp;
    HPersonalContract.View view;
    public HPersonalPresenter(HPersonalContract.View view){
        this.view=view;
        view.setPresenter(this);
        mHttp=new HPersonalHttp();
        mHttp.setListener(this);
    }
    @Override
    public void start() {

    }

    @Override
    public void loadData(int mPage, String cataId,String provinceId,String cityId) {
        mHttp.getData(mPage,cataId,provinceId,cityId);
    }

    @Override
    public void error(Exception e) {
        view.hideProgress();
    }

    @Override
    public void success(MyTopData mTopData) {
        view.hideProgress();
        view.showData(mTopData);
    }
}
