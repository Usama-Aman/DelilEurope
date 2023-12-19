package com.dalileuropeapps.dalileurope.api.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedCategory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_sv")
    @Expose
    private String nameSv;
    @SerializedName("name_de")
    @Expose
    private String nameDe;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("category_key")
    @Expose
    private String categoryKey;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("lang_key")
    @Expose
    private Object langKey;
    @SerializedName("fullImage")
    @Expose
    private String fullImage;
    @SerializedName("thumbImage")
    @Expose
    private String thumbImage;
    boolean selected = false;

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

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

    public String getNameSv() {
        return nameSv;
    }

    public void setNameSv(String nameSv) {
        this.nameSv = nameSv;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getLangKey() {
        return langKey;
    }

    public void setLangKey(Object langKey) {
        this.langKey = langKey;
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