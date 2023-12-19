package com.dalileuropeapps.dalileurope.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dalileuropeapps.dalileurope.adapter.GalleryAdapter;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponseDataDetail;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.PaginationScrollListener;
import com.dalileuropeapps.dalileurope.utils.SpacesItemDecoration;
import com.dalileuropeapps.dalileurope.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/***
 * Fragment to populate list of event's images
 * To upload images and to download images
 */
public class GalleryActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView tvNoDataFound;
    ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mGridLayoutManager;
    private AppCompatActivity mContext;
    private RecyclerView rvGalleryList;
    GalleryAdapter adapter;
    private ArrayList<GalleryResponseDataDetail> photosListData = new ArrayList<GalleryResponseDataDetail>();
    int adId = 0;
    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    Toolbar toolbar;
    int currentPage = 0;
    private int TOTAL_PAGES = 0;
    boolean isLoading = false;
    boolean isLastPage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mContext = (AppCompatActivity) this;
     //   transparentStatusBar();
        getActivityData();
        initToolBar();

        initViews();
        onRefresh();
    }


    public void initToolBar() {


        iToolbar = (View) findViewById(R.id.iToolbar);
        toolbar = (Toolbar) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_back_arrow_small);

        btnToolbarRight = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_right);
        btnToolbarRight.setVisibility(View.GONE);


        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(getResources().getString(R.string.tv_gallery));

        btnToolbarBack.setOnClickListener(this);
        btnToolbarRight.setOnClickListener(this);

    }

    public void getActivityData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            adId = bundle.getInt("ad_id");
        }
    }


    // init Views
    public void initViews() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        rvGalleryList = (RecyclerView) findViewById(R.id.rvPhotosList);
        rvGalleryList.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(mContext, 3);
        rvGalleryList.setLayoutManager(mGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._8sdp);
        rvGalleryList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        tvNoDataFound = (TextView) findViewById(R.id.tvNoDataFound);
        progressBar = findViewById(R.id.progressBar);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);


    }


    /*****************************************************************************************/
    /*****************************************************************************************/


    public void callGetFirstReviews() {


        currentPage = 1;
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
        apiClient.getGalleryList(adsId, page, new Callback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {

                        if (response.body().getData() != null && response.body().getData().getGalleryList() != null) {
                            if (response.body().getData().getGalleryList().size() > 0) {
                                showReviewsListFirst(response);
                            } else {
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                rvGalleryList.setVisibility(View.GONE);
                                tvNoDataFound.setVisibility(View.VISIBLE);
                                rvGalleryList.setVisibility(View.INVISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            rvGalleryList.setVisibility(View.INVISIBLE);
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
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                rvGalleryList.setVisibility(View.INVISIBLE);
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
            rvGalleryList.post(new Runnable() {
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
        apiClient.getGalleryList(adsId, page, new Callback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getGalleryList() != null) {
                            if (response.body().getData().getGalleryList().size() > 0) {
                                showReviewsListNext(response);
                            } else {
                                adapter.removeLoadingFooter();
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                tvNoDataFound.setVisibility(View.VISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            rvGalleryList.setVisibility(View.INVISIBLE);
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
                        rvGalleryList.setVisibility(View.INVISIBLE);
                        tvNoDataFound.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                rvGalleryList.setVisibility(View.INVISIBLE);
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


    public void showReviewsListFirst(Response<GalleryResponse> response) {

        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tvNoDataFound.setVisibility(View.GONE);
        rvGalleryList.setVisibility(View.VISIBLE);

        currentPage = response.body().getData().getCurrentPage();
        TOTAL_PAGES = response.body().getData().getLastPage();
        isLoading = false;
        photosListData.clear();
        photosListData.addAll(response.body().getData().getGalleryList());
        adapter = new GalleryAdapter(mContext, photosListData, new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onView(final int position) {

            }
        });
        rvGalleryList.setAdapter(adapter);
        rvGalleryList.addOnScrollListener(new PaginationScrollListener(mGridLayoutManager) {
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

    public void showReviewsListNext(Response<GalleryResponse> response) {


        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tvNoDataFound.setVisibility(View.GONE);
        rvGalleryList.setVisibility(View.VISIBLE);

        currentPage = response.body().getData().getCurrentPage();
        TOTAL_PAGES = response.body().getData().getLastPage();

        if (adapter != null)
            adapter.removeLoadingFooter();
        isLoading = false;
        photosListData.addAll(response.body().getData().getGalleryList());
        adapter.notifyDataSetChanged();
        rvGalleryList.addOnScrollListener(new PaginationScrollListener(mGridLayoutManager) {
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
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    public ArrayList<GalleryResponseDataDetail> getList() {
        return adapter.getList();
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

}