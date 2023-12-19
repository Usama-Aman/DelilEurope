package com.dalileuropeapps.dalileurope.api.retrofit.message;

//import com.examples.dalileurope.models.UserMessages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageData {
    @SerializedName("user_message")
    @Expose
    private UserMessages user_message = null;
    @SerializedName("count_unread")
    @Expose
    private Integer countUnread;

    public UserMessages getUser_message() {
        return user_message;
    }

    public void setUser_message(UserMessages user_message) {
        this.user_message = user_message;
    }

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }

//    public UserMessages getUserMessage() {
//        return user_message;
//    }
//
//    public void setUserMessage(UserMessages user_message) {
//        this.user_message = user_message;
//    }
}
