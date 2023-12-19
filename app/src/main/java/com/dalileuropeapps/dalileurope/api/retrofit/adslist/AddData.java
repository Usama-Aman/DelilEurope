package com.dalileuropeapps.dalileurope.api.retrofit.adslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddData {
    @SerializedName("ads")
    @Expose
    private AddPagedResponse addResponse;

    public AddPagedResponse getAddResponse() {
        return addResponse;
    }

    public void setAddResponse(AddPagedResponse addResponse) {
        this.addResponse = addResponse;
    }
}
