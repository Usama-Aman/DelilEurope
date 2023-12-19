package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.MapsActivity;
import com.dalileuropeapps.dalileurope.activities.StripActivity;
import com.dalileuropeapps.dalileurope.activities.SubscriptionsFragment;
import com.dalileuropeapps.dalileurope.adapter.AllCatAdaptor;
import com.dalileuropeapps.dalileurope.adapter.AllSubCatAdaptor;
import com.dalileuropeapps.dalileurope.adapter.BusinessImagesAdaptor;
import com.dalileuropeapps.dalileurope.adapter.CityLisAdaptor;
import com.dalileuropeapps.dalileurope.adapter.CountryListAdaptor;
import com.dalileuropeapps.dalileurope.adapter.StateListAdaptor;
import com.dalileuropeapps.dalileurope.adapter.SubscriptionsAdaptor;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinesCatResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessCategory;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessHour;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessImage;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessOrderDetail;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponseData;
import com.dalileuropeapps.dalileurope.api.retrofit.CategoryDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.City;
import com.dalileuropeapps.dalileurope.api.retrofit.CityResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.Country;
import com.dalileuropeapps.dalileurope.api.retrofit.CountryResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.State;
import com.dalileuropeapps.dalileurope.api.retrofit.StateResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.Subscriptions;
import com.dalileuropeapps.dalileurope.api.retrofit.SubscriptionsResponse;
import com.dalileuropeapps.dalileurope.imagepicke_pack.ImagePickerActivity;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
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
import static com.dalileuropeapps.dalileurope.activities.MainActivity.REQUEST_CODE_PAYMENT;
import static com.dalileuropeapps.dalileurope.activities.MainActivity.isBusinessAccount;
import static com.dalileuropeapps.dalileurope.activities.MainActivity.isBusinessAccountPaid;
import static com.dalileuropeapps.dalileurope.activities.MainActivity.paymentConfig;
import static com.dalileuropeapps.dalileurope.utils.ApplicationUtils.showToast;

//import com.examples.dalileurope.api.SubscriptionsResponse;

public class BusinessProfileFragment extends BaseFragment implements ProgressRequestBody.UploadCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //image recyclerview
    GridLayoutManager mLinearLayoutManager;
    BusinessImagesAdaptor mGAdapter;


    List<BusinessHour> hoursList;
    ArrayList<BusinessImage> businessImageList;
    String monStartTm = "00:00:00", monEndTm = "00:00:00", tueStartTm = "00:00:00", tueEndTm = "00:00:00", wedStartTm = "00:00:00", wedEndTm = "00:00:00";
    String thuStartTm = "00:00:00", thuEndTm = "00:00:00", friStartTm = "00:00:00", friEndTm = "00:00:00", satStartTm = "00:00:00", satEndTm = "00:00:00", sunStartTm = "00:00:00", sunEndTm = "00:00:00";
    Subscriptions subscription;


    String language = "";
    int paymentMethodClass = 1;

    ArrayList<String> mDeletedImages = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int businessIdPayment = 0;
    int businessOrderId = 0;
    boolean isPaidBusiness = false;
    String paymentAmount = "0";
    String currency = "$";
    int businessid = 0;
    int isBusiness = -1;
    String address;
    double lat = -1, lon = -1;
    File profileImgFile;
    Boolean isProfileSet = false;

    private static int whichClicked = 0;
    Boolean isuserExist = false;

    int mHour;
    int mMinute;


    TextInputLayout inputLayoutBusinessName, inputLayoutCategory, inputLayoutSubCategory, inputLayoutEmail, inputLayoutPhone, inputLayoutAdress,
            inputLayoutCountry, inputLayoutState, inputLayoutCity, inputLayoutZipCode, inputLayoutAboutBusiness,
            inputLayoutWebsite, inputLayoutMonStart, inputLayoutMonEnd, inputLayoutTueStart, inputLayoutTueEnd, inputLayoutWedStart, inputLayoutWedEnd, inputLayoutThuStart, inputLayoutThuEnd, inputLayoutFriStart,
            inputLayoutFriEnd, inputLayoutSatStart, inputLayoutSatEnd, inputLayoutSunStart, inputLayoutSunEnd, inputLayoutNumber;
    TextInputEditText etBusinessName, etCategory, etSubCategory, etEmail, etPhone, etAddress, etCountry, etState, etCity, etZipCode, etAboutBusiness,
            etWebiste, etMonStart, etMonEnd, etTueStart, etTueEnd, etWedStart, etWedEnd, etThuStart, etThuEnd, etFriStart, etFriEnd, etSatStart, etSatEnd, etSunStart, etSunEnd, etNumber;
    Button btnSave;
    ImageView imgProfileIcon;
    View view;
    Activity mActivityContext;
    Context mContext;
    ProgressBar progressBar;

    AppCompatCheckBox acceptCheckBox;
    RecyclerView rvImagesList, rvSubscriptionsList;
    LinearLayout linsubScriptionPlan;
    RelativeLayout relMain;
    ScrollView scrollView;
    LinearLayout llView;
    public Boolean isActive;

    // tollbar
    private ImageView btnToolbarBack;
    private TextView tvToolbarTitle;
    RelativeLayout toolbar;
    View iToolbar;
    private ActivityListener listener;

    int REQ_CODE_MAP2 = 107;

    public BusinessProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessProfileFragment.
     */
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_profile, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mActivityContext = getActivity();
        mContext = view.getContext();

        paymentMethodClass = 1;
        AppController.isStrip = false;
        AppController.isPayPal = false;
        AppController.stripToken = "";

        try {
            listener = (ActivityListener) mActivityContext;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
        progressBar = view.findViewById(R.id.progressBar);
        transparentStatusBar();
        setStatusBar();
        initToolBar();
        initAndSetViews();

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


    public void initToolBar() {


        iToolbar = (View) view.findViewById(R.id.iToolbar);

        iToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(getActivity());
            }
        });

        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);


        tvToolbarTitle = (TextView) iToolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.menu_business_profile));
        tvToolbarTitle.setVisibility(View.VISIBLE);


        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mActivityContext != null)
                    ApplicationUtils.hideKeyboard(mActivityContext);
                listener.callToolbarBack(true);
            }
        });


        tvToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(getActivity());
            }
        });

    }


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
        inputLayoutNumber = view.findViewById(R.id.inputLayoutNumber);


        etNumber = view.findViewById(R.id.etNumber);
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

        relMain = (RelativeLayout) view.findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(getActivity());
            }
        });


        llView = (LinearLayout) view.findViewById(R.id.llView);
        llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(getActivity());
            }
        });

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(getActivity());
            }
        });

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

                removeGalleryItem(position);

            }

            @Override
            public void onItemClick(View view, int position) {

                whichClicked = 2;
                selectMediaWithRealQuality();

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
                getActivity().startActivityForResult(intent, REQ_CODE_MAP2);
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

        if (SharedPreference.getUserData(mActivityContext).getCurrency() != null) {
            if (ApplicationUtils.isSet(SharedPreference.getUserData(mActivityContext).getCurrency()))
                currency = SharedPreference.getUserData(mActivityContext).getCurrency();
        }


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


                launchCameraIntent();
            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();


                launchGalleryIntent();


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


    void showStartTimePicker(final TextInputEditText etTextToChange, final TextInputEditText etStart, final TextInputEditText etEnd, String startTime, String endTime, final TextInputLayout inputLayoutStart, final TextInputLayout inputLayoutEnd, final String day, final String type) {


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
                                try {

                                    setTime(etTextToChange, day, hourOfDay + ":" + minute, "s");
                                    setTime(etEnd, day, hourOfDay + ":" + (minute + 10), "e");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (hourOfDay == endHour) {
                                if (minute >= endMinute) {
                                    try {
                                        setTime(etTextToChange, day, hourOfDay + ":" + minute, "s");
                                        setTime(etEnd, day, hourOfDay + ":" + (minute + 10), "e");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        setTime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                try {
                                    setTime(etTextToChange, day, hourOfDay + ":" + minute, type);
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

    void setTimeText(TextInputEditText etStartTime, String timeStartStr, TextInputEditText etEndTime, String timeEndStr, boolean isAddedFake) {

        if (!ApplicationUtils.isSet(etBusinessName.getText().toString()))
            return;

        if (isAddedFake)
            return;


        if (ApplicationUtils.isSet(timeStartStr) && ApplicationUtils.isSet(timeEndStr))
            if (timeStartStr.equals("00:00:00") && timeEndStr.equals("00:00:00"))
                return;

        SimpleDateFormat timFormatter = new SimpleDateFormat("HH:mm:ss");
        if (timeStartStr != null) {
            try {
                Date time = timFormatter.parse(timeStartStr);
                SimpleDateFormat timAmPm = new SimpleDateFormat("hh:mm a");
                etStartTime.setText(timAmPm.format(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (timeEndStr != null) {
            try {
                Date time = timFormatter.parse(timeEndStr);
                SimpleDateFormat timAmPm = new SimpleDateFormat("hh:mm a");
                etEndTime.setText(timAmPm.format(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    void setTime(TextInputEditText ettimeToSet, String day, String timeStr, String type) throws ParseException {

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
        } else if (day.equalsIgnoreCase("Fri")) {
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

                                ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_time_error), false);

                            } else if (hourOfDay == startHour) {
                                if (hourOfDay == 0) {
                                    try {
                                        setTime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    if (minute <= startMinute) {

                                        ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_time_error), false);
                                        Toast.makeText(mActivityContext, "Again Wrong Time Select", Toast.LENGTH_SHORT).show();
                                    } else {

//                                    Toast.makeText(mActivityContext, "Again right Time Select", Toast.LENGTH_SHORT).show();
                                        try {
                                            setTime(etTextToChange, day, hourOfDay + ":" + minute, type);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            } else {

                                try {
                                    setTime(etTextToChange, day, hourOfDay + ":" + minute, type);
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
        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }


    void setUserDetail(BusinessDetails user) {

        hoursList = new ArrayList<>();

        if (user.getThumbLogo() != null)
            Glide.with(mContext)
                    .load(user.getThumbLogo())
                    .error(R.drawable.user_placeholder)
                    .into(imgProfileIcon);

        etBusinessName.setText(user.getName());
        etNumber.setText(user.getOrganisation_no());

        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhoneNumber());
        if (SharedPreference.getAppLanguage(mActivityContext) != null) {
            language = SharedPreference.getAppLanguage(mActivityContext);
        } else {
            language = "en";
        }


        if (user.getBusiness_category_details() != null) {

            catId = user.getBusiness_category_details().getId();
            if (language.equalsIgnoreCase("sv")) {
                catName = user.getBusiness_category_details().getNameSv();
            } else if (language.equalsIgnoreCase("de")) {
                catName = user.getBusiness_category_details().getNameDe();
            } else if (language.equalsIgnoreCase("ar")) {
                catName = user.getBusiness_category_details().getNameAr();
            } else {
                catName = user.getBusiness_category_details().getName();
            }
            etCategory.setText(catName);
        }

        if (user.getBusiness_subcategory_details() != null) {

            subCatId = user.getBusiness_subcategory_details().getId();
            if (language.equalsIgnoreCase("sv")) {
                subCatName = user.getBusiness_subcategory_details().getNameSv();
            } else if (language.equalsIgnoreCase("de")) {
                subCatName = user.getBusiness_subcategory_details().getNameDe();
            } else if (language.equalsIgnoreCase("ar")) {
                subCatName = user.getBusiness_subcategory_details().getNameAr();
            } else {
                subCatName = user.getBusiness_subcategory_details().getName();
            }
            etSubCategory.setText(subCatName);
        }

        if ((user.getAddress() != null) && (user.getLatitude() != null) && (user.getLongitude() != null)) {
            etAddress.setText(user.getAddress());
            lat = user.getLatitude();
            lon = user.getLongitude();

        }
        if (user.getCountry_details() != null) {
            countryId = user.getCountry_details().getId();

            if (language.equalsIgnoreCase("sv")) {
                countryName = user.getCountry_details().getName_sv();
            } else if (language.equalsIgnoreCase("de")) {
                countryName = user.getCountry_details().getName_de();
            } else if (language.equalsIgnoreCase(Constants.arabic)) {
                countryName = user.getCountry_details().getName_ar();
            } else {
                countryName = user.getCountry_details().getName();
            }
            etCountry.setText(countryName);
            getAllStates();
        }
        if (user.getState_details() != null) {
            stateId = user.getState_details().getId();

            if (language.equalsIgnoreCase("sv")) {
                stateName = user.getState_details().getName_sv();
            } else if (language.equalsIgnoreCase("de")) {
                stateName = user.getState_details().getName_de();
            } else if (language.equalsIgnoreCase(Constants.arabic)) {
                stateName = user.getState_details().getName_ar();
            } else {
                stateName = user.getState_details().getName();
            }

            etState.setText(stateName);
            getAllCities();
        }
        if (user.getCity_details() != null) {
            cityId = user.getCity_details().getId();

            if (language.equalsIgnoreCase("sv")) {
                cityName = user.getCity_details().getName_sv();
            } else if (language.equalsIgnoreCase("de")) {
                cityName = user.getCity_details().getName_de();
            } else if (language.equalsIgnoreCase(Constants.arabic)) {
                cityName = user.getCity_details().getName_ar();
            } else {
                cityName = user.getCity_details().getName();
            }

            etCity.setText(cityName);
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

            for (BusinessHour hour : hoursList) {
                hour.setAddedFake(false);
            }

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


                setTimeText(etMonStart, hoursList.get(i).getStartTime(), etMonEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                // setTimeText(etMonEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());


            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Tuesday")) {
                tueStartTm = hoursList.get(i).getStartTime();
                tueEndTm = hoursList.get(i).getEndTime();


                setTimeText(etTueStart, hoursList.get(i).getStartTime(), etTueEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etTueEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Wednesday")) {
                wedStartTm = hoursList.get(i).getStartTime();
                wedEndTm = hoursList.get(i).getEndTime();


                setTimeText(etWedStart, hoursList.get(i).getStartTime(), etWedEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etWedEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Thursday")) {
                thuStartTm = hoursList.get(i).getStartTime();
                thuEndTm = hoursList.get(i).getEndTime();


                setTimeText(etThuStart, hoursList.get(i).getStartTime(), etThuEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etThuEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Friday")) {
                friStartTm = hoursList.get(i).getStartTime();
                friEndTm = hoursList.get(i).getEndTime();


                setTimeText(etFriStart, hoursList.get(i).getStartTime(), etFriEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etFriEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Saturday")) {
                satStartTm = hoursList.get(i).getStartTime();
                satEndTm = hoursList.get(i).getEndTime();


                setTimeText(etSatStart, hoursList.get(i).getStartTime(), etSatEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                // setTimeText(etSatEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Sunday")) {
                sunStartTm = hoursList.get(i).getStartTime();
                sunEndTm = hoursList.get(i).getEndTime();


                setTimeText(etSunStart, hoursList.get(i).getStartTime(), etSunEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etSunEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            }
        }

        if (user.getBusiness_images() != null && user.getBusiness_images().size() > 0) {
            businessImageList = user.getBusiness_images();

            for (int i = 0; i < businessImageList.size(); i++) {
                businessImageList.get(i).setUploaded(true);

            }


        } else {
            businessImageList = new ArrayList<>();
        }

        if (businessImageList.size() < 5) {
            BusinessImage businessImage = new BusinessImage();
            businessImage.setId(-1);
            businessImageList.add(0, businessImage);
        }


        linsubScriptionPlan.setVisibility(View.VISIBLE);

        if (user.getIs_business() == 1) {
            isBusiness = 1;

            if (ApplicationUtils.isSet(user.getPaymentStatus()))
                if (user.getPaymentStatus().equalsIgnoreCase("paid")) {
                    btnSave.setText(mContext.getResources().getString(R.string.save));
                    linsubScriptionPlan.setVisibility(View.GONE);
                }

        } else {
            isBusiness = user.getIs_business();
            if (user.getBusiness_subscriptions() != null) {
                subscriptionId = user.getBusiness_subscriptions().getId();
                subscriptionIdTemp = user.getBusiness_subscriptions().getId();
            } else {
                subscriptionId = -1;
                subscriptionIdTemp = -1;
            }


        }

        getAllSubscriptons();

        mGAdapter.addItems(businessImageList);

        if (user.getAboutBusiness() != null) {
            etAboutBusiness.setText(user.getAboutBusiness());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == REQ_CODE_MAP2) {
                if (data != null) {
                    address = data.getStringExtra("address");
                    lat = data.getDoubleExtra("lat", 0.0);
                    lon = data.getDoubleExtra("lon", 0.0);
                    if (address != null && address != "") {
                        etAddress.setText(address);
                    }

                }

            } else if (requestCode == REQUEST_CODE_PAYMENT) {


                if (AppController.isPayPal || AppController.isStrip) {
                    saveData();
                }

            } else {
                if (data != null) {
                    if (whichClicked == 1) {
                        File file = null;
                        if (resultCode == Activity.RESULT_OK) {
                            Uri uri = data.getParcelableExtra("path");
                            try {
                                // You can update this bitmap to your server


                                // loading profile image from local cache
                                String path = uri.toString();
                                file = new File(new URI(path));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }

                        if (file != null) {
                            profileImgFile = file;
                            isProfileSet = true;
                            Glide.with(mActivityContext).load(file).into(imgProfileIcon);
                        }
                    } else if (whichClicked == 2) {
                        BusinessImage businessImage = new BusinessImage();
                        businessImage.setId(-1);
                        File file = null;
                        if (resultCode == Activity.RESULT_OK) {
                            Uri uri = data.getParcelableExtra("path");
                            try {
                                String path = uri.toString();
                                file = new File(new URI(path));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                        businessImage.setUploaded(false);
                        businessImage.setImgFile(file);
                        businessImageList.remove(0);
                        businessImageList.add(businessImage);
                        if (businessImageList.size() < 5) {
                            BusinessImage emptyImage = new BusinessImage();
                            emptyImage.setId(-1);
                            businessImageList.add(0, emptyImage);
                        }
                        mGAdapter.addItems(businessImageList);
                        mGAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }


    int REQUEST_IMAGE = 300;

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
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

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    public void paymentFailure(String msg) {

        DialogFactory.showDropDownNotificationError(mActivityContext,
                mContext.getString(R.string.alert_information),
                msg);

    }

    void setDataIfUserNotExist() {

        getAllCategories();
        getAllcountries();
        getAllStates();
        getAllCities();

        hoursList = new ArrayList<>();
        hoursList.add(new BusinessHour("Monday", "00:00:00", "00:00:00"));
        hoursList.add(new BusinessHour("Tuesday", "00:00:00", "00:00:00"));
        hoursList.add(new BusinessHour("Wednesday", "00:00:00", "00:00:00"));
        hoursList.add(new BusinessHour("Thursday", "00:00:00", "00:00:00"));
        hoursList.add(new BusinessHour("Friday", "00:00:00", "00:00:00"));
        hoursList.add(new BusinessHour("Saturday", "00:00:00", "00:00:00"));
        hoursList.add(new BusinessHour("Sunday", "00:00:00", "00:00:00"));

        for (int i = 0; i < hoursList.size(); i++) {
            if (hoursList.get(i).getDay().equalsIgnoreCase("Monday")) {


                monStartTm = hoursList.get(i).getStartTime();
                monEndTm = hoursList.get(i).getEndTime();


                setTimeText(etMonStart, hoursList.get(i).getStartTime(), etMonEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etMonEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Tuesday")) {

                tueStartTm = hoursList.get(i).getStartTime();
                tueEndTm = hoursList.get(i).getEndTime();


                setTimeText(etTueStart, hoursList.get(i).getStartTime(), etTueEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                // setTimeText(etTueEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Wednesday")) {

                wedStartTm = hoursList.get(i).getStartTime();
                wedEndTm = hoursList.get(i).getEndTime();


                setTimeText(etWedStart, hoursList.get(i).getStartTime(), etWedEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                // setTimeText(etWedEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Thursday")) {

                thuStartTm = hoursList.get(i).getStartTime();
                thuEndTm = hoursList.get(i).getEndTime();


                setTimeText(etThuStart, hoursList.get(i).getStartTime(), etThuEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etThuEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Friday")) {

                friStartTm = hoursList.get(i).getStartTime();
                friEndTm = hoursList.get(i).getEndTime();


                setTimeText(etFriStart, hoursList.get(i).getStartTime(), etFriEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //setTimeText(etFriEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Saturday")) {

                satStartTm = hoursList.get(i).getStartTime();
                satEndTm = hoursList.get(i).getEndTime();


                setTimeText(etSatStart, hoursList.get(i).getStartTime(), etSatEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                // setTimeText(etSatEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            } else if (hoursList.get(i).getDay().equalsIgnoreCase("Sunday")) {

                sunStartTm = hoursList.get(i).getStartTime();
                sunEndTm = hoursList.get(i).getEndTime();


                setTimeText(etSunStart, hoursList.get(i).getStartTime(), etSunEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());
                //  setTimeText(etSunEnd, hoursList.get(i).getEndTime(), hoursList.get(i).isAddedFake());

            }
        }

        businessImageList = new ArrayList<>();
        BusinessImage businessImage = new BusinessImage();
        businessImage.setId(-1);
        businessImageList.add(0, businessImage);
        mGAdapter.addItems(businessImageList);
        subscriptionId = -1;
        subscriptionIdTemp = -1;
        getAllSubscriptons();

    }

    List<BusinessCategory> businessCategories;

    void getAllCategories() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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

    RecyclerView list;
    int catIdTemp = -1;
    String catNameTemp = "";
    int catId = -1;
    String catName = "";
    AllCatAdaptor allCatAdaptor;
    BusinessCategory businessCategory;

    void showCategoryDialog() {

        catIdTemp = -1;
        catNameTemp = "";
        businessCategory = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = mActivityContext.getLayoutInflater().inflate(R.layout.dialog_list, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.tv_select_category));

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
                if (language.equalsIgnoreCase("sv")) {
                    catNameTemp = country.getNameSv();
                } else if (language.equalsIgnoreCase("de")) {
                    catNameTemp = country.getNameDe();
                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                    catNameTemp = country.getNameAr();
                } else {
                    catNameTemp = country.getName();
                }

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
        builder.setTitle(getResources().getString(R.string.tv_sub_category));

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
                if (language.equalsIgnoreCase("sv")) {
                    subCatNameTemp = country.getNameSv();
                } else if (language.equalsIgnoreCase("de")) {
                    subCatNameTemp = country.getNameDe();
                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                    subCatNameTemp = country.getNameAr();
                } else {
                    subCatNameTemp = country.getName();
                }

            }
        }, subCatId);
        list.setAdapter(allSubCatAdaptor);
//        adaptor.addItems(countries);
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

                if (language.equalsIgnoreCase("sv")) {
                    countryNameTemp = country.getName_sv();
                } else if (language.equalsIgnoreCase("de")) {
                    countryNameTemp = country.getName_de();
                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                    countryNameTemp = country.getName_ar();
                } else {
                    countryNameTemp = country.getName();
                }

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
        builder.setTitle(getResources().getString(R.string.select_state));

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

                if (language.equalsIgnoreCase("sv")) {
                    stateNameTemp = country.getName_sv();
                } else if (language.equalsIgnoreCase("de")) {
                    stateNameTemp = country.getName_de();
                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                    stateNameTemp = country.getName_ar();
                } else {
                    stateNameTemp = country.getName();
                }

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
        builder.setTitle(getResources().getString(R.string.select_city));

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

                if (language.equalsIgnoreCase("sv")) {
                    cityNameTemp = country.getName_sv();
                } else if (language.equalsIgnoreCase("de")) {
                    cityNameTemp = country.getName_de();
                } else if (language.equalsIgnoreCase(Constants.arabic)) {
                    cityNameTemp = country.getName_ar();
                } else {
                    cityNameTemp = country.getName();
                }

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
//            etBusinessName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutBusinessName.setError(getResources().getString(R.string.tv_err_msg_name));
//            etBusinessName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_business_name), false);
            return false;
        }
        return true;
    }

    public boolean validateCategoryName(boolean errorShouldShow) {
        if (catId == -1) {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_category), false);
            return false;

        }
        return true;
    }

    public boolean validateSubCategoryName(boolean errorShouldShow) {
        if (subCatId == -1) {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_sub_category), false);
            return false;
        }
        return true;
    }


    public boolean validateEmail(boolean errorShouldShow) {
        if (ApplicationUtils.validateEmail(etEmail.getText().toString())) {
//            inputLayoutEmail.setErrorEnabled(false);
//            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutEmail.setError(getResources().getString(R.string.tv_err_msg_email));
//            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_business_email), false);
            return false;
        }
        return true;
    }

    public boolean validatePhone(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etPhone.getText().toString())) {
//            inputLayoutPhone.setErrorEnabled(false);
//            etPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutPhone.setError(getResources().getString(R.string.tv_err_msg_phone));
//            etPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_phone), false);
            return false;
        }
        return true;
    }


    public boolean validateZipCode(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etZipCode.getText().toString())) {
        } else {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_zipcode), false);
            return false;
        }
        return true;
    }


    public boolean validateSubScription(boolean errorShouldShow) {
        if (!isuserExist) {
            if (subscriptionId == -1) {
                ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_subscription), false);
                return false;
            }
        } else {
            if (isBusiness == 0) {
                if (subscriptionId == -1) {
                    ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_subscription), false);
                    return false;
                }
            }

        }
        return true;

    }


    public boolean validateCountry(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etCountry.getText().toString())) {
//            inputLayoutCountry.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutCountry.setError(getResources().getString(R.string.tv_err_msg_country));
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_country), false);
            return false;
        }
        return true;
    }


    public boolean validateState(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etState.getText().toString())) {
//            inputLayoutState.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutState.setError(getResources().getString(R.string.tv_err_msg_state));
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_state), false);
            return false;
        }
        return true;
    }

    public boolean validateCity(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etCity.getText().toString())) {
//            inputLayoutCity.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutCity.setError(getResources().getString(R.string.tv_err_msg_city));
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_city), false);

            return false;
        }
        return true;
    }

    public boolean validateAddress(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etAddress.getText().toString())) {
//            inputLayoutAdress.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutAdress.setError(getResources().getString(R.string.tv_err_msg_address));
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_address), false);
            return false;
        }
        return true;
    }

    public boolean validateDetails(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etAboutBusiness.getText().toString())) {
//            inputLayoutAdress.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutAdress.setError(getResources().getString(R.string.tv_err_msg_address));
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_business_detail), false);
            return false;
        }
        return true;
    }


    public boolean validateNumber(boolean errorShouldShow) {
        if (ApplicationUtils.validateName(etNumber.getText().toString())) {
//            inputLayoutAdress.setErrorEnabled(false);
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_auth_selected, 0);
        } else {
//            if (errorShouldShow)
//                inputLayoutAdress.setError(getResources().getString(R.string.tv_err_msg_address));
//            etCountry.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_organization_number), false);
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
        if (validateNumber(true) == false)
            return;
        if (validateDetails(true) == false)
            return;
        if (validateSubScription(true) == false)
            return;

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mActivityContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {


            if (isPaidBusiness) {
                saveData();
            } else if ((AppController.isPayPal || AppController.isStrip) && subscriptionIdForPayment == subscriptionId) {
                saveData();
            } else {
                subscriptionIdForPayment = subscriptionId;
                paymentMethodClass = 1;
                AppController.isStrip = false;
                AppController.isPayPal = false;
                AppController.stripToken = "";
                paymentBottomSheet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    RequestBody website;

    void saveData() {


        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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
        // RequestBody id = RequestBody.create(MediaType.parse("text/plain"), bu);
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
        website = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");
        if (ApplicationUtils.isSet(etWebiste.getText().toString()))
            website = RequestBody.create(okhttp3.MediaType.parse("text/plain"), etWebiste.getText().toString());
        RequestBody about_business = RequestBody.create(MediaType.parse("text/plain"), etAboutBusiness.getText().toString());
        RequestBody org_number = RequestBody.create(MediaType.parse("text/plain"), etNumber.getText().toString());
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


        RequestBody subscription_id = null;
        subscription_id = RequestBody.create(MediaType.parse("text/plain"), subscriptionId + "");


        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;


        RequestBody paymentMethod = RequestBody.create(MediaType.parse("text/plain"), "paypal");
        RequestBody stripToken = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody isPaidStrip = RequestBody.create(MediaType.parse("text/plain"), "");

        if (!isPaidBusiness) {

            if (AppController.isPayPal) {
                paymentMethod = RequestBody.create(MediaType.parse("text/plain"), "paypal");
                isPaidStrip = RequestBody.create(MediaType.parse("text/plain"), "1");


            } else if (AppController.isStrip) {
                stripToken = RequestBody.create(MediaType.parse("text/plain"), AppController.stripToken);
                paymentMethod = RequestBody.create(MediaType.parse("text/plain"), "credit_card");

            }


        }


        if (!isPaidBusiness && AppController.isStrip) {


            Call<BusinessProfileResponse> call = api.updateBusinessProfileStrip(token,
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
                    website, imageBodyPart, about_business, start_time, end_time, day, subscription_id, org_number, paymentMethod, stripToken, delete_images, imageParts);
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


                                paymentMethodClass = 1;
                                AppController.stripToken = "";
                                AppController.isStrip = false;
                                AppController.isPayPal = false;

                                ApplicationUtils.showToast(mActivityContext, response.body().getMessage(), true);

                                isBusinessAccount = false;
                                isBusinessAccountPaid = false;

                                BusinessProfileResponseData data = response.body().getData();


                                if (data.getData() != null) {
                                    isBusinessAccount = true;
                                    isuserExist = true;
                                    setUserDetail(data.getData());
                                    // Toast.makeText(mActivityContext, "User exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    isuserExist = false;
                                    // Toast.makeText(mActivityContext, "User not exist", Toast.LENGTH_SHORT).show();
                                    setDataIfUserNotExist();
                                }

                                mDeletedImages.clear();
                                mDeletedImages = new ArrayList<>();


                                if (response.body().getData().getOrder() != null) {
                                    BusinessOrderDetail businessOrderDetail = response.body().getData().getOrder();
                                    businessIdPayment = businessOrderDetail.getBusinessId();
                                    businessOrderId = businessOrderDetail.getId();
                                    paymentAmount = businessOrderDetail.getAmount();
                                    isPaidBusiness = false;
                                    if (businessOrderDetail.getStatus().equals("paid")) {
                                        isPaidBusiness = true;
                                        isBusinessAccountPaid = true;
                                    }

                                    if (isPaidBusiness) {
                                        linsubScriptionPlan.setVisibility(View.GONE);
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
        } else if (!isPaidBusiness && AppController.isPayPal) {
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
                    website, imageBodyPart, about_business, delete_images, imageParts, start_time, end_time, day, subscription_id, org_number, paymentMethod, isPaidStrip);
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

                                paymentMethodClass = 1;
                                AppController.stripToken = "";
                                AppController.isStrip = false;
                                AppController.isPayPal = false;

                                ApplicationUtils.showToast(mActivityContext, response.body().getMessage(), true);

                                isBusinessAccount = false;
                                isBusinessAccountPaid = false;

                                BusinessProfileResponseData data = response.body().getData();


                                if (data.getData() != null) {
                                    isBusinessAccount = true;
                                    isuserExist = true;
                                    setUserDetail(data.getData());
                                    // Toast.makeText(mActivityContext, "User exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    isuserExist = false;
                                    // Toast.makeText(mActivityContext, "User not exist", Toast.LENGTH_SHORT).show();
                                    setDataIfUserNotExist();
                                }

                                mDeletedImages.clear();
                                mDeletedImages = new ArrayList<>();


                                if (response.body().getData().getOrder() != null) {
                                    BusinessOrderDetail businessOrderDetail = response.body().getData().getOrder();
                                    businessIdPayment = businessOrderDetail.getBusinessId();
                                    businessOrderId = businessOrderDetail.getId();
                                    paymentAmount = businessOrderDetail.getAmount();
                                    isPaidBusiness = false;
                                    if (businessOrderDetail.getStatus().equals("paid")) {
                                        isPaidBusiness = true;
                                        isBusinessAccountPaid = true;
                                    }
                                    if (isPaidBusiness) {
                                        linsubScriptionPlan.setVisibility(View.GONE);
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
        } else {
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
                    website, imageBodyPart, about_business, delete_images, imageParts, start_time, end_time, day, subscription_id, org_number);
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

                                paymentMethodClass = 1;
                                AppController.stripToken = "";
                                AppController.isStrip = false;
                                AppController.isPayPal = false;

                                ApplicationUtils.showToast(mActivityContext, response.body().getMessage(), true);

                                isBusinessAccount = false;
                                isBusinessAccountPaid = false;

                                BusinessProfileResponseData data = response.body().getData();


                                if (data.getData() != null) {
                                    isBusinessAccount = true;
                                    isuserExist = true;
                                    setUserDetail(data.getData());
                                    // Toast.makeText(mActivityContext, "User exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    isuserExist = false;
                                    // Toast.makeText(mActivityContext, "User not exist", Toast.LENGTH_SHORT).show();
                                    setDataIfUserNotExist();
                                }

                                mDeletedImages.clear();
                                mDeletedImages = new ArrayList<>();


                                if (response.body().getData().getOrder() != null) {
                                    BusinessOrderDetail businessOrderDetail = response.body().getData().getOrder();
                                    businessIdPayment = businessOrderDetail.getBusinessId();
                                    businessOrderId = businessOrderDetail.getId();
                                    paymentAmount = businessOrderDetail.getAmount();
                                    isPaidBusiness = false;
                                    if (businessOrderDetail.getStatus().equals("paid")) {
                                        isPaidBusiness = true;
                                        isBusinessAccountPaid = true;
                                    }


                                    if (isPaidBusiness) {
                                        linsubScriptionPlan.setVisibility(View.GONE);
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


    }

    private void getUserDetails() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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

                            paymentMethodClass = 1;
                            AppController.stripToken = "";
                            AppController.isStrip = false;
                            AppController.isPayPal = false;

                            isPaidBusiness = false;
                            isBusinessAccount = false;
                            isBusinessAccountPaid = false;
                            BusinessDetails user = response.body().getData().getData();
                            if (user != null) {
                                isBusinessAccount = true;
                                isuserExist = true;
                                setUserDetail(user);
                                if (ApplicationUtils.isSet(user.getPaymentStatus()))
                                    if (user.getPaymentStatus().equals("paid")) {
                                        isPaidBusiness = true;
                                        isBusinessAccountPaid = true;
                                    }

                            } else {
                                isuserExist = false;
                                setDataIfUserNotExist();
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

        businessImageList.remove(position);
        if (businessImageList.get(0).getId() == -1) {
            if (businessImageList.get(0).getImgFile() != null) {
                BusinessImage businessImage = new BusinessImage();
                businessImage.setId(-1);
                businessImage.setUploaded(false);
                businessImageList.add(0, businessImage);
            }
        } else {
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
    int subscriptionIdForPayment = -1;
    int subscriptionId = -1;
    int subscriptionIdTemp = -1;
    SubscriptionsAdaptor subscriptionsAdaptor;

    void getAllSubscriptons() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


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
                                subscriptionsAdaptor = new SubscriptionsAdaptor(subscriptions, mActivityContext, currency, new SubscriptionsAdaptor.RecItemClick() {
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

    /**********************************************************************************************************************************************************
     * PaymentCode
     ***********************************************************************************************************************************************************/


    public void callPaypalPayment(String amount, String itemName) {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mActivityContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        whichClicked = 3;
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, amount, itemName);
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

                        getUserDetails();


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

    int REQUEST_STRIP = 900;

    public void openStrip() {
        paymentMethodClass = 2;
        Intent intent = new Intent(mContext, StripActivity.class);
        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    public void openPayPal() {
        paymentMethodClass = 1;
        Intent intent = new Intent(mContext, SubscriptionsFragment.class);
        intent.putExtra("type", "package");
        intent.putExtra("package_id", String.valueOf(subscriptionId));
        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }


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
}
