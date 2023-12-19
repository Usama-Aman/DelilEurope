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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.adapter.callbacks.PaginationAdapterCallback;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsReview;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private PaginationAdapterCallback mCallback;
    private ArrayList<AdsReview> adsList;
    private Activity mContext;
    String errorMsg;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    onRowClickListener listener;
    String mLang = "en";

    public interface onRowClickListener {
        void onItemClick(int position);
    }


    public ReviewPaginationAdapter(Activity context, ArrayList<AdsReview> adsListData, onRowClickListener listener) {
        this.mContext = context;
        adsList = adsListData;
        this.listener = listener;
        this.mLang = SharedPreference.getAppLanguage(mContext);
    }

    public List<AdsReview> getAdsReviews() {
        return adsList;
    }

    public void setAdsReviews(ArrayList<AdsReview> adsList) {
        this.adsList = adsList;
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
        View v1 = inflater.inflate(R.layout.item_ads_reviews, parent, false);
        viewHolder = new ReviewsVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final AdsReview review = adsList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                try {

                    final ReviewsVH reviewsVH = (ReviewsVH) holder;
                    reviewsVH.tvTitle.setText("");
                    reviewsVH.tvName.setText("");
                    reviewsVH.tvDate.setText("");
                    reviewsVH.tvComments.setText("");
                    reviewsVH.rbRate.setRating(0);
                    reviewsVH.rbRateListExpert.setRating(0);
                    reviewsVH.rbRateListProfessional.setRating(0);
                    reviewsVH.tvTitle.setVisibility(View.GONE);
                    reviewsVH.rbRateListExpert.setVisibility(View.GONE);
                    reviewsVH.rbRateListProfessional.setVisibility(View.GONE);

                    reviewsVH.tvRateTitleExpert.setVisibility(View.GONE);
                    reviewsVH.tvRateTitleProfessional.setVisibility(View.GONE);

                    if (ApplicationUtils.isSet(review.getUserDetails().getFirstName()))
                        reviewsVH.tvName.setText(review.getUserDetails().getFirstName());

                    if (ApplicationUtils.isSet(review.getCreatedAt())) {
                        reviewsVH.tvDate.setText(ApplicationUtils.formatDateMonthDateYear(review.getCreatedAt()));
                    }

                    if (ApplicationUtils.isSet(review.getReviewTitle())) {
                        reviewsVH.tvTitle.setText(review.getReviewTitle());
                        reviewsVH.tvTitle.setVisibility(View.VISIBLE);
                    }

                    if (ApplicationUtils.isSet(review.getReview()))
                        reviewsVH.tvComments.setText(review.getReview());

                    if (ApplicationUtils.isSet(review.getRating())) {
                        reviewsVH.rbRate.setRating(Float.parseFloat(review.getRating()));
                    }

                    if (ApplicationUtils.isSet(review.getRatingExpertise())) {

                        if (Float.parseFloat(review.getRatingExpertise()) > 0) {
                            reviewsVH.rbRateListExpert.setRating(Float.parseFloat(review.getRatingExpertise()));
                            reviewsVH.rbRateListExpert.setVisibility(View.GONE);
                            reviewsVH.tvRateTitleExpert.setVisibility(View.GONE);
                        } else {
                            reviewsVH.rbRateListExpert.setVisibility(View.GONE);
                            reviewsVH.tvRateTitleExpert.setVisibility(View.GONE);
                        }


                    }

                    if (ApplicationUtils.isSet(review.getRatingProfessionalism())) {


                        if (Float.parseFloat(review.getRatingProfessionalism()) > 0) {
                            reviewsVH.rbRateListProfessional.setRating(Float.parseFloat(review.getRatingProfessionalism()));
                            reviewsVH.rbRateListProfessional.setVisibility(View.GONE);
                            reviewsVH.tvRateTitleProfessional.setVisibility(View.GONE);
                        } else {
                            reviewsVH.rbRateListProfessional.setVisibility(View.GONE);
                            reviewsVH.tvRateTitleProfessional.setVisibility(View.GONE);
                        }


                    }

                    Glide.with(mContext)
                            .load(review.getUserDetails().getFullImage())
                            .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                            .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                            .into(reviewsVH.ivProfile);

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

    public void add(AdsReview mc) {
        adsList.add(mc);
        notifyItemInserted(adsList.size() - 1);
    }

    public void addAll(List<AdsReview> mcList) {
        for (AdsReview mc : mcList) {
            add(mc);
        }
    }

    public void remove(AdsReview city) {
        int position = adsList.indexOf(city);
        if (position > -1) {
            adsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new AdsReview());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = adsList.size() - 1;
        AdsReview item = getItem(position);

        if (item != null) {
            adsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AdsReview getItem(int position) {
        return adsList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ReviewsVH extends RecyclerView.ViewHolder {

        public TextView tvName, tvDate, tvTitle, tvRateTitleExpert, tvRateTitleProfessional;
        public RatingBar rbRate, rbRateListExpert, rbRateListProfessional;
        ExpandableTextView tvComments;
        /*      ScrollView svComments;*/
        ImageView ivProfile;



        public ReviewsVH(View view) {
            super(view);
            /*   svComments = view.findViewById(R.id.svComments);*/
            tvName = view.findViewById(R.id.tvName);
            tvComments = view.findViewById(R.id.tvComments);
            tvDate = view.findViewById(R.id.tvDate);
            rbRate = view.findViewById(R.id.rbRateList);
            ivProfile = view.findViewById(R.id.imgUserProfile);
            tvRateTitleExpert = view.findViewById(R.id.tvRateTitleExpert);
            tvRateTitleProfessional = view.findViewById(R.id.tvRateTitleProfessional);
            rbRateListExpert = view.findViewById(R.id.rbRateListExpert);
            rbRateListProfessional = view.findViewById(R.id.rbRateListProfessional);
            tvTitle = view.findViewById(R.id.tvTitle);
/*
            svComments.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });*/
           // tvComments.setMovementMethod(new ScrollingMovementMethod());
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



