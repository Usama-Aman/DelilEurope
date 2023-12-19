package com.dalileuropeapps.dalileurope.api.retrofit;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedCategoriesData {

    @SerializedName("categories")
    @Expose
    private List<FeaturedCategory> categories = null;

    public List<FeaturedCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<FeaturedCategory> categories) {
        this.categories = categories;
    }




}