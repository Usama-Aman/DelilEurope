package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_sv")
    @Expose
    private String name_sv;
    @SerializedName("name_de")
    @Expose
    private String name_de;
    @SerializedName("name_ar")
    @Expose
    private String name_ar;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("short_code")
    @Expose
    private Object shortCode;

    private Boolean isChecked;

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

    public String getName_sv() {
        return name_sv;
    }

    public void setName_sv(String name_sv) {
        this.name_sv = name_sv;
    }

    public String getName_de() {
        return name_de;
    }

    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Object getShortCode() {
        return shortCode;
    }

    public void setShortCode(Object shortCode) {
        this.shortCode = shortCode;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
