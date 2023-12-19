package com.dalileuropeapps.dalileurope.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.utils.AppController;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;


@SuppressLint("SetJavaScriptEnabled")
public class StripActivity extends AppCompatActivity {


    private Activity mContext;
    TextView tvToolbarTitle;

    private View toolBar;
    private ImageView btnToolbarBack;
    public static boolean isOpened = false;
    private String userID;
    String packageID;
    Handler mHandler;
    ProgressBar progressBar;
    private CardMultilineWidget cardMultilineWidget;

    private Stripe stripe = null;
    private String stripeToken = "";

    Button payButton;

    long mLastClickTime;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment);
        mContext = this;
        mHandler = new Handler();
        getActivityData();
        initToolBar();
        viewInitialize();
    }


    public void initToolBar() {
        toolBar = mContext.findViewById(R.id.iToolbar);
        btnToolbarBack = (ImageView) toolBar.findViewById(R.id.btn_toolbar_back);
        tvToolbarTitle = (TextView) toolBar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(getResources().getString(R.string.title_strip));
        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void viewInitialize() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        cardMultilineWidget = findViewById(R.id.card_multiline_widget);
        payButton = findViewById(R.id.payButton);

        stripe = new Stripe(this, getResources().getString(R.string.stip_pb_key));


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                progressBar.setVisibility(View.VISIBLE);

                Card cardToSave = cardMultilineWidget.getCard();
                if (cardToSave == null) {
                    DialogFactory.showDropDownNotificationError(StripActivity.this, getString(R.string.alert_information), getResources().getString(R.string.invalid_card));
                    return;
                }

                ApplicationUtils.hideKeyboard(StripActivity.this);
                stripe.createToken(
                        cardToSave, new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(@NonNull Token result) {

                                Intent tokenIntent = new Intent("TokenFromStripe");
                                tokenIntent.putExtra("tokenKey", result.getId());
                                stripeToken = result.getId();


                                AppController.isStrip = true;
                                AppController.stripToken = stripeToken;

                                Intent returnIntent = new Intent();
                                String resultIntent = "strip";
                                returnIntent.putExtra("result", resultIntent);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }

                            @Override
                            public void onError(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                DialogFactory.showDropDownNotificationError(StripActivity.this, getString(R.string.alert_information), e.getLocalizedMessage());
                            }
                        });

            }
        });


    }


    public static void showProgressBar(final boolean progressVisible) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void getActivityData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            User userDetail = SharedPreference.getUserData(mContext);
            userID = userDetail.getId().toString();
            // packageID = bundle.getString("package_id");

        }
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}