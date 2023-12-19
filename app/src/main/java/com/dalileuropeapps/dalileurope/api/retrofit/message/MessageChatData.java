package com.dalileuropeapps.dalileurope.api.retrofit.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageChatData {
    @SerializedName("user_message")
    @Expose
    private User_Message userMessage = null;

    public User_Message getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(User_Message userMessage) {
        this.userMessage = userMessage;
    }

    public MessageChatData(User_Message userMessage) {
        this.userMessage = userMessage;
    }







}
