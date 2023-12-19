package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.message.MessageListResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.pagination_pack.MessagesThreadPAginationAdaptor;
import com.dalileuropeapps.dalileurope.pagination_pack.PaginationListener;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

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

//import com.examples.dalileurope.Utils.Common;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MessagesListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    // pagination
    private MessagesThreadPAginationAdaptor adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    RelativeLayout toolbar;
    private ActivityListener listener;


    Context mContext;
    EditText etSearch;
    RecyclerView recyclerView;
    View view;
    boolean isEditSearch = false;
    ProgressBar progressBar;
    Activity mActivityContext;
    TextView tvNoAds;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessagesListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessagesListFragment newInstance(int columnCount) {
        MessagesListFragment fragment = new MessagesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        isFragmentActive = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messageslist, container, false);

        // Set the adapter
        mContext = view.getContext();
        mActivityContext = getActivity();
        try {
            listener = (ActivityListener) mContext;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }

        isFragmentActive = true;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("threadupdates"));
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

        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);

        btnToolbarRight = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_right);
        btnToolbarRight.setVisibility(View.VISIBLE);
        btnToolbarRight.setImageResource(R.drawable.ic_search_small);

        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.title_messages));

        etSearch = (EditText) iToolbar.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(textChangedListener);
        etSearch.setVisibility(View.GONE);

        btnToolbarBack.setOnClickListener(this);
        btnToolbarRight.setOnClickListener(this);

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


        getMessagesList();

        //pagination
        recyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                getMessagesList();

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


        adapter = new MessagesThreadPAginationAdaptor(userMessage, getActivity(), mListener);
        recyclerView.setAdapter(adapter);

    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        if (mContext instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    SwipeRefreshLayout swipeRefresh;
    Boolean isRefreshing = false;


    @Override
    public void onRefresh() {
        itemCount = 0;
        isRefreshing = true;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        getMessagesList();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(UserMessages item);
    }


    List<UserMessages> userMessage = new ArrayList<>();
    List<UserMessages> filteredMessages;


    void getMessagesList() {
        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }

        userMessage = new ArrayList<>();
        filteredMessages = new ArrayList<>();
        if (currentPage == 1) {
            if (!isRefreshing) {
                showProgressBar(true);
            }
        }
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<MessageListResponse> call = api.getMessagesThread(token, currentPage);

        call.enqueue(new Callback<MessageListResponse>() {
            @Override
            public void onResponse(Call<MessageListResponse> call, Response<MessageListResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getUserMessageThreadData().getData() != null && response.body().getUserMessageThreadData().getData() != null) {
                                userMessage = response.body().getUserMessageThreadData().getData();

                                //pagination

                                totalPage = response.body().getUserMessageThreadData().getLastPage();
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                adapter.addItems(userMessage);
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

                                if (userMessage == null) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else if (userMessage != null && userMessage.size() == 0) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                            } else {
                                if (userMessage == null) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else if (userMessage != null && userMessage.size() == 0) {
                                    tvNoAds.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                            }

                        } else {
                            showToast(getActivity(), response.body().getMessage() + "", false);
                            if (userMessage == null) {
                                tvNoAds.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else if (userMessage != null && userMessage.size() == 0) {
                                tvNoAds.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                    } else {
                        if (userMessage == null) {
                            tvNoAds.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else if (userMessage != null && userMessage.size() == 0) {
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
            public void onFailure(Call<MessageListResponse> call, Throwable t) {
                if (userMessage == null) {
                    tvNoAds.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else if (userMessage != null && userMessage.size() == 0) {
                    tvNoAds.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            adapter.getFilter().filter(s.toString());
        }
    };


    // fragment active or not
    public static Boolean isFragmentActive = false;

    @Override
    public void onStart() {
        super.onStart();
        isFragmentActive = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentActive = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentActive = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isFragmentActive = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentActive = false;
    }


//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context mContext, Intent intent) {
//            // Get extra data included in the Intent
//            String status = intent.getStringExtra("Status");
//            String message = intent.getStringExtra("thread_obj");
//            if (message != null) {
//                UserMessages userMessages = ApplicationUtils.fromJsonNotificationData(getActivity(), message);
//                if (userMessages != null) {
//                    adapter.addCounterOrItem(userMessages);
//
//                }
//            }
//        }
//    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String status = intent.getStringExtra("Status");
            String message = intent.getStringExtra("thread_obj");
            if (message != null) {
                UserMessages userMessages = ApplicationUtils.fromJsonNotificationData(getActivity(), message);
                if (userMessages != null) {
                    userMessages.getTo_user_details().setId(userMessages.getFrom_user_details().getId());
                    userMessages.getTo_user_details().setFirstName(userMessages.getFrom_user_details().getFirstName());
                    adapter.addCounterOrItem(userMessages);

                }
            }
//            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_toolbar_back:

                if (!isEditSearch) {
                    if (mActivityContext != null)
                        ApplicationUtils.hideKeyboard(mActivityContext);
                    listener.callToolbarBack(true);
                } else {
                    ApplicationUtils.hideKeyboard(((Activity) mContext));
                    isEditSearch = false;
                    etSearch.setVisibility(View.GONE);
                    tvToolbarTitle.setVisibility(View.VISIBLE);
                    etSearch.setText("");
                    btnToolbarRight.setImageResource(R.drawable.ic_search_small);
                    btnToolbarBack.setImageResource(R.drawable.ic_menu);
                    ApplicationUtils.hideKeyboardOnButtonClick(getActivity(), etSearch);
                }

                break;
            case R.id.btn_toolbar_right:

                if (!isEditSearch) {
                    isEditSearch = true;
                    tvToolbarTitle.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    btnToolbarBack.setImageResource(R.drawable.ic_back_arrow_small);
                    btnToolbarRight.setImageResource(R.drawable.ic_cross_small);
                    etSearch.setFocusable(true);
                    etSearch.setPressed(true);
                    etSearch.setTextIsSelectable(true);
                    etSearch.setCursorVisible(true);
                    showSoftKeyboard(etSearch);
                } else {
                    ApplicationUtils.hideKeyboard(((Activity) mContext));
                    btnToolbarBack.setImageResource(R.drawable.ic_menu);
                    btnToolbarRight.setImageResource(R.drawable.ic_search_small);
                    isEditSearch = false;
                    etSearch.setText("");
                    etSearch.setVisibility(View.GONE);
                    tvToolbarTitle.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private void showProgressBar(final boolean progressVisible) {
        mActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

}
