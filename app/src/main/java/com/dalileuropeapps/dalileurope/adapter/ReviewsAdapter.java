package com.dalileuropeapps.dalileurope.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsReview;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;


//import me.grantland.widget.AutofitTextView;


public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<AdsReview> reviews;
    private Activity context;


    public ReviewsAdapter(Activity context, List<AdsReview> reviews) {
        this.context = context;
        this.reviews = reviews;


    }

    public List<AdsReview> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<AdsReview> reviews) {
        this.reviews = reviews;
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
        View v1 = inflater.inflate(R.layout.item_ads_reviews, parent, false);
        viewHolder = new ReviewsVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            final AdsReview review = reviews.get(position);


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

            if (ApplicationUtils.isSet(review.getReview())) {
            /*    reviewsVH.svComments.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // Disallow the touch request for parent scroll on touch of child view
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        view.onTouch(motionEvent);
                        return true;
                    }
                });*/
                //reviewsVH.tvComments.setMovementMethod(new ScrollingMovementMethod());
                reviewsVH.tvComments.setText(review.getReview());
            }


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

            Glide.with(context)
                    .load(review.getUserDetails().getFullImage())
                    .placeholder(context.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                    .error(context.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                    .into(reviewsVH.ivProfile);


        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    protected class ReviewsVH extends RecyclerView.ViewHolder {

        public TextView tvName, tvDate, tvTitle, tvRateTitleExpert, tvRateTitleProfessional;
        ExpandableTextView tvComments;
        public RatingBar rbRate, rbRateListExpert, rbRateListProfessional;
        /*   ScrollView svComments;*/


        ImageView ivProfile;

        public ReviewsVH(View view) {
            super(view);
            /*    svComments = view.findViewById(R.id.svComments);*/
            tvName = view.findViewById(R.id.tvName);
            tvComments = view.findViewById(R.id.tvComments);
            tvDate = view.findViewById(R.id.tvDate);
            rbRate = view.findViewById(R.id.rbRateList);
            ivProfile = view.findViewById(R.id.imgUserProfile);
            tvTitle = view.findViewById(R.id.tvTitle);
            rbRateListExpert = view.findViewById(R.id.rbRateListExpert);
            rbRateListProfessional = view.findViewById(R.id.rbRateListProfessional);

            tvRateTitleExpert = view.findViewById(R.id.tvRateTitleExpert);
            tvRateTitleProfessional = view.findViewById(R.id.tvRateTitleProfessional);


        }

        public void bind() {

        }
    }

}