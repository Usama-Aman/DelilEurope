package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.Ads;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.List;


public class FeatureAndHighlightedAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Ads> adsList;
    private Activity mContext;
    onRowClickListener listener;
    String mLang = "en";

    public interface onRowClickListener {
        void onItemClick(int position);
    }


    public FeatureAndHighlightedAdsAdapter(Activity context, List<Ads> adsList, onRowClickListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.adsList = adsList;
        this.mLang = SharedPreference.getAppLanguage(mContext);
    }

    public List<Ads> getAdsList() {
        return adsList;
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
        View v1 = inflater.inflate(R.layout.item_featured, parent, false);
        viewHolder = new AdsVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            final Ads ads = adsList.get(position);


            final AdsVH adsVH = (AdsVH) holder;
            adsVH.tvLocation.setText("");
            adsVH.tvTitle.setText("");

            if (ApplicationUtils.isSet(ads.getName()))
                adsVH.tvTitle.setText(ads.getName());

            if (ApplicationUtils.isSet(ads.getAdLocation()))
                adsVH.tvLocation.setText(ads.getAdLocation());

            adsVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            if (ads.getBusinessDetails() != null) {
                BusinessDetails businessDetails = ads.getBusinessDetails();
                Glide.with(mContext)
                        .load(businessDetails.getThumbLogo())
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(adsVH.ivLogo);
            }


            Glide.with(mContext)
                    .load(ads.getFullFile())
                    .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                    .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                    .into(adsVH.ivImage);

            if (ads.getAdsImages() != null && ads.getAdsImages().size() != 0) {
                Glide.with(mContext)
                        .load(ads.getAdsImages().get(0).getFullImage())
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(adsVH.ivImage);
            }


            adsVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });


            adsVH.tvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });


            adsVH.ivLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });


            adsVH.rlFeatured.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.llFeatured.setOnClickListener(new View.OnClickListener() {
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
        return adsList == null ? 0 : adsList.size();
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    protected class AdsVH extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        ImageView ivImage;
        ImageView ivLogo;
        TextView tvLocation;
        LinearLayout llFeatured;
        RelativeLayout rlFeatured;


        public AdsVH(View view) {
            super(view);
            llFeatured = view.findViewById(R.id.llFeatured);
            rlFeatured = view.findViewById(R.id.rlFeatured);
            tvTitle = view.findViewById(R.id.tvTitleFeatured);
            ivImage = view.findViewById(R.id.ivImageFeatured);
            ivLogo = view.findViewById(R.id.ivLogoFeatured);
            tvLocation = view.findViewById(R.id.tvLocationFeatured);
        }

        public void bind() {

        }
    }

}