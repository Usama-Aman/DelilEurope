package com.dalileuropeapps.dalileurope.api.retrofit.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageListResponse {
    @SerializedName("data")
    @Expose
    private UserMessageThreadData data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public UserMessageThreadData getUserMessageThreadData() {
        return data;
    }

    public void setUserMessageThreadData(UserMessageThreadData data) {
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
