package com.gkn.testchip.domain.presenters;

import android.content.Context;
import android.os.Handler;

import com.gkn.testchip.data.datamodels.response.ResponseRows;
import com.gkn.testchip.data.interactors.ApiInteractor;
import com.gkn.testchip.data.interactors.ApiInteractorImpl;
import com.gkn.testchip.domain.APNConfig;
import com.gkn.testchip.domain.helpers.LogHelper;

public class MainPresenterImpl implements MainPresenter, ApiInteractor.Callbacks {

    public static final String APN = "globokas.pe";
    public static final String APN_NAME = "globokas.pe";

    private final View view;
    private final Context context;
    private final ApiInteractor apiInteractor;

    public MainPresenterImpl(View view) {
        this.view = view;
        this.context = (Context) view;
        this.apiInteractor = new ApiInteractorImpl(this);
    }

    @Override
    public void validateAPN(){
        APNConfig apnConfig = new APNConfig(context);
        String apnSelect = apnConfig.getAPNSelect();
        if(!apnSelect.toUpperCase().equals(APN.toUpperCase())){
            int id = apnConfig.insertAPN(APN, APN_NAME);
            if(id != -1) {
                apnConfig.setDefaultAPN(id);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    view.listenDbInsertFinished();
                }, 7000);
            }else{
                view.listenDbInsertFinished();
            }
        }else{
            LogHelper.printLog("APN ya cuenta con el APN globokas");
            view.listenDbInsertFinished();
        }
    }

    @Override
    public void getRows(String rows) {
        apiInteractor.getRows(rows);
    }

    @Override
    public void errorRequest(Integer errorCode, String method) {

    }

    @Override
    public void unauthorized(String method) {

    }

    //
    //
    // CALLBACKS API SUCCESS
    //
    //

    @Override
    public void successGetRows(ResponseRows response) {
        int size = response.getTasas().size();
        view.setData(String.valueOf(size), response.getTasas().get(size-1).getName());
    }
}
