package com.dalileuropeapps.dalileurope.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import static com.dalileuropeapps.dalileurope.activities.DetailAdsActivity.toRefresh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.adapter.ReviewPaginationAdapter;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsReview;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseReviews;

import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.dialogs.AllCategoriesDialog;
import com.dalileuropeapps.dalileurope.dialogs.RatingDialog;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.PaginationScrollListener;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.dalileuropeapps.dalileurope.utils.SpacesItemDecoration;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView tvNoDataFound;
    ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager mLayoutManagerReviewsAds;
    private AppCompatActivity mContext;
    private RecyclerView rvReviewList;
    ReviewPaginationAdapter adapter;
    private ArrayList<AdsReview> adsReviews = new ArrayList<AdsReview>();
    int adId = 0;
    int userId = 0;
    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    Toolbar toolbar;

    int currentPage = 0;
    private int TOTAL_PAGES = 0;
    boolean isLoading = false;
    boolean isLastPage = false;
    ConstraintLayout clReviewDetail;
    EditText etReview;
    RatingBar rbRateGive;
    Button btSubmit;

    boolean isReviewsShown = false;
    View iReviews;
    TextView tvImageDetail;
    private String imagePath = "";
    private ThreadPoolExecutor executor;
    ProgressDialog pbProgress;


    View ivReviews;
    ImageView ivReviewsImage;
    ImageView ivDelete;
    int adUserId = 0;
    User userDetail;


    EditText etTitle;
    RatingBar rbExpert;
    RatingBar rbProfessional;
    TextView tvRating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mContext = (AppCompatActivity) this;
        //transparentStatusBar();
        getUserDetail();
        getActivityData();
        initToolBar();
        initViews();
        onRefresh();

    }

    public void getUserDetail() {
        userDetail = SharedPreference.getUserData(mContext);
        userId = userDetail.getId();
    }

    public void initToolBar() {


        iToolbar = (View) findViewById(R.id.iToolbar);
        toolbar = (Toolbar) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_back_arrow_small);

        btnToolbarRight = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_right);
        btnToolbarRight.setVisibility(View.VISIBLE);
        btnToolbarRight.setImageResource(R.drawable.ic_star_white_small);

        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.tv_rating_and_reviews));

        btnToolbarBack.setOnClickListener(this);
        btnToolbarRight.setOnClickListener(this);

    }

    public void getActivityData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            adId = bundle.getInt("ad_id");
            adUserId = bundle.getInt("ad_user_id");

        }
    }


    // init Views
    public void initViews() {


        if (adUserId != 0 && userId == adUserId)
            btnToolbarRight.setVisibility(View.GONE);

        iReviews = findViewById(R.id.iReviewsDetail);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        rvReviewList = (RecyclerView) findViewById(R.id.rvReviewList);
        rvReviewList.setHasFixedSize(true);
        mLayoutManagerReviewsAds = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvReviewList.setLayoutManager(mLayoutManagerReviewsAds);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._4sdp);
        rvReviewList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        clReviewDetail = findViewById(R.id.clReviewDetail);

        tvNoDataFound = (TextView) findViewById(R.id.tvNoDataFound);
        progressBar = findViewById(R.id.progressBar);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        tvImageDetail = findViewById(R.id.tvImageDetail);
        btSubmit = findViewById(R.id.btSubmitDetail);
        etReview = findViewById(R.id.etReviewDetail);
        rbRateGive = findViewById(R.id.rbRateGiveDetail);


        ivReviews = findViewById(R.id.ivReviews);
        ivReviewsImage = findViewById(R.id.ivGalleryImageDetails);
        ivDelete = findViewById(R.id.ivDelete);

        etTitle = findViewById(R.id.etTitle);
        rbProfessional = findViewById(R.id.rbProfessional);
        tvRating = findViewById(R.id.tvRating);
        rbExpert = findViewById(R.id.rbExpert);


        pbProgress = new ProgressDialog(mContext);
        pbProgress.setMax(100); // Progress Dialog Max Value
        pbProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
        pbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_states));
        pbProgress.setCancelable(false);

        ivDelete.setOnClickListener(this);
        iReviews.setOnClickListener(this);
        clReviewDetail.setOnClickListener(this);
        tvImageDetail.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }


    /*****************************************************************************************/
    /*****************************************************************************************/


    public void callGetFirstReviews() {

        currentPage = 1;
        TOTAL_PAGES = 0;
        isLoading = false;
        isLastPage = false;
        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
            tvNoDataFound.setVisibility(View.GONE);
            showProgressBar(true);
            getReviewsDataFirst(adId, currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReviewsDataFirst(int adsId, int page) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getReviewsList(adsId, page, new Callback<ResponseReviews>() {
            @Override
            public void onResponse(Call<ResponseReviews> call, Response<ResponseReviews> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {

                        if (response.body().getData() != null && response.body().getData().getAdsReviews() != null) {
                            if (response.body().getData().getAdsReviews().size() > 0) {
                                showReviewsListFirst(response);
                            } else {
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                rvReviewList.setVisibility(View.GONE);
                                tvNoDataFound.setVisibility(View.VISIBLE);
                                rvReviewList.setVisibility(View.INVISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            rvReviewList.setVisibility(View.INVISIBLE);
                            showProgressBar(false);
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                        tvNoDataFound.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReviews> call, Throwable t) {
                rvReviewList.setVisibility(View.INVISIBLE);
                tvNoDataFound.setVisibility(View.VISIBLE);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    private void callReviewsListNext() {

        if (currentPage < TOTAL_PAGES) {
            currentPage = currentPage + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!ApplicationUtils.isOnline(mContext)) {
                            DialogFactory.showDropDownNotificationError(mContext,
                                    mContext.getString(R.string.alert_information),
                                    mContext.getString(R.string.alert_internet_connection));
                            return;
                        }

                        tvNoDataFound.setVisibility(View.GONE);
                        getReviewsDataNext(adId, currentPage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            rvReviewList.post(new Runnable() {
                public void run() {
                    // There is no need to use notifyDataSetChanged()
                    isLoading = false;
                    adapter.removeLoadingFooter();
                    isLastPage = true;
                }
            });
        }
    }

    private void getReviewsDataNext(int adsId, int page) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getReviewsList(adsId, page, new Callback<ResponseReviews>() {
            @Override
            public void onResponse(Call<ResponseReviews> call, Response<ResponseReviews> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getAdsReviews() != null) {
                            if (response.body().getData().getAdsReviews().size() > 0) {
                                showReviewsListNext(response);
                            } else {
                                adapter.removeLoadingFooter();
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                tvNoDataFound.setVisibility(View.VISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            rvReviewList.setVisibility(View.INVISIBLE);
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            showProgressBar(false);
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                        rvReviewList.setVisibility(View.INVISIBLE);
                        tvNoDataFound.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReviews> call, Throwable t) {
                rvReviewList.setVisibility(View.INVISIBLE);
                tvNoDataFound.setVisibility(View.VISIBLE);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }


    public void showReviewsListFirst(Response<ResponseReviews> response) {

        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tvNoDataFound.setVisibility(View.GONE);
        rvReviewList.setVisibility(View.VISIBLE);

        currentPage = response.body().getData().getCurrentPage();
        TOTAL_PAGES = response.body().getData().getLastPage();
        isLoading = false;
        adsReviews.clear();
        adsReviews.addAll(response.body().getData().getAdsReviews());

        adapter = new ReviewPaginationAdapter(mContext, adsReviews, new ReviewPaginationAdapter.onRowClickListener() {
            @Override
            public void onItemClick(int position) {
            }

        });
        rvReviewList.setAdapter(adapter);
        rvReviewList.addOnScrollListener(new PaginationScrollListener(mLayoutManagerReviewsAds) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                callReviewsListNext();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        if (currentPage < TOTAL_PAGES) {
            adapter.addLoadingFooter();
        } else {
            isLastPage = true;
            mSwipeRefreshLayout.setRefreshing(false);
        }


    }

    public void showReviewsListNext(Response<ResponseReviews> response) {


        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tvNoDataFound.setVisibility(View.GONE);
        rvReviewList.setVisibility(View.VISIBLE);

        currentPage = response.body().getData().getCurrentPage();
        TOTAL_PAGES = response.body().getData().getLastPage();

        if (adapter != null)
            adapter.removeLoadingFooter();
        isLoading = false;
        adsReviews.addAll(response.body().getData().getAdsReviews());
        adapter.notifyDataSetChanged();
        rvReviewList.addOnScrollListener(new PaginationScrollListener(mLayoutManagerReviewsAds) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                callReviewsListNext();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        if (currentPage < TOTAL_PAGES) {
            adapter.addLoadingFooter();
        } else {
            isLastPage = true;
            mSwipeRefreshLayout.setRefreshing(false);
        }


    }

    /*****************************************************************************************/
    /*****************************************************************************************/


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_toolbar_back:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                finish();
                break;
            case R.id.btn_toolbar_right:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);

                openRatingDialog();

        /*        if (!isReviewsShown) {

                    if (adsReviews != null && adsReviews.size() == 0)
                        tvNoDataFound.setVisibility(View.GONE);

                    isReviewsShown = true;
                    iReviews.setVisibility(View.VISIBLE);
                } else {

                    if (adsReviews != null && adsReviews.size() == 0)
                        tvNoDataFound.setVisibility(View.VISIBLE);

                    etReview.setText("");
                    rbRateGive.setRating(0);
                    isReviewsShown = false;
                    iReviews.setVisibility(View.GONE);
                }*/
                break;
            case R.id.btSubmitDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                createReviewRequestData();
                break;
            case R.id.clReviewDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                break;
            case R.id.tvImageDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                openMediaChooserSheet();
                break;
            case R.id.ivDelete:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                if (ApplicationUtils.isSet(imagePath)) {

                    try {
                        File file = new File(imagePath);
                        ApplicationUtils.removeFileFromDisk(file);
                        ivReviews.setVisibility(View.GONE);
                        imagePath = null;
                    } catch (Exception e) {
                        ivReviews.setVisibility(View.GONE);
                        imagePath = null;
                    }
                }

                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (toRefresh) {
            toRefresh = false;
            onRefresh();
        }
    }


    public void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        try {
            resetPagination();
            callGetFirstReviews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void resetPagination() {
        currentPage = 0;
        TOTAL_PAGES = 0;
        isLastPage = false;
        isLoading = false;
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    MultipartBody.Part imageBodyPart;
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
            final RequestBody review = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etReview.getText().toString());
            final RequestBody title = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etTitle.getText().toString());
            final RequestBody rating = RequestBody.create(okhttp3.MediaType.parse("text/plain"), rbRateGive.getRating() + "");
            ratingExpert = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "0");
            ratingProfessional = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "0");

            if (rbExpert.getRating() != 0)
                ratingExpert = RequestBody.create(okhttp3.MediaType.parse("text/plain"), rbExpert.getRating() + "");

            if (rbProfessional.getRating() != 0)
                ratingProfessional = RequestBody.create(okhttp3.MediaType.parse("text/plain"), rbProfessional.getRating() + "");


            imageBodyPart = null;
            if (ApplicationUtils.isSet(imagePath)) {
                File profileImgFile = new File(imagePath);
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

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                iReviews.setVisibility(View.GONE);
                                toRefresh = true;
                                isReviewsShown = false;
                                onRefresh();
                            }
                        }, 500);


                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
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

    /***********************************************************************/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (imageReturnedIntent != null) {
            imagePath = ImagePicker.Companion.getFilePath(imageReturnedIntent);
            if (ApplicationUtils.isSet(imagePath)) {
                Uri uri = Uri.fromFile(new File(imagePath));
                Picasso.with(mContext).load(uri)
                        .into(ivReviewsImage);
                ivReviews.setVisibility(View.VISIBLE);
            }
        }
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

    public void openRatingDialog() {
        RatingDialog ratingDialog = new RatingDialog(new RatingDialog.onClickListener() {
            @Override
            public void onSubmitReview() {
                onRefresh();
            }

        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("adId", adId + "");
        ratingDialog.setArguments(args);
        ratingDialog.show(ft, AllCategoriesDialog.TAG);
    }
}

