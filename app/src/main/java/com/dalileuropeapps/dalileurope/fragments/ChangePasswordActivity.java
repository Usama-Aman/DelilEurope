package com.dalileuropeapps.dalileurope.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultLogin;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangePasswordActivity extends BaseFragment implements View.OnTouchListener {

    View rlMainView;
    ProgressBar progressBar;

    TextInputLayout inputLayoutCurrentPassword, inputLayoutPass, inputLayoutConfirmPass;
    TextInputEditText etPassword, etConfirmPassword, etCurrentPassword;
    private ImageView btnToolbarBack;
    private TextView tvToolbarTitle;
    RelativeLayout toolbar;
    View iToolbar;
    Button btnReset;
    View view;
    Activity mContext;
    private ActivityListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_change_password, container, false);
        mContext = getActivity();
        try {
            listener = (ActivityListener) mContext;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
        transparentStatusBar();
        setStatusBar();
        initToolBar();
        initViews();

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
        //toolbar.setBackgroundResource(R.drawable.bg_header);
        btnToolbarBack = (ImageView) toolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);


        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.tv_change_password));


        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext!=null)
                    ApplicationUtils.hideKeyboard(mContext);
                listener.callToolbarBack(true);
            }
        });


    }

    public void initViews() {
        rlMainView = view.findViewById(R.id.rlMainView);
        progressBar = view.findViewById(R.id.progressBar);
        inputLayoutCurrentPassword = view.findViewById(R.id.inputLayoutCurrentPassword);
        inputLayoutPass = view.findViewById(R.id.inputLayoutPassword);
        inputLayoutConfirmPass = view.findViewById(R.id.inputLayoutConfirmPassword);

        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnReset = view.findViewById(R.id.btnReset);


        etCurrentPassword.addTextChangedListener(new MyTextWatcher(etCurrentPassword));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        etConfirmPassword.addTextChangedListener(new MyTextWatcher(etConfirmPassword));

        inputLayoutCurrentPassword.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutConfirmPass.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));

        onFocusListener(etCurrentPassword);
        onFocusListener(etPassword);
        onFocusListener(etConfirmPassword);

        btnReset.setOnTouchListener(this);
        rlMainView.setOnTouchListener(this);
    }


    private void validateData() {

        if (validateCurrentPasswordEmpty(true) == false)
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

    public boolean validateCurrentPasswordEmpty(boolean errorShouldShow) {
        if (ApplicationUtils.isSet(etCurrentPassword.getText().toString())) {
            return validateCurrentPassword(true);
        } else {
            if (errorShouldShow)
                inputLayoutCurrentPassword.setError(getResources().getString(R.string.tv_current_password_required));
            etCurrentPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etCurrentPassword);
            return false;
        }
    }

    public boolean validateCurrentPassword(boolean errorShouldShow) {
        if (ApplicationUtils.validatePassword(etCurrentPassword.getText().toString(), 6)) {
            inputLayoutCurrentPassword.setErrorEnabled(false);
            etCurrentPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
            return true;
        } else {
            if (errorShouldShow)
                inputLayoutCurrentPassword.setError(getResources().getString(R.string.tv_err_msg_password));
            etCurrentPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            requestFocus(etCurrentPassword);
            return false;
        }

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
            // requestFocus(etConfirmPassword);
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
     /*  if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                case R.id.etCurrentPassword:
                    validateCurrentPasswordEmpty(true);
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
                    case R.id.etCurrentPassword:
                        if (hasFocus) {
                            inputLayoutCurrentPassword.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutCurrentPassword.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
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
            String code = etCurrentPassword.getText().toString();
            String pass = etPassword.getText().toString();
            String confPass = etConfirmPassword.getText().toString();

            try {
                userResetPass(code, pass, confPass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userResetPass(String currentPass, String pass, String confPass) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.changePassword(currentPass, pass, confPass, new Callback<ResultLogin>() {
            @Override
            public void onResponse(Call<ResultLogin> call, final retrofit2.Response<ResultLogin> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {

                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            SharedPreference.saveProfileDefaults(mContext, response.body().getUser());
                            SharedPreference.saveLoginDefaults(mContext, response.body().getUser(), response.body().getToken(), true);
                            DialogFactory.showDropDownNotificationSuccess(mContext,
                                    mContext.getString(R.string.alert_success),
                                    response.body().getMessage());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    listener.callHomeReset();
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
            public void onFailure(Call<ResultLogin> call, Throwable t) {
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
