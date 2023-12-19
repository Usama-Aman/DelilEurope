package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchesResponseData implements Cloneable{
        @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("value_sv")
    @Expose
    private String valueSv;
    @SerializedName("value_de")
    @Expose
    private String valueDe;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("key")
    @Expose
    private String key;



    public String getCategory() {
        return category;
    }

    public String getValueSv() {
        return valueSv;
    }

    public void setValueSv(String valueSv) {
        this.valueSv = valueSv;
    }

    public String getValueDe() {
        return valueDe;
    }

    public void setValueDe(String valueDe) {
        this.valueDe = valueDe;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }




    @Override
    public SearchesResponseData clone() {
        try {
            return (SearchesResponseData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

}
