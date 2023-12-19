package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BusinessDetails {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("business_key")
    @Expose
    private String businessKey;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("detail")
    @Expose
    private Object detail;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private Integer subCategoryId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private Integer country;
    @SerializedName("city")
    @Expose
    private Integer city;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("is_approve")
    @Expose
    private Integer isApprove;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("about_business")
    @Expose
    private String aboutBusiness;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("fullLogo")
    @Expose
    private String fullLogo;
    @SerializedName("thumbLogo")
    @Expose
    private String thumbLogo;

    @SerializedName("business_images")
    @Expose
    private ArrayList<BusinessImage> business_images;
    @SerializedName("business_category_details")
    @Expose
    private CategoryDetails business_category_details;
    @SerializedName("business_subcategory_details")
    @Expose
    private CategoryDetails business_subcategory_details;

    @SerializedName("country_details")
    @Expose
    private Country country_details;

    @SerializedName("state_details")
    @Expose
    private State state_details;

    @SerializedName("city_details")
    @Expose
    private State city_details;
    @SerializedName("business_subscriptions")
    @Expose
    private BusinessSubscriptions business_subscriptions;

    @SerializedName("is_business")
    @Expose
    private int is_business;

    public BusinessSubscriptions getBusiness_subscriptions() {
        return business_subscriptions;
    }

    public void setBusiness_subscriptions(BusinessSubscriptions business_subscriptions) {
        this.business_subscriptions = business_subscriptions;
    }

    public ArrayList<BusinessImage> getBusiness_images() {
        return business_images;
    }

    public void setBusiness_images(ArrayList<BusinessImage> business_images) {
        this.business_images = business_images;
    }

    public CategoryDetails getBusiness_category_details() {
        return business_category_details;
    }

    public void setBusiness_category_details(CategoryDetails business_category_details) {
        this.business_category_details = business_category_details;
    }

    public CategoryDetails getBusiness_subcategory_details() {
        return business_subcategory_details;
    }

    public void setBusiness_subcategory_details(CategoryDetails business_subcategory_details) {
        this.business_subcategory_details = business_subcategory_details;
    }

    public Country getCountry_details() {
        return country_details;
    }

    public void setCountry_details(Country country_details) {
        this.country_details = country_details;
    }

    public State getState_details() {
        return state_details;
    }

    public void setState_details(State state_details) {
        this.state_details = state_details;
    }

    public State getCity_details() {
        return city_details;
    }

    public void setCity_details(State city_details) {
        this.city_details = city_details;
    }

    @SerializedName("business_hours")
    @Expose
    private List<BusinessHour> businessHours = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(Integer isApprove) {
        this.isApprove = isApprove;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAboutBusiness() {
        return aboutBusiness;
    }

    public void setAboutBusiness(String aboutBusiness) {
        this.aboutBusiness = aboutBusiness;
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

    public String getFullLogo() {
        return fullLogo;
    }

    public void setFullLogo(String fullLogo) {
        this.fullLogo = fullLogo;
    }

    public String getThumbLogo() {
        return thumbLogo;
    }

    public void setThumbLogo(String thumbLogo) {
        this.thumbLogo = thumbLogo;
    }

    public List<BusinessHour> getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(List<BusinessHour> businessHours) {
        this.businessHours = businessHours;
    }

//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }

    public int getIs_business() {
        return is_business;
    }

    public void setIs_business(int is_business) {
        this.is_business = is_business;
    }


    @SerializedName("organisation_no")
    @Expose
    private String organisation_no;

    public String getOrganisation_no() {
        return organisation_no;
    }

    public void setOrganisation_no(String name) {
        this.organisation_no = name;
    }
}
