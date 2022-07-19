package com.gkn.testchip.data.datamodels.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseRows {

    @SerializedName("tasas")
    @Expose
    private List<Tasa> tasas = null;

    public List<Tasa> getTasas() {
        return tasas;
    }

    public void setTasas(List<Tasa> tasas) {
        this.tasas = tasas;
    }
}
