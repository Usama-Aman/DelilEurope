package com.dalileuropeapps.dalileurope.api.retrofit.adslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavAddListDatum {
    @SerializedName("ads")
    @Expose
    private FavAddPagedResponse ads;

    public FavAddPagedResponse getAds() {
        return ads;
    }

    public void setAds(FavAddPagedResponse ads) {
        this.ads = ads;
    }
}
