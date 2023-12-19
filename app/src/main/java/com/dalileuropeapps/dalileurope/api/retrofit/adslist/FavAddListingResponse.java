package com.dalileuropeapps.dalileurope.api.retrofit.adslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavAddListingResponse {
    @SerializedName("data")
    @Expose
    private FavAddListDatum favAddListDatum;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public FavAddListDatum getFavAddListDatum() {
        return favAddListDatum;
    }

    public void setFavAddListDatum(FavAddListDatum favAddListDatum) {
        this.favAddListDatum = favAddListDatum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
