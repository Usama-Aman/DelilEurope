package com.dalileuropeapps.dalileurope.api.retrofit.message;

//import com.examples.dalileurope.models.UserMessages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageDetail {
    @SerializedName("user_message")
    @Expose
    private UserMessages userMessage;
    @SerializedName("count_unread")
    @Expose
    private Integer countUnread;

    public UserMessages getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessages userMessage) {
        this.userMessage = userMessage;
    }

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }

}
