package com.dalileuropeapps.dalileurope.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dalileuropeapps.dalileurope.activities.DetailAdsActivity.toRefresh;

public class RatingDialog extends DialogFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    public static final String TAG = "RatingDialog";

    RelativeLayout rlMain;
    ImageButton ibClose;
    View iReviewsDetail;
    ConstraintLayout clReviewDetail;
    RequestBody ratingExpert;
    RequestBody ratingProfessional;

    EditText etTitle;
    RatingBar rbExpert;
    RatingBar rbProfessional;
    TextView tvRating;
    EditText etReview;
    RatingBar rbRateGive;
    Button btSubmit;
    TextView tvAdditionalRating;
    ProgressBar progressBar;
    onClickListener listener;
    String adID = "0";

    RelativeLayout rlReviewsDetail;
    ScrollView scReviewDetail;

    View viewFragment;

    public interface onClickListener {
        void onSubmitReview();
    }


    public RatingDialog(onClickListener listener) {
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
            DialogFactory.dialogSettings(mContext, dialog, rlMain, 0.80f, 0.95f);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        viewFragment = getActivity().getLayoutInflater().inflate(R.layout.dialog_review, parent, false);
        getActivityData();
        viewInitialize(viewFragment);
        return viewFragment;
    }


    private void viewInitialize(View v) {

        progressBar = v.findViewById(R.id.progressBar);

        rlMain = v.findViewById(R.id.rlMain);
        ibClose = v.findViewById(R.id.ibClose);

        etTitle = v.findViewById(R.id.etTitle);
        rbProfessional = v.findViewById(R.id.rbProfessional);
        tvRating = v.findViewById(R.id.tvRating);
        btSubmit = v.findViewById(R.id.btSubmitDetail);
        rbRateGive = v.findViewById(R.id.rbRateGiveDetail);
        clReviewDetail = v.findViewById(R.id.clReviewDetail);
        etTitle = v.findViewById(R.id.etTitle);
        rbProfessional = v.findViewById(R.id.rbProfessional);
        tvRating = v.findViewById(R.id.tvRating);
        rbExpert = v.findViewById(R.id.rbExpert);
        etReview = v.findViewById(R.id.etReviewDetail);
        iReviewsDetail = v.findViewById(R.id.iReviewsDetail);
        tvAdditionalRating = v.findViewById(R.id.tvAdditionalRating);
        rlReviewsDetail = v.findViewById(R.id.rlReviewsDetail);
        scReviewDetail = v.findViewById(R.id.scReviewDetail);

        btSubmit.setOnClickListener(this);
        ibClose.setOnClickListener(this);

        iReviewsDetail.setOnClickListener(this);
        rlReviewsDetail.setOnClickListener(this);
        scReviewDetail.setOnClickListener(this);
        clReviewDetail.setOnClickListener(this);
        tvAdditionalRating.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibClose:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                ApplicationUtils.clearFocus(etTitle);
                dismiss();
                break;
            case R.id.btSubmitDetail:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                ApplicationUtils.clearFocus(etTitle);
                createReviewRequestData();
                break;
            case R.id.iReviewsDetail:
            case R.id.rlReviewsDetail:
            case R.id.scReviewDetail:
            case R.id.clReviewDetail:
            case R.id.tvAdditionalRating:
                ApplicationUtils.hideKeyboard(mContext);
                ApplicationUtils.clearFocus(etReview);
                ApplicationUtils.clearFocus(etTitle);
                break;
        }
    }


    private boolean validateRate() {

        if (rbRateGive.getRating() == 0) {


            DialogFactory.showDropDownNotification(mContext,
                    viewFragment,
                    R.id.notificationLayout,
                    mContext.getResources().getColor(R.color.colorRed),
                    mContext.getResources().getString(R.string.tv_msg_error),
                    getResources().getString(R.string.error_msg_review_rating_required));


            requestFocus(rbRateGive);
            return false;
        }
        return true;
    }

    private boolean validateReviewTitle() {
        if (etTitle.getText().toString().trim().isEmpty()) {


            DialogFactory.showDropDownNotification(mContext,
                    viewFragment,
                    R.id.notificationLayout,
                    mContext.getResources().getColor(R.color.colorRed),
                    mContext.getResources().getString(R.string.tv_msg_error),
                    getResources().getString(R.string.error_msg_review_title_required));

            requestFocus(etTitle);
            return false;
        }
        return true;
    }

    private boolean validateReview() {
        if (etReview.getText().toString().trim().isEmpty()) {


            DialogFactory.showDropDownNotification(mContext,
                    viewFragment,
                    R.id.notificationLayout,
                    mContext.getResources().getColor(R.color.colorRed),
                    mContext.getResources().getString(R.string.tv_msg_error),
                    getResources().getString(R.string.error_msg_review_note_required));

            requestFocus(etReview);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void createReviewRequestData() {
        try {


            if (!ApplicationUtils.isOnline(mContext)) {


                DialogFactory.showDropDownNotification(mContext,
                        viewFragment,
                        R.id.notificationLayout,
                        mContext.getResources().getColor(R.color.colorRed),
                        mContext.getResources().getString(R.string.tv_msg_error),
                        getResources().getString(R.string.alert_internet_connection));
                return;
            }

            if (!validateRate())
                return;
            if (!validateReviewTitle())
                return;
            if (!validateReview())
                return;


            showProgressBar(true);
            final RequestBody adsIdbody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(adID));
            final RequestBody review = RequestBody.create(MediaType.parse("text/plain"), etReview.getText().toString());
            final RequestBody title = RequestBody.create(MediaType.parse("text/plain"), etTitle.getText().toString());
            final RequestBody rating = RequestBody.create(MediaType.parse("text/plain"), rbRateGive.getRating() + "");
            ratingExpert = RequestBody.create(MediaType.parse("text/plain"), "0");
            ratingProfessional = RequestBody.create(MediaType.parse("text/plain"), "0");

            if (rbExpert.getRating() != 0)
                ratingExpert = RequestBody.create(MediaType.parse("text/plain"), rbExpert.getRating() + "");

            if (rbProfessional.getRating() != 0)
                ratingProfessional = RequestBody.create(MediaType.parse("text/plain"), rbProfessional.getRating() + "");

            try {
                callGiveReview(adsIdbody, review, rating, null, ratingExpert, ratingProfessional, title);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGiveReview(RequestBody adsId, RequestBody review, RequestBody rating, MultipartBody.Part imageBodyPart, RequestBody ratingExpert, RequestBody ratingProfessional, RequestBody title) {

        ApiClient apiClient = ApiClient.getInstance();
        ibClose.setEnabled(false);

        apiClient.giveReview(adsId, review, rating, imageBodyPart, ratingExpert, ratingProfessional, title, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, final Response<GeneralResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {


                        DialogFactory.showDropDownNotification(mContext,
                                viewFragment,
                                R.id.notificationLayout,
                                mContext.getResources().getColor(R.color.colorGreen),
                                mContext.getResources().getString(R.string.alert_success),
                                response.body().getMessage());

                        etReview.setText("");
                        etTitle.setText("");
                        rbRateGive.setRating(0);
                        rbExpert.setRating(0);
                        rbProfessional.setRating(0);

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                toRefresh = true;
                                listener.onSubmitReview();
                                dismiss();
                            }
                        }, 500);


                    } else {
                        ibClose.setEnabled(true);
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {

                            DialogFactory.showDropDownNotification(mContext,
                                    viewFragment,
                                    R.id.notificationLayout,
                                    mContext.getResources().getColor(R.color.colorRed),
                                    mContext.getResources().getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
                    ibClose.setEnabled(true);
                    if (response.code() == 404) {

                        DialogFactory.showDropDownNotification(mContext,
                                viewFragment,
                                R.id.notificationLayout,
                                mContext.getResources().getColor(R.color.colorRed),
                                mContext.getResources().getString(R.string.alert_error),
                                mContext.getString(R.string.alert_api_not_found));
                    }
                    if (response.code() == 500) {

                        DialogFactory.showDropDownNotification(mContext,
                                viewFragment,
                                R.id.notificationLayout,
                                mContext.getResources().getColor(R.color.colorRed),
                                mContext.getResources().getString(R.string.alert_error),
                                mContext.getString(R.string.alert_internal_server_error));
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ibClose.setEnabled(true);
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotification(mContext,
                            viewFragment,
                            R.id.notificationLayout,
                            mContext.getResources().getColor(R.color.colorRed),
                            mContext.getResources().getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotification(mContext,
                            viewFragment,
                            R.id.notificationLayout,
                            mContext.getResources().getColor(R.color.colorRed),
                            mContext.getResources().getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else

                    DialogFactory.showDropDownNotification(mContext,
                            viewFragment,
                            R.id.notificationLayout,
                            mContext.getResources().getColor(R.color.colorRed),
                            mContext.getResources().getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
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