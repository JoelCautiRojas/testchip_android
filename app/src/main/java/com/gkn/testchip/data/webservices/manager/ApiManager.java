package com.gkn.testchip.data.webservices.manager;

import com.gkn.testchip.BuildConfig;
import com.gkn.testchip.data.restutils.RestUtil;
import com.gkn.testchip.data.webservices.interfaces.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiManager {

    private static final String BASE_URL = BuildConfig.URL_HOST;
    private static ApiService apiService;

    public static ApiService getApiService(String token){
        OkHttpClient httpClient = RestUtil.getHttpClient(token);
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.client(httpClient);
        builder.addConverterFactory(JacksonConverterFactory.create(RestUtil.getObjectMapperConfiguration()));
        Retrofit retrofit = builder.build();
        apiService = retrofit.create(ApiService.class);
        return apiService;
    }
}
