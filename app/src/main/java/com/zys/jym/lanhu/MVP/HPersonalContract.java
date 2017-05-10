package com.zys.jym.lanhu.MVP;

import com.zys.jym.lanhu.MVP.MVPPresenter.BasePresenter;
import com.zys.jym.lanhu.MVP.MVPView.BaseView;
import com.zys.jym.lanhu.bean.MyTopData;

/**
 * Created by Administrator on 2017/2/16.
 */

public interface  HPersonalContract {
    interface Presenter extends BasePresenter {
        void loadData(int mPage, String cataId,String provinceId,String cityId);
    }

    interface View extends BaseView<Presenter> {
        void showProgress();
        void hideProgress();
        void showData(MyTopData mTopData);
    }
}
