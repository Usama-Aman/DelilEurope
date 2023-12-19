package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeResponseData {


    @SerializedName("fea_ad")
    @Expose
    private List<Ads> feaAd = null;
    @SerializedName("high_ad")
    @Expose
    private List<Ads> highAd = null;
    @SerializedName("ads_data")
    @Expose
    private List<Ads> adsData = null;

    public List<Ads> getFeaAd() {
        return feaAd;
    }

    public void setFeaAd(List<Ads> feaAd) {
        this.feaAd = feaAd;
    }

    public List<Ads> getHighAd() {
        return highAd;
    }

    public void setHighAd(List<Ads> highAd) {
        this.highAd = highAd;
    }

    public List<Ads> getAdsData() {
        return adsData;
    }

    public void setAdsData(List<Ads> adsData) {
        this.adsData = adsData;
    }

}
