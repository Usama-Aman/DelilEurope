package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostResponseData {
    @SerializedName("order")
    @Expose
    private AdsOders order;
    @SerializedName("ads")
    @Expose
    private ResponseDetailAdsData ads;

    public AdsOders getOrder() {
        return order;
    }

    public void setOrder(AdsOders order) {
        this.order = order;
    }


    public ResponseDetailAdsData getData() {
        return ads;
    }

    public void setData(ResponseDetailAdsData ads) {
        this.ads = ads;
    }
}
