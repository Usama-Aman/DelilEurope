package com.dalileuropeapps.dalileurope.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.utils.AppController;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import org.json.JSONObject;


@SuppressLint("SetJavaScriptEnabled")
public class SubscriptionsFragment extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private Activity mContext;
    private static ProgressBar progressBar;


    private View toolBar;
    private ImageView btnToolbarBack;
    private AutoCompleteTextView etToolbarSearch;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    private TextView tvToolbarClose;

    public static boolean isOpened = false;


    private String userID;
    String packageID;
    String type;

    private static SwipeRefreshLayout mSwipeRefreshLayout;


    Handler mHandler;

    String deviceId;
    WebView webView;
    boolean isSuccess = false;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        mContext = this;
        isOpened = true;
        mHandler = new Handler();
        getActivityData();
        initToolBar();
        viewInitialize();


    }


    public void initToolBar() {
        toolBar = mContext.findViewById(R.id.iToolbar);
        btnToolbarBack = (ImageView) toolBar.findViewById(R.id.btn_toolbar_back);
        tvToolbarTitle = (TextView) toolBar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(getResources().getString(R.string.title_paypal));
        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void viewInitialize() {
        webView = (WebView) findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        tvToolbarTitle = (TextView) toolBar.findViewById(R.id.tv_toolbar_title);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        webView.setVisibility(View.VISIBLE);
        loadSubscriptionWebView();
    }


    public static void showProgressBar(final boolean progressVisible) {
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(progressVisible ? true : false);
    }


    @Override
    public void onClick(View v) {


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

            type = bundle.getString("type");


            if (type.equalsIgnoreCase("package"))
                packageID = bundle.getString("package_id");

        }
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void loadSubscriptionWebView() {


        try {

            String base64UserId = userID;

            byte[] data = base64UserId.getBytes("UTF-8");

            base64UserId = Base64.encodeToString(data, Base64.DEFAULT);


            WebSettings webSetting = webView.getSettings();
            webSetting.setBuiltInZoomControls(true);
            webSetting.setJavaScriptEnabled(true);
            webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
            webSetting.setLoadWithOverviewMode(true);
            webSetting.setDomStorageEnabled(true);
            webView.clearCache(true);
            webView.clearHistory();
            webView.setWebViewClient(new WebViewClient());
            webView.addJavascriptInterface(new PayGatePaymentJavaScriptInterface(mContext), "MobileEvent");


            if (ApplicationUtils.isSet(packageID)) {
                String base64PackageId = packageID;
                byte[] data2 = base64PackageId.getBytes("UTF-8");
                base64PackageId = Base64.encodeToString(data2, Base64.DEFAULT);

             /*   Log.e("packageID: ", "----" + packageID);
                Log.e("base64UserId: ", "----" + base64UserId);
                Log.e("base64PackageId: ", "----" + base64PackageId);*/
                showProgressBar(true);
                //      Log.e("url: ", "----: " + ("https://elxdrive.com/dalieurope/paypal_payment/" + base64UserId + "/" + base64PackageId + "/" + "package"));
                webView.loadUrl("https://elxdrive.com/dalieurope/paypal_payment/" + base64UserId + "/" + base64PackageId + "/" + "package");
            } else {

                String base64PackageId = "0";
                byte[] data2 = base64PackageId.getBytes("UTF-8");
                base64PackageId = Base64.encodeToString(data2, Base64.DEFAULT);

                showProgressBar(true);
                webView.loadUrl("https://elxdrive.com/dalieurope/paypal_payment/" + base64UserId + "/" + base64PackageId + "/" + type);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            showProgressBar(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            showProgressBar(false);
        }

    }

    //fee
    public class PayGatePaymentJavaScriptInterface {
        private Activity mInterfaceContext;

        /**
         * Instantiate the interface and set the context
         */
        PayGatePaymentJavaScriptInterface(Activity c) {
            mInterfaceContext = c;
        }

        @JavascriptInterface
        public void textFromWeb(String result) {


            if (!ApplicationUtils.isSet(result)) {
                DialogFactory.showDropDownNotificationError(mInterfaceContext,
                        mInterfaceContext.getString(R.string.alert_information),
                        "Something is going wrong on payment. Please refresh page.");
                return;
            }


            try {


                JSONObject jObject = new JSONObject(result);
                if (jObject.has("status")) {
                    if (ApplicationUtils.isSet(jObject.getString("status"))) {
                        if (jObject.getString("status").equalsIgnoreCase("true")) {
                            DialogFactory.showDropDownNotificationSuccess(mContext,
                                    mContext.getString(R.string.alert_success),
                                    "Payment has been made successfully.");
                            //callSubscriptionApi();
                            AppController.isPayPal = true;

                            Intent returnIntent = new Intent();
                            String resultIntent = "paypal";
                            returnIntent.putExtra("result", resultIntent);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /***************************************************************/


    @Override
    public void onRefresh() {
        loadSubscriptionWebView();
    }


}