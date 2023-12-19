package com.dalileuropeapps.dalileurope.api.retrofit;


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ResponseReviews {

    @SerializedName("data")
    @Expose
    private ResponseReviewsData data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ResponseReviewsData getData() {
        return data;
    }

    public void setData(ResponseReviewsData data) {
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