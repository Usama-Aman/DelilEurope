package com.dalileuropeapps.dalileurope.api.retrofit;


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ResponseDetailAds {

    @SerializedName("data")
    @Expose
    private ResponseDetailAdsData data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ResponseDetailAdsData getData() {
        return data;
    }

    public void setData(ResponseDetailAdsData data) {
        this.data = data;
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