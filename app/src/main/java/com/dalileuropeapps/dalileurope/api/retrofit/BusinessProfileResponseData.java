package com.dalileuropeapps.dalileurope.api.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessProfileResponseData {

    @SerializedName("business")
    @Expose
    private BusinessDetails business;
    @SerializedName("order")
    @Expose
    private BusinessOrderDetail order;

    public BusinessDetails getData() {
        return business;
    }

    public void setData(BusinessDetails business) {
        this.business = business;
    }


    public BusinessOrderDetail getOrder() {
        return order;
    }

    public void setOrder(BusinessOrderDetail order) {
        this.order = order;
    }
}
