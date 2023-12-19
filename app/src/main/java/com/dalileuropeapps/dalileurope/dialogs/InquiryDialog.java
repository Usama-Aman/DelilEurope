package com.dalileuropeapps.dalileurope.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.ChatActivity;
import com.dalileuropeapps.dalileurope.api.retrofit.message.SendMessageResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryDialog extends DialogFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    public static final String TAG = "InquiryDialog";

    RelativeLayout rlMain;
    ImageButton ibClose;
    View iMessagesDetail;
    ConstraintLayout clMessageDetail;


    ProgressBar progressBar;
    onClickListener listener;
    String adID = "0";

    RelativeLayout rlMessagesDetail;
    ScrollView scMessageDetail;


    EditText etMessage;
    Button btSubmitMessage;
    ImageView ivProfileMessage;
    TextView tvNameMessage;
    String toUserId = "";
    String userFirstName = "";

    String toUserImage = "";
    String userYear = "";
    String userName = "";


    View viewFragment;

    public interface onClickListener {
        void onSubmitMessage();
    }


    public InquiryDialog(onClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    private void getActivityData() {

        Bundle bundle = mContext.getIntent().getExtras();
        if (bundle != null) {
            adID = getArguments().getString("adId");
            toUserId = getArguments().getString("toUserId");
            toUserImage = getArguments().getString("toUserImage");
            userYear = getArguments().getString("userYear");
            userName = getArguments().getString("userName");

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            ColorDrawable back = new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimaryDark));
            InsetDrawable inset = new InsetDrawable(back, 20, 80, 20, 80);
            dialog.getWindow().setBackgroundDrawable(inset);
            DialogFactory.dialogSettings(mContext, dialog, rlMain, 0.55f, 0.96f);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        viewFragment = getActivity().getLayoutInflater().inflate(R.layout.dialog_message, parent, false);
        getActivityData();
        viewInitialize(viewFragment);
        return viewFragment;
    }


    private void viewInitialize(View v) {

        progressBar = v.findViewById(R.id.progressBar);

        rlMain = v.findViewById(R.id.rlMain);
        ibClose = v.findViewById(R.id.ibClose);

        clMessageDetail = v.findViewById(R.id.clMessage);

        iMessagesDetail = v.findViewById(R.id.iMessageDetail);

        rlMessagesDetail = v.findViewById(R.id.rlMessageDetail);
        scMessageDetail = v.findViewById(R.id.scMessageDetail);

        etMessage = v.findViewById(R.id.etMessage);
        btSubmitMessage = v.findViewById(R.id.btSubmitMessage);
        ivProfileMessage = v.findViewById(R.id.imgUserProfileMessage);
        tvNameMessage = v.findViewById(R.id.tvNameMessage);

        ibClose.setOnClickListener(this);

        iMessagesDetail.setOnClickListener(this);
        rlMessagesDetail.setOnClickListener(this);
        scMessageDetail.setOnClickListener(this);
        clMessageDetail.setOnClickListener(this);

        btSubmitMessage.setOnClickListener(this);

        if (ApplicationUtils.isSet(toUserImage) && mContext != null) {
            try {
                Glide.with(mContext)
                        .load(toUserImage)
                        .placeholder(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                        .error(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                        .into(ivProfileMessage);

            } catch (Exception e) {
                e.getMessage();
            }
        }

        if (ApplicationUtils.isSet(userName)) {
            tvNameMessage.setText(userName + userYear);
        }


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

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibClose:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);

                dismiss();
                break;
            case R.id.btSubmitMessage:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);


                if (!ApplicationUtils.isOnline(mContext)) {

                    DialogFactory.showDropDownNotification(mContext, viewFragment,
                            R.id.notificationLayout,
                            mContext.getResources().getColor(R.color.colorRed),
                            mContext.getResources().getString(R.string.alert_information),
                            getResources().getString(R.string.alert_internet_connection));
                    return;
                }


                if (!validateMessage())
                    return;
                showProgressBar(true);
                sendMessage();


                break;
            case R.id.iMessageDetail:
            case R.id.rlMessageDetail:
            case R.id.scMessageDetail:
            case R.id.clMessage:
            case R.id.tvAdditionalRating:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etMessage);

                break;
        }
    }

    void sendMessage() {

        showProgressBar(true);
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(mContext, Constants.USER_TOKEN);
        token = "Bearer " + token;
        etMessage.setEnabled(false);
        ibClose.setEnabled(false);

        Call<SendMessageResponse> call = api.sendMessage(token, "0" + "", toUserId, etMessage.getText().toString());
        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {


                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {

                            showProgressBar(false);
                            etMessage.setText("");

                            if (response.body().getData() != null) {
                                final UserMessages userMessages = response.body().getData();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ApplicationUtils.hideKeyboard(((Activity) mContext));
                                        Intent intent = new Intent(mContext, ChatActivity.class);
                                        intent.putExtra("thread_id", userMessages.getThreadId());
                                        intent.putExtra("name", userName);
                                        intent.putExtra("to_user_id", toUserId + "");
                                        startActivity(intent);
                                        dismiss();
                                    }
                                }, 1000);
                            }
                        } else {
                            etMessage.setEnabled(true);
                            ibClose.setEnabled(true);
                            DialogFactory.showDropDownNotification(mContext, viewFragment, R.id.notificationLayout, mContext.getResources().getColor(R.color.colorRed), mContext.getResources().getString(R.string.tv_msg_error), response.body().getMessage() + "");
                        }
                    } else {
                        etMessage.setEnabled(true);
                        ibClose.setEnabled(true);
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        DialogFactory.showDropDownNotification(mContext, viewFragment, R.id.notificationLayout, mContext.getResources().getColor(R.color.colorRed), mContext.getResources().getString(R.string.tv_msg_error), jsonObject.getString("message") + "");

                    }

                } catch (Exception e) {
                    etMessage.setEnabled(true);
                    ibClose.setEnabled(true);
                    showProgressBar(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                etMessage.setEnabled(true);
                ibClose.setEnabled(true);
                showProgressBar(false);
                DialogFactory.showDropDownNotification(mContext, viewFragment, R.id.notificationLayout, mContext.getResources().getColor(R.color.colorRed), mContext.getResources().getString(R.string.tv_msg_error), t.getMessage()+ "");

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private boolean validateMessage() {
        if (etMessage.getText().toString().trim().isEmpty()) {

            DialogFactory.showDropDownNotification(mContext, viewFragment,
                    R.id.notificationLayout,
                    mContext.getResources().getColor(R.color.colorRed),
                    mContext.getResources().getString(R.string.alert_error),
                    getResources().getString(R.string.error_message_required));

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


    public void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

}