package com.dalileuropeapps.dalileurope.api.local;

import com.dalileuropeapps.dalileurope.api.retrofit.SearchesResponseData;

import java.util.ArrayList;

public class SearchesModel {
    boolean isHeading = false;
    ArrayList<SearchesResponseData> searchesResponseList = new ArrayList<>();
    String heading = "";

    public boolean isHeading() {
        return isHeading;
    }

    public void setHeading(boolean heading) {
        isHeading = heading;
    }

    public ArrayList<SearchesResponseData> getSearchesResponseList() {
        return searchesResponseList;
    }

    public void setSearchesResponseList(ArrayList<SearchesResponseData> searchesResponseList) {
        this.searchesResponseList = searchesResponseList;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
