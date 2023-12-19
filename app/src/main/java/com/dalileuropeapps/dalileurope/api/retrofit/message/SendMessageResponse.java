package com.dalileuropeapps.dalileurope.api.retrofit.message;

//import com.examples.dalileurope.models.UserMessages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessageResponse {
    @SerializedName("data")
    @Expose
    private UserMessages data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public UserMessages getData() {
        return data;
    }

    public void setData(UserMessages data) {
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
