package com.dalileuropeapps.dalileurope.api.retrofit.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationData {
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
    @SerializedName("is_read")
    @Expose
    private Integer isRead;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("message_thread")
    @Expose
    private UserMessages messageThread;
    @SerializedName("user_details_from")
    @Expose
    private UserDetailsFrom userDetailsFrom;
    @SerializedName("user_details_to")
    @Expose
    private UserDetailsTo userDetailsTo;

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

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
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

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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

    public UserMessages getMessageThread() {
        return messageThread;
    }

    public void setMessageThread(UserMessages messageThread) {
        this.messageThread = messageThread;
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
}
