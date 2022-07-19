package com.gkn.testchip.data.interactors;

import com.gkn.testchip.data.datamodels.response.ResponseRows;
import com.gkn.testchip.data.webservices.manager.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiInteractorImpl implements ApiInteractor{

    private final ApiInteractor.Callbacks callbacks;

    public ApiInteractorImpl(ApiInteractor.Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void getRows(String rows) {
        final String method = "getRows";
        ApiManager.getApiService( "").rows(rows).enqueue(new Callback<ResponseRows>() {
            @Override
            public void onResponse(Call<ResponseRows> call, Response<ResponseRows> response) {
                switch (response.code()){
                    case 200:
                    case 201:
                        ResponseRows responseRowsBean = response.body();
                        callbacks.successGetRows(responseRowsBean);
                        break;
                    case 401:
                        callbacks.unauthorized(method);
                        break;
                    case 500:
                        callbacks.errorRequest(2,method);
                        break;
                    default:
                        callbacks.errorRequest(1,method);
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseRows> call, Throwable t) {
                callbacks.errorRequest(0,method);
            }
        });
    }
}
