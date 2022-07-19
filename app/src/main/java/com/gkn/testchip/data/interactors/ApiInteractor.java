package com.gkn.testchip.data.interactors;

import com.gkn.testchip.data.datamodels.response.ResponseRows;

public interface ApiInteractor {

    void getRows(String rows);

    interface Callbacks {

        void errorRequest(Integer errorCode, String method);
        void unauthorized(String method);
        void successGetRows(ResponseRows response);
    }
}
