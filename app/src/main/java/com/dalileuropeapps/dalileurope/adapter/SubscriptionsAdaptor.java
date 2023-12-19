package com.dalileuropeapps.dalileurope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.Subscriptions;

import java.util.List;

public class SubscriptionsAdaptor extends RecyclerView.Adapter<SubscriptionsAdaptor.ViewHolder> {


    public List<Subscriptions> mPostItems;
    Context ctx;


    Context context;
    CountyListFilter filter;


    RecItemClick recItemClick;

    CountryListAdaptor adaptor;
    String currency;

    int checkedPosition = -1;

    public interface RecItemClick {
        public void ItemClick(Subscriptions country);
    }


    public SubscriptionsAdaptor(List<Subscriptions> postItems, Context ctx, String currency, RecItemClick listener, int selectedPosition) {
        this.mPostItems = postItems;
        this.ctx = ctx;
        this.context = ctx;
        this.recItemClick = listener;
        this.currency = currency;
        this.checkedPosition = selectedPosition;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.subscription_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if (mPostItems.get(position).getDuration() > 1) {
            holder.tvName.setText(context.getResources().getString(R.string.tv_subscription_text) + " " + currency + mPostItems.get(position).getFee() + " / " + mPostItems.get(position).getDuration() + " " + context.getResources().getString(R.string.tv_months) + ")");

        } else {
            holder.tvName.setText(context.getResources().getString(R.string.tv_subscription_text) + " " + currency + mPostItems.get(position).getFee() + " / " + mPostItems.get(position).getDuration() + " " + context.getResources().getString(R.string.tv_month) + ")");
        }


        if (mPostItems.get(position).getId() == checkedPosition) {
            holder.dialoglistCheck.setChecked(true);
        } else {
            holder.dialoglistCheck.setChecked(false);
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

    Subscriptions getItem(int position) {
        return mPostItems.get(position);
    }

    public void addItems(List<Subscriptions> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvName;
        AppCompatRadioButton dialoglistCheck;
        CardView relHeaderListItem;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.dialoglistCheck = (AppCompatRadioButton) itemView.findViewById(R.id.dialoglistCheck);
            this.relHeaderListItem = (CardView) itemView.findViewById(R.id.relHeaderListItem);
        }

        protected void clear() {

        }


    }


}
