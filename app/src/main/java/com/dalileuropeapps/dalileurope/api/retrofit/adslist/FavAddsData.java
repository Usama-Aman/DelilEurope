package com.dalileuropeapps.dalileurope.api.retrofit.adslist;

import com.dalileuropeapps.dalileurope.api.retrofit.Ads;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavAddsData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ad_id")
    @Expose
    private Integer ad_id;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("created_at")
    @Expose
    private String  created_at;
    @SerializedName("updated_at")
    @Expose
    private String  updated_at;

    @SerializedName("ads_details")
    @Expose
    private Ads ads_details;
    @SerializedName("user_details")
    @Expose
    private User user_details;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAd_id() {
        return ad_id;
    }

    public void setAd_id(Integer ad_id) {
        this.ad_id = ad_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Ads getAds_details() {
        return ads_details;
    }

    public void setAds_details(Ads ads_details) {
        this.ads_details = ads_details;
    }

    public User getUser_details() {
        return user_details;
    }

    public void setUser_details(User user_details) {
        this.user_details = user_details;
    }
}
