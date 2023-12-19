/*
package com.examples.dalileurope.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.examples.dalileurope.R;
import com.examples.dalileurope.activities.MapsActivity;
import com.examples.dalileurope.adapter.AllCatAdaptor;
import com.examples.dalileurope.adapter.AllSubCatAdaptor;
import com.examples.dalileurope.adapter.BusinessImagesAdaptor;
import com.examples.dalileurope.adapter.CityLisAdaptor;
import com.examples.dalileurope.adapter.CountryListAdaptor;
import com.examples.dalileurope.adapter.StateListAdaptor;
import com.examples.dalileurope.adapter.SubscriptionsAdaptor;

import com.examples.dalileurope.api.retrofit.BusinesCatResponse;
import com.examples.dalileurope.api.retrofit.BusinessCategory;
import com.examples.dalileurope.api.retrofit.BusinessDetails;
import com.examples.dalileurope.api.retrofit.BusinessHour;
import com.examples.dalileurope.api.retrofit.BusinessImage;
import com.examples.dalileurope.api.retrofit.BusinessOrderDetail;
import com.examples.dalileurope.api.retrofit.BusinessProfileResponse;
import com.examples.dalileurope.api.retrofit.CategoryDetails;
import com.examples.dalileurope.api.retrofit.City;
import com.examples.dalileurope.api.retrofit.CityResponse;
import com.examples.dalileurope.api.retrofit.Country;
import com.examples.dalileurope.api.retrofit.CountryResponse;
import com.examples.dalileurope.api.retrofit.State;
import com.examples.dalileurope.api.retrofit.StateResponse;
import com.examples.dalileurope.api.retrofit.Subscriptions;
import com.examples.dalileurope.api.retrofit.SubscriptionsResponse;
import com.examples.dalileurope.interfaces.ActivityListener;
import com.examples.dalileurope.interfaces.OnImagePostAdClick;
import com.examples.dalileurope.network.ApiClient;
import com.examples.dalileurope.network.ApiInterface;
import com.examples.dalileurope.network.RetrofitClient;
import com.examples.dalileurope.utils.ApplicationUtils;
import com.examples.dalileurope.utils.Constants;
import com.examples.dalileurope.utils.DialogFactory;
import com.examples.dalileurope.utils.ProgressRequestBody;
import com.examples.dalileurope.utils.SharedPreference;
import com.examples.dalileurope.utils.SpacesItemDecoration;
import com.examples.dalileurope.utils.URLogs;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.widget.LinearLayout.VERTICAL;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.examples.dalileurope.activities.MainActivity.REQUEST_CODE_PAYMENT;
import static com.examples.dalileurope.activities.MainActivity.paymentConfig;
import static com.examples.dalileurope.utils.ApplicationUtils.showToast;
import static com.examples.dalileurope.activities.MainActivity.REQ_CODE_MAP_BUSINESSFRAGMENT;

public class BusinessProfileFragment extends BaseFragment implements ProgressRequestBody.UploadCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int businessIdPayment = 0;
    int businessOrderId = 0;
    boolean isPaidBusiness = false;
    String paymentAmount = "0";
    String currency = "";
    RecyclerView list;
    int catIdTemp = -1;
    String catNameTemp = "";
    int catId = -1;
    String catName = "";
    AllCatAdaptor allCatAdaptor;
    BusinessCategory businessCategory;


    public BusinessProfileFragment() {
        // Required empty public constructor
    }

*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessProfileFragment.


    // TODO: Rename and change types and number of parameters
    public static BusinessProfileFragment newInstance(String param1, String param2) {
        BusinessProfileFragment fragment = new BusinessProfileFragment();
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

    TextInputLayout inputLayoutBusinessName, inputLayoutCategory, inputLayoutSubCategory, inputLayoutEmail, inputLayoutPhone, inputLayoutAdress,
            inputLayoutCountry, inputLayoutState, inputLayoutCity, inputLayoutZipCode, inputLayoutAboutBusiness,
            inputLayoutWebsite, inputLayoutMonStart, inputLayoutMonEnd, inputLayoutTueStart, inputLayoutTueEnd, inputLayoutWedStart, inputLayoutWedEnd, inputLayoutThuStart, inputLayoutThuEnd, inputLayoutFriStart,
            inputLayoutFriEnd, inputLayoutSatStart, inputLayoutSatEnd, inputLayoutSunStart, inputLayoutSunEnd;
    TextInputEditText etBusinessName, etCategory, etSubCategory, etEmail, etPhone, etAddress, etCountry, etState, etCity, etZipCode, etAboutBusiness,
            etWebiste, etMonStart, etMonEnd, etTueStart, etTueEnd, etWedStart, etWedEnd, etThuStart, etThuEnd, etFriStart, etFriEnd, etSatStart, etSatEnd, etSunStart, etSunEnd;
    Button btnSave;
    ImageView imgProfileIcon;
    View view;
    Activity mActivityContext;
    Context mContext;
    ProgressBar progressBar;

    AppCompatCheckBox acceptCheckBox;
    RecyclerView rvImagesList, rvSubscriptionsList;
    LinearLayout linsubScriptionPlan;

    public Boolean isActive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_profile, container, false);
        mActivityContext = getActivity();
        mContext = view.getContext();

        try {
            listener = (ActivityListener) mActivityContext;
        } catch (ClassCastException castException) {
* The activity does not implement the listener.

        }
        progressBar = view.findViewById(R.id.progressBar);
        transparentStatusBar();
        setStatusBar();
        initToolBar();
        initAndSetViews();

        //////////////////
        pbProgress = new ProgressDialog(mContext);
        pbProgress.setMax(100); // Progress Dialog Max Value
        pbProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
        pbProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_states));
        pbProgress.setCancelable(false);

        return view;


    }

    public void setStatusBar() {
        if (ApplicationUtils.getDeviceStatusBarHeight(getActivity()) > 0) {
            transparentStatusBar();
        }
    }

    // tollbar
    private ImageView btnToolbarBack;
    private TextView tvToolbarTitle;
    RelativeLayout toolbar;
    View iToolbar;
    private ActivityListener listener;

    public void initToolBar() {


        iToolbar = (View) view.findViewById(R.id.iToolbar);
//        toolbar = (RelativeLayout) iToolbar.findViewById(R.id.toolbar);
        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);


        tvToolbarTitle = (TextView) iToolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setVisibility(View.GONE);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.menu_my_ads));


        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_SHORT).show();
                listener.callToolbarBack(true);
            }
        });


    }

    //image recyclerview
    GridLayoutManager mLinearLayoutManager;
    BusinessImagesAdaptor mGAdapter;

    public void initAndSetViews() {
        progressBar = view.findViewById(R.id.progressBar);
        getAllcountries();
        getAllCategories();
        inputLayoutBusinessName = view.findViewById(R.id.inputLayoutBusinessName);
        inputLayoutCategory = view.findViewById(R.id.inputLayoutCategory);
        inputLayoutSubCategory = view.findViewById(R.id.inputLayoutSubCategory);
        inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
        inputLayoutPhone = view.findViewById(R.id.inputLayoutPhone);
        inputLayoutAdress = view.findViewById(R.id.inputLayoutAdress);
        inputLayoutCountry = view.findViewById(R.id.inputLayoutCountry);
        inputLayoutState = view.findViewById(R.id.inputLayoutState);
        inputLayoutCity = view.findViewById(R.id.inputLayoutCity);
        inputLayoutZipCode = view.findViewById(R.id.inputLayoutZipCode);
        inputLayoutAboutBusiness = view.findViewById(R.id.inputLayoutAboutMe);

        inputLayoutWebsite = view.findViewById(R.id.inputLayoutWebsite);
        inputLayoutMonStart = view.findViewById(R.id.inputLayoutMonStart);
        inputLayoutMonEnd = view.findViewById(R.id.inputLayoutMonEnd);
        inputLayoutTueStart = view.findViewById(R.id.inputLayoutTueStart);
        inputLayoutTueEnd = view.findViewById(R.id.inputLayoutTueEnd);
        inputLayoutWedStart = view.findViewById(R.id.inputLayoutWedStart);
        inputLayoutWedEnd = view.findViewById(R.id.inputLayoutWedEnd);
        inputLayoutThuStart = view.findViewById(R.id.inputLayoutThuStart);
        inputLayoutThuEnd = view.findViewById(R.id.inputLayoutThuEnd);
        inputLayoutFriStart = view.findViewById(R.id.inputLayoutFriStart);
        inputLayoutFriEnd = view.findViewById(R.id.inputLayoutFriEnd);
        inputLayoutSatStart = view.findViewById(R.id.inputLayoutSatStart);
        inputLayoutSatEnd = view.findViewById(R.id.inputLayoutSatEnd);
        inputLayoutSunStart = view.findViewById(R.id.inputLayoutSunStart);
        inputLayoutSunEnd = view.findViewById(R.id.inputLayoutSunEnd);


        etBusinessName = view.findViewById(R.id.etBusinessName);
        etCategory = view.findViewById(R.id.etCategory);
        etSubCategory = view.findViewById(R.id.etSubCategory);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);
        etCountry = view.findViewById(R.id.etCountry);
        etState = view.findViewById(R.id.etState);
        etCity = view.findViewById(R.id.etCity);
        etZipCode = view.findViewById(R.id.etZipCode);
        etAboutBusiness = view.findViewById(R.id.etAboutMe);


        etWebiste = view.findViewById(R.id.etWebsite);
        etMonStart = view.findViewById(R.id.etMonStart);
        etMonEnd = view.findViewById(R.id.etMonEnd);
        etTueStart = view.findViewById(R.id.etTueStart);
        etTueEnd = view.findViewById(R.id.etTueEnd);
        etWedStart = view.findViewById(R.id.etWedStart);
        etWedEnd = view.findViewById(R.id.etWedEnd);
        etThuStart = view.findViewById(R.id.etThunStart);
        etThuEnd = view.findViewById(R.id.etThuEnd);
        etFriStart = view.findViewById(R.id.etFriStart);
        etFriEnd = view.findViewById(R.id.etFriEnd);
        etSatStart = view.findViewById(R.id.etSatStart);
        etSatEnd = view.findViewById(R.id.etSatEnd);
        etSunStart = view.findViewById(R.id.etSunStart);
        etSunEnd = view.findViewById(R.id.etSunEnd);

        acceptCheckBox = view.findViewById(R.id.acceptCheckBox);
        rvImagesList = (RecyclerView) view.findViewById(R.id.rvImagesList);

        rvSubscriptionsList = (RecyclerView) view.findViewById(R.id.rvSubscriptionsList);

        linsubScriptionPlan = (LinearLayout) view.findViewById(R.id.linsubScriptionPlan);


        btnSave = view.findViewById(R.id.btnSave);
        imgProfileIcon = (ImageView) view.findViewById(R.id.imgProfileIcon);


        businessImageList = new ArrayList<>();
        mLinearLayoutManager = new GridLayoutManager(mContext, 3);
        rvImagesList.setLayoutManager(mLinearLayoutManager);

        mGAdapter = new BusinessImagesAdaptor(mActivityContext, businessImageList);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._4sdp);
        rvImagesList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvImagesList.setAdapter(mGAdapter);
        mGAdapter.SetOnItemClickListener(new OnImagePostAdClick() {
            @Override
            public void onDeleteClick(View view, int position) {
//                removeGalleryItem(position);
//                checkMediaItems();
                removeGalleryItem(position);
//                Toast.makeText(mActivityContext, position + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(View view, int position) {
//                selectMediaWithRealQuality(PostAdActivity.MediaTYPE.IMAGE);
                whichClicked = 2;
                selectMediaWithRealQuality();
//                Toast.makeText(mActivityContext, position + "", Toast.LENGTH_SHORT).show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        etCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessCategories.size() > 0)
                    showCategoryDialog();
            }
        });

        etSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCategories.size() > 0)
                    showSubCategoryDialog();
            }
        });


        etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countries.size() > 0)
                    showCountryDialog();
            }
        });
        etState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (states.size() > 0)
                    showAllAllStatesDialog();
            }
        });
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cities.size() > 0)
                    showAllCities();
            }
        });
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("address", etAddress.getText().toString());
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                getActivity().startActivityForResult(intent, REQ_CODE_MAP_BUSINESSFRAGMENT);
            }
        });
        imgProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichClicked = 1;
                selectMediaWithRealQuality();
            }
        });


        etMonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etMonStart, etMonStart, etMonEnd, monStartTm, monEndTm, inputLayoutMonStart, inputLayoutMonEnd, "Mon", "s");
            }
        });

        etMonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etMonEnd, etMonStart, etMonEnd, monStartTm, monEndTm, inputLayoutMonStart, inputLayoutMonEnd, "Mon", "e");
            }
        });


        etTueStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etTueStart, etTueStart, etTueEnd, tueStartTm, tueEndTm, inputLayoutTueStart, inputLayoutTueEnd, "Tue", "s");
            }
        });

        etTueEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etTueEnd, etTueStart, etTueEnd, tueStartTm, tueEndTm, inputLayoutTueStart, inputLayoutTueEnd, "Tue", "e");
            }
        });


        etWedStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etWedStart, etWedStart, etWedEnd, wedStartTm, wedEndTm, inputLayoutWedStart, inputLayoutWedEnd, "Wed", "s");
            }
        });

        etWedEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etWedEnd, etWedStart, etWedEnd, wedStartTm, wedEndTm, inputLayoutWedStart, inputLayoutWedEnd, "Wed", "e");
            }
        });


        etThuStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etThuStart, etThuStart, etThuEnd, thuStartTm, thuEndTm, inputLayoutThuStart, inputLayoutThuEnd, "Thu", "s");
            }
        });

        etThuEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etThuEnd, etThuStart, etThuEnd, thuStartTm, thuEndTm, inputLayoutThuStart, inputLayoutThuEnd, "Thu", "e");
            }
        });


        etFriStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etFriStart, etFriStart, etFriEnd, friStartTm, friEndTm, inputLayoutFriStart, inputLayoutFriEnd, "Fri", "s");
            }
        });

        etFriEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etFriEnd, etFriStart, etFriEnd, friStartTm, friEndTm, inputLayoutFriStart, inputLayoutFriEnd, "Fri", "e");
            }
        });


        etSatStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etSatStart, etSatStart, etSatEnd, satStartTm, satEndTm, inputLayoutSatStart, inputLayoutSatEnd, "Sat", "s");
            }
        });

        etSatEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etSatEnd, etSatStart, etSatEnd, satStartTm, satEndTm, inputLayoutSatStart, inputLayoutSatEnd, "Sat", "e");
            }
        });


        etSunStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePicker(etSunStart, etSunStart, etSunEnd, sunStartTm, sunEndTm, inputLayoutSunStart, inputLayoutSunEnd, "Sun", "s");
            }
        });

        etSunEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePicker(etSunEnd, etSunStart, etSunEnd, sunStartTm, sunEndTm, inputLayoutSunStart, inputLayoutSunEnd, "Sun", "e");
            }
        });


        getUserDetails();

    }

    ////////////////////
    private void selectMediaWithRealQuality() {


        ApplicationUtils.hideKeyboard(mActivityContext);

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
//                whichClicked = 2;
                ImagePicker.Companion.with(mActivityContext)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080).cameraOnly()    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();

//                whichClicked = 2;
                ImagePicker.Companion.with(mActivityContext)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080).galleryOnly()    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


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


    ///////////////////

    int mHour;
    int mMinute;

    void showStartTimePicker(final TextInputEditText etTextToChange, final TextInputEditText etStart, final TextInputEditText etEnd, String startTime, String endTime, final TextInputLayout inputLayoutStart, final TextInputLayout inputLayoutEnd, final String day, final String type) {
        Toast.makeText(mActivityContext, startTime + "   " + endTime, Toast.LENGTH_SHORT).show();

        try {
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat hourFormatter = new SimpleDateFormat("HH");
            SimpleDateFormat minFormatter = new SimpleDateFormat("mm");


            Date startTim = timeFormatter.parse(startTime);
            Date endTim = timeFormatter.parse(endTime);
            int startHour = Integer.parseInt(hourFormatter.format(startTim));
            int startMinute = Integer.parseInt(minFormatter.format(startTim));

            final int endHour = Integer.parseInt(hourFormatter.format(endTim));
            final int endMinute = Integer.parseInt(minFormatter.format(endTim));
            mHour = startHour;
            mMinute = startMinute;

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay > endHour) {
//                                inputLayoutEnd.setError(getResources().getString(R.string.tv_time_error));
//                                etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                try {
//                                    inputLayoutEnd.setErrorEnabled(false);
//                                    etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                    inputLayoutStart.setErrorEnabled(false);
//                                    etStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
                                    settime(etTextToChange, day, hourOfDay + ":" + minute, "s");
                                    settime(etEnd, day, hourOfDay + ":" + (minute + 10), "e");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                                Toast.makeText(mActivityContext, "Wrong Time Select", Toast.LENGTH_SHORT).show();
                            } else if (hourOfDay == endHour) {
                                if (minute >= endMinute) {
//                                    inputLayoutEnd.setError(getResources().getString(R.string.tv_time_error));
//                                    etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                                    inputLayoutEnd.setErrorEnabled(false);
//                                    etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                    inputLayoutStart.setErrorEnabled(false);
//                                    etStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
                                    try {
                                        settime(etTextToChange, day, hourOfDay + ":" + minute, "s");
                                        settime(etEnd, day, hourOfDay + ":" + (minute + 10), "e");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
//                                    Toast.makeText(mActivityContext, "Again Wrong Time Select", Toast.LENGTH_SHORT).show();
                                } else {
//                                    inputLayoutEnd.setErrorEnabled(false);
//                                    etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                    inputLayoutStart.setErrorEnabled(false);
//                                    etStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                    Toast.makeText(mActivityContext, "Again right Time Select", Toast.LENGTH_SHORT).show();
                                    try {
                                        settime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
//                                inputLayoutStart.setErrorEnabled(false);
//                                etStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                inputLayoutEnd.setErrorEnabled(false);
//                                etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                Toast.makeText(mActivityContext, " right Time Select", Toast.LENGTH_SHORT).show();
                                try {
                                    settime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    void setTimeText(TextInputEditText etTime, String timeStr) {
        SimpleDateFormat timFormatter = new SimpleDateFormat("HH:mm:ss");
        if (timeStr != null) {
            try {
                Date time = timFormatter.parse(timeStr);
                SimpleDateFormat timAmPm = new SimpleDateFormat("hh:mm a");
                etTime.setText(timAmPm.format(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    void settime(TextInputEditText ettimeToSet, String day, String timeStr, String type) throws ParseException {
        SimpleDateFormat timFormatter = new SimpleDateFormat("HH:mm:ss");
        Date time = timFormatter.parse(timeStr + ":00");
        SimpleDateFormat timeAmPm = new SimpleDateFormat("hh:mm a");
        ettimeToSet.setText(timeAmPm.format(time));
        if (day.equalsIgnoreCase("Mon")) {
            if (type.equalsIgnoreCase("s")) {
                monStartTm = timFormatter.format(time);
                hoursList.get(0).setStartTime(monStartTm);
            } else {
                monEndTm = timFormatter.format(time);
                hoursList.get(0).setEndTime(monEndTm);
            }
        } else if (day.equalsIgnoreCase("Tue")) {
            if (type.equalsIgnoreCase("s")) {
                tueStartTm = timFormatter.format(time);
                hoursList.get(1).setStartTime(tueStartTm);
            } else {
                tueEndTm = timFormatter.format(time);
                hoursList.get(1).setEndTime(tueEndTm);
            }
        } else if (day.equalsIgnoreCase("Wed")) {
            if (type.equalsIgnoreCase("s")) {
                wedStartTm = timFormatter.format(time);
                hoursList.get(2).setStartTime(wedStartTm);
            } else {
                wedEndTm = timFormatter.format(time);
                hoursList.get(2).setEndTime(wedEndTm);
            }
        } else if (day.equalsIgnoreCase("Thu")) {
            if (type.equalsIgnoreCase("s")) {
                thuStartTm = timFormatter.format(time);
                hoursList.get(3).setStartTime(thuStartTm);
            } else {
                thuEndTm = timFormatter.format(time);
                hoursList.get(3).setEndTime(thuEndTm);
            }
        } else if (day.equalsIgnoreCase("Fir")) {
            if (type.equalsIgnoreCase("s")) {
                friStartTm = timFormatter.format(time);
                hoursList.get(4).setStartTime(friStartTm);
            } else {
                friEndTm = timFormatter.format(time);
                hoursList.get(4).setEndTime(friEndTm);
            }
        } else if (day.equalsIgnoreCase("Sat")) {
            if (type.equalsIgnoreCase("s")) {
                satStartTm = timFormatter.format(time);
                hoursList.get(5).setStartTime(satStartTm);
            } else {
                satEndTm = timFormatter.format(time);
                hoursList.get(5).setEndTime(satEndTm);
            }
        } else if (day.equalsIgnoreCase("Sun")) {
            if (type.equalsIgnoreCase("s")) {
                sunStartTm = timFormatter.format(time);
                hoursList.get(6).setStartTime(sunStartTm);
            } else {
                sunEndTm = timFormatter.format(time);
                hoursList.get(6).setEndTime(sunEndTm);
            }
        }

    }


    void showEndTimePicker(final TextInputEditText etTextToChange, TextInputEditText etStart, final TextInputEditText etEnd, String startTime, String endTime, TextInputLayout inputLayoutStart, final TextInputLayout inputLayoutEnd, final String day, final String type) {
        Toast.makeText(mActivityContext, startTime + "   " + endTime, Toast.LENGTH_SHORT).show();

        try {
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat hourFormatter = new SimpleDateFormat("HH");
            SimpleDateFormat minFormatter = new SimpleDateFormat("mm");

            Date startTim = timeFormatter.parse(startTime);
            Date endTim = timeFormatter.parse(endTime);
            final int startHour = Integer.parseInt(hourFormatter.format(startTim));
            final int startMinute = Integer.parseInt(minFormatter.format(startTim));

            final int endHour = Integer.parseInt(hourFormatter.format(endTim));
            final int endMinute = Integer.parseInt(minFormatter.format(endTim));
            mHour = endHour;
            mMinute = endMinute;

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay < startHour) {
//                                inputLayoutEnd.setError(getResources().getString(R.string.tv_time_error));
//                                etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_time_error), false);
//                                Toast.makeText(mActivityContext, "Wrong Time Select", Toast.LENGTH_SHORT).show();
                            } else if (hourOfDay == startHour) {

                                if (minute <= startMinute) {
//                                    inputLayoutEnd.setError(getResources().getString(R.string.tv_time_error));
//                                    etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_time_error), false);
//                                    Toast.makeText(mActivityContext, "Again Wrong Time Select", Toast.LENGTH_SHORT).show();
                                } else {
//                                    etTextToChange.setText(hourOfDay + ":" + minute);
//                                    inputLayoutEnd.setErrorEnabled(false);
//                                    etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                    Toast.makeText(mActivityContext, "Again right Time Select", Toast.LENGTH_SHORT).show();
                                    try {
                                        settime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
//                                inputLayoutEnd.setErrorEnabled(false);
//                                etEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
//                                etTextToChange.setText(hourOfDay + ":" + minute);
                                try {
                                    settime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                                Toast.makeText(mActivityContext, " right Time Select", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    int CAP_IMG_CODE = 200;

    void pickImage() {

//        ImagePicker.Companion.with(this)
//                .crop(1f, 1f)                    //Crop image(Optional), Check Customization for more option
//                .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
//               .start();

        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    List<BusinessHour> hoursList;
    ArrayList<BusinessImage> businessImageList;
    String monStartTm = "00:00:00", monEndTm = "00:00:00", tueStartTm = "00:00:00", tueEndTm = "00:00:00", wedStartTm = "00:00:00", wedEndTm = "00:00:00";
    String thuStartTm = "00:00:00", thuEndTm = "00:00:00", friStartTm = "00:00:00", friEndTm = "00:00:00", satStartTm = "00:00:00", satEndTm = "00:00:00", sunStartTm = "00:00:00", sunEndTm = "00:00:00";
    Subscriptions subscription;

    void setUserDetail(BusinessDetails user) {
        hoursList = new ArrayList<>();


        if (user.getThumbLogo() != null)
            Glide.with(mContext)
                    .load(user.getThumbLogo())
                    .error(R.drawable.user_placeholder)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imgProfileIcon);

        etBusinessName.setText(user.getName());

        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhoneNumber());

        if (user.getBusiness_category_details() != null) {
            etCategory.setText(user.getBusiness_category_details().getName());
            catId = user.getBusiness_category_details().getId();
            catName = user.getBusiness_category_details().getName();
        }

        if (user.getBusiness_subcategory_details() != null) {
            etSubCategory.setText(user.getBusiness_subcategory_details().getName());
            subCatId = user.getBusiness_subcategory_details().getId();
            subCatName = user.getBusiness_subcategory_details().getName();
        }

        if (user.getAddress() != null && user.getLatitude() > 0 && user.getLongitude() > 0) {
            etAddress.setText(user.getAddress());
            lat = user.getLatitude();
            lon = user.getLongitude();

        }
        if (user.getCountry_details() != null) {
            countryId = user.getCountry_details().getId();
            countryName = user.getCountry_details().getName();
            etCountry.setText(user.getCountry_details().getName());
            getAllStates();
        }
        if (user.getState_details() != null) {
            stateId = user.getState_details().getId();
            stateName = user.getState_details().getName();
            etState.setText(user.getState_details().getName());
            getAllCities();
        }
        if (user.getCity_details() != null) {
            cityId = user.getCity_details().getId();
            cityName = user.getCity_details().getName();
            etCity.setText(user.getCity_details().getName());
        }
        if (user.getZipCode() != null) {
            etZipCode.setText(user.getZipCode().toString());
        }

        if (user.getWebsite() != null) {
            etWebiste.setText(user.getWebsite());
        }
        if (user.getIsApprove() != null) {
            if (user.getIsApprove() == 1) {
                acceptCheckBox.setChecked(true);
            } else {
                acceptCheckBox.setChecked(false);
            }
        }
        if (user.getBusinessHours() != null && user.getBusinessHours().size() == 7) {
            hoursList = user.getBusinessHours();

        } else {

            hoursList.add(new BusinessHour("Monday", "00:00:00", "00:00:00"));
            hoursList.add(new BusinessHour("Tuesday", "00:00:00", "00:00:00"));
            hoursList.add(new BusinessHour("Wednesday", "00:00:00", "00:00:00"));
            hoursList.add(new BusinessHour("Thursday", "00:00:00", "00:00:00"));
            hoursList.add(new BusinessHour("Friday", "00:00:00", "00:00:00"));
            hoursList.add(new BusinessHour("Saturday", "00:00:00", "00:00:00"));
            hoursList.add(new BusinessHour("Sunday", "00:00:00", "00:00:00"));

        }
        for (int i = 0; i < hoursList.size(); i++) {
            if (hoursList.get(i).getDay().equalsIgnoreCase("Monday")) {
                monStartTm = hoursList.get(i).getStartTime();
                monEndTm = hoursList.get(i).getEndTime();
                setTimeText(etMonStart, hoursList.get(i).getStartTime());
                setTimeText(etMonEnd, hoursList.get(i).getEndTime());
//                    etMonStart.setText(hoursList.get(i).getStartTime());
//                    etMonEnd.setText(hoursList.get(i).getEndTime());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Tuesday")) {
                tueStartTm = hoursList.get(i).getStartTime();
                tueEndTm = hoursList.get(i).getEndTime();
//                    etTueStart.setText(hoursList.get(i).getStartTime());
//                    etTueEnd.setText(hoursList.get(i).getEndTime());
                setTimeText(etTueStart, hoursList.get(i).getStartTime());
                setTimeText(etTueEnd, hoursList.get(i).getEndTime());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Wednesday")) {
                wedStartTm = hoursList.get(i).getStartTime();
                wedEndTm = hoursList.get(i).getEndTime();
//                    etWedStart.setText(hoursList.get(i).getStartTime());
//                    etWedEnd.setText(hoursList.get(i).getEndTime());
                setTimeText(etWedStart, hoursList.get(i).getStartTime());
                setTimeText(etWedEnd, hoursList.get(i).getEndTime());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Thursday")) {
                thuStartTm = hoursList.get(i).getStartTime();
                thuEndTm = hoursList.get(i).getEndTime();
//                    etThuStart.setText(hoursList.get(i).getStartTime());
//                    etThuEnd.setText(hoursList.get(i).getEndTime());
                setTimeText(etThuStart, hoursList.get(i).getStartTime());
                setTimeText(etThuEnd, hoursList.get(i).getEndTime());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Friday")) {
                friStartTm = hoursList.get(i).getStartTime();
                friEndTm = hoursList.get(i).getEndTime();
//                    etFriStart.setText(hoursList.get(i).getStartTime());
//                    etFriEnd.setText(hoursList.get(i).getEndTime());
                setTimeText(etFriStart, hoursList.get(i).getStartTime());
                setTimeText(etFriEnd, hoursList.get(i).getEndTime());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Saturday")) {
                satStartTm = hoursList.get(i).getStartTime();
                satEndTm = hoursList.get(i).getEndTime();
//                    etSatStart.setText(hoursList.get(i).getStartTime());
//                    etSatEnd.setText(hoursList.get(i).getEndTime());
                setTimeText(etSatStart, hoursList.get(i).getStartTime());
                setTimeText(etSatEnd, hoursList.get(i).getEndTime());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Sunday")) {
                sunStartTm = hoursList.get(i).getStartTime();
                sunEndTm = hoursList.get(i).getEndTime();
//                    etSunStart.setText(hoursList.get(i).getStartTime());
//                    etSunEnd.setText(hoursList.get(i).getEndTime());
                setTimeText(etSunStart, hoursList.get(i).getStartTime());
                setTimeText(etSunEnd, hoursList.get(i).getEndTime());

            }
        }

        if (user.getBusiness_images() != null && user.getBusiness_images().size() > 0) {
            businessImageList = user.getBusiness_images();

            for (int i = 0; i < businessImageList.size(); i++) {
                businessImageList.get(i).setUploaded(true);

            }

//            businessImageList = new ArrayList<>();

        } else {
            businessImageList = new ArrayList<>();
        }

        if (businessImageList.size() < 5) {
            BusinessImage businessImage = new BusinessImage();
            businessImage.setId(-1);
            businessImageList.add(0, businessImage);
        }

        if (user.getIs_business() == 1) {
            linsubScriptionPlan.setVisibility(View.GONE);
        } else {
  if (user.getBusiness_subscriptions() != null) {
                subscriptionId = user.getBusiness_subscriptions().getId();
                subscriptionIdTemp = user.getBusiness_subscriptions().getId();
            } else {
                subscriptionId = -1;
                subscriptionIdTemp = -1;
                getAllSubscriptons();
            }


            showInfoToUser(mActivityContext, "Information", "Your subscription has been expired. Please renew your subscription");
            getAllSubscriptons();

        }


        mGAdapter.addItems(businessImageList);

        if (user.getAboutBusiness() != null) {
            etAboutBusiness.setText(user.getAboutBusiness());
        }
    }


    String address;
    double lat = -1, lon = -1;
    File profileImgFile;
    Boolean isProfileSet = false;

    private static int whichClicked = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQ_CODE_MAP_BUSINESSFRAGMENT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    address = data.getStringExtra("address");
                    lat = data.getDoubleExtra("lat", 0.0);
                    lon = data.getDoubleExtra("lon", 0.0);
                    if (address != null && address != "") {
                        etAddress.setText(address);
                    }

                }
            }
        } else if (requestCode == REQUEST_CODE_PAYMENT) {

            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject().toString(4));
*
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend



                        showProgressBar(true);
                        postBusinessPaymentInfo(String.valueOf(businessIdPayment), paymentAmount, String.valueOf(businessOrderId));

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
            return;

        } else {
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    if (whichClicked == 1) {
                        Bitmap bmp = BitmapFactory.decodeFile(ImagePicker.Companion.getFile(data).toString());
                        imgProfileIcon.setImageBitmap(bmp);
                        File file = ImagePicker.Companion.getFile(data);
                        if (file != null) {
                            profileImgFile = file;
                            isProfileSet = true;
                        }
                    } else if (whichClicked == 2) {
                        BusinessImage businessImage = new BusinessImage();
                        businessImage.setId(-1);
                        businessImage.setFile_image(null);
                        Bitmap bmp = BitmapFactory.decodeFile(ImagePicker.Companion.getFile(data).toString());
                        businessImage.setBitmap(bmp);
                        businessImage.setUploaded(false);
                        businessImage.setImgFile(ImagePicker.Companion.getFile(data));
                        businessImageList.remove(0);
                        businessImageList.add(0, businessImage);

                        if (businessImageList.size() < 5) {
                            BusinessImage emptyImage = new BusinessImage();
                            emptyImage.setId(-1);
                            businessImageList.add(0, emptyImage);
                        }
                        mGAdapter.addItems(businessImageList);
                    }
                }
            }
        }

    }


    List<BusinessCategory> businessCategories;

    void getAllCategories() {
        businessCategories = new ArrayList<>();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;
        showProgressBar(true);

        Call<BusinesCatResponse> call = api.getallCategories(token);

        call.enqueue(new Callback<BusinesCatResponse>() {
            @Override
            public void onResponse(Call<BusinesCatResponse> call, Response<BusinesCatResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null && response.body().getData().getCategories().size() > 0) {
                                businessCategories = response.body().getData().getCategories();
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
            public void onFailure(Call<BusinesCatResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    List<Country> countries;

    void getAllcountries() {
        countries = new ArrayList<>();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<CountryResponse> call = api.getallCountries(token);

        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                countries = response.body().getData();

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
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    List<State> states;

    void getAllStates() {
        states = new ArrayList<>();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;
        showProgressBar(true);

        Call<StateResponse> call = api.getallStates(token, countryId + "");

        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                states = response.body().getData();
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
            public void onFailure(Call<StateResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    List<City> cities;

    void getAllCities() {
        cities = new ArrayList<>();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;
        showProgressBar(true);

        Call<CityResponse> call = api.getallCities(token, stateId + "");

        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                cities = response.body().getData();


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
            public void onFailure(Call<CityResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    void showCategoryDialog() {

        catIdTemp = -1;
        catNameTemp = "";
        businessCategory = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = mActivityContext.getLayoutInflater().inflate(R.layout.dialog_list, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.select_country));

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (allCatAdaptor != null) {
                    if (catIdTemp != -1 && catNameTemp != "") {
                        catId = catIdTemp;
                        catName = catNameTemp;
                        etCategory.setText(catName);

                        if (businessCategory != null) {
                            if (businessCategory.getSubCategories() != null && businessCategory.getSubCategories().size() > 0) {
                                subCategories = businessCategory.getSubCategories();
                            } else {
                                subCategories = new ArrayList<>();
                            }
                        }

                        subCatName = "";
                        subCatId = -1;
                        subCatNameTemp = "";
                        subCatIdTemp = -1;
                        etSubCategory.setText("");
                    }
                }
            }
        });
        Dialog dialog = builder.create();

        list = view.findViewById(R.id.list);
        etName = view.findViewById(R.id.etName);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        allCatAdaptor = new AllCatAdaptor(businessCategories, mActivityContext, new AllCatAdaptor.RecItemClick() {
            @Override
            public void ItemClick(BusinessCategory country) {
                catIdTemp = country.getId();
                catNameTemp = country.getName();
                businessCategory = country;

            }
        }, catId);
        list.setAdapter(allCatAdaptor);
//        adaptor.addItems(countries);
        dialog.show();


    }


    int subCatIdTemp = -1;
    String subCatNameTemp = "";
    int subCatId = -1;
    String subCatName = "";
    AllSubCatAdaptor allSubCatAdaptor;
    List<CategoryDetails> subCategories = new ArrayList<>();

    void showSubCategoryDialog() {

        subCatIdTemp = -1;
        subCatNameTemp = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = mActivityContext.getLayoutInflater().inflate(R.layout.dialog_list, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.select_country));

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (allSubCatAdaptor != null) {
                    if (subCatIdTemp != -1 && subCatNameTemp != "") {
                        subCatId = subCatIdTemp;
                        subCatName = subCatNameTemp;
                        etSubCategory.setText(subCatName);

                    }
                }
            }
        });
        Dialog dialog = builder.create();

        list = view.findViewById(R.id.list);
        etName = view.findViewById(R.id.etName);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        allSubCatAdaptor = new AllSubCatAdaptor(subCategories, mActivityContext, new AllSubCatAdaptor.RecItemClick() {
            @Override
            public void ItemClick(CategoryDetails country) {
                subCatIdTemp = country.getId();
                subCatNameTemp = country.getName();

            }
        }, subCatId);
        list.setAdapter(allSubCatAdaptor);
        dialog.show();


    }


    int countryIdTemp = -1, countryId = -1;
    String countryNameTemp, countryName = "";
    EditText etName;
    public CountryListAdaptor adaptor;

    void showCountryDialog() {
        countryIdTemp = -1;
        countryNameTemp = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = mActivityContext.getLayoutInflater().inflate(R.layout.dialog_list, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.select_country));

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (adaptor != null) {
                    if (countryIdTemp != -1 && countryNameTemp != "") {
                        countryId = countryIdTemp;
                        countryName = countryNameTemp;
                        etCountry.setText(countryName);
                        getAllStates();
                        stateName = "";
                        stateId = -1;
                        stateNameTemp = "";
                        stateIdTemp = -1;
                        cityName = "";
                        cityId = -1;
                        cityIdTemp = -1;
                        cityNameTemp = "";
                        etState.setText("");
                        etCity.setText("");
                    }
                }
            }
        });
        Dialog dialog = builder.create();

        list = view.findViewById(R.id.list);
        etName = view.findViewById(R.id.etName);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        adaptor = new CountryListAdaptor(countries, mActivityContext, new CountryListAdaptor.RecItemClick() {
            @Override
            public void ItemClick(Country country) {
                countryIdTemp = country.getId();
                countryNameTemp = country.getName();

            }
        }, countryId);
        list.setAdapter(adaptor);
//        adaptor.addItems(countries);
        dialog.show();


    }

    int stateIdTemp = -1, stateId = -1;
    String stateNameTemp = "", stateName = "";
    StateListAdaptor adaptor2;

    void showAllAllStatesDialog() {

        stateIdTemp = -1;
        stateNameTemp = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = mActivityContext.getLayoutInflater().inflate(R.layout.dialog_list, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.select_country));

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (adaptor2 != null) {
                    if (stateIdTemp != -1 && stateNameTemp != "") {
                        stateId = stateIdTemp;
                        stateName = stateNameTemp;
                        etState.setText(stateName);
                        getAllCities();

                        cityName = "";
                        cityId = -1;
                        cityIdTemp = -1;
                        cityNameTemp = "";
                        etCity.setText("");
                    }
                }
            }
        });
        Dialog dialog = builder.create();

        list = view.findViewById(R.id.list);
        etName = view.findViewById(R.id.etName);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        adaptor2 = new StateListAdaptor(states, mActivityContext, new StateListAdaptor.RecItemClick() {
            @Override
            public void ItemClick(State country) {
                stateIdTemp = country.getId();
                stateNameTemp = country.getName();
            }
        }, stateId);
        list.setAdapter(adaptor2);
//        adaptor.addItems(countries);
        dialog.show();
    }

    int cityIdTemp = -1, cityId = -1;
    String cityNameTemp, cityName = "";
    CityLisAdaptor adaptor3;

    void showAllCities() {
        cityIdTemp = -1;
        cityNameTemp = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = mActivityContext.getLayoutInflater().inflate(R.layout.dialog_list, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.select_country));

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (adaptor3 != null) {
                    if (cityIdTemp != -1 && cityNameTemp != "") {
                        cityId = cityIdTemp;
                        cityName = cityNameTemp;
                        etCity.setText(cityName);
                    }
                }
            }
        });
        Dialog dialog = builder.create();

        list = view.findViewById(R.id.list);
        etName = view.findViewById(R.id.etName);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        adaptor3 = new CityLisAdaptor(cities, mActivityContext, new CityLisAdaptor.RecItemClick() {
            @Override
            public void ItemClick(City country) {
                cityIdTemp = country.getId();
                cityNameTemp = country.getName();
            }
        }, cityId);
        list.setAdapter(adaptor3);
//        adaptor.addItems(countries);
        dialog.show();
    }

    private void showProgressBar(final boolean progressVisible) {
        mActivityContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public boolean validateBusinessName(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etBusinessName.getText().toString())) {
//            inputLayoutBusinessName.setErrorEnabled(false);
//            etBusinessName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutBusinessName.setError(getResources().getString(R.string.tv_err_msg_name));
//            etBusinessName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_business_name), false);
            return false;
        }
        return true;
    }

    public boolean validateCategoryName(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etCategory.getText().toString())) {
//            inputLayoutCategory.setErrorEnabled(false);
//            etCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutCategory.setError(getResources().getString(R.string.tv_err_msg_last_name));
//            etCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_category), false);
            return false;
        }
        return true;
    }

    public boolean validateSubCategoryName(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etSubCategory.getText().toString())) {
//            inputLayoutCategory.setErrorEnabled(false);
//            etCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutCategory.setError(getResources().getString(R.string.tv_err_msg_last_name));
//            etCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_sub_category), false);
            return false;
        }
        return true;
    }


    public boolean validateEmail(boolean errorShouldShow) {
        if (ApplicationUtils.validateEmail(etEmail.getText().toString())) {
//            inputLayoutEmail.setErrorEnabled(false);
//            etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutEmail.setError(getResources().getString(R.string.tv_err_msg_email));
//            etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_business_email), false);
            return false;
        }
        return true;
    }

    public boolean validatePhone(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etPhone.getText().toString())) {
//            inputLayoutPhone.setErrorEnabled(false);
//            etPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutPhone.setError(getResources().getString(R.string.tv_err_msg_phone));
//            etPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_phone), false);
            return false;
        }
        return true;
    }


    public boolean validateZipCode(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etZipCode.getText().toString())) {
//            inputLayoutZipCode.setErrorEnabled(false);
//            etZipCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutZipCode.setError(getResources().getString(R.string.tv_err_msg_zipcode));
//            etZipCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_zipcode), false);
            return false;
        }
        return true;
    }

    public boolean validateWebsite(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etWebiste.getText().toString())) {
//            inputLayoutZipCode.setErrorEnabled(false);
//            etZipCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutZipCode.setError(getResources().getString(R.string.tv_err_msg_zipcode));
//            etZipCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_website), false);
            return false;
        }
        return true;
    }


    public boolean validateCountry(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etCountry.getText().toString())) {
//            inputLayoutCountry.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutCountry.setError(getResources().getString(R.string.tv_err_msg_country));
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_country), false);
            return false;
        }
        return true;
    }


    public boolean validateState(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etState.getText().toString())) {
//            inputLayoutState.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutState.setError(getResources().getString(R.string.tv_err_msg_state));
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_state), false);
            return false;
        }
        return true;
    }

    public boolean validateCity(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etCity.getText().toString())) {
//            inputLayoutCity.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutCity.setError(getResources().getString(R.string.tv_err_msg_city));
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_city), false);

            return false;
        }
        return true;
    }

    public boolean validateAddress(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etAddress.getText().toString())) {
//            inputLayoutAdress.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutAdress.setError(getResources().getString(R.string.tv_err_msg_address));
//            etCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_address), false);
            return false;
        }
        return true;
    }


    private void validateData() {

        if (validateBusinessName(true) == false)
            return;
        if (validateCategoryName(true) == false)
            return;
        if (validateSubCategoryName(true) == false)
            return;
        if (validateEmail(true) == false)
            return;
        if (validatePhone(true) == false)
            return;
        if (validateAddress(true) == false)
            return;
        if (validateCountry(true) == false)
            return;
        if (validateState(true) == false)
            return;
        if (validateCity(true) == false)
            return;
        if (validateZipCode(true) == false)
            return;
        if (validateWebsite(true) == false)
            return;

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mActivityContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
//            showProgressBar(true);
//            registerUser(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etConfirmPassword.getText().toString());


            saveData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void saveData() {
        String daysStr = "", startTimesStr = "", endTimesStr = "";
        for (int i = 0; i < hoursList.size(); i++) {
            daysStr = daysStr + hoursList.get(i).getDay() + ",";
            startTimesStr = startTimesStr + hoursList.get(i).getStartTime() + ",";
            endTimesStr = endTimesStr + hoursList.get(i).getEndTime() + ",";
        }
        daysStr = daysStr.substring(0, daysStr.length() - 1);
        startTimesStr = startTimesStr.substring(0, startTimesStr.length() - 1);
        endTimesStr = endTimesStr.substring(0, endTimesStr.length() - 1);

        String deltedImgs = "";
        for (int i = 0; i < mDeletedImages.size(); i++) {
            deltedImgs = deltedImgs + mDeletedImages.get(i) + ",";
        }
        if (deltedImgs.length() > 2)
            deltedImgs = deltedImgs.substring(0, deltedImgs.length() - 1);
/////
        int ivCount = 0;
        for (int i = 0; i < businessImageList.size(); i++) {
            if (!businessImageList.get(i).isUploaded() && businessImageList.get(i).getImgFile() != null) {
                ivCount++;
            }
        }


        MultipartBody.Part[] imageParts = new MultipartBody.Part[ivCount];
        int ivIndex = 0;
        if (ivCount != 0) {
            for (int index = 0; index < businessImageList.size(); index++) {
                if (businessImageList.get(index).getId() == -1 && !businessImageList.get(index).isUploaded()) {
                    if (businessImageList.get(index).getImgFile() != null) {
                        ProgressRequestBody imageProgress = new ProgressRequestBody(businessImageList.get(index).getImgFile(), "image", this);
                        imageParts[ivIndex++] = MultipartBody.Part.createFormData("business_image[]", businessImageList.get(index).getImgFile().getName(), imageProgress);
                    }
                }
            }
        }
        ///

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), etBusinessName.getText().toString());
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), catId + "");
        RequestBody sub_category_id = RequestBody.create(MediaType.parse("text/plain"), subCatId + "");
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), etAddress.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), lat + "");
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), lon + "");
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), countryId + "");
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), stateId + "");
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), cityId + "");
        RequestBody zip_code = RequestBody.create(MediaType.parse("text/plain"), etZipCode.getText().toString());
        RequestBody phone_number = RequestBody.create(MediaType.parse("text/plain"), etPhone.getText().toString());
        RequestBody website = RequestBody.create(MediaType.parse("text/plain"), etWebiste.getText().toString());
        RequestBody about_business = RequestBody.create(MediaType.parse("text/plain"), etAboutBusiness.getText().toString());
        MultipartBody.Part imageBodyPart = null;
        if (profileImgFile != null) {
            RequestBody image = RequestBody.create(
                    MediaType.parse("image/jpeg"),
                    profileImgFile
            );
            imageBodyPart = MultipartBody.Part.createFormData("logo", profileImgFile.getName(), image);
        }


        RequestBody delete_images = RequestBody.create(MediaType.parse("text/plain"), deltedImgs);

        RequestBody start_time = RequestBody.create(MediaType.parse("text/plain"), startTimesStr);
        RequestBody end_time = RequestBody.create(MediaType.parse("text/plain"), endTimesStr);
        RequestBody day = RequestBody.create(MediaType.parse("text/plain"), daysStr);
//        Toast.makeText(mActivityContext, subscriptionId + "", Toast.LENGTH_SHORT).show();
        RequestBody subscription_id = RequestBody.create(MediaType.parse("text/plain"), "1");

        RequestBody detail = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody service = RequestBody.create(MediaType.parse("text/plain"), "1");


        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;

        Call<BusinessProfileResponse> call = api.updateBusinessProfile(token,
                name,
                category_id,
                sub_category_id,
                email,
                address,
                latitude,
                longitude,
                country,
                state,
                city,
                zip_code,
                phone_number,
                website, imageBodyPart, about_business, delete_images, imageParts, start_time, end_time, day, subscription_id, detail, service);
        showProgressBar(true);
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
                            ApplicationUtils.showToast(mActivityContext, response.body().getMessage(), true);

                            BusinessDetails user = response.body().getData().getData();
                            if (user != null) {
                                setUserDetail(user);
                            }
                            mDeletedImages = new ArrayList<>();

                            if (response.body().getData().getOrder() != null) {
                                BusinessOrderDetail businessOrderDetail = response.body().getData().getOrder();
                                businessIdPayment = businessOrderDetail.getBusinessId();
                                businessOrderId = businessOrderDetail.getId();
                                paymentAmount = businessOrderDetail.getAmount();
                                isPaidBusiness = false;
                                if (businessOrderDetail.getStatus().equals("paid"))
                                    isPaidBusiness = true;
                                if (!isPaidBusiness) {
                                    callPaypalPayment(paymentAmount, "Monthly Subscription");
                                }

                            }
                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mActivityContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
                    if (response.code() == 401)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_unauthorized_user));
                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    private void getUserDetails() {
        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;
        ApiClient apiClient = ApiClient.getInstance();
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
                                setUserDetail(user);
                            } else {

                            }


                        }
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mActivityContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
                    if (response.code() == 401)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_unauthorized_user));
                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }


    TextWatcher phoneTextChangedListners = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                inputLayoutPhone.setHint("Phone");
            } else {
                inputLayoutPhone.setHint("Phone");
            }
        }
    };

    //////////////////////////////////
    ArrayList<String> mDeletedImages = new ArrayList<>();

    public void removeGalleryItem(int position) { //removes the row


        BusinessImage galleryModelToRemove = businessImageList.get(position);
        if (!galleryModelToRemove.isUploaded()) {

            try {
                if (galleryModelToRemove.getImgFile() != null)
                    ApplicationUtils.removeFileFromDisk(galleryModelToRemove.getImgFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mDeletedImages.add(galleryModelToRemove.getId() + "");
        }
//        businessImageList.remove(position);
//        mGAdapter.notifyItemRemoved(position);
//        mGAdapter.notifyDataSetChanged();
        businessImageList.remove(position);
        if (businessImageList.get(0).getId() != -1) {
            BusinessImage businessImage = new BusinessImage();
            businessImage.setId(-1);
            businessImage.setUploaded(false);
            businessImageList.add(0, businessImage);
        }
        mGAdapter.addItems(businessImageList);
    }


    ProgressDialog pbProgress;

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


    List<Subscriptions> subscriptions;
    int subscriptionId = -1;
    int subscriptionIdTemp = -1;
    SubscriptionsAdaptor subscriptionsAdaptor;

    void getAllSubscriptons() {
        subscriptions = new ArrayList<>();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;
        showProgressBar(true);

        Call<SubscriptionsResponse> call = api.getallSubscriptions(token);

        call.enqueue(new Callback<SubscriptionsResponse>() {
            @Override
            public void onResponse(Call<SubscriptionsResponse> call, Response<SubscriptionsResponse> response) {

                showProgressBar(false);
                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                subscriptions = response.body().getData();
                                rvSubscriptionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
//                                rvSubscriptionsList.addItemDecoration(dividerItemDecoration);
                                subscriptionsAdaptor = new SubscriptionsAdaptor(subscriptions, mActivityContext, new SubscriptionsAdaptor.RecItemClick() {
                                    @Override
                                    public void ItemClick(Subscriptions country) {
                                        subscriptionId = country.getId();

                                        subscription = country;

                                    }
                                }, subscriptionId);
                                rvSubscriptionsList.setAdapter(subscriptionsAdaptor);
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
            public void onFailure(Call<SubscriptionsResponse> call, Throwable t) {
                showProgressBar(false);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

**********************************************************************************************************************************************************
     * PaymentCode
     ***********************************************************************************************************************************************************



    public void callPaypalPayment(String amount, String itemName) {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mActivityContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        whichClicked = 3;
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.


        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, amount, itemName);

         * See getStuffToBuy(..) for examples of some available payment options.



        Intent intent = new Intent(mContext, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paymentConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent, String amount, String itemName) {
        return new PayPalPayment(new BigDecimal(amount), "USD", itemName,
                paymentIntent);
    }

    private void postBusinessPaymentInfo(final String businessId, String amount, String orderId) {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.postBusinessPayment(businessId, amount, orderId, new Callback<BusinessProfileResponse>() {
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

                        DialogFactory.showDropDownNotificationSuccess(
                                mActivityContext,
                                mContext.getString(R.string.alert_success),
                                response.body().getMessage());


                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            DialogFactory.showDropDownNotificationError(
                                    mActivityContext,
                                    mContext.getString(R.string.alert_error),
                                    response.body().getMessage());
                        }
                    }
                } else {
                    if (response.code() == 401)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_unauthorized_user));
                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mActivityContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mActivityContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    public void showInfoToUser(Context ctx, String title, String msg) {
        androidx.appcompat.app.AlertDialog.Builder aler = new androidx.appcompat.app.AlertDialog.Builder(ctx);
        aler.setTitle(title);
        aler.setMessage(msg);
        aler.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        aler.show();
    }
}
*/
