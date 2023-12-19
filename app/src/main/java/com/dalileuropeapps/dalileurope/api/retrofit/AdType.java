package com.dalileuropeapps.dalileurope.api.retrofit;


public class AdType {



    private double adFee = 0;
    private String type;
    private String currency;



    public double getAdFee() {
        return adFee;
    }

    public void setAdFee(double adFee) {
        this.adFee = adFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}