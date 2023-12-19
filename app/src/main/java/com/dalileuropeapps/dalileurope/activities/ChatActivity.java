package com.dalileuropeapps.dalileurope.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.message.GenResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.MessagesChatResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.SendMessageResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.pagination_pack.ChatRecPaginationAdaptor;
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

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.dalileuropeapps.dalileurope.pagination_pack.PaginationListener.PAGE_START;

public class ChatActivity extends BaseActivity implements ChatRecPaginationAdaptor.RecItemClick, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    private Context applicationContext;
    AppCompatActivity mContext;
    RecyclerView recyclerView;
    EditText etMessage;
    ImageView imgSend;
    String to_user_id = "";
    int thread_id;
    SwipeRefreshLayout swipeRefresh;
    Boolean isRefreshing = false;
    // pagination
    int pageNo = 0;
    private ChatRecPaginationAdaptor adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    // is application running
    public static Boolean isActivityOn = false;

    List<UserMessages> userMessage = new ArrayList<>();
    List<UserMessages> filteredMessages;

    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    Toolbar toolbar;

    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        isActivityOn = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityOn = true;
        //  onRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityOn = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityOn = false;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActivityOn = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityOn = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isActivityOn = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        statusBarTextColorWhite();
//        removeStatusBar();
        initToolBar();
        initAndSetView();

     /*   try {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception exc) {
            Log.d("Notification_Cancel_Exc", exc.getMessage());
        }*/
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
        tvToolbarTitle.setText("");

        btnToolbarBack.setOnClickListener(this);
        btnToolbarRight.setOnClickListener(this);

    }

    public void initAndSetView() {
        mContext = this;
        progressBar = findViewById(R.id.progressBar);
        applicationContext = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        etMessage = (EditText) findViewById(R.id.etMessage);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);


        etMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (etMessage.getText().toString().replace(" ", "").length() > 0 && etMessage.getText().toString().replace("\n", "").length() > 0) {
//                    if (thread_id != 0 && to_user_id != null) {
//                        // ApplicationUtils.hideKeyboard(mContext);
//                        sendMessage();
//                    }
//                } else {
//                    ApplicationUtils.showToast(ChatActivity.this, "Please Write Something then send" + "", false);
//                }
                String msg = etMessage.getText().toString().replace(" ", "");
                msg = msg.replace("\n", "");
                if (msg.length() > 0) {
                    if (thread_id != 0 && to_user_id != null) {
                        // ApplicationUtils.hideKeyboard(mContext);
                        sendMessage();
                    }
                }
//                else {
//                    ApplicationUtils.showToast(ChatActivity.this, "Please Write Something then send" + "", false);
//                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider));


        Intent intent = getIntent();
        thread_id = intent.getIntExtra("thread_id", 0);
        String name = intent.getStringExtra("name");
        to_user_id = intent.getStringExtra("to_user_id");
        tvToolbarTitle.setText(name);
        LocalBroadcastManager.getInstance(ChatActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));


        //pagination
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                getMessagesList(thread_id);

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


        adapter = new ChatRecPaginationAdaptor(userMessage, ChatActivity.this, this);
        recyclerView.setAdapter(adapter);
        getMessagesList(thread_id);

        isActivityOn = true;

    }

    @Override
    public void delItemClick(int position) {
        dellMessage(userMessage.get(position).getId().toString(), position);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        isRefreshing = true;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        getMessagesList(thread_id);
    }


    void getMessagesList(int id) {

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
        String token = SharedPreference.getSharedPrefValue(ChatActivity.this, Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<MessagesChatResponse> call = api.getMessagesList(token, id + "", currentPage);

        call.enqueue(new Callback<MessagesChatResponse>() {
            @Override
            public void onResponse(Call<MessagesChatResponse> call, Response<MessagesChatResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getMessageData() != null && response.body().getMessageData().getUserMessage() != null) {
                                userMessage = response.body().getMessageData().getUserMessage().getData();

                                //pagination
                                totalPage = response.body().getMessageData().getUserMessage().getLastPage();
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                adapter.addItems(userMessage);
                                itemCount = adapter.getItemCount();
                                swipeRefresh.setRefreshing(false);

                                // check weather is last page or not
                                if (currentPage < totalPage) {
                                    adapter.addLoading();
                                } else {
                                    isLastPage = true;
                                }
                                isLoading = false;


                                isRefreshing = false;
                                swipeRefresh.setRefreshing(false);


                            }

                        } else {
                            ApplicationUtils.showToast(ChatActivity.this, response.body().getMessage() + "", false);

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        ApplicationUtils.showToast(ChatActivity.this, jsonObject.getString("message") + "", false);
                    }

                } catch (Exception e) {
                    showProgressBar(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MessagesChatResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    void sendMessage() {

        showProgressBar(true);
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(ChatActivity.this, Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<SendMessageResponse> call = api.sendMessage(token, thread_id + "", to_user_id, etMessage.getText().toString());
        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {


                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            showProgressBar(false);
                            etMessage.setText("");
                            if (response.body().getData() != null) {
                                adapter.addOneItem(response.body().getData());
                                recyclerView.smoothScrollToPosition(0);
                            }
                        } else {
                            ApplicationUtils.showToast(ChatActivity.this, response.body().getMessage() + "", false);
                        }
                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        ApplicationUtils.showToast(ChatActivity.this, jsonObject.getString("message") + "", false);
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


    void dellMessage(String id, final int position) {


        showProgressBar(true);
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(ChatActivity.this, Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<GenResponse> call = api.delMessage(token, id);
        call.enqueue(new Callback<GenResponse>() {
            @Override
            public void onResponse(Call<GenResponse> call, Response<GenResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            ApplicationUtils.showToast(ChatActivity.this, response.body().getMessage() + "", true);
                            adapter.removeOneItem(position);

                        } else {
                            ApplicationUtils.showToast(ChatActivity.this, response.body().getMessage() + "", false);

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        ApplicationUtils.showToast(ChatActivity.this, jsonObject.getString("message") + "", false);
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

    private void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String status = intent.getStringExtra("Status");
//            String message = intent.getStringExtra("message_obj");
//            if (message != null) {
//                UserMessages userMessages = ApplicationUtils.fromJson(getApplicationContext(), message);
//                if (userMessages != null) {
//                    adapter.addOneItem(userMessages);
//                }
//            }
//        }
//    };


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String status = intent.getStringExtra("Status");
            String message = intent.getStringExtra("message_obj");
            if (message != null) {
                UserMessages userMessages = ApplicationUtils.fromJson(getApplicationContext(), message);
                if (userMessages != null) {
                    if (adapter.mPostItems != null && adapter.mPostItems.size() > 0) {
                        if (adapter.mPostItems.get(0).getThreadId() == userMessages.getThreadId()) {
                            adapter.addOneItem(userMessages);
                            setReadMessageTrue(userMessages.getId().toString());
                        } else {


                        }
                    }
                }
            }
//            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

        }
    };


    void setReadMessageTrue(String id) {


//        showProgressBar(true);
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(ChatActivity.this, Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<GenResponse> call = api.readMessage(token, id);
        call.enqueue(new Callback<GenResponse>() {
            @Override
            public void onResponse(Call<GenResponse> call, Response<GenResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
//                            ApplicationUtils.showToast(ChatActivity.this, response.body().getMessage() + "", true);


                        } else {
//                            ApplicationUtils.showToast(ChatActivity.this, response.body().getMessage() + "", false);

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        ApplicationUtils.showToast(ChatActivity.this, jsonObject.getString("message") + "", false);
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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_toolbar_back:
                ApplicationUtils.hideKeyboard((AppCompatActivity) mContext);
                finish();
                break;
            case R.id.btn_toolbar_right:

                break;
        }
    }
}
