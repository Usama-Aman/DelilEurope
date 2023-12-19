package com.dalileuropeapps.dalileurope.adapter.models;

import com.dalileuropeapps.dalileurope.api.retrofit.SubCategory;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;


public class MultiCheckCategories extends MultiCheckExpandableGroup {


    public MultiCheckCategories(String title, List<SubCategory> subItems) {
        super(title, subItems);

    }


}
