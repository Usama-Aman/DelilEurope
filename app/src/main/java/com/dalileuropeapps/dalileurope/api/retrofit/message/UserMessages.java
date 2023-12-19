package com.dalileuropeapps.dalileurope.api.retrofit.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class UserMessages implements Comparator<UserMessages> {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("thread_id")
    @Expose
    private Integer threadId;
    @SerializedName("from_user_id")
    @Expose
    private Integer fromUserId;
    @SerializedName("to_user_id")
    @Expose
    private Integer toUserId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_deleted_from")
    @Expose
    private String isDeletedFrom;
    @SerializedName("is_deleted_to")
    @Expose
    private String isDeletedTo;
    @SerializedName("user_details_from")
    @Expose
    private UserDetailsFrom userDetailsFrom;
    @SerializedName("user_details_to")
    @Expose
    private UserDetailsTo userDetailsTo;


    @SerializedName("from_user_details")
    @Expose
    private UserDetailsTo from_user_details;

    @SerializedName("to_user_details")
    @Expose
    private UserDetailsTo to_user_details;

    public UserDetailsTo getFrom_user_details() {
        return from_user_details;
    }

    public void setFrom_user_details(UserDetailsTo from_user_details) {
        this.from_user_details = from_user_details;
    }

    public UserDetailsTo getTo_user_details() {
        return to_user_details;
    }

    public void setTo_user_details(UserDetailsTo to_user_details) {
        this.to_user_details = to_user_details;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("readable_time")
    @Expose
    private String readableTime ;
    @SerializedName("is_read")
    @Expose
    private String is_read;

    @SerializedName("count_unread")
    @Expose
    private Integer countUnread;

    @SerializedName("count_unread_notify")
    @Expose
    private Integer count_unread_notify;

    public Integer getCount_unread_notify() {
        return count_unread_notify;
    }

    public void setCount_unread_notify(Integer count_unread_notify) {
        this.count_unread_notify = count_unread_notify;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserMessages() {
    }

    public String getReadableTime() {
        return readableTime;
    }

    public void setReadableTime(String readableTime) {
        this.readableTime = readableTime;
    }

    public UserMessages(Integer id, Integer threadId, Integer fromUserId, Integer toUserId, String message, String isDeletedFrom, String isDeletedTo, UserDetailsFrom userDetailsFrom, UserDetailsTo userDetailsTo) {
        this.id = id;
        this.threadId = threadId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.isDeletedFrom = isDeletedFrom;
        this.isDeletedTo = isDeletedTo;
        this.userDetailsFrom = userDetailsFrom;
        this.userDetailsTo = userDetailsTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsDeletedFrom() {
        return isDeletedFrom;
    }

    public void setIsDeletedFrom(String isDeletedFrom) {
        this.isDeletedFrom = isDeletedFrom;
    }

    public String getIsDeletedTo() {
        return isDeletedTo;
    }

    public void setIsDeletedTo(String isDeletedTo) {
        this.isDeletedTo = isDeletedTo;
    }

    public UserDetailsFrom getUserDetailsFrom() {
        return userDetailsFrom;
    }

    public void setUserDetailsFrom(UserDetailsFrom userDetailsFrom) {
        this.userDetailsFrom = userDetailsFrom;
    }

    public UserDetailsTo getUserDetailsTo() {
        return userDetailsTo;
    }

    public void setUserDetailsTo(UserDetailsTo userDetailsTo) {
        this.userDetailsTo = userDetailsTo;
    }

    @Override
    public int compare(UserMessages o1, UserMessages o2) {
        return o1.getCountUnread()==o2.getCountUnread() ? 1:0;
    }


    //    @SerializedName("created_at")
//    @Expose
//    private String created_at;

//    @SerializedName("updated_at")
//    @Expose
//    private String updated_at;

//    public String getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(String created_at) {
//        this.created_at = created_at;
//    }
//
//    public String getUpdated_at() {
//        return updated_at;
//    }
//
//    public void setUpdated_at(String updated_at) {
//        this.updated_at = updated_at;
//    }

}
