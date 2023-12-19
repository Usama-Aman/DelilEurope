package com.dalileuropeapps.dalileurope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.City;
import com.dalileuropeapps.dalileurope.fragments.ProfileFragment;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.List;

public class CityLisAdaptor extends RecyclerView.Adapter<CityLisAdaptor.ViewHolder> {


    public List<City> mPostItems;
    Context ctx;


    Context context;
    CountyListFilter filter;


    RecItemClick recItemClick;

    CountryListAdaptor adaptor;

    int checkedPosition = -1;
    private String language = "";

    public interface RecItemClick {
        public void ItemClick(City country);
    }

    ProfileFragment profileFragment;

    public CityLisAdaptor(List<City> postItems, Context ctx, RecItemClick listener, int selectedPosition) {
        this.mPostItems = postItems;
        this.ctx = ctx;
        this.context = ctx;
        this.recItemClick = listener;
        this.profileFragment = profileFragment;
        this.checkedPosition = selectedPosition;

        if (SharedPreference.getAppLanguage(context) != null) {
            language = SharedPreference.getAppLanguage(context);
        } else {
            language = "en";
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.dialog_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (language.equalsIgnoreCase(Constants.swedish)) {
            holder.tvName.setText(mPostItems.get(position).getName_sv());
        } else if (language.equalsIgnoreCase(Constants.germany)) {
            holder.tvName.setText(mPostItems.get(position).getName_de());
        } else if (language.equalsIgnoreCase(Constants.arabic)) {
            holder.tvName.setText(mPostItems.get(position).getName_ar());
        } else {
            holder.tvName.setText(mPostItems.get(position).getName());
        }

        if (mPostItems.get(position).getId() == checkedPosition) {
            holder.dialoglistCheck.setVisibility(View.VISIBLE);
        } else {
            holder.dialoglistCheck.setVisibility(View.GONE);
        }
        holder.relHeaderListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedPosition = mPostItems.get(position).getId();
                recItemClick.ItemClick(mPostItems.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }


    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    City getItem(int position) {
        return mPostItems.get(position);
    }

    public void addItems(List<City> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvName;
        AppCompatCheckBox dialoglistCheck;
        CardView relHeaderListItem;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.dialoglistCheck = (AppCompatCheckBox) itemView.findViewById(R.id.dialoglistCheck);
            this.relHeaderListItem = (CardView) itemView.findViewById(R.id.relHeaderListItem);
        }

        protected void clear() {

        }


    }


    //RETURN FILTER OBJ
//    @Override
//    public Filter getFilter() {
//        if (filter == null) {
//            filter = new CountyListFilter(mPostItems, this);
//        }
//        return filter;
//    }
}
