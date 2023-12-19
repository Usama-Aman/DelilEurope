package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.DetailAdsActivity;
import com.dalileuropeapps.dalileurope.activities.PostAdActivity;
import com.dalileuropeapps.dalileurope.adapter.AddListAdaptor;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAdsData;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.AddListingResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.GenResponse;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.pagination_pack.PaginationListener;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.LinearLayout.VERTICAL;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.dalileuropeapps.dalileurope.pagination_pack.PaginationListener.PAGE_START;
import static com.dalileuropeapps.dalileurope.utils.ApplicationUtils.showToast;


public class AddListingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AddListAdaptor.RecItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Activity mActivityContext;
    ProgressBar progressBar;
    View view;
    Context mContext;
    // tollbar
    private ImageView btnToolbarBack;
    private TextView tvToolbarTitle;
    RelativeLayout toolbar;
    View iToolbar;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    // pagination
    private AddListAdaptor adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    Boolean isRefreshing = false;
    private ActivityListener listener;
    public static boolean toRefreshAddListFragment = false;
    public static boolean isFromAddListFragment = false;
    TextView tvNoAds;

    public AddListingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddListingFragment newInstance(String param1, String param2) {
        AddListingFragment fragment = new AddListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        mActivityContext = getActivity();
        mContext = view.getContext();
        try {
            listener = (ActivityListener) mContext;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
        progressBar = view.findViewById(R.id.progressBar);
        transparentStatusBar();
        setStatusBar();
        initToolBar();
        initAndSetViews();


        return view;
    }


    public void setStatusBar() {
        if (ApplicationUtils.getDeviceStatusBarHeight(getActivity()) > 0) {
            transparentStatusBar();
        }
    }

    public void initToolBar() {


        iToolbar = (View) view.findViewById(R.id.iToolbar);
        toolbar = (RelativeLayout) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) toolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);


        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.menu_my_ads));


        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivityContext != null)
                    ApplicationUtils.hideKeyboard(mActivityContext);
                listener.callToolbarBack(true);
            }
        });


    }

    public void initAndSetViews() {

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        tvNoAds = (TextView) view.findViewById(R.id.tvNoAds);

        LinearLayoutManager linearLayoutManager;
        if (mColumnCount <= 1) {
            linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            linearLayoutManager = new GridLayoutManager(mContext, mColumnCount);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        getAddLists();

        //pagination
        recyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                getAddLists();

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


        adapter = new AddListAdaptor(addsList, getActivity(), this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onRefresh() {
//        showProgressBar(true);
        itemCount = 0;
        isRefreshing = true;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        getAddLists();
    }

    private void showProgressBar(final boolean progressVisible) {
        mActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    List<ResponseDetailAdsData> addsList;

    void getAddLists() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        addsList = new ArrayList<>();

        if (currentPage == 1) {
            if (!isRefreshing) {
                showProgressBar(true);
            }
        }
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<AddListingResponse> call = api.getAddList(token, currentPage);

        call.enqueue(new Callback<AddListingResponse>() {
            @Override
            public void onResponse(Call<AddListingResponse> call, Response<AddListingResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null && response.body().getData().getAddResponse() != null) {
                                addsList = response.body().getData().getAddResponse().getData();

                                //pagination
                                totalPage = response.body().getData().getAddResponse().getLastPage();
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                adapter.addItems(addsList);
                                itemCount = adapter.getItemCount();
                                swipeRefresh.setRefreshing(false);
                                tvNoAds.setVisibility(View.GONE);
                                // check weather is last page or not
                                if (currentPage < totalPage) {
                                    adapter.addLoading();
                                } else {
                                    isLastPage = true;
                                }
                                isLoading = false;


                                isRefreshing = false;
                                swipeRefresh.setRefreshing(false);

                                if (addsList == null) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else if (addsList != null && addsList.size() == 0) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                            } else {
                                if (addsList == null) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else if (addsList != null && addsList.size() == 0) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            }

                        } else {
                            showToast(getActivity(), response.body().getMessage() + "", false);
                            if (addsList == null) {
                                tvNoAds.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else if (addsList != null && addsList.size() == 0) {
                                tvNoAds.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                    } else {
                        if (addsList == null) {
                            tvNoAds.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else if (addsList != null && addsList.size() == 0) {
                            tvNoAds.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        showToast(getActivity(), jsonObject.getString("message") + "", false);
                    }

                } catch (Exception e) {
                    showProgressBar(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddListingResponse> call, Throwable t) {
                if (addsList == null) {
                    tvNoAds.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else if (addsList != null && addsList.size() == 0) {
                    tvNoAds.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    void dellAdd(String id, final int position) {


        showProgressBar(true);
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<GenResponse> call = api.delAdd(token, id);
        call.enqueue(new Callback<GenResponse>() {
            @Override
            public void onResponse(Call<GenResponse> call, Response<GenResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            ApplicationUtils.showToast(getActivity(), response.body().getMessage() + "", true);
                            adapter.removeOneItem(position);

                            if (adapter.getItemCount() == 0) {
                                tvNoAds.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            ApplicationUtils.showToast(getActivity(), response.body().getMessage() + "", false);

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        ApplicationUtils.showToast(getActivity(), jsonObject.getString("message") + "", false);
                    }

                } catch (Exception e) {
                    showProgressBar(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GenResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public void viewItemClick(int position) {
        Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
        detailIntent.putExtra("ad_id", adapter.mPostItems.get(position).getId());
        startActivity(detailIntent);
    }

    @Override
    public void editItemClick(int position) {

        if (adapter.mPostItems.get(position).getIsActive() == 1) {
            isFromAddListFragment = true;
            Intent updateIntent = new Intent(mContext, PostAdActivity.class);
            updateIntent.putExtra("ad_detail", (new Gson()).toJson(adapter.mPostItems.get(position)));
            startActivity(updateIntent);
            return;
        } else if (adapter.mPostItems.get(position).getIsActive() == 0) {

            if (adapter.mPostItems.get(position).getIsFeatured() != 1) {
                if (!ApplicationUtils.isCurrentDateAfterDate(adapter.mPostItems.get(position).getEndDate()) && !ApplicationUtils.isSet(adapter.mPostItems.get(position).getPaymentStartDate())) {
                    isFromAddListFragment = true;
                    Intent updateIntent = new Intent(mContext, PostAdActivity.class);
                    updateIntent.putExtra("ad_detail", (new Gson()).toJson(adapter.mPostItems.get(position)));
                    startActivity(updateIntent);
                    return;

                }
            }
        }

        DialogFactory.showDropDownNotificationError(mActivityContext,
                mContext.getString(R.string.alert_information),
                mContext.getString(R.string.msg_error_ad_expire));
    }

    @Override
    public void delItemClick(int position) {
        dellAdd(adapter.mPostItems.get(position).getId().toString(), position);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (toRefreshAddListFragment)
            onRefresh();
        toRefreshAddListFragment = false;
        isFromAddListFragment = false;

    }

}
