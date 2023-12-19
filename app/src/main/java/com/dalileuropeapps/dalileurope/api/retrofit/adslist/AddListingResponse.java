package com.dalileuropeapps.dalileurope.api.retrofit.adslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddListingResponse {

    @SerializedName("data")
    @Expose
    private AddData data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public AddData getData() {
        return data;
    }

    public void setData(AddData data) {
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
