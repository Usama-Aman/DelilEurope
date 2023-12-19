
package com.dalileuropeapps.dalileurope.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.FeaturedCategory;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;


//import me.grantland.widget.AutofitTextView;


public class FeaturedCategoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<FeaturedCategory> categories;
    private Activity mContext;
    onRowClickListener listener;
    String mLang = "en";

    public interface onRowClickListener {
        void onItemClick(int position);
    }


    public FeaturedCategoriesListAdapter(Activity context, List<FeaturedCategory> categories, onRowClickListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.categories = categories;
        this.mLang = SharedPreference.getAppLanguage(mContext);
    }

    public List<FeaturedCategory> getCategories() {
        return categories;
    }

    public void setHalls(ArrayList<FeaturedCategory> categories) {
        this.categories = categories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = getViewHolder(parent, inflater);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_featured_category, parent, false);
        viewHolder = new HallsVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            final FeaturedCategory category = categories.get(position);


            final HallsVH serviceVH = (HallsVH) holder;

            serviceVH.tvCategory.setText("");

            if (mLang.equals(Constants.swedish)) {
                serviceVH.tvCategory.setText(category.getNameSv());
            } else if (mLang.equals(Constants.germany)) {
                serviceVH.tvCategory.setText(category.getNameDe());
            } else if (mLang.equals(Constants.arabic)) {
                serviceVH.tvCategory.setText(category.getNameAr());
            } else {
                serviceVH.tvCategory.setText(category.getName());
            }

            if (category.isSelected())
                serviceVH.tvCategory.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            else
                serviceVH.tvCategory.setTextColor(mContext.getResources().getColor(R.color.colorTextGrey));

            serviceVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });


        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    protected class HallsVH extends RecyclerView.ViewHolder {

        public TextView tvCategory;


        public HallsVH(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
        }

        public void bind() {

        }
    }

}