package com.dalileuropeapps.dalileurope.api.retrofit;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDetailAdsData {

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
    private String adService;
    @SerializedName("ad_languages")
    @Expose
    private String adLanguages;
    @SerializedName("avg_ratings")
    @Expose
    private String avgRatings;
    @SerializedName("fullFile")
    @Expose
    private String fullFile;
    @SerializedName("views_count")
    @Expose
    private Integer viewsCount;
    @SerializedName("favorite_count")
    @Expose
    private Integer favoriteCount;
    @SerializedName("reviews_count")
    @Expose
    private Integer reviewsCount;
    @SerializedName("user_details")
    @Expose
    private UserDetailsAd userDetails;
    @SerializedName("category_details")
    @Expose
    private CategoryDetails categoryDetails;
    @SerializedName("subcategory_details")
    @Expose
    private SubCategory subcategoryDetails;
    @SerializedName("business_details")
    @Expose
    private BusinessDetails businessDetails;
    @SerializedName("ads_images")
    @Expose
    private List<GalleryResponseDataDetail> adsImages = null;
    @SerializedName("ads_oders")
    @Expose
    private AdsOders adsOders;
    @SerializedName("ads_views")
    @Expose
    private List<AdsView> adsViews = null;
    @SerializedName("ads_likes")
    @Expose
    private List<AdLikes> adsLikes = null;

    @SerializedName("details_payment_methods")
    @Expose
    private List<PaymentMethod> detailsPaymentMethods = null;
    @SerializedName("ads_payment_methods")
    @Expose
    private List<PaymentMethod> adsPaymentMethods = null;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<PaymentMethod> getAdsPaymentMethods() {
        return adsPaymentMethods;
    }

    public void setAdsPaymentMethods(List<PaymentMethod> adsPaymentMethods) {
        this.adsPaymentMethods = adsPaymentMethods;
    }

    @SerializedName("ads__features")
    @Expose
    private List<AdsFeature> adsFeatures = null;
    @SerializedName("ads_reviews")
    @Expose
    private List<AdsReview> adsReviews = null;

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

    public String getAdService() {
        return adService;
    }

    public void setAdService(String adService) {
        this.adService = adService;
    }

    public String getAdLanguages() {
        return adLanguages;
    }

    public void setAdLanguages(String adLanguages) {
        this.adLanguages = adLanguages;
    }

    public String getAvgRatings() {
        return avgRatings;
    }

    public void setAvgRatings(String avgRatings) {
        this.avgRatings = avgRatings;
    }

    public String getFullFile() {
        return fullFile;
    }

    public void setFullFile(String fullFile) {
        this.fullFile = fullFile;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public UserDetailsAd getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsAd userDetails) {
        this.userDetails = userDetails;
    }

    public CategoryDetails getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(CategoryDetails categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

    public SubCategory getSubcategoryDetails() {
        return subcategoryDetails;
    }

    public void setSubcategoryDetails(SubCategory subcategoryDetails) {
        this.subcategoryDetails = subcategoryDetails;
    }

    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }

    public List<GalleryResponseDataDetail> getAdsImages() {
        return adsImages;
    }

    public void setAdsImages(List<GalleryResponseDataDetail> adsImages) {
        this.adsImages = adsImages;
    }

    public AdsOders getAdsOders() {
        return adsOders;
    }

    public void setAdsOders(AdsOders adsOders) {
        this.adsOders = adsOders;
    }

    public List<AdsView> getAdsViews() {
        return adsViews;
    }

    public void setAdsViews(List<AdsView> adsViews) {
        this.adsViews = adsViews;
    }

    public List<AdLikes> getAdsLikes() {
        return adsLikes;
    }

    public void setAdsLikes(List<AdLikes> adsLikes) {
        this.adsLikes = adsLikes;
    }


    public List<AdsFeature> getAdsFeatures() {
        return adsFeatures;
    }

    public void setAdsFeatures(List<AdsFeature> adsFeatures) {
        this.adsFeatures = adsFeatures;
    }

    public List<AdsReview> getAdsReviews() {
        return adsReviews;
    }

    public void setAdsReviews(List<AdsReview> adsReviews) {
        this.adsReviews = adsReviews;
    }

    public List<PaymentMethod> getDetailsPaymentMethods() {
        return detailsPaymentMethods;
    }

    public void setDetailsPaymentMethods(List<PaymentMethod> detailsPaymentMethods) {
        this.detailsPaymentMethods = detailsPaymentMethods;
    }

}