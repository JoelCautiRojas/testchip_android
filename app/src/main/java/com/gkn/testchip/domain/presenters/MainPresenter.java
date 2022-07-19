package com.gkn.testchip.domain.presenters;

import com.gkn.testchip.presentation.viewcontrollers.base.BaseActivityView;

public interface MainPresenter {

    void getRows(String rows);
    void validateAPN();

    interface View extends BaseActivityView{

        void listenDbInsertFinished();
        void setData(String valueOf, String name);
    }
}
