package com.dalileuropeapps.dalileurope.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultRegister;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.CustomTypefaceSpan;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends BaseActivity implements View.OnTouchListener {

    TextView tvTerms, tvSignIn;
    ImageView ivTerms;

    View rlMainView;
    View llMainView;
    ProgressBar progressBar;
    TextInputLayout inputLayoutEmail, inputLayoutPass, inputLayoutConfirmPass, inputLayoutFirstName, inputLayoutLastName;
    TextInputEditText etEmail, etPassword, etConfirmPassword, etFirstName, etLastName;
    Button btnSignUp;
    boolean isTermsChecked = false;
    Activity mContext;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;

        setStatusBarColor(getResources().getColor(R.color.white));
        initViews();
        getData();
        setData();
    }


    public void initViews() {
        llMainView = findViewById(R.id.llMainView);
        rlMainView = findViewById(R.id.rlMainView);
        progressBar = findViewById(R.id.progressBar);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPass = findViewById(R.id.inputLayoutPassword);
        inputLayoutConfirmPass = findViewById(R.id.inputLayoutConfirmPassword);
        inputLayoutFirstName = findViewById(R.id.inputLayoutFirstName);
        inputLayoutLastName = findViewById(R.id.inputLayoutLastName);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);

        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);

        tvTerms = findViewById(R.id.tvTerms);
        ivTerms = findViewById(R.id.ivTerms);

        btnSignUp.setOnTouchListener(this);
        tvTerms.setOnTouchListener(this);
        tvSignIn.setOnTouchListener(this);
        ivTerms.setOnTouchListener(this);
        rlMainView.setOnTouchListener(this);

        inputLayoutEmail.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutConfirmPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutFirstName.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutLastName.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));

        onFocusListener(etEmail);
        onFocusListener(etPassword);
        onFocusListener(etConfirmPassword);
        onFocusListener(etLastName);
        onFocusListener(etFirstName);

        etFirstName.addTextChangedListener(new MyTextWatcher(etFirstName));
        etLastName.addTextChangedListener(new MyTextWatcher(etLastName));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        etConfirmPassword.addTextChangedListener(new MyTextWatcher(etConfirmPassword));

        setTextViewColors();
    }

    public void getData() {

    }

    public void setData() {

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
            return false;
        }
        return true;
    }


    public boolean validateFirstName(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etFirstName.getText().toString())) {
            inputLayoutFirstName.setErrorEnabled(false);
            etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
            if (errorShouldShow)
                inputLayoutFirstName.setError(getResources().getString(R.string.tv_err_msg_name));
            etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }
        return true;
    }

    public boolean validateLastName(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etLastName.getText().toString())) {
            inputLayoutLastName.setErrorEnabled(false);
            etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
            if (errorShouldShow)
                inputLayoutLastName.setError(getResources().getString(R.string.tv_err_msg_last_name));
            etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
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
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        /*if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }*/
    }

    private void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void validateData() {

        if (validateFirstName(true) == false)
            return;

        if (validateLastName(true) == false)
            return;

        if (validateEmailEmpty(true) == false)
            return;

        if (validatePasswordEmpty(true) == false)
            return;

        if (validateConfirmPasswordEmpty(true) == false)
            return;

        if (!isTermsChecked) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_error),
                    mContext.getString(R.string.alert_terms_conditions));
            return;
        }

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
            showProgressBar(true);
            registerUser(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etConfirmPassword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    validateEmail(true);
                    break;
                case R.id.etPassword:
                    validatePassword(true);
                    break;
                case R.id.etConfirmPassword:
                    validateConfirmPassword(true);
                    break;
                case R.id.etFirstName:
                    validateFirstName(true);
                    break;
                case R.id.etLastName:
                    validateLastName(true);
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
            case R.id.tvSignIn:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    finish();
                }

                break;
            case R.id.btnSignUp:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    validateData();
                }
                break;
            case R.id.ivTerms:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (view.getTag().toString().equals("1")) {
                        ivTerms.setImageResource(R.drawable.ic_checked_box);
                        view.setTag("2");
                        isTermsChecked = true;
                    } else {
                        ivTerms.setImageResource(R.drawable.ic_unchecked_box);
                        view.setTag("1");
                        isTermsChecked = false;
                    }
                }

                break;
            case R.id.tvTerms:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.link_terms_and_conditions)));
                    startActivity(browserIntent);
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

                    case R.id.etConfirmPassword:
                        if (hasFocus) {
                            inputLayoutConfirmPass.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutConfirmPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
                        }
                        break;

                    case R.id.etFirstName:
                        if (hasFocus) {
                            inputLayoutFirstName.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutFirstName.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
                        }
                        break;


                    case R.id.etLastName:
                        if (hasFocus) {
                            inputLayoutLastName.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutLastName.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
                        }
                        break;

                }

            }
        });

    }

    public void setTextViewColors() {

        Typeface robotoRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface robotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", robotoRegular);
        TypefaceSpan robotoBoldSpan = new CustomTypefaceSpan("", robotoBold);
        String iAgree = getResources().getString(R.string.tv_i_agree) + " ";
        String termsConditions = getResources().getString(R.string.tv_terms_and_conditions);
        final SpannableStringBuilder spB = new SpannableStringBuilder(iAgree + termsConditions);
        spB.setSpan(robotoRegularSpan, 0, iAgree.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spB.setSpan(robotoBoldSpan, iAgree.length(), spB.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spB.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorDefaultText)), 0, iAgree.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spB.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), iAgree.length(), spB.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvTerms.setText(spB);

    }


    private void registerUser(
            String firstName,
            String lastName,
            String email,
            String password,
            String confirmPassword) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.registerUser(firstName,
                lastName,
                email,
                password,
                confirmPassword, new Callback<ResultRegister>() {
                    @Override
                    public void onResponse(Call<ResultRegister> call, final retrofit2.Response<ResultRegister> response) {
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
                    public void onFailure(Call<ResultRegister> call, Throwable t) {
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
