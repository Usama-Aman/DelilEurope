package com.dalileuropeapps.dalileurope.pagination_pack;

import android.widget.Filter;


import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;

import java.util.ArrayList;
import java.util.List;

public class CustomPaginationFilter extends Filter {
    MessagesThreadPAginationAdaptor adapter;
    List<UserMessages> filterList;

    public CustomPaginationFilter(List<UserMessages> filterList, MessagesThreadPAginationAdaptor adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<UserMessages> filteredPlayers = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                //CHECK
                if (filterList.get(i).getTo_user_details().getFirstName().toUpperCase().contains(constraint)) {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.mPostItems = (ArrayList<UserMessages>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}