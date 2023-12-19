package com.dalileuropeapps.dalileurope.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.dalileuropeapps.dalileurope.fragments.FavrouteAddListingFragment.toRefreshFavroute;
import static com.dalileuropeapps.dalileurope.fragments.FavrouteAddListingFragment.fromFavroute;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.adapter.AdsPhotoListAdapter;
import com.dalileuropeapps.dalileurope.adapter.ReviewsAdapter;
import com.dalileuropeapps.dalileurope.api.retrofit.AdLikes;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsFeature;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsReview;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessHour;
import com.dalileuropeapps.dalileurope.api.retrofit.CategoryDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponseDataDetail;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.PaymentMethod;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAds;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAdsData;
import com.dalileuropeapps.dalileurope.api.retrofit.SubCategory;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.api.retrofit.UserDetailsAd;
import com.dalileuropeapps.dalileurope.api.retrofit.message.SendMessageResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.dialogs.AllCategoriesDialog;
import com.dalileuropeapps.dalileurope.dialogs.InquiryDialog;
import com.dalileuropeapps.dalileurope.dialogs.popup.VPPhotoFullPopupWindow;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.ProgressRequestBody;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.dalileuropeapps.dalileurope.utils.SpacesItemDecorationHorizontal;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DetailAdsActivity extends BaseActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    NestedScrollView nsMainDetail;
    public TextView tvTitle;
    TextView tvTagDetail;
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
    TextView tvCategories;
    TextView tvStatus;
    TextView tvLink;
    AdsPhotoListAdapter mGAdapter;
    RelativeLayout rlImages;
    RecyclerView rvImages;
    ImageView ivAddExpandDetail;
    TextView tvServices;
    TextView tvLanguages;
    TextView tvPrice;
    TextView tvPayment;
    TextView tvCategory;
    TextView tvImageDetail;
    LinearLayout llOther;
    LinearLayout llHours;
    ConstraintLayout clMessage;
    ProgressBar progressBar;
    int userId = 0;
    Button btSubmit;
    Button btReviewsExpand;
    EditText etReview;
    RatingBar rbRateGive;
    User userDetail;
    RecyclerView rvReviews;
    RelativeLayout rlReviewsDetail;
    LinearLayoutManager mLayoutManagerReview;
    Activity mContext;
    LinearLayoutManager mGridLayoutManager;
    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    Toolbar toolbar;
    double lat = 0.0;
    double lng = 0.0;
    int adId = 0;
    int adUserId = 0;
    boolean isReviewsShown = false;
    boolean isMessageShown = false;
    ArrayList<GalleryResponseDataDetail> mGalleryList = new ArrayList<>();
    ArrayList<AdsReview> reviewsListData = new ArrayList<>();
    ReviewsAdapter mReviewsAdapter;
    View iReviews;
    View iMessageDetail;
    TextView tvPhotosDetail;
    TextView tvPhotosViewAllDetail;
    private static final int PERMISSIONS_REQUEST_ACCESS_CALL = 1;

    EditText etMessage;
    Button btSubmitMessage;
    ImageView ivProfileMessage;
    TextView tvNameMessage;
    String toUserId = "";
    String toUserImage = "";
    String userYear = "";
    String userName = "";
    String userFirstName = "";
    ImageView ivChat;
    public static boolean toRefresh = false;

    private ThreadPoolExecutor executor;
    ProgressDialog pbProgress;
    ConstraintLayout clReviewDetail;
    String imageReviewPath;

    MultipartBody.Part imageBodyPart;

    View vReviewsImage;
    ImageView ivReviewsImage;
    ImageView ivDelete;
    ConstraintLayout clDetails;
    String mCurrency = "";

    EditText etTitle;
    RatingBar rbExpert;
    RatingBar rbProfessional;
    TextView tvRatingText;
    private String language = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_detail_ads);
        mContext = this;
        // transparentStatusBar();
        mCurrency = SharedPreference.getSharedPrefValue(mContext, Constants.USERDEFAULT_CURRENCY);
        getUserDetail();
        getData();
        initToolBar();
        initViews();
        getData();
        setData();
        getAdsDetail();
    }


    public void initToolBar() {

        iToolbar = (View) findViewById(R.id.iToolbar);
        toolbar = (Toolbar) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_back_arrow_small);

        btnToolbarRight = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_right);
        btnToolbarRight.setVisibility(View.GONE);
        btnToolbarRight.setImageResource(R.drawable.ic_share_small);

        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.tv_ads_detail));

        btnToolbarBack.setOnClickListener(this);
        btnToolbarRight.setOnClickListener(this);


    }


    public void initViews() {
        language = SharedPreference.getAppLanguage(this);


        clMessage = findViewById(R.id.clMessage);
        clDetails = findViewById(R.id.clDetails);
        tvTagDetail = findViewById(R.id.tvTagDetail);
        iReviews = findViewById(R.id.iReviewsDetail);
        iMessageDetail = findViewById(R.id.iMessageDetail);
        progressBar = findViewById(R.id.progressBar);
        nsMainDetail = findViewById(R.id.nsMainDetail);
        btReviewsExpand = findViewById(R.id.btReviewsExpandDetail);
        tvTitle = findViewById(R.id.tvTitleDetail);
        ivAds = findViewById(R.id.ivAdsDetail);
        ivLogo = findViewById(R.id.ivLogoDetail);
        tvLocation = findViewById(R.id.tvLocationDetail);
        rbRate = findViewById(R.id.rbRateDetail);
        tvRating = findViewById(R.id.tvRatingDetail);
        tvDate = findViewById(R.id.tvDateDetail);
        tvDescription = findViewById(R.id.tvDescriptionDetail);
        ibView = findViewById(R.id.ibViewDetail);
        ibComment = findViewById(R.id.ibCommentDetail);
        ibFav = findViewById(R.id.ibFavDetail);
        tvCategories = findViewById(R.id.tvCategoriesDetail);
        tvStatus = findViewById(R.id.tvStatusDetail);
        tvStatus.setText("");
        tvLink = findViewById(R.id.tvLinkDetail);
        rlImages = findViewById(R.id.rlImagesDetail);
        rvImages = findViewById(R.id.rvImagesDetail);
        ivChat = findViewById(R.id.ivChat);
        ivChat.setVisibility(View.INVISIBLE);
        tvServices = findViewById(R.id.tvServicesDetail);
        tvLanguages = findViewById(R.id.tvLanguagesDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvPayment = findViewById(R.id.tvPaymentDetail);
        tvCategory = findViewById(R.id.tvCategoryDetail);
        ivAddExpandDetail = findViewById(R.id.ivAddExpandDetail);
        tvPhotosDetail = findViewById(R.id.tvPhotosDetail);
        tvPhotosViewAllDetail = findViewById(R.id.tvPhotosViewAllDetail);

        tvContact = findViewById(R.id.tvContactDetail);
        llOther = findViewById(R.id.llOtherDetail);
        llHours = findViewById(R.id.llHoursDetail);
        btSubmit = findViewById(R.id.btSubmitDetail);
        etReview = findViewById(R.id.etReviewDetail);
        rbRateGive = findViewById(R.id.rbRateGiveDetail);
        rvReviews = findViewById(R.id.rvReviewsDetail);
        rlReviewsDetail = findViewById(R.id.rlReviewsDetail);

        etMessage = findViewById(R.id.etMessage);
        btSubmitMessage = findViewById(R.id.btSubmitMessage);
        ivProfileMessage = findViewById(R.id.imgUserProfileMessage);
        tvNameMessage = findViewById(R.id.tvNameMessage);

        tvImageDetail = findViewById(R.id.tvImageDetail);

        clReviewDetail = findViewById(R.id.clReviewDetail);

        pbProgress = new ProgressDialog(mContext);
        pbProgress.setMax(100); // Progress Dialog Max Value
        pbProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
        pbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_states));
        pbProgress.setCancelable(false);

        etTitle = findViewById(R.id.etTitle);
        rbExpert = findViewById(R.id.rbExpert);
        rbProfessional = findViewById(R.id.rbProfessional);
        tvRatingText = findViewById(R.id.tvRating);


        vReviewsImage = findViewById(R.id.ivReviews);
        ivReviewsImage = findViewById(R.id.ivGalleryImageDetails);
        ivDelete = findViewById(R.id.ivDelete);


    }

    public void getUserDetail() {
        userDetail = SharedPreference.getUserData(mContext);
        userId = userDetail.getId();
    }

    public void getData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            adId = bundle.getInt("ad_id");
        }
    }

    public void setData() {
        clMessage.setOnClickListener(this);
        clDetails.setOnClickListener(this);
        ivAds.setOnClickListener(this);
        nsMainDetail.setOnClickListener(this);
        etMessage.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (etMessage.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


        etReview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (etReview.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


        mGridLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(mGridLayoutManager);
        mGAdapter = new AdsPhotoListAdapter(mContext, mGalleryList);
        rvImages.setItemAnimator(new DefaultItemAnimator());
        rvImages.addItemDecoration(new SpacesItemDecorationHorizontal(16, true, 2));
        rvImages.setAdapter(mGAdapter);


        rvReviews.setHasFixedSize(true);
        mLayoutManagerReview = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(mLayoutManagerReview);
        mReviewsAdapter = new ReviewsAdapter(mContext, reviewsListData);
        rvReviews.setAdapter(mReviewsAdapter);

        // btReviewsExpand.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        tvPhotosDetail.setOnClickListener(this);
        tvPhotosViewAllDetail.setOnClickListener(this);
        ibComment.setOnClickListener(this);
        ibFav.setOnClickListener(this);
        tvLink.setOnClickListener(this);
        tvContact.setOnClickListener(this);
        btSubmitMessage.setOnClickListener(this);
        iMessageDetail.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        iReviews.setOnClickListener(this);


        tvImageDetail.setOnClickListener(this);
        clReviewDetail.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
    }


    public void getAdsDetail() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        etMessage.setText("");
        etReview.setText("");
        etTitle.setText("");
        rbRateGive.setRating(0);
        rbExpert.setRating(0);
        rbProfessional.setRating(0);
        showProgressBar(true);
        getAdsDetail(adId);
    }

    private void getAdsDetail(int id) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getAdsDetail(id, new Callback<ResponseDetailAds>() {
            @Override
            public void onResponse(Call<ResponseDetailAds> call,
                                   final Response<ResponseDetailAds> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData() != null) {

                            ResponseDetailAdsData detailAdsData = response.body().getData();

                            adUserId = detailAdsData.getUserId();


                            if (detailAdsData.getIsActive() == 1) {
                                tvStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                                tvStatus.setText(getResources().getString(R.string.tv_active));
                            } else {
                                tvStatus.setTextColor(Color.RED);
                                tvStatus.setText(getResources().getString(R.string.tv_not_active));
                                if (detailAdsData.getIsFeatured() != 1) {
                                    if (!ApplicationUtils.isCurrentDateAfterDate(detailAdsData.getEndDate()) && !ApplicationUtils.isSet(detailAdsData.getPaymentStartDate())) {
                                        tvStatus.setTextColor(getResources().getColor(R.color.colorOrange));
                                        tvStatus.setText(getResources().getString(R.string.tv_in_active));
                                    }
                                }
                            }

                            if (ApplicationUtils.isSet(detailAdsData.getTag())) {
                                tvTagDetail.setText(detailAdsData.getTag());
                            }

                            if (ApplicationUtils.isSet(detailAdsData.getEndDate())) {
                                tvDate.setText(ApplicationUtils.formatDateMonthDateYear(detailAdsData.getEndDate()));
                            }

                            if (detailAdsData.getUserDetails() != null) {
                                UserDetailsAd user = detailAdsData.getUserDetails();
                                toUserId = user.getId().toString();

                                if (ApplicationUtils.isSet(user.getFullImage()) && mContext != null) {
                                    try {
                                        Glide.with(mContext)
                                                .load(detailAdsData.getUserDetails().getFullImage())
                                                .placeholder(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                                                .error(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                                                .into(ivProfileMessage);
                                        toUserImage = detailAdsData.getUserDetails().getFullImage();
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }

                                if (ApplicationUtils.isSet(user.getFirstName())) {
                                    String year = "\n" + getResources().getString(R.string.tv_member_since) + ApplicationUtils.formatDateAsYear(user.getCreatedAt());
                                    tvNameMessage.setText(user.getFirstName() + year);
                                    userFirstName = user.getFirstName();
                                    userName = user.getFirstName();
                                    userYear = year;
                                }

                            }

                            if (ApplicationUtils.isSet(detailAdsData.getName()))
                                tvTitle.setText(detailAdsData.getName());

                            if (detailAdsData.getAvgRatings() != null) {
                                if (ApplicationUtils.isSet(detailAdsData.getAvgRatings().toString())) {
                                    float stars = 0;
                                    stars = Float.parseFloat(detailAdsData.getAvgRatings().toString());
                                    rbRate.setRating(stars);
                                }
                            } else {
                                rbRate.setRating(0);
                            }

                            if (detailAdsData.getReviewsCount() != null)
                                tvRating.setText("(" + detailAdsData.getReviewsCount() + ")");
                            else
                                tvRating.setText("(" + 0 + ")");

                            if (mContext != null) {
                                try {
                                    Glide.with(mContext)
                                            .load(detailAdsData.getFullFile())
                                            .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                            .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                            .into(ivAds);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                            ivAds.setTag(detailAdsData.getFullFile());

                            if (detailAdsData.getBusinessDetails() != null) {
                                BusinessDetails businessDetails = detailAdsData.getBusinessDetails();
                                if (ApplicationUtils.isSet(businessDetails.getPhoneNumber()))
                                    tvContact.setText(businessDetails.getPhoneNumber());

                                if (mContext != null) {
                                    try {
                                        Glide.with(mContext)
                                                .load(businessDetails.getThumbLogo())
                                                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                                .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                                .into(ivLogo);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }

                                if (llHours.getChildCount() != 0)
                                    llHours.removeAllViews();

                                if (businessDetails.getBusinessHours() != null && businessDetails.getBusinessHours().size() != 0) {
                                    List<BusinessHour> businessHours = businessDetails.getBusinessHours();

                                    for (int i = 0; i < businessHours.size(); i++) {
                                        BusinessHour businessHourOne = businessHours.get(i++);

                                        BusinessHour businessHourTwo = null;
                                        if (i < businessHours.size())
                                            businessHourTwo = businessHours.get(i);

                                        final LayoutInflater layoutInflater =
                                                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View v = layoutInflater.inflate(R.layout.item_hours, null, false);

                                        TextView tvDayNameOne = (TextView) v.findViewById(R.id.tvDayNameOne);
                                        TextView tvTimeOne = (TextView) v.findViewById(R.id.tvTimeOne);

                                        TextView tvDayNameTwo = (TextView) v.findViewById(R.id.tvDayNameTwo);
                                        TextView tvTimeTwo = (TextView) v.findViewById(R.id.tvTimeTwo);

                                        tvDayNameOne.setText(businessHourOne.getDay());

                                        if (businessHourTwo != null)
                                            tvDayNameTwo.setText(businessHourTwo.getDay());

                                        tvTimeOne.setText("Closed");
                                        tvTimeOne.setTextColor(Color.RED);
                                        tvTimeTwo.setTextColor(Color.RED);

                                        if (businessHourTwo != null) {
                                            tvTimeTwo.setText("Closed");
                                        }


                                        if (!businessHourOne.getDayStatus().equalsIgnoreCase("closed") && !businessHourOne.getDayStatus().equalsIgnoreCase("00:00:00-00:00:00")) {
                                            tvTimeOne.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
                                            tvTimeOne.setText(businessHourOne.getStartTime() + " - " + businessHourOne.getEndTime());
                                        }
                                        if (businessHourTwo != null)
                                            if (!businessHourTwo.getDayStatus().equalsIgnoreCase("closed") && !businessHourTwo.getDayStatus().equalsIgnoreCase("00:00:00-00:00:00")) {
                                                tvTimeTwo.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
                                                tvTimeTwo.setText(businessHourTwo.getStartTime() + " - " + businessHourTwo.getEndTime());
                                            }

                                        llHours.addView(v);
                                    }


                                } else {
                                    llHours.setVisibility(View.GONE);
                                }

                            }

                            ibComment.setText(" " + detailAdsData.getReviewsCount() + " " + mContext.getResources().getString(R.string.tv_comments));
                            ibView.setText(" " + detailAdsData.getViewsCount() + " " + mContext.getResources().getString(R.string.tv_views));
                            if (detailAdsData.getViewsCount() != 0) {
                                ibView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_view_select_small, 0, 0, 0);
                            } else {
                                ibView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_view_unselect_small, 0, 0, 0);
                            }


                            if (detailAdsData.getReviewsCount() != 0) {
                                ibComment.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_comments_select_small, 0, 0, 0);
                            } else {
                                ibComment.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_comments_unselect_small, 0, 0, 0);
                            }

                            ibFav.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_fav_uselect_small, 0, 0, 0);
                            if (detailAdsData.getFavoriteCount() != 0) {
                                if (detailAdsData.getAdsLikes() != null && detailAdsData.getAdsLikes().size() != 0) {
                                    for (AdLikes adLikes : detailAdsData.getAdsLikes()) {
                                        if (adLikes.getUserId() == userId) {
                                            ibFav.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_fav_select_small, 0, 0, 0);
                                            break;
                                        }
                                    }
                                }
                            }


                            ibFav.setText(" " + detailAdsData.getFavoriteCount() + " " + mContext.getResources().getString(R.string.tv_likes));

                            if (ApplicationUtils.isSet(detailAdsData.getDescription()))
                                tvDescription.setText(detailAdsData.getDescription());

                            if (ApplicationUtils.isSet(detailAdsData.getAdLocation()))
                                tvLocation.setText(detailAdsData.getAdLocation());

                            if (ApplicationUtils.isSet(detailAdsData.getAdUrl()))
                                tvLink.setText(detailAdsData.getAdUrl());

                            if (ApplicationUtils.isSet(detailAdsData.getLatitude()) && ApplicationUtils.isSet(detailAdsData.getLongitude())) {
                                lat = Double.parseDouble(detailAdsData.getLatitude());
                                lng = Double.parseDouble(detailAdsData.getLongitude());
                            }


                            String price = getResources().getString(R.string.labelRegularPrice) + " " + mCurrency;
                            String dPrice = getResources().getString(R.string.labelDiscountedPrice) + " " + mCurrency;
                            String priceVal = "0.00";
                            String dPriceVal = "0.00";

                            if (ApplicationUtils.isSet(detailAdsData.getAdPrice())) {
                                priceVal = detailAdsData.getAdPrice();
                            }
                            if (ApplicationUtils.isSet(detailAdsData.getAdDiscPrice())) {
                                dPriceVal = detailAdsData.getAdDiscPrice();
                            }

                            tvPrice.setText(price + priceVal + "\n" + dPrice + dPriceVal);
                            String paymentMethods = "";
                            if (detailAdsData.getAdsPaymentMethods() != null && detailAdsData.getAdsPaymentMethods().size() != 0) {
                                for (PaymentMethod paymentMethod : detailAdsData.getAdsPaymentMethods()) {
                                    if (paymentMethod.getMethodId() == 3)
                                        paymentMethods = mContext.getResources().getString(R.string.tv_credit_card) + "," + paymentMethods;
                                    else if (paymentMethod.getMethodId() == 4)
                                        paymentMethods = mContext.getResources().getString(R.string.tv_paypal) + "," + paymentMethods;
                                    else if (paymentMethod.getMethodId() == 5)
                                        paymentMethods = mContext.getResources().getString(R.string.tv_master_card) + "," + paymentMethods;
                                }
                            }

                            if (ApplicationUtils.isSet(paymentMethods))
                                tvPayment.setText(removeLastChar(paymentMethods));
                            else {
                                tvPayment.setVisibility(View.GONE);
                            }

                            String categories = "";
                            if (detailAdsData.getCategoryDetails() != null) {
                                CategoryDetails categoryDetails = detailAdsData.getCategoryDetails();

                                if (language.equalsIgnoreCase(Constants.english)) {
                                    categories = categoryDetails.getName();
                                } else if (language.equalsIgnoreCase(Constants.swedish)) {
                                    categories = categoryDetails.getNameSv();
                                } else if (language.equalsIgnoreCase(Constants.germany)) {
                                    categories = categoryDetails.getNameDe();
                                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                                    categories = categoryDetails.getNameAr();
                                }


                            }
                            if (detailAdsData.getSubcategoryDetails() != null) {
                                SubCategory subcategoryDetails = detailAdsData.getSubcategoryDetails();

                                if (language.equalsIgnoreCase(Constants.english)) {
                                    categories = categories + ", " + subcategoryDetails.getName();
                                } else if (language.equalsIgnoreCase(Constants.swedish)) {
                                    categories = categories + ", " + subcategoryDetails.getNameSv();
                                } else if (language.equalsIgnoreCase(Constants.germany)) {
                                    categories = categories + ", " + subcategoryDetails.getNameDe();
                                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                                    categories = categories + ", " + subcategoryDetails.getNameAr();
                                }

                            }

                            if (ApplicationUtils.isSet(categories)) {
                                tvCategories.setText(categories);
                                tvCategory.setText(categories);
                            } else {
                                tvCategories.setText("");
                                tvCategory.setText("");
                            }

                            if (ApplicationUtils.isSet(detailAdsData.getAdLanguages())) {
                                tvLanguages.setText(detailAdsData.getAdLanguages());
                            } else {
                                tvLanguages.setVisibility(View.GONE);
                            }

                            if (ApplicationUtils.isSet(detailAdsData.getAdService())) {
                                tvServices.setText(detailAdsData.getAdService());
                            } else {
                                tvServices.setVisibility(View.GONE);
                            }

                            if (llOther.getChildCount() != 0)
                                llOther.removeAllViews();

                            if (detailAdsData.getAdsFeatures() != null && detailAdsData.getAdsFeatures().size() != 0) {

                                List<AdsFeature> adsFeatures = detailAdsData.getAdsFeatures();
                                for (int i = 0; i < adsFeatures.size(); i++) {

                                    AdsFeature adsFeature = adsFeatures.get(i);
                                    final LayoutInflater layoutInflater =
                                            (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View v = layoutInflater.inflate(R.layout.item_other, null, false);

                                    TextView tvNameOther = (TextView) v.findViewById(R.id.tvNameOther);
                                    TextView tvStatusOther = (TextView) v.findViewById(R.id.tvStatusOther);


                                    tvNameOther.setText(adsFeature.getKeyName() + ": ");
                                    tvStatusOther.setText(adsFeature.getValue());

                                    llOther.addView(v);

                                }
                            }

                            if (detailAdsData.getAdsImages() != null && detailAdsData.getAdsImages().size() != 0) {
                                mGalleryList.clear();
                                mGalleryList.addAll(detailAdsData.getAdsImages());
                                mGAdapter.notifyDataSetChanged();
                                mGAdapter.setPopupViewImages();

                                if (mContext != null) {
                                    try {
                                        Glide.with(mContext)
                                                .load(detailAdsData.getAdsImages().get(0).getFullImage())
                                                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                                .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                                .into(ivAds);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }


                                ivAds.setTag(detailAdsData.getAdsImages().get(0).getFullImage());
                            }

                            //ApplicationUtils.collapse(rlReviewsDetail);
                            if (detailAdsData.getAdsReviews() != null && detailAdsData.getAdsReviews().size() != 0) {
                                btReviewsExpand.setVisibility(View.VISIBLE);
                                rvReviews.setVisibility(View.VISIBLE);
                                ivAddExpandDetail.setVisibility(View.VISIBLE);
                                reviewsListData.clear();
                                reviewsListData.addAll(detailAdsData.getAdsReviews());
                                mReviewsAdapter.notifyDataSetChanged();
                            } else {
                                btReviewsExpand.setVisibility(View.GONE);
                                rvReviews.setVisibility(View.GONE);
                                ivAddExpandDetail.setVisibility(View.GONE);
                            }

                            if (userId != detailAdsData.getUserId()) {
                                iReviews.setVisibility(View.VISIBLE);
                                ivChat.setVisibility(View.VISIBLE);
                            }

                        } else {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_information),
                                    mContext.getString(R.string.no_data_found_text));
                        }
                    }
                } else {
                    if (response.code() == 401)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_unauthorized_user));
                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailAds> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }


    public String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }


    RequestBody ratingExpert;
    RequestBody ratingProfessional;

    private void createReviewRequestData() {
        try {


            if (!ApplicationUtils.isOnline(mContext)) {
                DialogFactory.showDropDownNotificationError(mContext,
                        mContext.getString(R.string.alert_information),
                        mContext.getString(R.string.alert_internet_connection));
                return;
            }

            if (!validateRate())
                return;
            if (!validateReviewTitle())
                return;
            if (!validateReview())
                return;

            showProgressBar(true);
            final RequestBody adsIdbody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(adId));
            final RequestBody title = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etTitle.getText().toString());
            final RequestBody review = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etReview.getText().toString());
            final RequestBody rating = RequestBody.create(okhttp3.MediaType.parse("text/plain"), rbRateGive.getRating() + "");

            ratingExpert = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "0");
            ratingProfessional = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "0");

            if (rbExpert.getRating() != 0)
                ratingExpert = RequestBody.create(okhttp3.MediaType.parse("text/plain"), rbExpert.getRating() + "");

            if (rbProfessional.getRating() != 0)
                ratingProfessional = RequestBody.create(okhttp3.MediaType.parse("text/plain"), rbProfessional.getRating() + "");


            imageBodyPart = null;
            if (ApplicationUtils.isSet(imageReviewPath)) {
                File profileImgFile = new File(imageReviewPath);
                RequestBody image = RequestBody.create(
                        MediaType.parse("image/jpeg"),
                        profileImgFile
                );
                imageBodyPart = MultipartBody.Part.createFormData("image", profileImgFile.getName(), image);
            }


            executor = new ThreadPoolExecutor(
                    5,
                    10,
                    30,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
            );
            executor.allowCoreThreadTimeOut(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        callGiveReview(adsIdbody, review, rating, imageBodyPart, ratingExpert, ratingProfessional, title);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callGiveReview(RequestBody adsId, RequestBody review, RequestBody rating, MultipartBody.Part imageBodyPart, RequestBody ratingExpert, RequestBody ratingProfessional, RequestBody title) {

        ApiClient apiClient = ApiClient.getInstance();

        apiClient.giveReview(adsId, review, rating, imageBodyPart, ratingExpert, ratingProfessional, title, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, final Response<GeneralResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {


                        DialogFactory.showDropDownNotificationSuccess(
                                mContext,
                                mContext.getString(R.string.alert_success),
                                response.body().getMessage());
                        etReview.setText("");
                        etTitle.setText("");
                        rbRateGive.setRating(0);
                        rbExpert.setRating(0);
                        rbProfessional.setRating(0);
                        getAdsDetail();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {


                                toRefresh = false;

                                Intent detailComments = new Intent(mContext, ReviewsActivity.class);
                                detailComments.putExtra("ad_id", adId);

                                detailComments.putExtra("ad_user_id", adUserId);
                                startActivity(detailComments);
                            }
                        }, 1000);


                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
                    if (response.code() == 401) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<GeneralResponse>() {
                        }.getType();
                        GeneralResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);

                        if (errorResponse.getMessage() != null && !errorResponse.getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    errorResponse.getMessage());
                        }
                    }
                    if (response.code() == 404) {
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    }
                    if (response.code() == 500) {
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }


    @Override
    public void onProgressUpdate(int percentage) {
        pbProgress.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        // do something on upload finished
        // for example start next uploading at queue
        pbProgress.setProgress(100);
        pbProgress.dismiss(); // Dismiss Progress Dialog
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (imageReturnedIntent != null) {
            imageReviewPath = ImagePicker.Companion.getFilePath(imageReturnedIntent);

            if (ApplicationUtils.isSet(imageReviewPath)) {
                Uri uri = Uri.fromFile(new File(imageReviewPath));
                Picasso.with(mContext).load(uri)
                        .into(ivReviewsImage);
                vReviewsImage.setVisibility(View.VISIBLE);
            }
        }


    }

    private boolean validateRate() {

        if (rbRateGive.getRating() == 0) {
            DialogFactory.showDropDownNotificationError(mContext,//context
                    getResources().getString(R.string.alert_error),//title
                    getResources().getString(R.string.error_msg_review_rating_required));//detail;
            requestFocus(rbRateGive);
            return false;
        }
        return true;
    }

    private boolean validateReviewTitle() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(//
                    mContext,//context
                    getResources().getString(R.string.alert_error),//title
                    getResources().getString(R.string.error_msg_review_title_required));//detail;
            requestFocus(etTitle);
            return false;
        }
        return true;
    }


    private boolean validateReview() {
        if (etReview.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(//
                    mContext,//context
                    getResources().getString(R.string.alert_error),//title
                    getResources().getString(R.string.error_msg_review_note_required));//detail;
            requestFocus(etReview);
            return false;
        }
        return true;
    }

    private boolean validateMessage() {
        if (etMessage.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(//
                    mContext,//context
                    getResources().getString(R.string.alert_error),//title
                    getResources().getString(R.string.error_message_required));//detail;
            requestFocus(etMessage);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void makeAdsFavAndLike() {
        if (!ApplicationUtils.isOnline(mContext)) {
            ibFav.setEnabled(true);
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        showProgressBar(true);
        postFavourite(adId);
    }

    public void postFavourite(int adsId) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.addToFavList(String.valueOf(adsId), new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call,
                                   final retrofit2.Response<GeneralResponse> response) {
                showProgressBar(false);
                ibFav.setEnabled(true);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {
                        DialogFactory.showDropDownNotificationSuccess(
                                mContext,
                                mContext.getString(R.string.alert_success),
                                response.body().getMessage());
                        if (fromFavroute)
                            toRefreshFavroute = true;
                        getAdsDetail();

                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
                    if (response.code() == 401)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_unauthorized_user));
                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                    ibFav.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(mContext,
                                new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_ACCESS_CALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "You can`t access calls.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .setMessage("Call permission required. Please allow the call permission.")
                .show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clMessage:
            case R.id.clDetails:
            case R.id.nsMainDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                break;
            case R.id.ivAdsDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                if (v.getTag() != null)
                    new VPPhotoFullPopupWindow(mContext, R.layout.dialog_pop_vp, v, v.getTag().toString(), null, 0);
                break;
            case R.id.iReviewsDetail:
            case R.id.iMessageDetail:
            case R.id.clReviewDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                break;
            case R.id.btn_toolbar_back:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                finish();
                break;
            case R.id.tvContactDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                if (Build.VERSION.SDK_INT < 23) {
                    if (ApplicationUtils.isSet(tvContact.getText().toString())) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvContact.getText().toString()));
                        startActivity(callIntent);
                    } else {
                        DialogFactory.showDropDownNotificationError(mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_no_information_provided));
                    }
                    return;
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    if (ApplicationUtils.isSet(tvContact.getText().toString())) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvContact.getText().toString()));
                        startActivity(callIntent);
                    } else {
                        DialogFactory.showDropDownNotificationError(mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_no_information_provided));
                    }
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE))
                        showRationaleDialog();
                    else
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_ACCESS_CALL);
                }

                break;
            case R.id.tvLinkDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                if (ApplicationUtils.isSet(tvLink.getText().toString())) {

                    String url = tvLink.getText().toString();
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }

                    if (Patterns.WEB_URL.matcher(url).matches()) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    } else {
                        DialogFactory.showDropDownNotificationError(mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_url_no_valid));
                    }
                } else {
                    DialogFactory.showDropDownNotificationError(mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_no_information_provided));
                }
                break;
            case R.id.ibFavDetail:

                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
             /*   if (adUserId != userId) {
                    ibFav.setEnabled(false);
                    makeAdsFavAndLike();
                }*/
                ibFav.setEnabled(false);
                makeAdsFavAndLike();
                break;
            case R.id.ibCommentDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);

                Intent detailComments = new Intent(mContext, ReviewsActivity.class);
                detailComments.putExtra("ad_id", adId);
                detailComments.putExtra("ad_user_id", adUserId);
                startActivity(detailComments);
                break;
            case R.id.tvPhotosViewAllDetail:
            case R.id.tvPhotosDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                Intent detailIntent = new Intent(mContext, GalleryActivity.class);
                detailIntent.putExtra("ad_id", adId);
                startActivity(detailIntent);
                break;
            case R.id.btSubmitDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                createReviewRequestData();
                break;
            case R.id.tvLocationDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);

                if (!ApplicationUtils.isSet(tvLocation.getText().toString()))
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_error),
                            mContext.getResources().getString(R.string.tv_location_not_found));

                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + tvLocation.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(mapIntent);
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_error),
                            mContext.getString(R.string.msg_google_apps_not_found));

                break;

            case R.id.btReviewsExpandDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                if (reviewsListData.size() != 0) {

                    if (!isReviewsShown) {
                        isReviewsShown = true;
                        ApplicationUtils.expand(rlReviewsDetail);
                        mLayoutManagerReview.scrollToPositionWithOffset(reviewsListData.size(), 20);
                    } else {
                        isReviewsShown = false;
                        ApplicationUtils.collapse(rlReviewsDetail);
                    }

                } else {
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_error),
                            mContext.getString(R.string.no_data_found_text));
                }

                break;
            case R.id.btSubmitMessage:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);

                if (!ApplicationUtils.isOnline(mContext)) {
                    DialogFactory.showDropDownNotificationError(mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_internet_connection));
                    return;
                }


                if (!validateMessage())
                    return;
                showProgressBar(true);
                sendMessage();


                break;

            case R.id.ivChat:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
              /*  if (!isMessageShown) {
                    isMessageShown = true;
                    iMessageDetail.setVisibility(View.VISIBLE);
                } else {
                    isMessageShown = false;
                    iMessageDetail.setVisibility(View.GONE);
                }*/

                openMessageDialog();

                break;
            case R.id.tvImageDetail:
                //openRatingDialog();
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                openMediaChooserSheet();
                break;
            case R.id.ivDelete:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);
                ApplicationUtils.clearFocus(etReview);
                if (ApplicationUtils.isSet(imageReviewPath)) {

                    try {
                        File file = new File(imageReviewPath);
                        ApplicationUtils.removeFileFromDisk(file);
                        vReviewsImage.setVisibility(View.GONE);
                        imageReviewPath = null;
                    } catch (Exception e) {
                        vReviewsImage.setVisibility(View.GONE);
                        imageReviewPath = null;
                    }
                }

                break;
        }
    }

    void sendMessage() {

        showProgressBar(true);
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(DetailAdsActivity.this, Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<SendMessageResponse> call = api.sendMessage(token, "0" + "", toUserId, etMessage.getText().toString());
        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {


                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            showProgressBar(false);
                            etMessage.setText("");
                            iMessageDetail.setVisibility(View.GONE);
                            if (response.body().getData() != null) {
                                final UserMessages userMessages = response.body().getData();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ApplicationUtils.hideKeyboard(((Activity) mContext));
                                        Intent intent = new Intent(mContext, ChatActivity.class);
                                        intent.putExtra("thread_id", userMessages.getThreadId());
                                        intent.putExtra("name", userFirstName);
                                        intent.putExtra("to_user_id", toUserId + "");
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }
                        } else {
                            ApplicationUtils.showToast(DetailAdsActivity.this, response.body().getMessage() + "", false);
                        }
                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        ApplicationUtils.showToast(DetailAdsActivity.this, jsonObject.getString("message") + "", false);
                    }

                } catch (Exception e) {
                    showProgressBar(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (toRefresh)
            getAdsDetail();
        toRefresh = false;
    }

    private void openMediaChooserSheet() {
        ApplicationUtils.hideKeyboard(mContext);

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mContext);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_upload_image, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        TextView tvCamera = (TextView) sheetView.findViewById(R.id.tvOpenCamera);
        TextView tvGallery = (TextView) sheetView.findViewById(R.id.tvGallery);
        Button btnCancel = (Button) sheetView.findViewById(R.id.btnCancel);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                openImageCamera();

            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                openImageGallery();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });
        mBottomSheetDialog.show();
    }

    //Gallery image selection
    public void openImageGallery() {
        ImagePicker.Companion.with(this)
                .galleryOnly()
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    //Camera Capturing
    public void openImageCamera() {
        ImagePicker.Companion.with(this)
                .cameraOnly()
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    public void openMessageDialog() {
        InquiryDialog inquiryDialog = new InquiryDialog(new InquiryDialog.onClickListener() {
            @Override
            public void onSubmitMessage() {

            }

        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("adId", adId + "");
        args.putString("toUserId", toUserId + "");

        args.putString("toUserImage", toUserImage + "");
        args.putString("userYear", userYear + "");
        args.putString("userName", userName + "");


        inquiryDialog.setArguments(args);
        inquiryDialog.show(ft, AllCategoriesDialog.TAG);
    }
}
