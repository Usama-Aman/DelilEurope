package com.dalileuropeapps.dalileurope.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.adapter.CategoriesSpinnerAdapter;
import com.dalileuropeapps.dalileurope.adapter.ImagePostAdAdapter;
import com.dalileuropeapps.dalileurope.adapter.SubCategoriesSpinnerAdapter;
import com.dalileuropeapps.dalileurope.api.local.GalleryModel;
import com.dalileuropeapps.dalileurope.api.retrofit.AdType;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsFeature;
import com.dalileuropeapps.dalileurope.api.retrofit.AdsOders;
import com.dalileuropeapps.dalileurope.api.retrofit.AllCategoriesResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessSubscriptions;
import com.dalileuropeapps.dalileurope.api.retrofit.Category;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponseDataDetail;
import com.dalileuropeapps.dalileurope.api.retrofit.PaymentMethod;
import com.dalileuropeapps.dalileurope.api.retrofit.PostAdResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAdsData;
import com.dalileuropeapps.dalileurope.api.retrofit.SubCategory;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.imagepicke_pack.ImagePickerActivity;
import com.dalileuropeapps.dalileurope.interfaces.OnImagePostAdClick;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.utils.AppController;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.ProgressRequestBody;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.dalileuropeapps.dalileurope.utils.SpacesItemDecoration;
import com.dalileuropeapps.dalileurope.utils.SpinnerCustomShape;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dalileuropeapps.dalileurope.activities.MainActivity.REQUEST_CODE_PAYMENT;
import static com.dalileuropeapps.dalileurope.activities.MainActivity.paymentConfig;
import static com.dalileuropeapps.dalileurope.fragments.AddListingFragment.isFromAddListFragment;
import static com.dalileuropeapps.dalileurope.fragments.AddListingFragment.toRefreshAddListFragment;


public class PostAdActivity extends BaseActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {
    private static final String TAG = "PostAdActivityPayments";

    NestedScrollView nsMainDetail;
    public EditText etTitle;
    SpinnerCustomShape spCategories;
    SpinnerCustomShape spSubCategories;
    EditText etTagLine;
    EditText etWebsite;
    TextView etLocation;
    EditText etPrice;
    EditText etDPrice;
    TextView etStartDate;
    TextView etEndDate;
    TextInputEditText etServices;
    TextInputEditText etLanguages;
    TextInputEditText etDesc;
    TextInputLayout ilServices;
    TextInputLayout ilLanguages;
    TextInputLayout ilDesc;


    ImagePostAdAdapter mGAdapter;
    RecyclerView rvImagesList;

    TextView tvNone;
    TextView tvFeatured;
    TextView tvHighlighted;
    int adType = 1;
    String adTypeName = "None";

    TextView tvCard;
    TextView tvPayPal;
    TextView tvMCard;


    LinearLayout llAddHours;

    ProgressBar progressBar;
    int userId = 0;
    Button btnSave;
    User userDetail;

    Activity mContext;
    GridLayoutManager mLinearLayoutManager;

    private ImageView btnToolbarBack;
    private ImageView btnToolbarRight;
    private TextView tvToolbarTitle;
    View iToolbar;
    Toolbar toolbar;
    double lat = 0.0;
    double lng = 0.0;
    int adId = 0;

    ArrayList<String> mDeletedImages = new ArrayList<>();

    private Category mSelectCategory;
    private SubCategory mSelectSubCategory;

    ArrayList<GalleryModel> mGalleryList = new ArrayList<>();

    ArrayList<Category> categoriesList = new ArrayList<>();
    ArrayList<SubCategory> subCategoriesList = new ArrayList<>();

    CategoriesSpinnerAdapter mCategoriesSpinnerAdapter;
    SubCategoriesSpinnerAdapter mSubCategoriesSpinnerAdapter;

    private boolean isStartDateClicked = false;
    private boolean isEndDateClicked = false;

    private int mStartDay;
    private int mStartMonth;
    private int mStartYear;
    private String mStartDate;


    private int mEndDay;
    private int mEndMonth;
    private int mEndYear;
    private String mEndDate;

    private String imagePath = "";
    private ThreadPoolExecutor executor;
    ProgressDialog pbProgress;
    Calendar todaysDate;

    public enum MediaTYPE {
        IMAGE
    }

    MediaTYPE mediaTYPESelected;

    private int iAddBusinessHours = 0;
    String rowHours = "rowHours_";
    String llMainTag = "llMain_";
    String llSubTag = "llSub_";
    String etFeatureTag = "etFeature_";
    String etYesNoTag = "etYesNo_";

    String btnAddTag = "btnAdd_";
    String btnDeleteTag = "btnDelete_";

    boolean boolCard = false;
    boolean boolPayPal = false;
    boolean boolMasterCard = false;
    private static final int SELECT_LOCATION = 101;


    String keyName = "";
    String keyValue = "";
    ResponseDetailAdsData adsExtras;
    boolean updatePost = false;

    ArrayList<AdType> adTypes = new ArrayList<>();

    double featurePrice = 0;
    double highlightedPrice = 0;

    boolean isBusinessAccount = false;
    boolean isBusinessAccountPaid = false;
    BusinessSubscriptions businessSubscriptions = null;
    String businessExpiry;
    boolean isExpiredSubscription = false;

    int adIdPayment = 0;
    int adOrderId = 0;
    boolean isPaidAd = false;
    String paymentAmount = "0";
    String currency = "";
    LinearLayout llAdType;
    ConstraintLayout clMainContent;
    LinearLayout llPayment;
    LinearLayout llImages;
    DatePickerDialog dpdEnd;
    boolean toGetPayment = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_post_ad);

        paymentMethod = 1;
        AppController.isStrip = false;
        AppController.isPayPal = false;
        AppController.stripToken = "";

        mContext = this;
        todaysDate = Calendar.getInstance();
        getUserDetail();
        getDataId();

        initToolBar();
        initViews();
        setAdType(1);
        setData();
        callAllCategories();


    }


    public void initToolBar() {

        iToolbar = (View) findViewById(R.id.iToolbar);
        toolbar = (Toolbar) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_back_arrow_small);

        btnToolbarRight = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_right);
        btnToolbarRight.setVisibility(View.GONE);
        btnToolbarRight.setImageResource(R.drawable.ic_share_small);

        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.tv_post_ad));

        btnToolbarBack.setOnClickListener(this);
        btnToolbarRight.setOnClickListener(this);

    }


    public void getDetailAdData() {

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("ad_detail")) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResponseDetailAdsData>() {
                }.getType();
                adsExtras = gson.fromJson(bundle.getString("ad_detail"), type);
                setExtraData();
            }
        }

        if (llAddHours.getChildCount() == 0)
            addBusinessHoursView("", "");
    }

    public void setExtraData() {

        if (adsExtras != null) {

            updatePost = true;
            adId = adsExtras.getId();


            if (adsExtras.getIsActive() == 1) {
                isPaidAd = true;
                toGetPayment = false;
                llAdType.setVisibility(View.GONE);
            } else if (adsExtras.getIsActive() == 0) {
                isPaidAd = true;
                toGetPayment = false;
                llAdType.setVisibility(View.GONE);
                if (adsExtras.getIsFeatured() != 1) {
                    if (!ApplicationUtils.isCurrentDateAfterDate(adsExtras.getEndDate()) && !ApplicationUtils.isSet(adsExtras.getPaymentStartDate())) {
                        llAdType.setVisibility(View.VISIBLE);
                        tvHighlighted.setEnabled(false);
                        tvFeatured.setEnabled(false);
                        tvNone.setEnabled(false);
                        toGetPayment = true;
                        isPaidAd = false;
                        llAdType.setVisibility(View.VISIBLE);
                    }
                }
            }


            if (ApplicationUtils.isSet(adsExtras.getLatitude()))
                lat = Double.parseDouble(adsExtras.getLatitude());

            if (ApplicationUtils.isSet(adsExtras.getLongitude()))
                lng = Double.parseDouble(adsExtras.getLongitude());

            etTitle.setText(adsExtras.getName());

            setCategories();

            if (ApplicationUtils.isSet(adsExtras.getTag()))
                etTagLine.setText(adsExtras.getTag());


            if (ApplicationUtils.isSet(adsExtras.getAdUrl()))
                etWebsite.setText(adsExtras.getAdUrl());

            if (ApplicationUtils.isSet(adsExtras.getAdPrice()))
                etPrice.setText(adsExtras.getAdPrice());


            if (ApplicationUtils.isSet(adsExtras.getAdDiscPrice()))
                etDPrice.setText(adsExtras.getAdDiscPrice());


            if (ApplicationUtils.isSet(adsExtras.getAdLocation()))
                etLocation.setText(adsExtras.getAdLocation());


            if (ApplicationUtils.isSet(adsExtras.getStartDate())) {
                mStartDate = ApplicationUtils.formatDateFromDateTime(adsExtras.getStartDate());
                etStartDate.setText(mStartDate);
            }


            if (ApplicationUtils.isSet(adsExtras.getEndDate())) {
                mEndDate = ApplicationUtils.formatDateFromDateTime(adsExtras.getEndDate());
                etEndDate.setText(mEndDate);
            }

            if (adsExtras.getAdService() != null)
                if (ApplicationUtils.isSet(adsExtras.getAdService().toString())) {
                    etServices.setText(adsExtras.getAdService().toString());
                }


            if (adsExtras.getAdLanguages() != null)
                if (ApplicationUtils.isSet(adsExtras.getAdLanguages().toString())) {
                    etLanguages.setText(adsExtras.getAdLanguages().toString());
                }

            if (adsExtras.getDescription() != null)
                if (ApplicationUtils.isSet(adsExtras.getDescription().toString())) {
                    etDesc.setText(adsExtras.getDescription().toString());
                }

            setAdType(adsExtras.getIsFeatured());

            if (adsExtras.getAdsPaymentMethods() != null && adsExtras.getAdsPaymentMethods().size() != 0) {
                for (PaymentMethod paymentMethod : adsExtras.getAdsPaymentMethods()) {


                    if (paymentMethod.getMethodId() == 3)
                        setCardPayment();
                    if (paymentMethod.getMethodId() == 4)
                        setPayPalPayment();
                    if (paymentMethod.getMethodId() == 5)
                        setMasterCardPayment();
                }
            }

            if (adsExtras.getAdsImages() != null && adsExtras.getAdsImages().size() != 0) {

                for (GalleryResponseDataDetail responseDataDetail : adsExtras.getAdsImages()) {
                    GalleryModel galleryModel = new GalleryModel(responseDataDetail.getFullImage(), responseDataDetail.getImage(), responseDataDetail.getId());
                    galleryModel.setUploaded(true);
                    mGalleryList.add(galleryModel);
                }
                mGAdapter.notifyDataSetChanged();
                checkIfNeedToAddPlusImageInGallery();
            }
            if (adsExtras.getAdsFeatures() != null && adsExtras.getAdsFeatures().size() != 0) {
                for (AdsFeature adsFeature : adsExtras.getAdsFeatures()) {
                    addBusinessHoursView(adsFeature.getKeyName(), adsFeature.getValue());
                }
            } else {
                addBusinessHoursView("", "");
            }

        }

    }

    public void initViews() {
        clMainContent = findViewById(R.id.clMainContent);
        llAdType = findViewById(R.id.llAdType);
        llPayment = findViewById(R.id.llPayment);
        llImages = findViewById(R.id.llImages);
        nsMainDetail = findViewById(R.id.nsMainDetail);
        etTitle = findViewById(R.id.etTitle);
        spCategories = findViewById(R.id.spCategories);
        spSubCategories = findViewById(R.id.spSubCategories);
        etTagLine = findViewById(R.id.etTagLine);

        etWebsite = findViewById(R.id.etWebsite);

        etLocation = findViewById(R.id.etLocation);
        etPrice = findViewById(R.id.etPrice);

        etDPrice = findViewById(R.id.etDPrice);

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etServices = findViewById(R.id.etServices);

        etLanguages = findViewById(R.id.etLanguages);

        etDesc = findViewById(R.id.etDesc);

        ilServices = findViewById(R.id.ilServices);
        ilLanguages = findViewById(R.id.ilLanguages);
        ilDesc = findViewById(R.id.ilDesc);
        rvImagesList = findViewById(R.id.rvImagesList);

        tvNone = findViewById(R.id.tvNone);
        tvFeatured = findViewById(R.id.tvFeatured);
        tvHighlighted = findViewById(R.id.tvHighlighted);

        tvCard = findViewById(R.id.tvCard);
        tvPayPal = findViewById(R.id.tvPayPal);
        tvMCard = findViewById(R.id.tvMCard);

        progressBar = findViewById(R.id.progressBar);
        llAddHours = findViewById(R.id.llAddHours);


        btnSave = findViewById(R.id.btnSave);

        pbProgress = new ProgressDialog(mContext);
        pbProgress.setMax(100); // Progress Dialog Max Value
        pbProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
        pbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_states));
        pbProgress.setCancelable(false);


    }

    public void getUserDetail() {
        userDetail = SharedPreference.getUserData(mContext);
        userId = userDetail.getId();

        if (ApplicationUtils.isSet(SharedPreference.getSharedPrefValue(mContext, Constants.USERDEFAULT_ADSTYPES))) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AdType>>() {
            }.getType();
            adTypes = gson.fromJson(SharedPreference.getSharedPrefValue(mContext, Constants.USERDEFAULT_ADSTYPES), type);
            featurePrice = adTypes.get(1).getAdFee();
            highlightedPrice = adTypes.get(2).getAdFee();
        }


        currency = SharedPreference.getSharedPrefValue(mContext, Constants.USERDEFAULT_CURRENCY);

        if (userDetail.getBusiness() != null && userDetail.getBusiness().size() != 0)
            getBusinessDetails(userDetail.getBusiness().get(0));

    }

    public void getBusinessDetails(BusinessDetails businessDetails) {

        isBusinessAccount = false;
        isBusinessAccountPaid = false;

        if (businessDetails.getIs_business() == 1) {
            isBusinessAccount = true;
            if (businessDetails.getPaymentStatus().equals("paid")) {
                isBusinessAccountPaid = true;
                if (isBusinessAccount && isBusinessAccountPaid) {
                    if (businessDetails.getBusiness_subscriptions() != null) {
                        businessSubscriptions = businessDetails.getBusiness_subscriptions();
                        if (ApplicationUtils.isSet(businessSubscriptions.getExpiredAt())) {
                            businessExpiry = businessSubscriptions.getExpiredAt();
                            isExpiredSubscription = ApplicationUtils.isUserSubscriptionExpired(businessExpiry);
                        }
                    }
                }
            }
        }

    }

    boolean toSelect = false;

    public void getDataId() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            adId = bundle.getInt("ad_id");
        }
    }

    public void setData() {

        etPrice.addTextChangedListener(priceWatcher);
        etDPrice.addTextChangedListener(dPriceWatcher);
        etServices.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (etServices.hasFocus()) {

                    if (ApplicationUtils.isSet(etServices.getText().toString())) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        etDesc.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {


                if (etDesc.hasFocus()) {
                    if (ApplicationUtils.isSet(etDesc.getText().toString())) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        etLanguages.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (etDesc.hasFocus()) {
                    if (ApplicationUtils.isSet(etDesc.getText().toString())) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        tvNone.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_select_small, 0, 0, 0);

        mLinearLayoutManager = new GridLayoutManager(mContext, 3);
        rvImagesList.setLayoutManager(mLinearLayoutManager);
        mGAdapter = new ImagePostAdAdapter(mContext, mGalleryList);
        checkIfNeedToAddPlusImageInGallery();
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._4sdp);
        rvImagesList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvImagesList.setAdapter(mGAdapter);
        mGAdapter.SetOnItemClickListener(new OnImagePostAdClick() {
            @Override
            public void onDeleteClick(View view, int position) {
                ApplicationUtils.hideKeyboard(mContext);
                removeGalleryItem(position);

            }

            @Override
            public void onItemClick(View view, int position) {
                ApplicationUtils.hideKeyboard(mContext);
                openMediaChooserSheet(MediaTYPE.IMAGE);
            }
        });


        mCategoriesSpinnerAdapter = new CategoriesSpinnerAdapter(mContext, categoriesList);
        Category emptyCategory = new Category();
        emptyCategory.setName(mContext.getResources().getString(R.string.tv_select_category));
        emptyCategory.setNameAr(mContext.getResources().getString(R.string.tv_select_category));
        emptyCategory.setNameDe(mContext.getResources().getString(R.string.tv_select_category));
        emptyCategory.setNameSv(mContext.getResources().getString(R.string.tv_select_category));
        categoriesList.add(emptyCategory);
        Collections.reverse(categoriesList);
        spCategories.setAdapter(mCategoriesSpinnerAdapter);

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    mSelectCategory = categoriesList.get(i);
                    mSelectSubCategory = null;
                    extractSubCategories(categoriesList.get(i), toSelect);
                } else {
                    if (subCategoriesList != null && subCategoriesList.size() != 0) {
                        spSubCategories.setSelection(0);
                        mSubCategoriesSpinnerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ApplicationUtils.hideKeyboard(mContext);
            }
        });


        mSubCategoriesSpinnerAdapter = new SubCategoriesSpinnerAdapter(mContext, subCategoriesList);

        SubCategory emptySubCategory = new SubCategory();
        emptySubCategory.setName(mContext.getResources().getString(R.string.tv_select_sub));
        emptySubCategory.setNameAr(mContext.getResources().getString(R.string.tv_select_sub));
        emptySubCategory.setNameDe(mContext.getResources().getString(R.string.tv_select_sub));
        emptySubCategory.setNameSv(mContext.getResources().getString(R.string.tv_select_sub));
        subCategoriesList.add(emptySubCategory);
        Collections.reverse(subCategoriesList);

        spSubCategories.setAdapter(mSubCategoriesSpinnerAdapter);
        spSubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0)
                    mSelectSubCategory = subCategoriesList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ApplicationUtils.hideKeyboard(mContext);
            }
        });

        etLocation.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);

        tvCard.setOnClickListener(this);
        tvPayPal.setOnClickListener(this);
        tvMCard.setOnClickListener(this);

        tvNone.setOnClickListener(this);
        tvFeatured.setOnClickListener(this);
        tvHighlighted.setOnClickListener(this);
        clMainContent.setOnClickListener(this);
        clMainContent.setOnClickListener(this);
        llAdType.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        llImages.setOnClickListener(this);
    }

    public void removeGalleryItem(int position) { //removes the row

        GalleryModel galleryModelToRemove = mGalleryList.get(position);
        if (!galleryModelToRemove.isUploaded() && galleryModelToRemove.getId() != -1) {
            String getFilePath = mGalleryList.get(position).getPath();
            try {
                File file = new File(getFilePath);
                ApplicationUtils.removeFileFromDisk(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mDeletedImages.add(galleryModelToRemove.getId() + "");
        }
        mGalleryList.remove(position);
        mGAdapter.notifyItemRemoved(position);
        mGAdapter.notifyDataSetChanged();
        checkIfNeedToAddPlusImageInGallery();
    }

    public void checkIfNeedToAddPlusImageInGallery() {

        if (mGalleryList != null && mGalleryList.size() != 0) {
            if (mGalleryList.size() > 5 && mGalleryList.get(0).getId() == -1)
                mGalleryList.remove(0);
            else if (mGalleryList.get(0).getId() != -1)
                addPlusImage();
        } else {
            addPlusImage();
        }

        mGAdapter.notifyDataSetChanged();
    }

    public void addPlusImage() {
        GalleryModel galleryModel = new GalleryModel("", "", -1);
        mGalleryList.add(0, galleryModel);
    }


    /*********************************************************/

    public void callAllCategories() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {

            showProgressBar(true);
            getAllCategories();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void getAllCategories() {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getAllCategories(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call,
                                   final retrofit2.Response<AllCategoriesResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getCategories() != null && response.body().getData().getCategories().size() != 0) {
                            categoriesList.addAll(response.body().getData().getCategories());
                            mCategoriesSpinnerAdapter.notifyDataSetChanged();
                            mSubCategoriesSpinnerAdapter.notifyDataSetChanged();


                            getBusinessUserDetails();
                        } else {

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
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {
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

    public void extractSubCategories(Category categorySelected, boolean toSelectt) {


        subCategoriesList.clear();

        SubCategory emptySubCategory = new SubCategory();
        emptySubCategory.setName(mContext.getResources().getString(R.string.tv_select_sub));
        subCategoriesList.add(emptySubCategory);
        Collections.reverse(subCategoriesList);

        for (Category category : categoriesList) {
            if (categorySelected.getId() == category.getId())
                if (category.getSubCategories() != null && category.getSubCategories().size() != 0) {
                    subCategoriesList.addAll(category.getSubCategories());
                    break;
                }
        }


        if (subCategoriesList != null && subCategoriesList.size() != 0 && !toSelectt) {
            spSubCategories.setSelection(0);
        }

        toSelect = false;

        mSubCategoriesSpinnerAdapter.notifyDataSetChanged();

    }


    private boolean validateTitle() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_title_required));
            requestFocus(etTitle);
            return false;
        }
        return true;
    }

    private boolean validateCategory() {
        if (mSelectCategory == null) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_category_required));
            requestFocus(spCategories);
            return false;
        }
        return true;
    }

    private boolean validateSubCategory() {

        if (mSelectCategory != null) {
            if (mSelectCategory.getSubCategories() != null && mSelectCategory.getSubCategories().size() != 0) {
                if (mSelectSubCategory == null) {
                    DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_category_sub_required));
                    requestFocus(spCategories);
                    return false;
                }
            } else {
                DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_category_sub_required));
                requestFocus(spCategories);
                return false;
            }

        } else {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_category_sub_required));
            requestFocus(spCategories);
            return false;
        }
        return true;
    }


    private boolean validateTag() {
        if (etTagLine.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_tagline_required));
            requestFocus(etTagLine);
            return false;
        }
        return true;
    }

    private boolean validateWebsite() {
        if (etWebsite.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_website_required));
            requestFocus(etWebsite);
            return false;
        }
        return true;
    }

    private boolean validateWebsiteFormat() {
        if (Patterns.WEB_URL.matcher(etWebsite.getText().toString()).matches()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_website_valid_required));
            requestFocus(etWebsite);
            return false;
        }
        return true;
    }

    private boolean validateDesc() {
        if (etDesc.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_desc_required));
            requestFocus(etDesc);
            return false;
        }
        return true;
    }

    private boolean validatePrice() {
        if (etPrice.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_price_required));
            requestFocus(etPrice);
            return false;
        }
        return true;
    }


    private boolean validateDiscountPrice() {
        if (etDPrice.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_dPrice_required));
            requestFocus(etDPrice);
            return false;
        }
        return true;
    }


    private boolean validateServices() {
        if (etServices.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_service_required));
            requestFocus(etServices);
            return false;
        }
        return true;
    }


    private boolean validateLanguages() {
        if (etLanguages.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_languages_required));
            requestFocus(etLanguages);
            return false;
        }
        return true;
    }


    private boolean validateStartDate() {
        if (etStartDate.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_start_date_required));
            requestFocus(etStartDate);
            return false;
        } else if (etStartDate.getText().toString().trim().equalsIgnoreCase(mContext.getResources().getString(R.string.tv_start_date))) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_start_date_required));
            requestFocus(etStartDate);
            return false;
        }
        return true;
    }


    private boolean validateLocation() {

        if (!ApplicationUtils.isSet(etLocation.getText().toString().trim())) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_location_required));
            requestFocus(etLocation);
            return false;
        } else if (etLocation.getText().toString().trim().equalsIgnoreCase(mContext.getResources().getString(R.string.tv_location))) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_select_location_required));
            requestFocus(etLocation);
            return false;
        }
        return true;
    }

    public boolean validateBusinessHours() {

        if (llAddHours.getChildCount() != 0) {
            for (int i = 0; i < llAddHours.getChildCount(); i++) {

                View rowView = llAddHours.getChildAt(i);
                final EditText etFeature = (EditText) rowView.findViewById(R.id.etFeature);
                final EditText etYesNo = (EditText) rowView.findViewById(R.id.etYesNo);

                if (!validateFeatureName(etFeature))
                    return false;

                if (!validateFeatureYesNo(etYesNo))
                    return false;

            }
        }
        return true;

    }


    private boolean validateFeatureName(EditText etFeature) {
        if (etFeature.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_feature_required));
            requestFocus(etFeature);
            return false;
        }
        return true;
    }


    private boolean validateFeatureYesNo(EditText etYesNo) {
        if (etYesNo.getText().toString().trim().isEmpty()) {
            DialogFactory.showDropDownNotificationError(mContext, mContext.getString(R.string.alert_information), mContext.getString(R.string.error_feature_required));
            requestFocus(etYesNo);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showEndDatePicker() {
        Calendar nowDate = Calendar.getInstance();


        if (ApplicationUtils.isSet(mEndDate)) {
            nowDate = ApplicationUtils.convertStrDateToCalendar(mEndDate);
        }
        dpdEnd = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        mEndDay = dayOfMonth;
                        mEndMonth = month;
                        mEndYear = year;

                        if (ApplicationUtils.isCurrentDateAfterDate(ApplicationUtils.get30DaysToDate(year + "-" + month + "-" + dayOfMonth, true, 30))) {

                            DialogFactory.showDropDownNotificationError(mContext,
                                    mContext.getString(R.string.alert_information),
                                    mContext.getString(R.string.msg_error_end_date));
                            dpdEnd.dismiss();
                            return;
                        }

                        mEndDate = year + "-" + month + "-" + dayOfMonth;
                        if (isEndDateClicked) {
                            mStartDate = ApplicationUtils.get30DaysToDate(mEndDate, true, 30);
                            etEndDate.setText(ApplicationUtils.formatDateFromDate(mEndDate));
                            etStartDate.setText(mStartDate);
                            isEndDateClicked = false;
                        }
                    }
                },
                nowDate.get(Calendar.YEAR),
                nowDate.get(Calendar.MONTH),
                nowDate.get(Calendar.DAY_OF_MONTH)
        );


        long nowDateCal = todaysDate.getTimeInMillis();
        dpdEnd.getDatePicker().setMinDate(nowDateCal);
        dpdEnd.show();
    }

    private void showStartDatePicker() {
        Calendar nowDate = Calendar.getInstance();


        if (ApplicationUtils.isSet(mStartDate)) {
            nowDate = ApplicationUtils.convertStrDateToCalendar(mStartDate);
        }

        DatePickerDialog dpd = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;

                        mStartDay = dayOfMonth;
                        mStartMonth = month;
                        mStartYear = year;
                        mStartDate = year + "-" + month + "-" + dayOfMonth;
                        if (isStartDateClicked) {
                            mEndDate = ApplicationUtils.get30DaysToDate(mStartDate, false, 30);
                            etEndDate.setText(mEndDate);
                            etStartDate.setText(ApplicationUtils.formatDateFromDate(mStartDate));
                            isStartDateClicked = false;
                        }
                    }
                },

                nowDate.get(Calendar.YEAR),
                nowDate.get(Calendar.MONTH),
                nowDate.get(Calendar.DAY_OF_MONTH)
        );


        long nowDateCal = todaysDate.getTimeInMillis();
        dpd.getDatePicker().setMinDate(nowDateCal);


        dpd.show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.clMainContent:
            case R.id.llAdType:
            case R.id.llPayment:
            case R.id.llImages:
                ApplicationUtils.hideKeyboard(mContext);
                break;
            case R.id.btn_toolbar_back:
                ApplicationUtils.hideKeyboard(mContext);
                finish();
                break;
            case R.id.etStartDate:
                ApplicationUtils.hideKeyboard(mContext);
                isStartDateClicked = true;
                showStartDatePicker();
                break;
            case R.id.etEndDate:
                ApplicationUtils.hideKeyboard(mContext);
                isEndDateClicked = true;
                showEndDatePicker();
                break;
            case R.id.etLocation:
                ApplicationUtils.hideKeyboard(mContext);
                Intent getLocation = new Intent(mContext, MapsActivity.class);
                getLocation.putExtra("address", etLocation.getText().toString());
                getLocation.putExtra("lat", lat);
                getLocation.putExtra("lon", lng);
                startActivityForResult(getLocation, SELECT_LOCATION);
                break;
            case R.id.tvCard:
                ApplicationUtils.hideKeyboard(mContext);
                setCardPayment();
                break;
            case R.id.tvPayPal:
                ApplicationUtils.hideKeyboard(mContext);
                setPayPalPayment();
                break;
            case R.id.tvMCard:
                ApplicationUtils.hideKeyboard(mContext);
                setMasterCardPayment();
                break;
            case R.id.tvNone:
                ApplicationUtils.hideKeyboard(mContext);
                setAdType(1);
                break;
            case R.id.tvFeatured:
                ApplicationUtils.hideKeyboard(mContext);
                setAdType(2);
                break;
            case R.id.tvHighlighted:
                ApplicationUtils.hideKeyboard(mContext);
                setAdType(3);
                break;
            case R.id.btnSave:
                ApplicationUtils.hideKeyboard(mContext);
                postForm();
                break;
        }

    }


    /****************************************************************************************/


    public void addBusinessHoursView(String keyName, String keyVal) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.item_add_business_hours, null);

        final LinearLayout llMain = (LinearLayout) rowView.findViewById(R.id.llMain);
        final LinearLayout llSub = (LinearLayout) rowView.findViewById(R.id.llSub);
        final EditText etFeature = (EditText) rowView.findViewById(R.id.etFeature);
        final EditText etYesNo = (EditText) rowView.findViewById(R.id.etYesNo);
        final ImageButton btnAddDelete = (ImageButton) rowView.findViewById(R.id.btnAddDelete);

        if (ApplicationUtils.isSet(keyName) && ApplicationUtils.isSet(keyVal)) {
            etFeature.setText(keyName);
            etYesNo.setText(keyVal);
        }


        llMain.setTag(llMainTag + iAddBusinessHours);
        llSub.setTag(llSubTag + iAddBusinessHours);
        etFeature.setTag(etFeatureTag + iAddBusinessHours);
        etYesNo.setTag(etYesNoTag + iAddBusinessHours);
        btnAddDelete.setTag(btnAddTag + iAddBusinessHours);
        rowView.setTag(rowHours + iAddBusinessHours);

        btnAddDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(mContext);
                String vTag = v.getTag().toString().split("_")[0];
                int vPosition = Integer.parseInt(v.getTag().toString().split("_")[1]);

                if (vTag.equals("btnAdd")) {
                    addBusinessHoursView("", "");
                } else {
                    iAddBusinessHours--;
                    llAddHours.removeViewAt(vPosition);
                    afterDeleteHoursRow();
                }
            }
        });

        llAddHours.addView(rowView);
        iAddBusinessHours++;
        makeLastAdd();
    }

    public void afterDeleteHoursRow() {

        if (llAddHours.getChildCount() != 0) {
            for (int i = 0; i < llAddHours.getChildCount(); i++) {

                View rowView = llAddHours.getChildAt(i);
                final LinearLayout llMain = (LinearLayout) rowView.findViewById(R.id.llMain);
                final LinearLayout llSub = (LinearLayout) rowView.findViewById(R.id.llSub);
                final EditText etFeature = (EditText) rowView.findViewById(R.id.etFeature);
                final EditText etYesNo = (EditText) rowView.findViewById(R.id.etYesNo);
                final ImageButton btnAddDelete = (ImageButton) rowView.findViewById(R.id.btnAddDelete);

                llMain.setTag(llMainTag + i);
                llSub.setTag(llSubTag + i);
                etFeature.setTag(etFeatureTag + i);
                etYesNo.setTag(etYesNoTag + i);
                btnAddDelete.setTag(btnAddDelete.getTag().toString().split("_")[0] + "_" + i);
                rowView.setTag(rowHours + i);
            }
        }
    }

    public void makeLastAdd() {


        if (llAddHours.getChildCount() != 0) {
            int targetAdd = llAddHours.getChildCount() - 1;

            for (int i = 0; i < llAddHours.getChildCount(); i++) {
                if (targetAdd == i) {
                    ImageButton ibYes = llAddHours.findViewWithTag(btnAddTag + i);
                    EditText et = llAddHours.findViewWithTag(etYesNoTag + i);
                    if (ibYes != null) {
                        ibYes.setImageResource(R.drawable.ic_blue_bg_add_small);
                        ibYes.setTag(btnAddTag + i);
                    }
                    if (et != null) {
                        et.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    }
                } else {
                    ImageButton ibNo = llAddHours.findViewWithTag(btnAddTag + i);
                    EditText et = llAddHours.findViewWithTag(etYesNoTag + i);
                    if (ibNo != null) {
                        ibNo.setTag(btnDeleteTag + i);
                        ibNo.setImageResource(R.drawable.ic_delete_small);
                    }

                    if (et != null) {
                        et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    }
                }
            }
        }
    }


    public void getBusinessHours() {
        keyName = "";
        keyValue = "";
        if (llAddHours.getChildCount() != 0) {
            for (int i = 0; i < llAddHours.getChildCount(); i++) {

                View rowView = llAddHours.getChildAt(i);
                final EditText etFeature = (EditText) rowView.findViewById(R.id.etFeature);
                final EditText etYesNo = (EditText) rowView.findViewById(R.id.etYesNo);
                keyName = etFeature.getText().toString() + "," + keyName;
                keyValue = etYesNo.getText().toString() + "," + keyValue;
            }
        }
        if (ApplicationUtils.isSet(keyName) && ApplicationUtils.isSet(keyValue)) {
            keyName = removeLastChar(keyName);
            keyValue = removeLastChar(keyValue);
        }

    }

    public String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    /***************************************************************/
    /***********************************************************************/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


            case REQUEST_CODE_PAYMENT:

                if (AppController.isPayPal || AppController.isStrip) {
                    createRequestData(false);
                }
                break;
            case SELECT_LOCATION:
                if (data != null) {
                    etLocation.setText("");
                    if (ApplicationUtils.isSet(data.getStringExtra("address")))
                        etLocation.setText(data.getStringExtra("address"));
                    lat = data.getDoubleExtra("lat", 0.0);
                    lng = data.getDoubleExtra("lon", 0.0);
                }
                break;
            default:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {
                        // You can update this bitmap to your server


                        // loading profile image from local cache
                        imagePath = uri.toString();

                        GalleryModel model = new GalleryModel(imagePath, "Image", 0);
                        mGalleryList.add(model);
                        mGAdapter.notifyDataSetChanged();
                        checkIfNeedToAddPlusImageInGallery();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }


    }


    private void openMediaChooserSheet(final MediaTYPE mediaTYPE) {
        ApplicationUtils.hideKeyboard(mContext);
        mediaTYPESelected = mediaTYPE;
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mContext);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_upload_image, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        TextView tvCamera = (TextView) sheetView.findViewById(R.id.tvOpenCamera);
        TextView tvGallery = (TextView) sheetView.findViewById(R.id.tvGallery);
        Button btnCancel = (Button) sheetView.findViewById(R.id.btnCancel);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                openImageCamera();

            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                openImageGallery();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });
        mBottomSheetDialog.show();
    }

    //Gallery image selection
    public void openImageGallery() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    int REQUEST_IMAGE = 300;

    //Camera Capturing
    public void openImageCamera() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }


    /*****************************************************************************************************/


    private void postForm() {

        if (!validateTitle()) {
            return;
        }

        if (!validateCategory()) {
            return;
        }

        if (!validateSubCategory()) {
            return;
        }

        if (!validateTag()) {
            return;
        }

   /*     if (!validateWebsite()) {
            return;
        }*/

/*
        if (!validateWebsiteFormat()) {
            return;
        }
*/

        if (!validateLocation()) {
            return;
        }

        if (!validatePrice()) {
            return;
        }


        if (!validateDiscountPrice()) {
            return;
        }

        if (!validateStartDate()) {
            return;
        }

        if (!validateServices()) {
            return;
        }

        if (!validateLanguages()) {
            return;
        }

        if (!validateDesc()) {
            return;
        }

        if (!validateBusinessHours()) {
            return;
        }


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


        if (!isBusinessAccount) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.msg_no_business_account));
            return;
        }

        if (!isBusinessAccountPaid) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.msg_business_no_paid));
            return;
        }

        if (isExpiredSubscription) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.msg_business_subscription_expired));
            return;
        }


        try {


            getBusinessHours();


            createRequestData(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    RequestBody website;
    RequestBody paymentMethodBody;
    RequestBody stripToken;
    RequestBody isPaidStrip;

    boolean isCheck = false;

    private void createRequestData(boolean isCheckApp) {
        try {

            isCheck = isCheckApp;
            final RequestBody adsId = RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(adId));
            final RequestBody title = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etTitle.getText().toString());

            final RequestBody category = RequestBody.create(okhttp3.MediaType.parse("text/plain"), mSelectCategory.getId() + "");

            final RequestBody subCategory;
            if (mSelectSubCategory != null)
                subCategory = RequestBody.create(okhttp3.MediaType.parse("text/plain"), mSelectSubCategory.getId() + "");
            else
                subCategory = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "0");


            final RequestBody services = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etServices.getText().toString());
            final RequestBody languages = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etLanguages.getText().toString());
            final RequestBody description = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etDesc.getText().toString());


            final RequestBody tagline = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etTagLine.getText().toString());

            website = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");
            if (ApplicationUtils.isSet(etWebsite.getText().toString()))
                website = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etWebsite.getText().toString());

            final RequestBody price = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etPrice.getText().toString());
            final RequestBody dPrice = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etDPrice.getText().toString());

            final RequestBody Lat;
            final RequestBody Lon;

            Lat = RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(lat));
            Lon = RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(lng));


            final RequestBody sDate = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etStartDate.getText().toString());
            final RequestBody eDate = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etEndDate.getText().toString());

            String payments = "";

            if (boolCard)
                payments = "3" + "," + payments;
            if (boolPayPal)
                payments = "4" + "," + payments;
            if (boolMasterCard)
                payments = "5" + "," + payments;

            final RequestBody paymentsBody;

            if (ApplicationUtils.isSet(payments))
                paymentsBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), removeLastChar(payments));
            else
                paymentsBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");

            final RequestBody adTypeBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(adType));
            final RequestBody keyNameBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), keyName);
            final RequestBody keyValueBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), keyValue);

            final RequestBody adLocation = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etLocation.getText().toString());

            int ivCount = 0;

            if (mGalleryList != null && mGalleryList.size() != 0)
                for (GalleryModel model : mGalleryList)
                    if (model.getId() != -1 && !model.isUploaded())
                        ivCount = ivCount + 1;

            final MultipartBody.Part[] imageParts = new MultipartBody.Part[ivCount];
            if (ivCount != 0) {
                ivCount = 0;
                for (int index = 0; index < mGalleryList.size(); index++) {
                    if (mGalleryList.get(index).getId() != -1 && !mGalleryList.get(index).isUploaded()) {
                        File file = new File(new URI(mGalleryList.get(index).getPath()));
                        ProgressRequestBody imageProgress = new ProgressRequestBody(file, "image", this);
                        imageParts[ivCount++] = MultipartBody.Part.createFormData("ad_image[]", file.getName(), imageProgress);
                    }
                }
            }


            final RequestBody mDeletedImagesBody;

            if (mDeletedImages != null && mDeletedImages.size() != 0) {
                mDeletedImagesBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), android.text.TextUtils.join(",", mDeletedImages));
            } else
                mDeletedImagesBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");


            paymentMethodBody = RequestBody.create(MediaType.parse("text/plain"), "paypal");
            stripToken = RequestBody.create(MediaType.parse("text/plain"), "");
            isPaidStrip = RequestBody.create(MediaType.parse("text/plain"), "0");


            if (isCheck && (adType == 2 || adType == 3)) {
                if ((!AppController.isPayPal && !AppController.isStrip) || adTypeForPayment != adType) {
                    adTypeForPayment = adType;
                    paymentMethod = 1;
                    AppController.isStrip = false;
                    AppController.isPayPal = false;
                    AppController.stripToken = "";
                }
                if (adType == 2 || adType == 3 && !isPaidAd) {
                    if (!isPaidAd) paymentBottomSheet();
                } else {
                    isCheck = false;
                }
            }


            if (!isCheck) {

                int ivCountImage = 0;

                if (mGalleryList != null && mGalleryList.size() != 0)
                    for (GalleryModel model : mGalleryList)
                        if (model.getId() != -1 && !model.isUploaded())
                            ivCountImage = ivCountImage + 1;

                if (ivCountImage != 0) {

                    pbProgress.setProgress(0);
                    pbProgress.setMessage(mContext.getResources().getString(R.string.alert_finish_uploading));
                    pbProgress.show();
                } else {

                    showProgressBar(true);
                }
            }


            executor = new ThreadPoolExecutor(
                    5,
                    10,
                    30,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
            );
            executor.allowCoreThreadTimeOut(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (!isCheck) {

                            if ((AppController.isStrip || AppController.isPayPal) && !isPaidAd) {

                                if (AppController.isPayPal) {
                                    paymentMethodBody = RequestBody.create(MediaType.parse("text/plain"), "paypal");
                                    isPaidStrip = RequestBody.create(MediaType.parse("text/plain"), "1");
                                } else if (AppController.isStrip) {
                                    stripToken = RequestBody.create(MediaType.parse("text/plain"), AppController.stripToken);
                                    paymentMethodBody = RequestBody.create(MediaType.parse("text/plain"), "credit_card");

                                }

                                postAdRequestApiCall(updatePost, adsId, title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                                        keyNameBody, keyValueBody,
                                        paymentsBody, adTypeBody, imageParts, adLocation, mDeletedImagesBody, true, paymentMethodBody, stripToken, isPaidStrip
                                );

                            } else {

                                postAdRequestApiCall(updatePost, adsId, title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                                        keyNameBody, keyValueBody,
                                        paymentsBody, adTypeBody, imageParts, adLocation, mDeletedImagesBody, false, paymentMethodBody, stripToken, isPaidStrip
                                );
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void postAdRequestApiCall(boolean updatePost, final RequestBody adId,
                                      RequestBody title, RequestBody category, RequestBody subCategory, RequestBody
                                              tagline, RequestBody website, RequestBody Lat, RequestBody Lon, RequestBody
                                              price, RequestBody dPrice, RequestBody sDate,
                                      RequestBody eDate, RequestBody services,
                                      RequestBody languages, RequestBody description,
                                      RequestBody keyNameBody, RequestBody keyValueBody,
                                      RequestBody paymentsBody, RequestBody adTypeBody, MultipartBody.Part[]
                                              imageParts, RequestBody adLocation, RequestBody itemsDeleted, boolean isPayment, RequestBody paymentMethod, RequestBody stripToken, RequestBody isPaid
    ) {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.postAd(updatePost, adId, title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                keyNameBody, keyValueBody,
                paymentsBody, adTypeBody, imageParts, adLocation, itemsDeleted,
                isPayment, paymentMethod, stripToken, isPaid,

                new Callback<PostAdResponse>() {
                    @Override
                    public void onResponse(Call<PostAdResponse> call, final Response<PostAdResponse> response) {
                        try {
                            btnSave.setEnabled(true);
                            pbProgress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus()) {


                                DialogFactory.showDropDownNotificationSuccess(
                                        mContext,
                                        mContext.getString(R.string.alert_success),
                                        response.body().getMessage());

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (response.body().getData().getOrder() != null) {
                                            AdsOders adsOders = response.body().getData().getOrder();
                                         /* adIdPayment = adsOders.getAdId();
                                            adOrderId = adsOders.getId();
                                            paymentAmount = adsOders.getAmount();
                                            isPaidAd = false;
                                            if (adsOders.getStatus().equalsIgnoreCase("paid"))
                                                isPaidAd = true;
*/
                                            if (isFromAddListFragment)
                                                toRefreshAddListFragment = true;

                                            Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                                            detailIntent.putExtra("ad_id", adsOders.getAdId());
                                            startActivity(detailIntent);
                                            finish();

                                        } else {
                                            Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                                            detailIntent.putExtra("ad_id", response.body().getData().getData().getId());
                                            startActivity(detailIntent);
                                            finish();
                                        }


                                    }
                                }, 1500);

                            } else {

                                if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {

                                    DialogFactory.showDropDownNotificationError(
                                            mContext,
                                            mContext.getString(R.string.alert_information),
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
                                        mContext.getString(R.string.alert_information),
                                        mContext.getString(R.string.alert_internal_server_error));

                        }
                    }

                    @Override
                    public void onFailure(Call<PostAdResponse> call, Throwable t) {
                        try {
                            btnSave.setEnabled(true);
                            pbProgress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (t instanceof IOException)
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_information),
                                    mContext.getString(R.string.alert_api_not_found));
                        else if (t instanceof SocketTimeoutException)
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_information),
                                    mContext.getString(R.string.alert_request_timeout));
                        else
                            DialogFactory.showDropDownNotificationError(
                                    mContext,
                                    mContext.getString(R.string.alert_information),
                                    t.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void onProgressUpdate(int percentage) {
        pbProgress.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        // do something on upload finished
        // for example start next uploading at queue
        pbProgress.setProgress(100);
        pbProgress.dismiss(); // Dismiss Progress Dialog
    }

    /********************************************************************************/


    public void setAdType(int type) {
        switch (type) {
            case 1:
                adType = 1;
                tvNone.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_select_small, 0, 0, 0);
                tvFeatured.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_unselect_small, 0, 0, 0);
                tvHighlighted.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_unselect_small, 0, 0, 0);

                tvFeatured.setText(mContext.getResources().getString(R.string.tv_featured_price) + " (" + currency + adTypes.get(1).getAdFee() + ")");
                tvHighlighted.setText(mContext.getResources().getString(R.string.tv_highlighted) + " (" + currency + adTypes.get(2).getAdFee() + ")");
                break;
            case 2:
                adType = 2;//Fe
                tvNone.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_unselect_small, 0, 0, 0);
                tvFeatured.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_select_small, 0, 0, 0);
                tvHighlighted.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_unselect_small, 0, 0, 0);

                tvFeatured.setText(mContext.getResources().getString(R.string.tv_featured_price)+ " (" + currency + adTypes.get(1).getAdFee() + ")");
                tvHighlighted.setText(mContext.getResources().getString(R.string.tv_highlighted) + " (" + currency + adTypes.get(2).getAdFee() + ")");
                adTypeName = "Featured";
                break;
            case 3:
                adType = 3;
                tvNone.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_unselect_small, 0, 0, 0);
                tvFeatured.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_unselect_small, 0, 0, 0);
                tvHighlighted.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_select_small, 0, 0, 0);
                adTypeName = "Highlighted";
                tvFeatured.setText(mContext.getResources().getString(R.string.tv_featured_price) + " (" + currency + adTypes.get(1).getAdFee() + ")");
                tvHighlighted.setText(mContext.getResources().getString(R.string.tv_highlighted) + " (" + currency + adTypes.get(2).getAdFee() + ")");
                break;
        }
    }

    public void setCardPayment() {
        if (boolCard) {
            boolCard = false;
            tvCard.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_unchecked_box_small, 0, 0, 0);
        } else {
            boolCard = true;
            tvCard.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mark_check_small, 0, 0, 0);
        }
    }

    public void setPayPalPayment() {
        if (boolPayPal) {
            boolPayPal = false;
            tvPayPal.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_unchecked_box_small, 0, 0, 0);
        } else {
            boolPayPal = true;
            tvPayPal.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mark_check_small, 0, 0, 0);
        }
    }

    public void setMasterCardPayment() {
        if (boolMasterCard) {
            boolMasterCard = false;
            tvMCard.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_unchecked_box_small, 0, 0, 0);
        } else {
            boolMasterCard = true;
            tvMCard.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mark_check_small, 0, 0, 0);
        }
    }


    /***********************************************************************************/


    public void callPaypalPayment(String amount, String itemName) {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, amount, itemName);
     /*   PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(String.valueOf(1.26)), "GBP", "people",
                PayPalPayment.PAYMENT_INTENT_AUTHORIZE);*/
        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(mContext, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paymentConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent, String amount, String itemName) {
        return new PayPalPayment(new BigDecimal(amount), "USD", itemName,
                paymentIntent);
    }


    private void postAdPaymentInfo(final String adId, String amount, String orderId) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.postAdPayments(adId, amount, orderId, new Callback<PostAdResponse>() {
            @Override
            public void onResponse(Call<PostAdResponse> call,
                                   final retrofit2.Response<PostAdResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {

                        DialogFactory.showDropDownNotificationSuccess(
                                mContext,
                                mContext.getString(R.string.alert_success),
                                response.body().getMessage());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (isFromAddListFragment)
                                    toRefreshAddListFragment = true;

                                Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                                detailIntent.putExtra("ad_id", Integer.parseInt(adId));
                                startActivity(detailIntent);
                                finish();

                            }
                        }, 1500);
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
            public void onFailure(Call<PostAdResponse> call, Throwable t) {
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


    private void getBusinessUserDetails() {

        String token = SharedPreference.getSharedPrefValue(mContext, Constants.USER_TOKEN);
        token = "Bearer " + token;
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        Call<BusinessProfileResponse> call = api.getUserBusinesProfile(token);

        call.enqueue(new Callback<BusinessProfileResponse>() {
            @Override
            public void onResponse(Call<BusinessProfileResponse> call,
                                   final Response<BusinessProfileResponse> response) {

                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getData() != null) {


                            BusinessDetails user = response.body().getData().getData();
                            if (user != null) {
                                getBusinessDetails(user);
                            }

                            getDetailAdData();

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

                setCategories();
            }

            @Override
            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {
                setCategories();
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

    public void setCategories() {

        if (adsExtras != null) {

            if (categoriesList != null && categoriesList.size() != 0) {

                int catIndex = 0;
                for (Category category : categoriesList) {
                    if (adsExtras.getCategoryId() == category.getId()) {
                        spCategories.setSelection(catIndex);
                        mSelectCategory = category;

                        if (category.getSubCategories() != null && category.getSubCategories().size() != 0) {
                            int subCatIndex = 0;

                            for (SubCategory subCategory : subCategoriesList) {

                                if (adsExtras.getSubCategoryId() == subCategory.getId()) {
                                    toSelect = true;

                                    spSubCategories.setSelection(subCatIndex);
                                    mSelectSubCategory = subCategory;
                                    break;
                                }
                                subCatIndex++;
                            }
                        }
                        break;
                    }
                    catIndex++;
                }
            }

        }


    }

    public void paymentFailure(String msg) {

        DialogFactory.showDropDownNotificationError(mContext,
                mContext.getString(R.string.alert_information),
                msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isFromAddListFragment)
                    toRefreshAddListFragment = true;

                Intent detailIntent = new Intent(mContext, DetailAdsActivity.class);
                detailIntent.putExtra("ad_id", adIdPayment);
                startActivity(detailIntent);
                finish();

            }
        }, 1500);
    }

    Timer timerPrice;
    TextWatcher priceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (timerPrice != null)
                timerPrice.cancel();
        }

        @Override
        public void afterTextChanged(Editable s) {
            etPrice.removeTextChangedListener(this);
            final String price = s.toString();
            timerPrice = new Timer();

            timerPrice.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ApplicationUtils.isSet(price) && ApplicationUtils.isSet(etDPrice.getText().toString())) {
                                if (Float.parseFloat(price) <= Float.parseFloat(etDPrice.getText().toString())) {
                                    if (price.length() > 1) {
                                        etPrice.setText(ApplicationUtils.removeLastCharacterFromString(price));
                                        DialogFactory.showDropDownNotificationError(mContext,
                                                mContext.getString(R.string.alert_information),
                                                mContext.getString(R.string.msg_error_price));
                                    } else {
                                        etPrice.setText("");
                                    }
                                    if (ApplicationUtils.isSet(etPrice.getText().toString()))
                                        etPrice.setSelection(etPrice.getText().length());
                                    else
                                        etDPrice.setText("");

                                }
                            }
                        }
                    });
                }
            }, 100);
            etPrice.addTextChangedListener(priceWatcher);
        }
    };

    TextWatcher dPriceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (timerPrice != null)
                timerPrice.cancel();
        }

        @Override
        public void afterTextChanged(Editable s) {
            etDPrice.removeTextChangedListener(this);
            final String price = s.toString();
            timerPrice = new Timer();

            timerPrice.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ApplicationUtils.isSet(price) && ApplicationUtils.isSet(etPrice.getText().toString())) {
                                if (Float.parseFloat(price) >= Float.parseFloat(etPrice.getText().toString())) {

                                    if (price.length() > 1) {
                                        etDPrice.setText(ApplicationUtils.removeLastCharacterFromString(price));

                                        DialogFactory.showDropDownNotificationError(mContext,
                                                mContext.getString(R.string.alert_information),
                                                mContext.getString(R.string.msg_error_discount_price));
                                    } else {
                                        etDPrice.setText("");
                                    }

                                    if (ApplicationUtils.isSet(etDPrice.getText().toString()))
                                        etDPrice.setSelection(etDPrice.getText().length());

                                }
                            }
                        }
                    });


                }
            }, 100);
            etDPrice.addTextChangedListener(dPriceWatcher);
        }
    };

    private void paymentBottomSheet() {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mContext);
        View sheetView = this.getLayoutInflater().inflate(R.layout.logout_bottomsheet_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        TextView tvPayPal = sheetView.findViewById(R.id.tvTitle);
        tvPayPal.setText("Payment via PayPal");
        TextView tvStrip = sheetView.findViewById(R.id.tvLogOut);
        tvStrip.setText("Payment via Strip");
        tvStrip.setTextColor(getResources().getColor(R.color.colorDefaultText));

        Button btnCancel = sheetView.findViewById(R.id.btnCancel);
        tvStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                openStrip();
            }
        });

        tvPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                openPayPal();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });
        mBottomSheetDialog.show();
    }

    int paymentMethod = 1;

    int adTypeForPayment = 0;

    public void openStrip() {
        paymentMethod = 2;
        Intent intent = new Intent(mContext, StripActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    public void openPayPal() {
        paymentMethod = 1;
        Intent intent = new Intent(mContext, SubscriptionsFragment.class);
        intent.putExtra("package_id", "");

        if (adType == 2) {
            intent.putExtra("type", "toplisting_fee");
        } else {
            intent.putExtra("type", "highlight_fee");
        }


        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }


}
