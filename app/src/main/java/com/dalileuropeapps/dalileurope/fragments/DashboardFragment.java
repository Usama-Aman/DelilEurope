package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.LinearLayout.VERTICAL;
import static com.dalileuropeapps.dalileurope.activities.MainActivity.isBusinessAccount;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.DetailAdsActivity;
import com.dalileuropeapps.dalileurope.activities.FusedLocationActivity;
import com.dalileuropeapps.dalileurope.activities.PostAdActivity;
import com.dalileuropeapps.dalileurope.adapter.AdsAdapter;
import com.dalileuropeapps.dalileurope.adapter.FeatureAndHighlightedAdsAdapter;
import com.dalileuropeapps.dalileurope.adapter.FeaturedCategoriesListAdapter;
import com.dalileuropeapps.dalileurope.adapter.SearchAdsAdapter;
import com.dalileuropeapps.dalileurope.adapter.SearchesAdapter;
import com.dalileuropeapps.dalileurope.api.retrofit.Ads;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.FeaturedCategory;
import com.dalileuropeapps.dalileurope.api.retrofit.HomeResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.HomeResponseData;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseFeaturedCategories;
import com.dalileuropeapps.dalileurope.api.retrofit.SearchResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.SearchesResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.SearchesResponseData;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.dialogs.AllCategoriesDialog;
import com.dalileuropeapps.dalileurope.dialogs.popup.FilterPopup;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.dalileuropeapps.dalileurope.utils.SpacesItemDecorationHorizontal;
import com.dalileuropeapps.dalileurope.utils.SpacesItemDecorationVertical;
import com.dalileuropeapps.dalileurope.utils.URLogs;
import com.dalileuropeapps.dalileurope.utils.WrapContentLinearLayoutManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import static com.dalileuropeapps.dalileurope.activities.MainActivity.AUTOCOMPLETE_REQUEST_CODE_DASHBOARD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends BaseFragment implements View.OnTouchListener {

    View view;

    RelativeLayout rlParent;
    TextView tvHeading;
    public Boolean isDashboardActive;
    LinearLayout llSearch;
    EditText etSearchAds;
    AutoCompleteTextView etLocation;
    boolean mLocationCall = false;
    LinearLayout llCategories;
    RecyclerView rvFeaturedCategories;
    TextView tvMore;
    ImageView ivMore;
    ProgressBar progressBar;
    TextView tvFeatured;
    RecyclerView rvFeatured;
    SeekBar sbFeatured;
    RecyclerView rvAds;
    RecyclerView rvSearchAds;
    LinearLayoutManager mLayoutManagerFeaturedCategories;
    LinearLayoutManager mLayoutManagerFeatured;
    LinearLayoutManager mLayoutManagerAds;
    LinearLayoutManager mLayoutManagerSearchAds;
    RelativeLayout rlFeatured;
    FeaturedCategoriesListAdapter featuredCategoriesListAdapter;
    ArrayList<FeaturedCategory> featuredCategories = new ArrayList<>();
    Activity mContext;
    TextView tvNotFoundAds, tvNotFoundSearchAds;
    TextView tvNotFoundFeaturedAds;

    ArrayList<Ads> featuredAndHighlightedAds = new ArrayList<>();
    ArrayList<Ads> normalAds = new ArrayList<>();
    ArrayList<Ads> searchAds = new ArrayList<>();

    FeatureAndHighlightedAdsAdapter featureAndHighlightedAdsAdapter;
    AdsAdapter adsAdapter;
    private PlacesClient placesClient;
    //  RelativeLayout mSwipeRefreshLayout;
    ArrayList<String> searchesResponseListHeadings = new ArrayList<>();
    Timer timer;

    ArrayList<SearchesResponseData> searchesList = new ArrayList<>();
    ArrayList<SearchesResponseData> finalSearches = new ArrayList<>();
    int currentPage = 0;
    private int TOTAL_PAGES = 0;
    SearchAdsAdapter mSearchAdapter;
    boolean isLoading = false;
    boolean isLastPage = false;
    int selectedCategoryId = 0;
    int selectedSubCategoryId = 0;
    int selectedFilter = 0;
    String searchId = "";
    Animation slide_down;
    Animation slide_up;
    ImageButton ibFilter;

    String searchedLocation = "";
    String searchedText = "";
    int searchType = 0;

    double lat = 0.0;
    double lng = 0.0;
    boolean sliderProgressUser = false;

    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    RelativeLayout toolbar;
    private ActivityListener listener;

    ImageButton ibAddAds;
    ImageView ivAddAds;
    TextView tvAddAds;

    User userDetail;
    int userId = 0;
    boolean isBusinessProfileCreated = false;

    RelativeLayout rlClear;
    ImageButton ibClear;

    RelativeLayout rlClearLoc;
    ImageButton ibClearLoc;


    boolean isBusinessAccountPaid = false;
    boolean fromCategoryClick = false;

    String selectedCategoryText = "";
    String selectedSubCategoryText = "";

    double locationLat = 0;
    double locationLng = 0;
    boolean toDetails = false;
    RelativeLayout rlTempSearch;
    RecyclerView rvSearches;
    SearchesAdapter searchesAdapter;
    NestedScrollView nsScrollAds;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        String apiKey = getString(R.string.places_api_billing_key);
        if (!Places.isInitialized())
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        placesClient = Places.createClient(getActivity());



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = getActivity();
        try {
            listener = (ActivityListener) mContext;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }


        new UIAsync().execute();

        transparentStatusBar();
        setStatusBar();
        getUserDetail();
        initToolBar();
        initViews();
        set();
        //onRefresh();
        slide_down = AnimationUtils.loadAnimation(mContext,
                R.anim.translate_down);
        slide_up = AnimationUtils.loadAnimation(mContext,
                R.anim.translate_up);

       /* Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
        detailIntent.putExtra("ad_id", 64);
        startActivity(detailIntent);*/


        return view;
    }

    public void getUserDetail() {
        userDetail = SharedPreference.getUserData(mContext);
        userId = userDetail.getId();
        isBusinessProfileCreated = SharedPreference.getBoolSharedPrefValue(mContext, Constants.USERDEFAULT_ISBUSINESSACCOUNT, false);
        isBusinessAccountPaid = SharedPreference.getBoolSharedPrefValue(mContext, Constants.USERDEFAULT_PAIDSUBCRIPTION, false);

        // lat = userDetail.getLatitude();
        // lng = userDetail.getLongitude();
    }


    public void setStatusBar() {
        if (ApplicationUtils.getDeviceStatusBarHeight(getActivity()) > 0) {
            transparentStatusBar();
        }
    }

    public void initToolBar() {


        iToolbar = (View) view.findViewById(R.id.iToolbar);
        toolbar = (RelativeLayout) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);

        btnToolbarRight = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_right);
        btnToolbarRight.setVisibility(View.GONE);


        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("");

        btnToolbarBack.setOnTouchListener(this);
        btnToolbarRight.setOnTouchListener(this);

    }


    public void initViews() {
        nsScrollAds = view.findViewById(R.id.nsScrollAds);
        attachKeyboardListeners(view.findViewById(R.id.rlParent));
        rlClear = view.findViewById(R.id.rlClear);
        ibClear = view.findViewById(R.id.ibClear);
        rvSearches = view.findViewById(R.id.rvSearches);
        rlTempSearch = view.findViewById(R.id.rlTempSearch);
        rlClearLoc = view.findViewById(R.id.rlClearLoc);
        ibClearLoc = view.findViewById(R.id.ibClearLoc);

        ibFilter = view.findViewById(R.id.ibFilter);
        rlParent = view.findViewById(R.id.rlParent);
        progressBar = view.findViewById(R.id.progressBar);
        tvHeading = view.findViewById(R.id.tvHeading);
        tvNotFoundAds = view.findViewById(R.id.tvNotFoundAds);
        tvNotFoundSearchAds = view.findViewById(R.id.tvNotFoundSearchAds);
        tvNotFoundFeaturedAds = view.findViewById(R.id.tvNotFoundFeaturedAds);
        //     mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
/*        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);*/

        llSearch = view.findViewById(R.id.llSearch);
        etSearchAds = view.findViewById(R.id.etSearchAds);
        etLocation = view.findViewById(R.id.tvLocation);
        rlFeatured = view.findViewById(R.id.rlFeatured);

        llCategories = view.findViewById(R.id.llCategories);
        rvFeaturedCategories = view.findViewById(R.id.rvFeaturedCategories);
        tvMore = view.findViewById(R.id.tvMore);
        ivMore = view.findViewById(R.id.ivMore);


        tvFeatured = view.findViewById(R.id.tvFeatured);
        rvFeatured = view.findViewById(R.id.rvFeatured);
        sbFeatured = view.findViewById(R.id.sbFeatured);

        rvSearchAds = view.findViewById(R.id.rvSearchAds);
        rvAds = view.findViewById(R.id.rvAds);
        rvSearchAds.setNestedScrollingEnabled(false);
        ibAddAds = view.findViewById(R.id.ibAddAds);
        ivAddAds = view.findViewById(R.id.ivAddAds);
        tvAddAds = view.findViewById(R.id.tvAddAds);
        etSearchAds.setEnabled(false);


        nsScrollAds.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                String TAG = "nested_sync";
                if (scrollY > oldScrollY) {
                    Log.e(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.e(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.e(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.e(TAG, "BOTTOM SCROLL");
                }

                View view = (View) nsScrollAds.getChildAt(nsScrollAds.getChildCount() - 1);

                int diff = (view.getBottom() - (nsScrollAds.getHeight() + nsScrollAds
                        .getScrollY()));

                if (diff == 0 && !isLastPage && !isLoading && searchType != 0) {


                    isLoading = true;

                    if (searchType == 1)
                        callSearchListNext();
                    else
                        callSearchListNextText();

                }
            }
        });


    }


    public void set() {

        WrapContentLinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rvSearches.setLayoutManager(mLayoutManager);
        rvSearches.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider));
        rvSearches.addItemDecoration(dividerItemDecoration);

        ibAddAds.setVisibility(View.GONE);
        tvAddAds.setVisibility(View.GONE);
        ivAddAds.setVisibility(View.GONE);

        etLocation.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                return false;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });

        etLocation.setLongClickable(false);
        etLocation.setTextIsSelectable(false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        etLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_location_pin_search, 0, 0, 0);
        etLocation.setThreshold(3);
        etLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (ApplicationUtils.isOnline(mContext)) {
                        // etLocation.requestFocus();
                        mLocationCall = true;
                        Intent autocompleteIntent =
                                new Autocomplete.IntentBuilder(getMode(), getPlaceFields())
                                        .build(mContext);

                        getActivity().startActivityForResult(autocompleteIntent, AUTOCOMPLETE_REQUEST_CODE_DASHBOARD);
                    } else {
                        DialogFactory.showDropDownNotificationError(mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internet_connection));
                    }

                } catch (Exception e) {
                    // TODO: Handle the error.
                }

            }
        });

        tvMore.setOnTouchListener(this);
        ivMore.setOnTouchListener(this);
        ibFilter.setOnTouchListener(this);

        ibAddAds.setOnTouchListener(this);
        ivAddAds.setOnTouchListener(this);
        tvAddAds.setOnTouchListener(this);
        rvFeaturedCategories.setHasFixedSize(true);
        mLayoutManagerFeaturedCategories = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedCategories.setLayoutManager(mLayoutManagerFeaturedCategories);
        rvFeaturedCategories.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedCategories.addItemDecoration(new SpacesItemDecorationHorizontal(22, true, 2));
        featuredCategoriesListAdapter = new FeaturedCategoriesListAdapter(getActivity(), featuredCategories, new FeaturedCategoriesListAdapter.onRowClickListener() {
            @Override
            public void onItemClick(int position) {
                lat = 0;
                lng = 0;
                selectedFilter = 0;
                ApplicationUtils.hideKeyboard(mContext);
                resetLocationSearch();
                if (featuredCategories.get(position).isSelected()) {
                    if (featuredCategories != null && featuredCategories.size() != 0) {
                        for (FeaturedCategory category : featuredCategories)
                            category.setSelected(false);
                        featuredCategoriesListAdapter.notifyDataSetChanged();
                    }
                    resetCategorySearch();
                    onRefresh();
                } else {
                    fromCategoryClick = true;
                    etSearchAds.setText("");
                    for (FeaturedCategory category : featuredCategories)
                        category.setSelected(false);
                    if (rlFeatured.getVisibility() != View.GONE)
                        animateFeaturedView(false);
                    resetViewsSearchView();
                    searchType = 1;

                    tvHeading.setText(mContext.getResources().getString(R.string.tv_search_results));
                    resetPagination();
                    featuredCategories.get(position).setSelected(true);
                    featuredCategoriesListAdapter.notifyDataSetChanged();
                    selectedCategoryId = featuredCategories.get(position).getId();
                    selectedCategoryText = featuredCategories.get(position).getName();
                    selectedSubCategoryText = "";
                    selectedSubCategoryId = 0;
                    onRefresh();
                }


            }

        });
        rvFeaturedCategories.setAdapter(featuredCategoriesListAdapter);


        rvFeatured.setHasFixedSize(true);
        mLayoutManagerFeatured = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvFeatured.setLayoutManager(mLayoutManagerFeatured);
        rvFeatured.setItemAnimator(new DefaultItemAnimator());
        rvFeatured.addItemDecoration(new SpacesItemDecorationHorizontal(16, true, 4));
        featureAndHighlightedAdsAdapter = new FeatureAndHighlightedAdsAdapter(getActivity(), featuredAndHighlightedAds, new FeatureAndHighlightedAdsAdapter.onRowClickListener() {
            @Override
            public void onItemClick(int position) {
                toDetails = true;
                Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                detailIntent.putExtra("ad_id", featuredAndHighlightedAds.get(position).getId());
                startActivity(detailIntent);
            }

        });
        rvFeatured.setAdapter(featureAndHighlightedAdsAdapter);


        rvAds.setHasFixedSize(true);
        mLayoutManagerAds = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(mLayoutManagerAds);
        rvAds.setItemAnimator(new DefaultItemAnimator());
        rvAds.addItemDecoration(new SpacesItemDecorationVertical(16));
        adsAdapter = new AdsAdapter(getActivity(), normalAds, new AdsAdapter.onRowClickListener() {
            @Override
            public void onItemClick(int position) {
                toDetails = true;
                Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                detailIntent.putExtra("ad_id", normalAds.get(position).getId());
                startActivity(detailIntent);
            }

        });
        rvAds.setAdapter(adsAdapter);


        mLayoutManagerSearchAds = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchAds.setLayoutManager(mLayoutManagerSearchAds);
        rvSearchAds.setNestedScrollingEnabled(false);
        rvSearchAds.setItemAnimator(new DefaultItemAnimator());
        rvSearchAds.addItemDecoration(new SpacesItemDecorationVertical(16));
        sbFeatured.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

                sliderProgressUser = fromUser;
                if (fromUser) {
                    if (progress > -1) {
                        sbFeatured.setProgress(progress);
                        rvFeatured.smoothScrollToPosition(progress);
                    }
                }
            }
        });


        rvFeatured.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int ydy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!sliderProgressUser)
                    if (mLayoutManagerFeatured.findFirstCompletelyVisibleItemPosition() > -1) {

                        sbFeatured.setProgress(mLayoutManagerFeatured.findFirstCompletelyVisibleItemPosition());
                    }

                sliderProgressUser = false;
            }
        });

        ibClear.setOnTouchListener(this);
        rlClear.setOnTouchListener(this);

        rlClearLoc.setOnTouchListener(this);
        ibClearLoc.setOnTouchListener(this);

        etSearchAds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorFieldsBG));
                }
            }
        });

        etSearchAds.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lat = 0;
                    lng = 0;
                    etSearchAds.removeTextChangedListener(searchesTextWatcher);
                    if (searchesAdapter != null)
                        if (searchesAdapter.getSearchesListFiltered() != null && searchesAdapter.getSearchesListFiltered().size() != 0)
                            return false;
                    //etSearchAds.clearFocus();
                    rvSearches.setVisibility(View.GONE);
                    searchId = etSearchAds.getText().toString();
                    selectedFilter = 0;
                    resetLocationSearch();
                    if (featuredCategories != null && featuredCategories.size() != 0) {
                        for (FeaturedCategory category : featuredCategories)
                            category.setSelected(false);
                        featuredCategoriesListAdapter.notifyDataSetChanged();
                    }
                    resetCategorySearch();
                    if (rlFeatured.getVisibility() != View.GONE)
                        animateFeaturedView(false);
                    ApplicationUtils.hideKeyboard(mContext);
                    rvAds.setVisibility(View.GONE);
                    resetViewsCategoryView();
                    searchType = 2;
                    searchedText = "";
                    tvHeading.setText(mContext.getResources().getString(R.string.tv_search_results));
                    resetPagination();
                    onRefresh();


                    return true;
                }
                return false;
            }
        });

        //etSearchAds.clearFocus();
        searchesTextWatcher = new SearchesTextWatcher(etSearchAds);
        etSearchAds.addTextChangedListener(searchesTextWatcher);
        tvHeading.setOnTouchListener(this);
        toolbar.setOnTouchListener(this);
        rlTempSearch.setOnTouchListener(this);
        tvFeatured.setOnTouchListener(this);
    }

    SearchesTextWatcher searchesTextWatcher;

    private void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void getFeaturedCategories() {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getFeaturedCategory(new Callback<ResponseFeaturedCategories>() {
            @Override
            public void onResponse(Call<ResponseFeaturedCategories> call,
                                   final retrofit2.Response<ResponseFeaturedCategories> response) {

                try {
                    etSearchAds.setEnabled(true);
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getCategories() != null && response.body().getData().getCategories().size() != 0) {
                            rvFeaturedCategories.setVisibility(View.VISIBLE);
                            featuredCategories.clear();
                            featuredCategories.addAll(response.body().getData().getCategories());
                            featuredCategoriesListAdapter.notifyDataSetChanged();
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
                getHomeAds();

            }

            @Override
            public void onFailure(Call<ResponseFeaturedCategories> call, Throwable t) {
                try {
                    etSearchAds.setEnabled(true);
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

                getHomeAds();
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (view.getId()) {

            case R.id.tvHeading:
            case R.id.toolbar:
            case R.id.rlTempSearch:
            case R.id.tvFeatured:
                ApplicationUtils.hideKeyboard(mContext);
                break;

            case R.id.rlClearLoc:
            case R.id.ibClearLoc:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    rlClearLoc.setVisibility(View.GONE);
                    if (ApplicationUtils.isSet(etLocation.getText().toString())) {
                        lat = 0;
                        lng = 0;
                        resetLocationSearch();
                        onRefresh();

                    }
                }
                break;
            case R.id.rlClear:
            case R.id.ibClear:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);

                    haveText = false;
                    if (ApplicationUtils.isSet(etSearchAds.getText().toString())) {
                        lat = 0;
                        lng = 0;
                        rvSearches.setVisibility(View.GONE);
                        selectedFilter = 0;
                        resetLocationSearch();
                        etSearchAds.setText("");
                        searchedText = "";
                        searchId = "";
                        onRefresh();
                    }
                }
                break;

            case R.id.btn_toolbar_back:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mContext != null)
                        ApplicationUtils.hideKeyboard(mContext);
                    listener.callToolbarBack(true);
                }
                break;
            case R.id.tvMore:
            case R.id.ivMore:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    openAllCategoriesDialog();
                }
                break;
            case R.id.ibFilter:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    FilterPopup tipWindow = new FilterPopup(mContext, selectedFilter, new FilterPopup.onClickListener() {
                        @Override
                        public void onButtonClick(int filter) {
                            lat = 0;
                            lng = 0;

                            if (filter == 4) {
                                lat = 0;
                                lng = 0;

                                if (userDetail != null) {
                                    if (userDetail.getLatitude() != null && userDetail.getLongitude() != null) {
                                        if (userDetail.getLatitude() != 0 && userDetail.getLongitude() != 0) {
                                            lat = userDetail.getLatitude();
                                            lng = userDetail.getLongitude();
                                        } else if (ApplicationUtils.isEnableGPS(mContext)) {
                                            if (FusedLocationActivity.mCurrentLocation != null) {
                                                lat = FusedLocationActivity.mCurrentLocation.getLatitude();
                                                lng = FusedLocationActivity.mCurrentLocation.getLongitude();
                                            }
                                        }
                                    } else {
                                        getGPSLocation();
                                    }
                                } else {
                                    getGPSLocation();
                                }
                            }

                            selectedFilter = filter;
                            resetPagination();
                            if (searchType == 1) {
                                //   callGetFirstSearch();
                                onRefresh();
                            } else if (searchType == 2) {
                                // callGetFirstSearchText();
                                onRefresh();
                            }

                        }

                    });
                    tipWindow.showToolTip(view);
                }
                break;
            case R.id.ivAddAds:
            case R.id.tvAddAds:
            case R.id.ibAddAds:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    Intent postAd = new Intent(mContext, PostAdActivity.class);
                    startActivity(postAd);
                }
                break;
        }
        return true;
    }

    public void openAllCategoriesDialog() {

        AllCategoriesDialog allCategoriesDialog = new AllCategoriesDialog(selectedCategoryId, selectedSubCategoryId, new AllCategoriesDialog.onClickListener() {
            @Override
            public void onCategoryClick(int catId, String catName) {
                lat = 0;
                lng = 0;
                selectedFilter = 0;
                fromCategoryClick = true;
                etSearchAds.setText("");
                resetLocationSearch();
                if (featuredCategories != null && featuredCategories.size() != 0) {
                    for (FeaturedCategory category : featuredCategories)
                        category.setSelected(false);
                    featuredCategoriesListAdapter.notifyDataSetChanged();
                }
                if (rlFeatured.getVisibility() != View.GONE)
                    animateFeaturedView(false);
                resetViewsSearchView();
                searchType = 1;
                tvHeading.setText(mContext.getResources().getString(R.string.tv_search_results));
                resetPagination();
                selectedCategoryId = catId;
                selectedSubCategoryId = 0;
                selectedCategoryText = catName;
                selectedSubCategoryText = "";
                onRefresh();
            }

            public void onSubCategoryClick(int catId, int subCatId, String catName, String subCatName) {
                lat = 0;
                lng = 0;
                selectedFilter = 0;
                resetLocationSearch();
                fromCategoryClick = true;
                etSearchAds.setText("");
                if (featuredCategories != null && featuredCategories.size() != 0) {
                    for (FeaturedCategory category : featuredCategories)
                        category.setSelected(false);
                    featuredCategoriesListAdapter.notifyDataSetChanged();
                }
                if (rlFeatured.getVisibility() != View.GONE)
                    animateFeaturedView(false);
                resetViewsSearchView();
                searchType = 1;

                tvHeading.setText(mContext.getResources().getString(R.string.tv_search_results));
                resetPagination();
                selectedCategoryId = catId;
                selectedSubCategoryId = subCatId;

                selectedCategoryText = catName;
                selectedSubCategoryText = subCatName;
                onRefresh();
            }
        });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        allCategoriesDialog.show(ft, AllCategoriesDialog.TAG);
    }


    private void getHomeAds() {

        showProgressBar(true);
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getHomeAds(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call,
                                   final retrofit2.Response<HomeResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData() != null) {
                            featuredAndHighlightedAds.clear();
                            normalAds.clear();
                            filterAds(response.body().getData());
                        } else {

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
                getSearches();
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
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
                getSearches();
            }
        });
    }

    public void filterAds(HomeResponseData homeResponseDataAds) {


        if (homeResponseDataAds.getFeaAd() != null && homeResponseDataAds.getFeaAd().size() != 0) {
            for (Ads ads : homeResponseDataAds.getFeaAd()) {
                featuredAndHighlightedAds.add(ads);
            }
        }


        if (homeResponseDataAds.getHighAd() != null && homeResponseDataAds.getHighAd().size() != 0) {
            for (Ads ads : homeResponseDataAds.getHighAd()) {
                featuredAndHighlightedAds.add(ads);
            }
        }


        if (homeResponseDataAds.getAdsData() != null && homeResponseDataAds.getAdsData().size() != 0) {
            for (Ads ads : homeResponseDataAds.getAdsData()) {
                normalAds.add(ads);
            }
        }

        if (featuredAndHighlightedAds != null && featuredAndHighlightedAds.size() != 0) {
            tvNotFoundFeaturedAds.setVisibility(View.GONE);
            rvFeatured.setVisibility(View.VISIBLE);
            sbFeatured.setVisibility(View.VISIBLE);
            tvFeatured.setVisibility(View.VISIBLE);
            rlFeatured.setVisibility(View.VISIBLE);
            sbFeatured.setMax(featuredAndHighlightedAds.size() - 1);
            sbFeatured.setProgress(0);
            featureAndHighlightedAdsAdapter.notifyDataSetChanged();
        } else {
            tvNotFoundFeaturedAds.setVisibility(View.GONE);
            rvFeatured.setVisibility(View.GONE);
            sbFeatured.setVisibility(View.GONE);
            tvFeatured.setVisibility(View.GONE);
            rlFeatured.setVisibility(View.GONE);
            sbFeatured.setEnabled(false);
            sbFeatured.setMax(0);
            sbFeatured.setProgress(0);
        }
        if (normalAds != null && normalAds.size() != 0) {
            tvNotFoundAds.setVisibility(View.GONE);
            rvAds.setVisibility(View.VISIBLE);
            adsAdapter.notifyDataSetChanged();
        } else {
            tvNotFoundAds.setVisibility(View.VISIBLE);
            rvAds.setVisibility(View.GONE);
        }

    }


    public void resetViewsCategoryView() {
        searchAds.clear();
        tvNotFoundAds.setVisibility(View.GONE);
        tvNotFoundFeaturedAds.setVisibility(View.GONE);
        selectedFilter = 0;
        rvSearchAds.setVisibility(View.GONE);

    }

    public void resetCategorySearch() {

        selectedCategoryId = 0;
        selectedSubCategoryId = 0;
        selectedCategoryText = "";
        selectedSubCategoryText = "";
    }

    public void resetLocationSearch() {
        etLocation.setText("");
        locationLng = 0;
        locationLat = 0;
        etLocation.setText(getResources().getString(R.string.tv_location));
        rlClearLoc.setVisibility(View.GONE);
        searchedLocation = "";
    }

    public void resetViewsSearchView() {

        searchAds.clear();
        tvNotFoundAds.setVisibility(View.GONE);
        tvNotFoundFeaturedAds.setVisibility(View.GONE);

        if (featuredCategories != null && featuredCategories.size() != 0) {
            for (FeaturedCategory category : featuredCategories)
                category.setSelected(false);
            featureAndHighlightedAdsAdapter.notifyDataSetChanged();
        }

        searchId = "";
        searchedText = "";
        etSearchAds.setText("");
        rvSearchAds.setVisibility(View.GONE);

    }


    public void onRefresh() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }



        etSearchAds.setEnabled(false);

        if (!ApplicationUtils.isSet(etSearchAds.getText().toString()) &&
                !ApplicationUtils.isSet(searchedLocation)
                &&
                !ApplicationUtils.isSet(searchedText)
                &&
                !ApplicationUtils.isSet(selectedCategoryText) &&
                !ApplicationUtils.isSet(selectedSubCategoryText)) {


            if (featuredCategories != null && featuredCategories.size() != 0) {
                for (FeaturedCategory category : featuredCategories)
                    category.setSelected(false);
                featuredCategoriesListAdapter.notifyDataSetChanged();
            }
            if (rlFeatured.getVisibility() != View.VISIBLE)
                animateFeaturedView(true);
            etSearchAds.setText("");
            searchType = 0;
            lat = 0;
            lng = 0;
            ibFilter.setVisibility(View.GONE);
            tvNotFoundSearchAds.setVisibility(View.GONE);
            tvHeading.setText(mContext.getResources().getString(R.string.tv_dashboard_head));
            resetViewsCategoryView();
            resetViewsSearchView();
            rvSearchAds.setVisibility(View.GONE);
            rvFeatured.setVisibility(View.VISIBLE);
            rvAds.setVisibility(View.VISIBLE);
            searchType = 0;
            showProgressBar(true);
            getFeaturedCategories();
        } else if (searchType == 1) {
            callGetFirstSearch();
        } else if (searchType == 2) {
            callGetFirstSearchText();
        }

    }


    private AutocompleteActivityMode getMode() {
        return AutocompleteActivityMode.OVERLAY;
    }

    private List<Place.Field> getPlaceFields() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        return fields;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);


        if (resultCode == AutocompleteActivity.RESULT_OK) {
            // selectedFilter = 0;
            mLocationCall = true;
            ApplicationUtils.hideKeyboard(mContext);
            Place place = Autocomplete.getPlaceFromIntent(returnedIntent);
            if (place != null) {
                if (rlFeatured.getVisibility() != View.GONE)
                    animateFeaturedView(false);
                ApplicationUtils.hideKeyboard(mContext);
                etLocation.setText(place.getName());
                etLocation.setTag(place.getName());
                etLocation.setSelection(etLocation.getText().length());
                etLocation.setFocusableInTouchMode(false);
                etLocation.setFocusable(false);
                ApplicationUtils.hideKeyboard(mContext);
                resetPagination();
                rlClearLoc.setVisibility(View.VISIBLE);
                searchedLocation = etLocation.getText().toString();


                locationLat = place.getLatLng().latitude;
                locationLng = place.getLatLng().longitude;

                if (rvSearches.getVisibility() != View.VISIBLE) {
                    tvHeading.setText(mContext.getResources().getString(R.string.tv_search_results));
                    if (!ApplicationUtils.isSet(etSearchAds.getText().toString()) && (ApplicationUtils.isSet(selectedCategoryText) || ApplicationUtils.isSet(selectedSubCategoryText))) {

                        searchType = 1;
                    } else if (ApplicationUtils.isSet(etSearchAds.getText().toString()) && ApplicationUtils.isSet(etLocation.getText().toString())) {
                        searchType = 2;
                    } else if (ApplicationUtils.isSet(etSearchAds.getText().toString()) && !ApplicationUtils.isSet(etLocation.getText().toString())) {
                        searchType = 2;
                    } else if (!ApplicationUtils.isSet(etSearchAds.getText().toString()) && ApplicationUtils.isSet(etLocation.getText().toString())) {
                        selectedFilter = 0;
                        searchType = 2;
                    }
                    onRefresh();
                }

            } else {
                DialogFactory.showDropDownNotificationError(mContext,
                        mContext.getString(R.string.alert_information),
                        mContext.getString(R.string.alert_internet_connection));
            }

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            mLocationCall = true;
            // TODO: Handle the error.
            ApplicationUtils.hideKeyboard(mContext);
            etLocation.setFocusableInTouchMode(false);
            etLocation.setFocusable(false);
            Status status = Autocomplete.getStatusFromIntent(returnedIntent);

        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            mLocationCall = true;
            // The user canceled the operation.
            ApplicationUtils.hideKeyboard(mContext);
            etLocation.setFocusableInTouchMode(false);
            etLocation.setFocusable(false);

        }
    }

    boolean haveText = false;

    private class SearchesTextWatcher implements TextWatcher {

        private View view;

        private SearchesTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (ApplicationUtils.isSet(etSearchAds.getText().toString())) {
                setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
                rlClear.setVisibility(View.VISIBLE);
            } else {
                if (!etSearchAds.hasFocus()) {
                    setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorFieldsBG));
                    rlClear.setVisibility(View.GONE);
                } else {
                    setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
                    rlClear.setVisibility(View.VISIBLE);
                }
            }
        }

        public void afterTextChanged(Editable editable) {


            haveText = false;
            if (etSearchAds.hasFocus()) {
                if (!ApplicationUtils.isSet(editable.toString())) {
                    rvSearches.setVisibility(View.GONE);
                } else if (ApplicationUtils.isSet(editable.toString())) {
                    haveText = true;
                    searchesAdapter.getFilter().filter(editable.toString());
                }
            }


            searchTo++;
        }
    }


    public void callGetFirstSearch() {

        etSearchAds.setEnabled(false);
        currentPage = 1;
        if (!ApplicationUtils.isOnline(getActivity())) {
            DialogFactory.showDropDownNotificationError(getActivity(),
                    getActivity().getString(R.string.alert_information),
                    getActivity().getString(R.string.alert_internet_connection));
            return;
        }
        try {
            tvNotFoundAds.setVisibility(View.GONE);
            showProgressBar(true);
            getSearchDataFirst(selectedCategoryId, selectedSubCategoryId, currentPage, selectedFilter, lat, lng, locationLat, locationLng);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSearchDataFirst(int catId, int subCatId, int page, int filter, double lat,
                                    double lng, double searchLat, double searchLong) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getSearchByCategorySubCatId(String.valueOf(catId), String.valueOf(subCatId), selectedFilter, lat, lng, page, searchLat, searchLong, new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                try {
                    etSearchAds.setEnabled(true);
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {

                        if (response.body().getData() != null && response.body().getData().getData() != null) {
                            if (response.body().getData().getData().size() > 0) {
                                showSearchListFirst(response);
                            } else {
                                //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                rvSearchAds.setVisibility(View.GONE);
                                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                                rvAds.setVisibility(View.GONE);
                                showProgressBar(false);
                            }
                        } else {
                            rvSearchAds.setVisibility(View.GONE);
                            tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                            rvAds.setVisibility(View.GONE);
                            showProgressBar(false);
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty() && !call.isCanceled()) {
                            DialogFactory.showDropDownNotificationError(
                                    getActivity(),
                                    getActivity().getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                        rvSearchAds.setVisibility(View.GONE);
                        tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                rvAds.setVisibility(View.GONE);
                etSearchAds.setEnabled(true);
                rvSearchAds.setVisibility(View.GONE);
                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_request_timeout));
                else if (!call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    private void callSearchListNext() {

        if (currentPage < TOTAL_PAGES) {
            etSearchAds.setEnabled(false);
            currentPage = currentPage + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!ApplicationUtils.isOnline(getActivity())) {
                            DialogFactory.showDropDownNotificationError(getActivity(),
                                    getActivity().getString(R.string.alert_information),
                                    getActivity().getString(R.string.alert_internet_connection));
                            return;
                        }

                        tvNotFoundSearchAds.setVisibility(View.GONE);
                        getEventsDataNext(selectedCategoryId, selectedSubCategoryId, currentPage, selectedFilter, lat, lng, locationLat, locationLng);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            rvSearchAds.post(new Runnable() {
                public void run() {
                    // There is no need to use notifyDataSetChanged()
                    isLoading = false;
                    if (mSearchAdapter != null)
                        mSearchAdapter.removeLoadingFooter();
                    isLastPage = true;
                }
            });
        }
    }

    private void getEventsDataNext(int catId, int subId, int page, int filter, double lat,
                                   double lng, double searchLat, double searchLong) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getSearchByCategorySubCatId(String.valueOf(catId), String.valueOf(subId), filter, lat, lng, page, searchLat, searchLong, new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                try {
                    etSearchAds.setEnabled(true);
                    rvSearches.setVisibility(View.GONE);
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getData() != null) {
                            if (response.body().getData().getData().size() > 0) {
                                showSearchListNext(response);
                            } else {
                                mSearchAdapter.removeLoadingFooter();
                                //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                rvSearchAds.setVisibility(View.GONE);
                                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            rvAds.setVisibility(View.GONE);
                            rvSearchAds.setVisibility(View.GONE);
                            tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                            showProgressBar(false);
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty() && !call.isCanceled()) {
                            DialogFactory.showDropDownNotificationError(
                                    getActivity(),
                                    getActivity().getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                        rvAds.setVisibility(View.GONE);
                        rvSearchAds.setVisibility(View.GONE);
                        tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                rvAds.setVisibility(View.GONE);
                rvSearches.setVisibility(View.GONE);
                rvSearchAds.setVisibility(View.GONE);
                etSearchAds.setEnabled(true);
                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_request_timeout));
                else if (!call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }


    /**********************************************************************/


    public void callGetFirstSearchText() {

        etSearchAds.setEnabled(false);
        currentPage = 1;
        if (!ApplicationUtils.isOnline(getActivity())) {
            DialogFactory.showDropDownNotificationError(getActivity(),
                    getActivity().getString(R.string.alert_information),
                    getActivity().getString(R.string.alert_internet_connection));
            return;
        }
        try {
            tvNotFoundSearchAds.setVisibility(View.GONE);
            showProgressBar(true);
            getSearchDataFirstText(searchedText, searchedLocation, selectedFilter, currentPage, lat, lng, locationLat, locationLng, searchId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSearchDataFirstText(String searchedText, String searchedLocation,
                                        int filter, int page, double lat, double lng, double searchLat, double searchLng, String searchID) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getSearchByText(searchedText, searchedLocation, filter, lat, lng, page, searchLat, searchLng, searchID, new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                try {
                    etSearchAds.setEnabled(true);
                    etSearchAds.addTextChangedListener(searchesTextWatcher);
                    rvSearches.setVisibility(View.GONE);
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {

                        if (response.body().getData() != null && response.body().getData().getData() != null) {
                            if (response.body().getData().getData().size() > 0) {
                                showSearchListFirst(response);
                            } else {
                                rvAds.setVisibility(View.GONE);
                                //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                rvSearchAds.setVisibility(View.GONE);
                                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            rvAds.setVisibility(View.GONE);
                            rvSearchAds.setVisibility(View.GONE);
                            tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                            showProgressBar(false);
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty() && !call.isCanceled()) {
                            DialogFactory.showDropDownNotificationError(
                                    getActivity(),
                                    getActivity().getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                        rvAds.setVisibility(View.GONE);
                        rvSearchAds.setVisibility(View.GONE);
                        tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                etSearchAds.setEnabled(true);
                rvAds.setVisibility(View.GONE);
                rvSearchAds.setVisibility(View.GONE);
                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                etSearchAds.addTextChangedListener(searchesTextWatcher);
                rvSearches.setVisibility(View.GONE);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_request_timeout));
                else if (!call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    private void callSearchListNextText() {

        if (currentPage < TOTAL_PAGES) {
            etSearchAds.setEnabled(false);
            currentPage = currentPage + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!ApplicationUtils.isOnline(getActivity())) {
                            DialogFactory.showDropDownNotificationError(getActivity(),
                                    getActivity().getString(R.string.alert_information),
                                    getActivity().getString(R.string.alert_internet_connection));
                            return;
                        }

                        tvNotFoundSearchAds.setVisibility(View.GONE);
                        getEventsDataNextText(searchedText, searchedLocation, selectedFilter, currentPage, lat, lng, locationLat, locationLng, searchId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            rvSearchAds.post(new Runnable() {
                public void run() {
                    // There is no need to use notifyDataSetChanged()
                    isLoading = false;
                    if (mSearchAdapter != null)
                        mSearchAdapter.removeLoadingFooter();
                    isLastPage = true;
                }
            });
        }
    }

    private void getEventsDataNextText(String searchedText, String searchedLocation, int filter,
                                       int page, double lat, double lng, double searchLat, double searchLng, String searchId) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getSearchByText(searchedText, searchedLocation, filter, lat, lng, page, searchLat, searchLng, searchId, new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                try {
                    etSearchAds.setEnabled(true);
                    etSearchAds.addTextChangedListener(searchesTextWatcher);
                    rvSearches.setVisibility(View.GONE);
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getData() != null) {
                            if (response.body().getData().getData().size() > 0) {
                                showSearchListNext(response);
                            } else {
                                mSearchAdapter.removeLoadingFooter();
                                //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                rvSearchAds.setVisibility(View.GONE);
                                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                                showProgressBar(false);
                            }
                        } else {
                            rvAds.setVisibility(View.GONE);
                            rvSearchAds.setVisibility(View.GONE);
                            tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                            showProgressBar(false);
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty() && !call.isCanceled()) {
                            DialogFactory.showDropDownNotificationError(
                                    getActivity(),
                                    getActivity().getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                        rvAds.setVisibility(View.GONE);
                        rvSearchAds.setVisibility(View.GONE);
                        tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                etSearchAds.setEnabled(true);
                etSearchAds.addTextChangedListener(searchesTextWatcher);
                rvAds.setVisibility(View.GONE);
                rvSearchAds.setVisibility(View.GONE);
                tvNotFoundSearchAds.setVisibility(View.VISIBLE);
                rvSearches.setVisibility(View.GONE);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException && !call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            getActivity().getString(R.string.alert_request_timeout));
                else if (!call.isCanceled())
                    DialogFactory.showDropDownNotificationError(
                            getActivity(),
                            getActivity().getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }


    /*********************************************************************/

    public void showSearchListFirst(Response<SearchResponse> response) {

        ibFilter.setVisibility(View.VISIBLE);
        //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tvNotFoundSearchAds.setVisibility(View.GONE);
        rvSearchAds.setVisibility(View.VISIBLE);

        rvAds.setVisibility(View.GONE);

        currentPage = response.body().getData().getCurrentPage();
        TOTAL_PAGES = response.body().getData().getLastPage();
        isLoading = false;
        searchAds.clear();
        searchAds.addAll(response.body().getData().getData());

        mSearchAdapter = new SearchAdsAdapter(getActivity(), searchAds, new SearchAdsAdapter.onRowClickListener() {
            @Override
            public void onItemClick(int position) {
                toDetails = true;
                Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                detailIntent.putExtra("ad_id", searchAds.get(position).getId());
                startActivity(detailIntent);
            }

        });
        rvSearchAds.setAdapter(mSearchAdapter);
/*        rvSearchAds.addOnScrollListener(new PaginationScrollListener(mLayoutManagerSearchAds) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                if (searchType == 1)
                    callSearchListNext();
                else
                    callSearchListNextText();
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
        });*/
        if (currentPage < TOTAL_PAGES) {
            mSearchAdapter.addLoadingFooter();
        } else {
            isLastPage = true;
        }


    }

    public void showSearchListNext(Response<SearchResponse> response) {

        ibFilter.setVisibility(View.VISIBLE);
        //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tvNotFoundSearchAds.setVisibility(View.GONE);
        rvSearchAds.setVisibility(View.VISIBLE);
        rvAds.setVisibility(View.GONE);

        currentPage = response.body().getData().getCurrentPage();
        TOTAL_PAGES = response.body().getData().getLastPage();

        if (mSearchAdapter != null)
            mSearchAdapter.removeLoadingFooter();
        isLoading = false;
        searchAds.addAll(response.body().getData().getData());
        mSearchAdapter.notifyDataSetChanged();
  /*      rvSearchAds.addOnScrollListener(new PaginationScrollListener(mLayoutManagerSearchAds) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                if (searchType == 1)
                    callSearchListNext();
                else
                    callSearchListNextText();
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
        });*/
        if (currentPage < TOTAL_PAGES) {
            mSearchAdapter.addLoadingFooter();
        } else {
            isLastPage = true;
            /* mSwipeRefreshLayout.setRefreshing(false);*/
        }


    }

    public void resetPagination() {
        currentPage = 0;
        TOTAL_PAGES = 0;
        isLastPage = false;
        isLoading = false;
    }

    public void animateFeaturedView(boolean toShow) {
        if (toShow) {
            ApplicationUtils.expand(rlFeatured);
        } else if (!toShow) {
            ApplicationUtils.collapse(rlFeatured);
        }
    }

    public static void setEditTextCursorColor(EditText editText, int color) {
        try {
            // Get the cursor resource id
            if (Build.VERSION.SDK_INT >= 28) {//set differently in Android P (API 28)
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                int drawableResId = field.getInt(editText);

                // Get the editor
                field = TextView.class.getDeclaredField("mEditor");
                field.setAccessible(true);
                Object editor = field.get(editText);

                // Get the drawable and set a color filter
                Drawable drawable = ContextCompat.getDrawable(editText.getContext(), drawableResId);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);

                // Set the drawables
                field = editor.getClass().getDeclaredField("mDrawableForCursor");
                field.setAccessible(true);
                field.set(editor, drawable);
            } else {
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                int drawableResId = field.getInt(editText);

                // Get the editor
                field = TextView.class.getDeclaredField("mEditor");
                field.setAccessible(true);
                Object editor = field.get(editText);

                // Get the drawable and set a color filter
                Drawable drawable = ContextCompat.getDrawable(editText.getContext(), drawableResId);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                Drawable[] drawables = {drawable, drawable};

                // Set the drawables
                field = editor.getClass().getDeclaredField("mCursorDrawable");
                field.setAccessible(true);
                field.set(editor, drawables);
            }

        } catch (Exception ignored) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        URLogs.m("onResume()");


        if (!mLocationCall) {

  /*          selectedCategoryText = "";
            selectedSubCategoryText = "";
            selectedCategoryId = 0;
            selectedSubCategoryId = 0;
            resetLocationSearch();
            resetViewsCategoryView();
            resetViewsSearchView();
            resetPagination();
            resetCategorySearch();
            searchType = 0;
            searchedText = "";
            searchedLocation = "";
            rvSearchAds.setVisibility(View.GONE);
            rvAds.setVisibility(View.VISIBLE);
            rvFeatured.setVisibility(View.VISIBLE);*/

            getUserDetails();
            if (etSearchAds != null)
                onRefresh();

            if (etSearchAds != null) {
                if (etSearchAds.hasFocus())
                    setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
                else
                    setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorFieldsBG));
            }
        }

        mLocationCall = false;
        toDetails = false;

    }

    @Override
    public void onPause() {
        super.onPause();



        if (!mLocationCall && !toDetails) {
            resetUI();
        } else if (toDetails) {
            resetLocationSearch();
        }

    }


    public void resetUI() {
        rvSearches.setVisibility(View.GONE);
        selectedCategoryText = "";
        selectedSubCategoryText = "";
        selectedCategoryId = 0;
        selectedSubCategoryId = 0;
        resetLocationSearch();
        resetViewsCategoryView();
        resetViewsSearchView();
        resetPagination();
        resetCategorySearch();
        searchType = 0;
        searchedText = "";
        searchId = "";
        searchedLocation = "";
        rvSearchAds.setVisibility(View.GONE);
        rvAds.setVisibility(View.VISIBLE);
        rvFeatured.setVisibility(View.VISIBLE);
    }

    private void getUserDetails() {

        String token = SharedPreference.getSharedPrefValue(mContext, Constants.USER_TOKEN);
        token = "Bearer " + token;
        ApiClient apiClient = ApiClient.getInstance();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        Call<BusinessProfileResponse> call = api.getUserBusinesProfile(token);

        call.enqueue(new Callback<BusinessProfileResponse>() {
            @Override
            public void onResponse(Call<BusinessProfileResponse> call,
                                   final Response<BusinessProfileResponse> response) {

                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getData() != null) {

                            isBusinessAccount = false;
                            isBusinessAccountPaid = false;
                            BusinessDetails user = response.body().getData().getData();
                            if (user != null) {
                                isBusinessAccount = true;
                                if (user.getPaymentStatus().equals("paid")) {
                                    isBusinessAccountPaid = true;
                                }
                            } else {
                                isBusinessAccount = false;
                            }
                            hidePostAds();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {

            }
        });
    }

    public void hidePostAds() {

        if (!isBusinessAccountPaid) {
            ibAddAds.setVisibility(View.GONE);
            tvAddAds.setVisibility(View.GONE);
            ivAddAds.setVisibility(View.GONE);
        } else {
            ibAddAds.setVisibility(View.GONE);
            tvAddAds.setVisibility(View.VISIBLE);
            ivAddAds.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        // do things when keyboard is shown
  /*      if (etSearchAds != null) {
            if (ApplicationUtils.isSet(etSearchAds.getText().toString())) {
                setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
            } else {
                if(etSearchAds.hasFocus())   setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
                else
                setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorFieldsBG));
            }
        }*/
    }

    @Override
    protected void onHideKeyboard() {
        // do things when keyboard is hidden
    /*    if (etSearchAds != null) {
            if (ApplicationUtils.isSet(etSearchAds.getText().toString())) {
                setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorPrimaryDark));
            } else {
                setEditTextCursorColor(etSearchAds, getResources().getColor(R.color.colorFieldsBG));
            }
        }*/
    }

    int searchTo = 0;

    private void getSearches() {
        etSearchAds.setEnabled(false);
        showProgressBar(true);
        String token = SharedPreference.getSharedPrefValue(mContext, Constants.USER_TOKEN);
        token = "Bearer " + token;
        ApiClient apiClient = ApiClient.getInstance();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        Call<SearchesResponse> call = api.getSearches(token);

        call.enqueue(new Callback<SearchesResponse>() {
            @Override
            public void onResponse(Call<SearchesResponse> call,
                                   final Response<SearchesResponse> response) {
                showProgressBar(false);

                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getData() != null) {
                            if (response.body().getData().size() != 0) {
                                searchesList.clear();
                                finalSearches.clear();
                                searchesList.addAll(response.body().getData());
                                arrangeSearch();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchesResponse> call, Throwable t) {
                showProgressBar(false);
            }
        });
    }

    public void arrangeSearch() {

        if (searchesResponseListHeadings != null && searchesResponseListHeadings.size() != 0) {
            searchesResponseListHeadings.clear();
        } else {
            searchesResponseListHeadings = new ArrayList<>();
        }
        finalSearches.clear();
        if (searchesAdapter != null) {
            rvSearches.getRecycledViewPool().clear();
            searchesAdapter.setDataChanged();
        }
        String lastHeading = "";

        if (searchesList != null && searchesList.size() != 0) {

            for (int i = 0; i < searchesList.size(); i++) {
                SearchesResponseData responseData = searchesList.get(i);
                if (!ApplicationUtils.isSet(lastHeading)) {
                    searchesResponseListHeadings.add(responseData.getCategory());
                } else if (!lastHeading.equals(responseData.getCategory())) {
                    searchesResponseListHeadings.add(responseData.getCategory());
                }
                lastHeading = responseData.getCategory();
            }

            if (searchesResponseListHeadings != null && searchesResponseListHeadings.size() != 0) {

                for (String sHeading : searchesResponseListHeadings) {

                    SearchesResponseData responseDataHeading = new SearchesResponseData();
                    responseDataHeading.setValue(sHeading);
                    responseDataHeading.setValueDe(sHeading);
                    responseDataHeading.setValueSv(sHeading);
                    responseDataHeading.setId(-1);
                    responseDataHeading.setCategory(sHeading);
                    responseDataHeading.setKey(sHeading);
                    finalSearches.add(responseDataHeading);


                    for (int i = 0; i < searchesList.size(); i++) {
                        SearchesResponseData responseData = searchesList.get(i);
                        if (responseData.getCategory().equals(sHeading)) {
                            finalSearches.add(responseData.clone());
                        }
                    }
                }

                setSearchesAdapter();

            }

        }
    }

    public void setSearchesAdapter() {

        searchesAdapter = new SearchesAdapter(mContext, searchesResponseListHeadings, finalSearches, new SearchesAdapter.SearchesAdapterListener() {
            @Override
            public void onSearchesResponseDataSelected(SearchesResponseData contact) {

                if (contact.getId() == -1)
                    return;

                etSearchAds.removeTextChangedListener(searchesTextWatcher);
                if (SharedPreference.getAppLanguage(mContext).equalsIgnoreCase("sv")) {
                    etSearchAds.setText(contact.getValueSv());
                } else if (SharedPreference.getAppLanguage(mContext).equalsIgnoreCase("de")) {
                    etSearchAds.setText(contact.getValueDe());
                } else {
                    etSearchAds.setText(contact.getValue());
                }

                rvSearches.setVisibility(View.GONE);
                //etSearchAds.clearFocus();

                searchTo = 1;
                rvSearches.setVisibility(View.GONE);
                selectedFilter = 0;
                resetLocationSearch();
                if (featuredCategories != null && featuredCategories.size() != 0) {
                    for (FeaturedCategory category : featuredCategories)
                        category.setSelected(false);
                    featuredCategoriesListAdapter.notifyDataSetChanged();
                }
                resetCategorySearch();
                if (rlFeatured.getVisibility() != View.GONE)
                    animateFeaturedView(false);
                ApplicationUtils.hideKeyboard(mContext);
                rvAds.setVisibility(View.GONE);
                resetViewsCategoryView();
                searchType = 2;
                searchedText = contact.getCategory();
                searchId = String.valueOf(contact.getId());
                tvHeading.setText(mContext.getResources().getString(R.string.tv_search_results));
                resetPagination();
                onRefresh();

            }

            @Override
            public void show(boolean toShow) {

                if (toShow) {
                    rvSearches.setVisibility(View.VISIBLE);
                } else {
                    rvSearches.setVisibility(View.GONE);
                }

            }
        });
        rvSearches.setAdapter(searchesAdapter);
        etSearchAds.setEnabled(true);
    }

    public void getGPSLocation() {
        if (ApplicationUtils.isEnableGPS(mContext)) {
            if (FusedLocationActivity.mCurrentLocation != null) {
                lat = FusedLocationActivity.mCurrentLocation.getLatitude();
                lng = FusedLocationActivity.mCurrentLocation.getLongitude();
            }
        }
    }


    class UIAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... arg0) {
            try {
                URLogs.m2("doInBackground");
                Thread.sleep(1500);
            } catch (Exception e) {
                e.getMessage();
            }
            return "";
        }

        protected void onPostExecute(String latLngAddress) {
            super.onPostExecute(latLngAddress);
            URLogs.m2("onPostExecute");

        }
    }
}
