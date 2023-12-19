package com.dalileuropeapps.dalileurope.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultLogin;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.LocaleHelper;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class SignInActivity extends BaseActivity implements View.OnTouchListener {
    View rlMainView;
    ProgressBar progressBar;
    TextInputLayout inputLayoutEmail, inputLayoutPass;
    TextInputEditText etEmail, etPassword;
    Button btnSignIn;
    TextView tvForgotPass, tvSignUp;
    Activity mContext;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
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
        inputLayoutPass = findViewById(R.id.inputLayoutPassword);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvSignUp = findViewById(R.id.tvSignUp);


        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));

        inputLayoutEmail.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));

        onFocusListener(etEmail);
        onFocusListener(etPassword);

        btnSignIn.setOnTouchListener(this);
        tvForgotPass.setOnTouchListener(this);
        tvSignUp.setOnTouchListener(this);
        rlMainView.setOnTouchListener(this);

    }

    public void getData() {

    }

    public void setData() {
    }

    private void validateData() {

        if (validateEmailEmpty(true) == false)
            return;
        if (validatePasswordEmpty(true) == false)
            return;

        showProgressBar(true);
        getLoginUser(etEmail.getText().toString(), etPassword.getText().toString());
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
        } else {
            if (errorShouldShow)
                inputLayoutPass.setError(getResources().getString(R.string.tv_err_msg_password));
            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etPassword);
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
                    validateEmail(false);
                    break;
                case R.id.etPassword:
                    validatePassword(false);
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
                ApplicationUtils.clearFocus(etPassword);
                break;
            case R.id.tvSignUp:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.clearFocus(etEmail);
                    ApplicationUtils.clearFocus(etPassword);
                    ApplicationUtils.hideKeyboard(mContext);
                    Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.btnSignIn:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    ApplicationUtils.clearFocus(etEmail);
                    ApplicationUtils.clearFocus(etPassword);
                    validateData();
                }

                break;
            case R.id.tvForgotPass:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ApplicationUtils.hideKeyboard(mContext);
                    ApplicationUtils.clearFocus(etEmail);
                    ApplicationUtils.clearFocus(etPassword);
                    Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
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
                    case R.id.etPassword:
                        if (hasFocus) {
                            inputLayoutPass.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
                        }
                        break;
                }

            }
        });

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    private void getLoginUser(String email, String password) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.loginUser(email, password, new Callback<ResultLogin>() {
            @Override
            public void onResponse(Call<ResultLogin> call,
                                   final retrofit2.Response<ResultLogin> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getUser() != null) {
                            if (ApplicationUtils.isSet(response.body().getUser().getLangKey())) {
                                String language = SharedPreference.unScrambleText(SharedPreference.getSharedPrefValue(mContext, Constants.PREF_LANGUAGE));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if (ApplicationUtils.isSet(language))
                                        LocaleHelper.setLocale(mContext, language);
                                    else {
                                        SharedPreference.savePrefValue(mContext, Constants.PREF_LANGUAGE, response.body().getUser().getLangKey());
                                        LocaleHelper.setLocale(mContext, response.body().getUser().getLangKey());
                                    }
                                }
                            }
                            User user = response.body().getUser();
                            SharedPreference.setAppLanguage(SignInActivity.this, user.getLangKey());
                            changeLanguageDefault(user.getLangKey());
                            SharedPreference.saveLoginDefaults(mContext, user, response.body().getToken(), true);

                            Intent i = new Intent(mContext, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            mContext.finish();


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
            public void onFailure(Call<ResultLogin> call, Throwable t) {
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

    private void changeLanguageDefault(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);
//        finishAffinity();

    }
}
