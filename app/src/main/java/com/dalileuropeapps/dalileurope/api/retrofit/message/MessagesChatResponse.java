package com.dalileuropeapps.dalileurope.api.retrofit.message;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesChatResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private MessageChatData messageData;

    public MessagesChatResponse(Boolean status, String message, MessageChatData messageData) {
        this.status = status;
        this.message = message;
        this.messageData = messageData;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageChatData getMessageData() {
        return messageData;
    }

    public void setMessageData(MessageChatData messageData) {
        this.messageData = messageData;
    }
}
