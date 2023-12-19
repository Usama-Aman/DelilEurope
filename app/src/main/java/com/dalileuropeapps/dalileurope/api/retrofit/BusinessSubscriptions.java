package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessSubscriptions {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("business_id")
    @Expose
    private Integer businessId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("subscription_id")
    @Expose
    private Integer subscriptionId;
    @SerializedName("expired_at")
    @Expose
    private String expiredAt;
    @SerializedName("paypal_profile_id")
    @Expose
    private String paypalProfileId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getPaypalProfileId() {
        return paypalProfileId;
    }

    public void setPaypalProfileId(String paypalProfileId) {
        this.paypalProfileId = paypalProfileId;
    }

}