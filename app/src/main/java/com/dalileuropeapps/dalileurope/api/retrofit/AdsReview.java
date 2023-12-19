package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsReview {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ad_id")
    @Expose
    private Integer adId;
    @SerializedName("business_id")
    @Expose
    private Integer businessId;
    @SerializedName("from_user_id")
    @Expose
    private Integer fromUserId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("rating_expertise")
    @Expose
    private String ratingExpertise;
    @SerializedName("rating_professionalism")
    @Expose
    private String ratingProfessionalism;
    @SerializedName("review_title")
    @Expose
    private String reviewTitle;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("review_photo")
    @Expose
    private Object reviewPhoto;
    @SerializedName("user_details")
    @Expose
    private UserDetailsAd userDetails;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("fullImage")
    @Expose
    private String fullImage;
    @SerializedName("thumbImage")
    @Expose
    private String thumbImage;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingExpertise() {
        return ratingExpertise;
    }

    public void setRatingExpertise(String ratingExpertise) {
        this.ratingExpertise = ratingExpertise;
    }

    public String getRatingProfessionalism() {
        return ratingProfessionalism;
    }

    public void setRatingProfessionalism(String ratingProfessionalism) {
        this.ratingProfessionalism = ratingProfessionalism;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Object getReviewPhoto() {
        return reviewPhoto;
    }

    public void setReviewPhoto(Object reviewPhoto) {
        this.reviewPhoto = reviewPhoto;
    }

    public UserDetailsAd getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsAd userDetails) {
        this.userDetails = userDetails;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }
}