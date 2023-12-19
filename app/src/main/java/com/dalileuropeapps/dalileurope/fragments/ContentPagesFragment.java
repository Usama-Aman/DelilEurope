package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.ContentPageData;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.ContentPageRespnse;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.dalileuropeapps.dalileurope.utils.ApplicationUtils.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContentPagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentPagesFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static ContentPagesFragment newInstance(String param1, String title) {
        ContentPagesFragment fragment = new ContentPagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    Activity mActivityContext;
    ProgressBar progressBar;
    View view;
    Context mContext;
    // tollbar
    private ImageView btnToolbarBack;
    private TextView tvToolbarTitle;
    RelativeLayout toolbar;
    View iToolbar;
    String pageContentType, pageTitle;
    WebView webView;
    private ActivityListener listener;
    Map<String, String> contentTypes = new HashMap<>();

    String language = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageContentType = getArguments().getString(ARG_PARAM1);
            pageTitle = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_content_pages, container, false);

        mActivityContext = getActivity();
        mContext = view.getContext();
        language = SharedPreference.getAppLanguage(mContext);
        try {
            listener = (ActivityListener) mContext;
        } catch (Exception castException) {
            /** The activity does not implement the listener. */
        }
        transparentStatusBar();
        setStatusBar();
        initToolBar();
        initAndSetViews();
        loadContentPages();
        return view;
    }


    public void initToolBar() {


        iToolbar = view.findViewById(R.id.iToolbar);
        toolbar = iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = toolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);


        tvToolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("");


        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivityContext != null)
                    ApplicationUtils.hideKeyboard(mActivityContext);
                listener.callToolbarBack(true);
            }
        });
    }

    private void initAndSetViews() {

        progressBar = view.findViewById(R.id.progressBar);
        tvToolbarTitle.setText(pageTitle);

        progressBar = view.findViewById(R.id.progressBar);
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setBuiltInZoomControls(true);

        getPageContent();
    }

    private void loadContentPages() {
        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mActivityContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        showProgressBar(true);
        getPageContent();
    }

    private void getPageContent() {

        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<ContentPageRespnse> call = api.getPageContent(token, pageContentType);

        call.enqueue(new Callback<ContentPageRespnse>() {
            @Override
            public void onResponse(Call<ContentPageRespnse> call, Response<ContentPageRespnse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null) {
                                ContentPageData data = response.body().getData();
                                webView.setWebViewClient(new WebViewClient());
                                webView.getSettings().setJavaScriptEnabled(true);
                                //  webView.zoomBy(0.01f);

                                if (language.equalsIgnoreCase(Constants.swedish)) {
                                    webView.loadData(data.getContentSv(), "text/html", "utf-8");
                                } else if (language.equalsIgnoreCase(Constants.germany)) {
                                    webView.loadData(data.getContentDe(), "text/html", "utf-8");
                                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                                    webView.loadData(data.getContentAr(), "text/html", "utf-8");
                                } else {
                                    webView.loadData(data.getContent(), "text/html", "utf-8");
                                }

                            }

                        } else {
                            showToast(getActivity(), response.body().getMessage() + "", false);

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        showToast(getActivity(), jsonObject.getString("message") + "", false);
                    }

                } catch (Exception e) {
                    showProgressBar(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ContentPageRespnse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private void showProgressBar(final boolean progressVisible) {
        mActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    private void setStatusBar() {
        if (ApplicationUtils.getDeviceStatusBarHeight(getActivity()) > 0) {
            transparentStatusBar();
        }
    }
}
