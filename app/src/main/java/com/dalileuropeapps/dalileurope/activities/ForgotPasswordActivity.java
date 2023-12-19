package com.dalileuropeapps.dalileurope.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordActivity extends BaseActivity implements View.OnTouchListener {
    View rlMainView;
    ProgressBar progressBar;
    TextInputLayout inputLayoutEmail;
    TextInputEditText etEmail;
    Button btnSend;
    TextView tvBackToLogin;
    Activity mContext;
    ScrollView svMainView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mContext = this;
        setStatusBarColor(getResources().getColor(R.color.white));
        initViews();
        getData();
        setData();
    }


    public void initViews() {
        rlMainView = findViewById(R.id.rlMainView);
        progressBar = findViewById(R.id.progressBar);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        svMainView = findViewById(R.id.svMainView);
        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);


        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        inputLayoutEmail.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        onFocusListener(etEmail);

        tvBackToLogin.setOnTouchListener(this);
        btnSend.setOnTouchListener(this);
        svMainView.setOnTouchListener(this);
        rlMainView.setOnTouchListener(this);
    }

    public void getData() {

    }

    public void setData() {
    }

    private void validateData() {

        if (validateEmailEmpty(true) == false)
            return;


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
            showProgressBar(true);
            createForgotData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean validateEmailEmpty(boolean errorShouldShow) {
        if (ApplicationUtils.isSet(etEmail.getText().toString())) {
            return validateEmail(true);
        } else {
            if (errorShouldShow)
                inputLayoutEmail.setError(getResources().getString(R.string.tv_err_msg_email_empty));
            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etEmail);
            return false;
        }

    }

    public boolean validateEmail(boolean errorShouldShow) {
        if (ApplicationUtils.validateEmail(etEmail.getText().toString())) {
            inputLayoutEmail.setErrorEnabled(false);
            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
            if (errorShouldShow)
                inputLayoutEmail.setError(getResources().getString(R.string.tv_err_msg_email));
            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etEmail);
            return false;
        }
        return true;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etEmail:
                    validateEmailEmpty(false);
                    break;
            }
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (view.getId()) {
            case R.id.rlMainView:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etEmail);
                break;
            case R.id.svMainView:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etEmail);
                break;
            case R.id.tvBackToLogin:

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    ApplicationUtils.hideKeyboard(mContext);
                    ApplicationUtils.clearFocus(etEmail);
                    finish();
                }

                break;
            case R.id.btnSend:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    validateData();
                }

                break;
        }
        return true;
    }

    public void onFocusListener(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                switch (editText.getId()) {
                    case R.id.etEmail:
                        if (hasFocus) {
                            inputLayoutEmail.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutEmail.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
                        }
                        break;
                }

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


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void createForgotData() {
        try {
            String email = etEmail.getText().toString();

            try {
                userForgotPass(email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userForgotPass(
            String email) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.userForgotPassword(email, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, final retrofit2.Response<GeneralResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {

                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationSuccess(mContext,
                                    mContext.getString(R.string.alert_success),
                                    response.body().getMessage());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent resetIntent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                                    resetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    resetIntent.putExtra("email", etEmail.getText().toString());
                                    startActivity(resetIntent);
                                    finish();
                                }
                            }, 1500);
                        }
                    } else {

                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {

                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {

                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_error),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_error),
                                mContext.getString(R.string.alert_internal_server_error));

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_error),
                            mContext.getString(R.string.alert_api_not_found));
                else if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_error),
                            mContext.getString(R.string.alert_request_timeout));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_error),
                            t.getLocalizedMessage());
            }
        });
    }


}
