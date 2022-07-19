package com.gkn.testchip.data.webservices.interfaces;

import com.gkn.testchip.BuildConfig;
import com.gkn.testchip.data.datamodels.response.ResponseRows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET(BuildConfig.ROWS)
    Call<ResponseRows> rows(@Query("rows") String rows);
}
