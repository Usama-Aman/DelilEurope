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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class ResetPasswordActivity extends BaseActivity implements View.OnTouchListener {
    View rlMainView;
    ProgressBar progressBar;
    TextInputLayout inputLayoutCode;
    TextInputEditText etCode;


    TextInputLayout inputLayoutPass, inputLayoutConfirmPass;
    TextInputEditText etPassword, etConfirmPassword;

    Button btnReset;
    TextView tvBackToLogin;
    Activity mContext;


    String email;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mContext = this;
        setStatusBarColor(getResources().getColor(R.color.white));
        initViews();
        getData();
        setData();
    }


    public void initViews() {
        rlMainView = findViewById(R.id.rlMainView);
        progressBar = findViewById(R.id.progressBar);
        inputLayoutCode = findViewById(R.id.inputLayoutCode);
        inputLayoutPass = findViewById(R.id.inputLayoutPassword);
        inputLayoutConfirmPass = findViewById(R.id.inputLayoutConfirmPassword);

        etCode = findViewById(R.id.etCode);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnReset = findViewById(R.id.btnReset);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);


        etCode.addTextChangedListener(new MyTextWatcher(etCode));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        etConfirmPassword.addTextChangedListener(new MyTextWatcher(etConfirmPassword));

        inputLayoutCode.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutConfirmPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));

        onFocusListener(etCode);
        onFocusListener(etPassword);
        onFocusListener(etConfirmPassword);

        btnReset.setOnTouchListener(this);
        tvBackToLogin.setOnTouchListener(this);
        rlMainView.setOnTouchListener(this);
    }


    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("email") != null)
                email = intent.getStringExtra("email");
        }
    }

    public void setData() {
    }

    private void validateData() {

        if (validateOTPCOdeEmpty(true) == false)
            return;
        if (validatePasswordEmpty(true) == false)
            return;
        if (validateConfirmPasswordEmpty(true) == false)
            return;


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
            showProgressBar(true);
            createResetData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean validateOTPCOdeEmpty(boolean errorShouldShow) {
        if (ApplicationUtils.isSet(etCode.getText().toString())) {
            return validateOTPCOde(true);
        } else {
            if (errorShouldShow)
                inputLayoutCode.setError(getResources().getString(R.string.tv_err_msg_otp_empty_code));
            etCode.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etCode);
            return false;
        }
    }

    public boolean validateOTPCOde(boolean errorShouldShow) {
        if (ApplicationUtils.validatePassword(etCode.getText().toString(), 4)) {
            inputLayoutCode.setErrorEnabled(false);
            etCode.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
            if (errorShouldShow)
                inputLayoutCode.setError(getResources().getString(R.string.tv_err_msg_otp_code));
            etCode.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etCode);
            return false;
        }
        return true;
    }

    public boolean validatePasswordEmpty(boolean errorShouldShow) {
        if (ApplicationUtils.isSet(etPassword.getText().toString())) {
            return validatePassword(true);
        } else {
            if (errorShouldShow)
                inputLayoutPass.setError(getResources().getString(R.string.tv_err_msg_password_empty));
            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etPassword);
            return false;
        }
    }


    public boolean validatePassword(boolean errorShouldShow) {
        if (ApplicationUtils.validatePassword(etPassword.getText().toString(), 6)) {
            inputLayoutPass.setErrorEnabled(false);
            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);

            if (ApplicationUtils.isSet(etConfirmPassword.getText().toString())) {
                return validateConfirmPassword(true);
            } else {
                return true;
            }
        } else {
            if (errorShouldShow)
                inputLayoutPass.setError(getResources().getString(R.string.tv_err_msg_password));
            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etPassword);
            if (ApplicationUtils.isSet(etConfirmPassword.getText().toString())) {
                return validateConfirmPassword(true);
            } else {
                return false;
            }

        }

    }

    public boolean validateConfirmPasswordEmpty(boolean errorShouldShow) {
        if (ApplicationUtils.isSet(etConfirmPassword.getText().toString())) {
            return validateConfirmPassword(true);
        } else {
            if (errorShouldShow)
                inputLayoutConfirmPass.setError(getResources().getString(R.string.tv_err_msg_password_confirm_empty));
            etConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            //requestFocus(etConfirmPassword);
            return false;
        }
    }

    public boolean validateConfirmPassword(boolean errorShouldShow) {


        if (ApplicationUtils.validateConfirmPassword(etPassword.getText().toString(), etConfirmPassword.getText().toString())) {
            inputLayoutConfirmPass.setErrorEnabled(false);
            etConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {

            if (errorShouldShow)
                inputLayoutConfirmPass.setError(getResources().getString(R.string.tv_err_msg_confirm_password));
            etConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            // requestFocus(etConfirmPassword);
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        /*if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }*/
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
                case R.id.etCode:
                    validateOTPCOde(true);
                    break;
                case R.id.etPassword:

                    validatePassword(true);
                    break;
                case R.id.etConfirmPassword:
                    validateConfirmPassword(true);
                    break;
            }
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (view.getId()) {
            case R.id.rlMainView:
                ApplicationUtils.hideKeyboard(mContext);
                break;

            case R.id.tvBackToLogin:

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    finish();
                }

                break;
            case R.id.btnReset:
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
                    case R.id.etCode:
                        if (hasFocus) {
                            inputLayoutCode.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutCode.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
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


    private void createResetData() {
        try {
            String code = etCode.getText().toString();
            String pass = etPassword.getText().toString();
            String confPass = etConfirmPassword.getText().toString();

            try {
                userResetPass(email, code, pass, confPass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userResetPass(String email, String code, String pass, String confPass) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.userResetPassword(email, code, pass, confPass, new Callback<GeneralResponse>() {
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
