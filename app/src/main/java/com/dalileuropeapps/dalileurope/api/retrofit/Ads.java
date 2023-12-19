package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ads {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("business_id")
    @Expose
    private Integer businessId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ad_url")
    @Expose
    private String adUrl;
    @SerializedName("ad_key")
    @Expose
    private String adKey;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private Integer subCategoryId;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("is_featured")
    @Expose
    private Integer isFeatured;
    @SerializedName("ad_location")
    @Expose
    private String adLocation;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("ad_price")
    @Expose
    private String adPrice;
    @SerializedName("ad_disc_price")
    @Expose
    private String adDiscPrice;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("payment_start_date")
    @Expose
    private String paymentStartDate;
    @SerializedName("payment_end_date")
    @Expose
    private String paymentEndDate;
    @SerializedName("average_ratings")
    @Expose
    private String averageRatings;
    @SerializedName("ad_service")
    @Expose
    private Object adService;
    @SerializedName("ad_languages")
    @Expose
    private Object adLanguages;
    @SerializedName("ads_likes_count")
    @Expose
    private Integer adsLikesCount;
    @SerializedName("ads_views_count")
    @Expose
    private Integer adsViewsCount;
    @SerializedName("ads_reviews_count")
    @Expose
    private Integer adsReviewsCount;
    @SerializedName("avg_ratings")
    @Expose
    private Object avgRatings;
    @SerializedName("category_details")
    @Expose
    private CategoryDetails categoryDetails;
    @SerializedName("business_details")
    @Expose
    private BusinessDetails businessDetails;
    @SerializedName("fullFile")
    @Expose
    private String fullFile;
    @SerializedName("ads_images")
    @Expose
    private List<GalleryResponseDataDetail> adsImages = null;

    public List<GalleryResponseDataDetail> getAdsImages() {
        return adsImages;
    }

    public void setAdsImages(List<GalleryResponseDataDetail> adsImages) {
        this.adsImages = adsImages;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdKey() {
        return adKey;
    }

    public void setAdKey(String adKey) {
        this.adKey = adKey;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(String adLocation) {
        this.adLocation = adLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(String adPrice) {
        this.adPrice = adPrice;
    }

    public String getAdDiscPrice() {
        return adDiscPrice;
    }

    public void setAdDiscPrice(String adDiscPrice) {
        this.adDiscPrice = adDiscPrice;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getPaymentStartDate() {
        return paymentStartDate;
    }

    public void setPaymentStartDate(String paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    public String getPaymentEndDate() {
        return paymentEndDate;
    }

    public void setPaymentEndDate(String paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
    }

    public String getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(String averageRatings) {
        this.averageRatings = averageRatings;
    }

    public Object getAdService() {
        return adService;
    }

    public void setAdService(Object adService) {
        this.adService = adService;
    }

    public Object getAdLanguages() {
        return adLanguages;
    }

    public void setAdLanguages(Object adLanguages) {
        this.adLanguages = adLanguages;
    }

    public Integer getAdsLikesCount() {
        return adsLikesCount;
    }

    public void setAdsLikesCount(Integer adsLikesCount) {
        this.adsLikesCount = adsLikesCount;
    }

    public Integer getAdsViewsCount() {
        return adsViewsCount;
    }

    public void setAdsViewsCount(Integer adsViewsCount) {
        this.adsViewsCount = adsViewsCount;
    }

    public Integer getAdsReviewsCount() {
        return adsReviewsCount;
    }

    public void setAdsReviewsCount(Integer adsReviewsCount) {
        this.adsReviewsCount = adsReviewsCount;
    }

    public Object getAvgRatings() {
        return avgRatings;
    }

    public void setAvgRatings(Object avgRatings) {
        this.avgRatings = avgRatings;
    }

    public CategoryDetails getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(CategoryDetails categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }

    public String getFullFile() {
        return fullFile;
    }

    public void setFullFile(String fullFile) {
        this.fullFile = fullFile;
    }
}