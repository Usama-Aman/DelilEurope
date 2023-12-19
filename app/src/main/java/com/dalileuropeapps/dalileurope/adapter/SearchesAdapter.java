package com.dalileuropeapps.dalileurope.adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalileuropeapps.dalileurope.R;

import androidx.recyclerview.widget.RecyclerView;


import com.dalileuropeapps.dalileurope.api.retrofit.SearchesResponseData;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class SearchesAdapter extends RecyclerView.Adapter<SearchesAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<SearchesResponseData> searchesList = new ArrayList<>();
    private List<SearchesResponseData> searchesListFiltered = new ArrayList<>();
    private List<SearchesResponseData> searchesListFilteredTemp = new ArrayList<>();
    private SearchesAdapterListener listener;
    String language = "";
    ArrayList<String> searchesResponseListHeadings = new ArrayList<>();

    public interface SearchesAdapterListener {
        void show(boolean toShow);

        void onSearchesResponseDataSelected(SearchesResponseData contact);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvName;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvName = view.findViewById(R.id.tvName);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSearchesResponseDataSelected(searchesListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public SearchesAdapter(Context context, ArrayList<String> searchesResponseListHeadings, List<SearchesResponseData> searchesList, SearchesAdapterListener listener) {
        this.context = context;
        this.listener = listener;

        for (SearchesResponseData responseData : searchesList) {
            this.searchesList.add(responseData.clone());
        }
        this.searchesListFiltered = searchesList;
        language = SharedPreference.getAppLanguage(context);
        this.searchesResponseListHeadings = searchesResponseListHeadings;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_searches, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SearchesResponseData responseData = searchesListFiltered.get(position);

        if (responseData.getId() == -1) {
            holder.tvTitle.setText(responseData.getCategory());
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.GONE);
        } else {

            holder.tvTitle.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.VISIBLE);


            if (language.equalsIgnoreCase("sv")) {
                holder.tvName.setText(responseData.getValueSv());
            } else if (language.equalsIgnoreCase("de")) {
                holder.tvName.setText(responseData.getValueDe());
            } else {
                holder.tvName.setText(responseData.getValue());
            }


        }


    }

    @Override
    public int getItemCount() {
        return searchesListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

               if (charString.isEmpty()) {
                    searchesListFiltered.clear();
                    for (SearchesResponseData responseData : searchesList) {
                        searchesListFiltered.add(responseData.clone());
                    }
                    listener.show(false);
                } else {

                    List<SearchesResponseData> filteredList = new ArrayList<>();
                    for (SearchesResponseData row : searchesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (language.equalsIgnoreCase("sv")) {

                            if (row.getValueSv().toLowerCase().contains(charString.toLowerCase()) && row.getId() != -1) {
                                filteredList.add(row);
                            }
                        } else if (language.equalsIgnoreCase("de")) {

                            if (row.getValueDe().toLowerCase().contains(charString.toLowerCase()) && row.getId() != -1) {
                                filteredList.add(row);
                            }
                        } else {

                            if (row.getValue().toLowerCase().contains(charString.toLowerCase()) && row.getId() != -1) {
                                filteredList.add(row);
                            }
                        }


                    }

                    searchesListFiltered = filteredList;


                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = searchesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                searchesListFiltered = (ArrayList<SearchesResponseData>) filterResults.values;

              if (searchesListFiltered != null && searchesListFiltered.size() != 0) {
                    arrangeSearch();
                } else {
                    notifyDataSetChanged();
                    listener.show(false);
                }
            }
        };
    }


    public void setDataChanged() {
        notifyDataSetChanged();
    }

    public void arrangeSearch() {

        searchesListFilteredTemp.clear();

        if (searchesListFiltered != null && searchesListFiltered.size() != 0) {


            searchesResponseListHeadings.clear();
            String lastHeading = "";

            for (int i = 0; i < searchesListFiltered.size(); i++) {
                SearchesResponseData responseData = searchesListFiltered.get(i);
                if (!ApplicationUtils.isSet(lastHeading)) {
                    searchesResponseListHeadings.add(responseData.getCategory());
                } else if (!lastHeading.equals(responseData.getCategory())) {
                    searchesResponseListHeadings.add(responseData.getCategory());
                }
                lastHeading = responseData.getCategory();
            }

            if (searchesResponseListHeadings != null && searchesResponseListHeadings.size() != 0) {

                for (String sHeading : searchesResponseListHeadings) {

                    SearchesResponseData responseDataHeading = new SearchesResponseData();
                    responseDataHeading.setValue(sHeading);
                    responseDataHeading.setValueDe(sHeading);
                    responseDataHeading.setValueSv(sHeading);
                    responseDataHeading.setId(-1);
                    responseDataHeading.setCategory(sHeading);
                    responseDataHeading.setKey(sHeading);
                    searchesListFilteredTemp.add(responseDataHeading);


                    for (int i = 0; i < searchesListFiltered.size(); i++) {
                        SearchesResponseData responseData = searchesListFiltered.get(i);

                        if (responseData.getCategory().equals(sHeading)) {
                            searchesListFilteredTemp.add(responseData.clone());
                        }
                    /*    for (int j = 0; j < searchesList.size(); j++) {
                            SearchesResponseData responseDataFilter = searchesList.get(j);
                            if (responseDataFilter.getId() != -1 && responseData.getId() != -1 && responseData.getId() == responseDataFilter.getId() && responseData.getCategory().equals(responseDataFilter.getCategory())) {

                                boolean yesEntered = false;
                                for (int k = 0; k < searchesListFilteredTemp.size(); k++) {
                                    SearchesResponseData responseDataFilterK = searchesListFilteredTemp.get(k);
                                    if (responseData.getId() == responseDataFilterK.getId() && responseData.getCategory().equals(responseDataFilterK.getCategory())) {
                                        yesEntered = true;
                                    }
                                }

                                if (!yesEntered)
                                    searchesListFilteredTemp.add(responseData.clone());
                            }
                        }*/
                    }
                }
                if (searchesListFilteredTemp != null && searchesListFilteredTemp.size() != 0) {
                    searchesListFiltered.clear();
                    searchesListFiltered = searchesListFilteredTemp;
                    listener.show(true);
                    notifyDataSetChanged();
                }


            }

        }
    }

    public List<SearchesResponseData> getSearchesListFiltered() {
        return searchesListFiltered;
    }

    public void setSearchesListFiltered(List<SearchesResponseData> searchesListFiltered) {
        this.searchesListFiltered = searchesListFiltered;
    }
}