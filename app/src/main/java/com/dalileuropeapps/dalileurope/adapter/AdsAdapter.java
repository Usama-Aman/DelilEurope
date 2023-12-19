package com.dalileuropeapps.dalileurope.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.Ads;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.List;


public class AdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Ads> adsList;
    private Activity mContext;
    onRowClickListener listener;
    String mLang = "en";

    public interface onRowClickListener {
        void onItemClick(int position);
    }


    public AdsAdapter(Activity context, List<Ads> adsList, onRowClickListener listener) {
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
        View v1 = inflater.inflate(R.layout.item_ads, parent, false);
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
            adsVH.tvRating.setText("(0)");
            adsVH.tvDate.setText("");
            adsVH.tvDescription.setText("");
            adsVH.rbRate.setNumStars(0);
            adsVH.tvTag.setText("");

            adsVH.ibComment.setText(" 0 " + mContext.getResources().getString(R.string.tv_comments));
            adsVH.ibView.setText(" 0 " + mContext.getResources().getString(R.string.tv_views));
            adsVH.ibFav.setText(" 0 " + mContext.getResources().getString(R.string.tv_likes));


            if (ApplicationUtils.isSet(ads.getName()))
                adsVH.tvTitle.setText(ads.getName());


            if (ApplicationUtils.isSet(ads.getTag())) {
                adsVH.tvTag.setText(ads.getTag());
                adsVH.tvTag.setVisibility(View.VISIBLE);
            } else {
                adsVH.tvTag.setVisibility(View.GONE);
            }

            if (ApplicationUtils.isSet(ads.getEndDate())) {
                adsVH.tvDate.setText(ApplicationUtils.formatDateMonthDateYear(ads.getEndDate()));
            }

            if (ads.getBusinessDetails() != null) {
                BusinessDetails businessDetails = ads.getBusinessDetails();
                if (ApplicationUtils.isSet(businessDetails.getPhoneNumber()))
                    adsVH.tvContact.setText(businessDetails.getPhoneNumber());


                Glide.with(mContext)
                        .load(businessDetails.getThumbLogo())
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(adsVH.ivLogo);



            }
            adsVH.ibComment.setText(" " + ads.getAdsReviewsCount() + " " + mContext.getResources().getString(R.string.tv_comments));
            adsVH.ibView.setText(" " + ads.getAdsViewsCount() + " " + mContext.getResources().getString(R.string.tv_views));
            adsVH.ibFav.setText(" " + ads.getAdsLikesCount() + " " + mContext.getResources().getString(R.string.tv_likes));

            if (ads.getAdsViewsCount() != 0) {
                adsVH.ibView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_select_small, 0, 0, 0);
            } else {
                adsVH.ibView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_unselect_small, 0, 0, 0);
            }

            if (ads.getAdsReviewsCount() != 0) {
                adsVH.ibComment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_comments_select_small, 0, 0, 0);
            } else {
                adsVH.ibComment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_comments_unselect_small, 0, 0, 0);
            }

            if (ads.getAdsLikesCount() != 0) {
                adsVH.ibFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav_select_small, 0, 0, 0);
            } else {
                adsVH.ibFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav_uselect_small, 0, 0, 0);
            }

            if (ApplicationUtils.isSet(ads.getDescription()))
                adsVH.tvDescription.setText(ads.getDescription());

            if (ApplicationUtils.isSet(ads.getAdLocation()))
                adsVH.tvLocation.setText(ads.getAdLocation());

            if (ads.getAvgRatings() != null)
                if (ApplicationUtils.isSet(ads.getAvgRatings().toString()))
                    adsVH.rbRate.setRating(Float.parseFloat(ads.getAvgRatings().toString()));

            if (ads.getAdsReviewsCount() != null)
                adsVH.tvRating.setText("(" + ads.getAdsReviewsCount() + ")");
            else
                adsVH.tvRating.setText("(" + 0 + ")");

            if(ApplicationUtils.isSet(ads.getFullFile()))
            Glide.with(mContext)
                    .load(ads.getFullFile())
                    .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                    .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                    .into(adsVH.ivAds);


            if (ads.getAdsImages() != null && ads.getAdsImages().size() != 0) {
                Glide.with(mContext)
                        .load(ads.getAdsImages().get(0).getFullImage())
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(adsVH.ivAds);
            }

            adsVH.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });


            adsVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

   /*         adsVH.ibView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailComments = new Intent(mContext, ReviewsActivity.class);
                    detailComments.putExtra("ad_id", ads.getId());
                    mContext.startActivity(detailComments);
                }
            });

            adsVH.ibFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });*/

            adsVH.tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.rbRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.tvDescription.setOnClickListener(new View.OnClickListener() {
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

            adsVH.ivAds.setOnClickListener(new View.OnClickListener() {
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

            adsVH.itemRlAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.itemClAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            adsVH.rlBG.setOnClickListener(new View.OnClickListener() {
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
        ImageView ivAds;
        ImageView ivLogo;
        TextView tvLocation;
        RatingBar rbRate;
        TextView tvRating;
        TextView tvDate;
        TextView tvDescription;
        TextView tvContact;
        TextView ibView;
        TextView tvTag;
        TextView ibComment;
        TextView ibFav;
        RelativeLayout itemRlAds;
        ConstraintLayout itemClAds;
        RelativeLayout rlBG;

        public AdsVH(View view) {
            super(view);

            tvTag = view.findViewById(R.id.tvTag);
            itemRlAds = view.findViewById(R.id.itemRlAds);
            itemClAds = view.findViewById(R.id.itemClAds);
            tvTitle = view.findViewById(R.id.tvTitle);
            ivAds = view.findViewById(R.id.ivAds);
            ivLogo = view.findViewById(R.id.ivLogo);
            tvLocation = view.findViewById(R.id.tvLocation);
            rbRate = view.findViewById(R.id.rbRate);
            tvRating = view.findViewById(R.id.tvRating);
            tvDate = view.findViewById(R.id.tvDate);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvContact = view.findViewById(R.id.tvContact);
            ibView = view.findViewById(R.id.ibView);
            ibFav = view.findViewById(R.id.ibFav);
            ibComment = view.findViewById(R.id.ibComment);
            rlBG = view.findViewById(R.id.rlBG);
        }

    }

}