package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusinessCategoryData {
    @SerializedName("categories")
    @Expose
    private List<BusinessCategory> categories = null;

    public List<BusinessCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<BusinessCategory> categories) {
        this.categories = categories;
    }
}
