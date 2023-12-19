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
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessCategory;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.List;

public class AllCatAdaptor extends RecyclerView.Adapter<AllCatAdaptor.ViewHolder> {


    public List<BusinessCategory> mPostItems;
    Context ctx;


    Context context;
    CountyListFilter filter;


    RecItemClick recItemClick;

    CountryListAdaptor adaptor;

    int checkedPosition = -1;

    public interface RecItemClick {
        public void ItemClick(BusinessCategory country);
    }


    String language = "";

    public AllCatAdaptor(List<BusinessCategory> postItems, Context ctx, RecItemClick listener, int selectedPosition) {
        this.mPostItems = postItems;
        this.ctx = ctx;
        this.context = ctx;
        this.recItemClick = listener;
        this.checkedPosition = selectedPosition;
        language = SharedPreference.getAppLanguage(ctx);
//        Toast.makeText(ctx, language, Toast.LENGTH_SHORT).show();

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
            holder.tvName.setText(mPostItems.get(position).getNameSv() + " sv");
        } else if (language.equalsIgnoreCase(Constants.germany)) {
            holder.tvName.setText(mPostItems.get(position).getNameDe() + " de");
        } else if (language.equalsIgnoreCase(Constants.arabic)) {
            holder.tvName.setText(mPostItems.get(position).getNameAr());
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

    BusinessCategory getItem(int position) {
        return mPostItems.get(position);
    }

    public void addItems(List<BusinessCategory> postItems) {
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


}

