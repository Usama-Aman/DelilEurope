package com.dalileuropeapps.dalileurope.api.retrofit.adslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentPageData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_keywords")
    @Expose
    private String metaKeywords;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("lang_key")
    @Expose
    private Object langKey;
    @SerializedName("page_bg")
    @Expose
    private String pageBg;
    @SerializedName("name_sv")
    @Expose
    private String nameSv;
    @SerializedName("content_sv")
    @Expose
    private String contentSv;
    @SerializedName("meta_title_sv")
    @Expose
    private String metaTitleSv;
    @SerializedName("meta_keywords_sv")
    @Expose
    private String metaKeywordsSv;
    @SerializedName("meta_description_sv")
    @Expose
    private String metaDescriptionSv;
    @SerializedName("name_de")
    @Expose
    private String nameDe;
    @SerializedName("content_de")
    @Expose
    private String contentDe;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("content_ar")
    @Expose
    private String contentAr;
    @SerializedName("meta_title_de")
    @Expose
    private String metaTitleDe;
    @SerializedName("meta_keywords_de")
    @Expose
    private String metaKeywordsDe;
    @SerializedName("meta_description_de")
    @Expose
    private String metaDescriptionDe;

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getContentAr() {
        return contentAr;
    }

    public void setContentAr(String contentAr) {
        this.contentAr = contentAr;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getLangKey() {
        return langKey;
    }

    public void setLangKey(Object langKey) {
        this.langKey = langKey;
    }

    public String getPageBg() {
        return pageBg;
    }

    public void setPageBg(String pageBg) {
        this.pageBg = pageBg;
    }

    public String getNameSv() {
        return nameSv;
    }

    public void setNameSv(String nameSv) {
        this.nameSv = nameSv;
    }

    public String getContentSv() {
        return contentSv;
    }

    public void setContentSv(String contentSv) {
        this.contentSv = contentSv;
    }

    public String getMetaTitleSv() {
        return metaTitleSv;
    }

    public void setMetaTitleSv(String metaTitleSv) {
        this.metaTitleSv = metaTitleSv;
    }

    public String getMetaKeywordsSv() {
        return metaKeywordsSv;
    }

    public void setMetaKeywordsSv(String metaKeywordsSv) {
        this.metaKeywordsSv = metaKeywordsSv;
    }

    public String getMetaDescriptionSv() {
        return metaDescriptionSv;
    }

    public void setMetaDescriptionSv(String metaDescriptionSv) {
        this.metaDescriptionSv = metaDescriptionSv;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getContentDe() {
        return contentDe;
    }

    public void setContentDe(String contentDe) {
        this.contentDe = contentDe;
    }

    public String getMetaTitleDe() {
        return metaTitleDe;
    }

    public void setMetaTitleDe(String metaTitleDe) {
        this.metaTitleDe = metaTitleDe;
    }

    public String getMetaKeywordsDe() {
        return metaKeywordsDe;
    }

    public void setMetaKeywordsDe(String metaKeywordsDe) {
        this.metaKeywordsDe = metaKeywordsDe;
    }

    public String getMetaDescriptionDe() {
        return metaDescriptionDe;
    }

    public void setMetaDescriptionDe(String metaDescriptionDe) {
        this.metaDescriptionDe = metaDescriptionDe;
    }
}
