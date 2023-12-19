package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.adapter.callbacks.PaginationAdapterCallback;
import com.dalileuropeapps.dalileurope.api.retrofit.Ads;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 3: red
 * 2: blue
 *
 * */
public class SearchAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private PaginationAdapterCallback mCallback;
    private ArrayList<Ads> adsList;
    private Activity mContext;
    String errorMsg;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    onRowClickListener listener;
    String mLang = "en";

    public interface onRowClickListener {
        void onItemClick(int position);
    }


    public SearchAdsAdapter(Activity context, ArrayList<Ads> adsListData, onRowClickListener listener) {
        this.mContext = context;
        adsList = adsListData;
        this.listener = listener;
        this.mLang = SharedPreference.getAppLanguage(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress_bar, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Ads ads = adsList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                try {


                    final AdsVH adsVH = (AdsVH) holder;
                    adsVH.tvLocation.setText("");
                    adsVH.tvTitle.setText("");
                    adsVH.tvRating.setText("(0)");
                    adsVH.tvDate.setText("");
                    adsVH.tvDescription.setText("");
                    adsVH.rbRate.setNumStars(0);


                    adsVH.ibComment.setText(" 0 " + mContext.getResources().getString(R.string.tv_comments));
                    adsVH.ibView.setText(" 0 " + mContext.getResources().getString(R.string.tv_views));
                    adsVH.ibFav.setText(" 0 " + mContext.getResources().getString(R.string.tv_likes));

                    if (ApplicationUtils.isSet(ads.getEndDate())) {
                        adsVH.tvDate.setText(ApplicationUtils.formatDateMonthDateYear(ads.getEndDate()));
                    }
                    if (ApplicationUtils.isSet(ads.getName()))
                        adsVH.tvTitle.setText(ads.getName());

                    if (ApplicationUtils.isSet(ads.getTag()))
                        adsVH.tvTag.setText(ads.getTag());

              /*      if (ApplicationUtils.isSet(ads.getStartDate()))
                        adsVH.tvDate.setText(ads.getStartDate().split(" ")[0]);*/

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

                    if (ApplicationUtils.isSet(ads.getFullFile()))
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

                    if (ads.getIsFeatured() == 2)
                        adsVH.rlBG.setBackgroundColor(mContext.getResources().getColor(R.color.colorHighlighted));
                    else if (ads.getIsFeatured() == 3)
                        adsVH.rlBG.setBackgroundColor(mContext.getResources().getColor(R.color.colorFeatured));

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

                    adsVH.ibView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(position);
                        }
                    });

                    adsVH.ibComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(position);
                        }
                    });

                    adsVH.ibFav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(position);
                        }
                    });

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

                    adsVH.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(position);
                        }
                    });

                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);
                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    mContext.getString(R.string.alert_msg_unknown));
                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return adsList == null ? 0 : adsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == adsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Ads mc) {
        adsList.add(mc);
        notifyItemInserted(adsList.size() - 1);
    }

    public void addAll(List<Ads> mcList) {
        for (Ads mc : mcList) {
            add(mc);
        }
    }

    public void remove(Ads city) {
        int position = adsList.indexOf(city);
        if (position > -1) {
            adsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            if (getItem(0) != null)
                remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Ads());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = adsList.size() - 1;
        Ads item = getItem(position);

        if (item != null) {
            adsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Ads getItem(int position) {
        try {
            return adsList.get(position);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
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
        TextView ibComment;
        TextView ibFav;
        RelativeLayout rlBG;
        RelativeLayout itemRlAds;
        ConstraintLayout itemClAds;
        private TextView tvTag;

        public AdsVH(View view) {
            super(view);
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
            tvTag = (TextView) itemView.findViewById(R.id.tvTag);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;

        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);


            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(adsList.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

}

