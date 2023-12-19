package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
// * {@link ContactUsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends BaseFragment implements View.OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ContactUsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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

    View rlMainView;
    ProgressBar progressBar;
    TextInputLayout inputLayoutFirstName, inputLayoutPhone, inputLayoutEmail, inputLayoutDescription;
    TextInputEditText etFirstName, etPhone, etEmail, etDescription;
    Button btnSave;

    Activity mContext;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mContext = getActivity();
        transparentStatusBar();
        initViews();
        getData();
        setData();
        return view;
    }
    public void initViews() {

        rlMainView = view.findViewById(R.id.rlMainView);
        progressBar = view.findViewById(R.id.progressBar);
        inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
        inputLayoutDescription = view.findViewById(R.id.inputLayoutDescription);
        inputLayoutPhone = view.findViewById(R.id.inputLayoutPhone);
        inputLayoutFirstName = view.findViewById(R.id.inputLayoutFirstName);


        etFirstName = view.findViewById(R.id.etFirstName);
        etPhone = view.findViewById(R.id.etPhone);
        etDescription = view.findViewById(R.id.etDescription);

        etEmail = view.findViewById(R.id.etEmail);

        btnSave = view.findViewById(R.id.btnSave);


        btnSave.setOnTouchListener(this);

        rlMainView.setOnTouchListener(this);

        inputLayoutEmail.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutDescription.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutPhone.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
        inputLayoutFirstName.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));



        onFocusListener(etDescription);
        onFocusListener(etEmail);
        onFocusListener(etPhone);
        onFocusListener(etFirstName);

        etFirstName.addTextChangedListener(new MyTextWatcher(etFirstName));
        etPhone.addTextChangedListener(new MyTextWatcher(etPhone));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etDescription.addTextChangedListener(new MyTextWatcher(etDescription));



    }

    public void getData() {

    }

    public void setData() {

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

    public boolean validatePhone(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etPhone.getText().toString())) {
            inputLayoutPhone.setErrorEnabled(false);
            etPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
            if (errorShouldShow)
                inputLayoutPhone.setError(getResources().getString(R.string.tv_err_msg_name));
            etPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }
        return true;
    }

    public boolean validateDesrption(boolean errorShouldShow) {
        if (ApplicationUtils.validatePassword(etDescription.getText().toString(), 5)) {
            inputLayoutDescription.setErrorEnabled(false);
            etDescription.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
            if (errorShouldShow)
                inputLayoutDescription.setError(getResources().getString(R.string.tv_err_msg_password));
            etDescription.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }
        return true;
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
        if (validatePhone(true) == false)
            return;
        if (validateEmail(true) == false)
            return;
        if (validateEmail(true) == false)
            return;
        if (validateDesrption(true) == false)
            return;


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
            showProgressBar(true);
//            registerUser(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etConfirmPassword.getText().toString());
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
                    validateEmail(false);
                    break;
                case R.id.etDescription:
                    validateDesrption(false);
                    break;
                case R.id.etFirstName:
                    validateFirstName(false);
                case R.id.etPhone:
                    validatePhone(false);
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
            case R.id.btnSave:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
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
                    case R.id.etDescription:
                        if (hasFocus) {
                            inputLayoutDescription.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutDescription.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
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


                    case R.id.etPhone:
                        if (hasFocus) {
                            inputLayoutPhone.setBoxBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            if (!ApplicationUtils.isSet(editText.getText().toString()))
                                inputLayoutPhone.setBoxBackgroundColor(getResources().getColor(R.color.colorFieldsBG));
                        }
                        break;

                }

            }
        });

    }




    private void registerUser(
            String firstName,
            String lastName,
            String email,
            String password,
            String confirmPassword) {


    }


}
