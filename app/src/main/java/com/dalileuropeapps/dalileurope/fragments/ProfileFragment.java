package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.MapsActivity;
import com.dalileuropeapps.dalileurope.adapter.CityLisAdaptor;
import com.dalileuropeapps.dalileurope.adapter.CountryListAdaptor;
import com.dalileuropeapps.dalileurope.adapter.StateListAdaptor;
import com.dalileuropeapps.dalileurope.api.retrofit.City;
import com.dalileuropeapps.dalileurope.api.retrofit.CityResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.Country;
import com.dalileuropeapps.dalileurope.api.retrofit.CountryResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultLogin;
import com.dalileuropeapps.dalileurope.api.retrofit.State;
import com.dalileuropeapps.dalileurope.api.retrofit.StateResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.imagepicke_pack.ImagePickerActivity;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import static com.dalileuropeapps.dalileurope.utils.ApplicationUtils.showToast;


public class ProfileFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    OnProfileFragmentInteractionListener mListener;
    int cityIdTemp = -1, cityId = -1;
    String cityNameTemp, cityName = "";
    CityLisAdaptor adaptor3;

    int stateIdTemp = -1, stateId = -1;
    String stateNameTemp = "", stateName = "";
    StateListAdaptor adaptor2;
    RecyclerView list;
    int countryIdTemp = -1, countryId = -1;
    String countryNameTemp, countryName = "";
    EditText etName;
    public CountryListAdaptor adaptor;

    List<City> cities;

    List<Country> countries;

    List<State> states;
    String address;
    double lat = -1, lon = -1;
    File profileImgFile;
    Boolean isProfileSet = false;
    public Boolean isProfileActive = false;

    private ImageView btnToolbarBack;
    private TextView tvToolbarTitle;
    View iToolbar;
    private ActivityListener listener;

    int REQ_CODE_MAP = 101;
    TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutEmail, inputLayoutPhone, inputLayoutAdress,
            inputLayoutCountry, inputLayoutState, inputLayoutCity, inputLayoutZipCode, inputLayoutAboutMe;
    TextInputEditText etFirstName, etLastName, etEmail, etFirstPhone, etAddress, etCountry, etState, etCity, etZipCode, etAboutMe;
    Button btnSave;
    ImageView imgProfileIcon;
    View view;
    Activity mActivityContext;
    Context mContext;
    ProgressBar progressBar;

    //hide keyboard
    RelativeLayout relMain;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String language = "";

//    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mActivityContext = getActivity();
        mContext = view.getContext();

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
        return view;
    }


    public void setStatusBar() {
        if (ApplicationUtils.getDeviceStatusBarHeight(getActivity()) > 0) {
            transparentStatusBar();
        }
    }


    public void initToolBar() {


        iToolbar = (View) view.findViewById(R.id.iToolbar);

        btnToolbarBack = (ImageView) iToolbar.findViewById(R.id.btn_toolbar_back);
        btnToolbarBack.setVisibility(View.VISIBLE);
        btnToolbarBack.setImageResource(R.drawable.ic_menu);


        tvToolbarTitle = (TextView) iToolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(mContext.getResources().getString(R.string.menu_profile));


        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mActivityContext != null)
                    ApplicationUtils.hideKeyboard(mActivityContext);
                listener.callToolbarBack(true);
            }
        });


    }

    public void initAndSetViews() {
        if (SharedPreference.getAppLanguage(mActivityContext) != null) {
            language = SharedPreference.getAppLanguage(mActivityContext);
        } else {
            language = "en";
        }

        progressBar = view.findViewById(R.id.progressBar);

        inputLayoutFirstName = view.findViewById(R.id.inputLayoutFirstName);
        inputLayoutLastName = view.findViewById(R.id.inputLayoutLastName);
        inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
        inputLayoutPhone = view.findViewById(R.id.inputLayoutPhone);
        inputLayoutAdress = view.findViewById(R.id.inputLayoutAdress);
        inputLayoutCountry = view.findViewById(R.id.inputLayoutCountry);
        inputLayoutState = view.findViewById(R.id.inputLayoutState);
        inputLayoutCity = view.findViewById(R.id.inputLayoutCity);
        inputLayoutZipCode = view.findViewById(R.id.inputLayoutZipCode);
        inputLayoutAboutMe = view.findViewById(R.id.inputLayoutAboutMe);

        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etEmail = view.findViewById(R.id.etEmail);
        etFirstPhone = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);
        etCountry = view.findViewById(R.id.etCountry);
        etState = view.findViewById(R.id.etState);
        etCity = view.findViewById(R.id.etCity);
        etZipCode = view.findViewById(R.id.etZipCode);
        etAboutMe = view.findViewById(R.id.etAboutMe);

        btnSave = view.findViewById(R.id.btnSave);
        imgProfileIcon = (ImageView) view.findViewById(R.id.imgProfileIcon);


//        etFirstPhone.addTextChangedListener(phoneTextChangedListeners);

        relMain = (RelativeLayout) view.findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.hideKeyboard(getActivity());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countries != null)
                    if (countries.size() > 0)
                        showCountryDialog();
            }
        });
        etState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (states != null)
                    if (states.size() > 0)
                        showAllAllStatesDialog();
            }
        });
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cities != null)
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
                getActivity().startActivityForResult(intent, REQ_CODE_MAP);
            }
        });
        imgProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMediaWithRealQuality();
            }
        });


        getAllCountries();
        getUserDetails();

    }


    void setUserDetail(User user) {

        if (user.getThumbImage() != null)
            Glide.with(mContext)
                    .load(user.getThumbImage())
                    .error(R.drawable.user_placeholder)
                    .into(imgProfileIcon);

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        etFirstPhone.setText(user.getPhoneNumber());
        if (user.getAddress() != null && user.getLatitude() != null && user.getLongitude() != null) {
            etAddress.setText(user.getAddress());
            lat = user.getLatitude();
            lon = user.getLongitude();

        }
        if (user.getCountry() != null) {
            countryId = user.getCountry().getId();
            countryName = user.getCountry().getName();
            etCountry.setText(user.getCountry().getName());
            getAllStates();
        }
        if (user.getState() != null) {
            stateId = user.getState().getId();
            stateName = user.getState().getName();
            etState.setText(user.getState().getName());
            getAllCities();
        }
        if (user.getCity() != null) {
            cityId = user.getCity().getId();
            cityName = user.getCity().getName();
            etCity.setText(user.getCity().getName());
        }
        if (user.getZipCode() != null) {
            etZipCode.setText(user.getZipCode().toString());
        }
        if (user.getAboutMe() != null) {
            etAboutMe.setText(user.getAboutMe());
        }
    }


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_MAP) {
                if (data != null) {
                    address = data.getStringExtra("address");
                    lat = data.getDoubleExtra("lat", 0.0);
                    lon = data.getDoubleExtra("lon", 0.0);
                    if (address != null && address != "") {
                        etAddress.setText(address);
                    }

                }

            } else {
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


//                File file = ImagePicker.Companion.getFile(data);

                if (file != null) {
                    Glide.with(mActivityContext).load(file).into(imgProfileIcon);
                    profileImgFile = file;
                    isProfileSet = true;
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

    void getAllCountries() {

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
        dialog.show();


    }


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

        dialog.show();
    }


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


    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        if (mContext instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileUpdated(Boolean updted);
    }

    public boolean validateFirstName(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etFirstName.getText().toString())) {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_name), false);
            return false;
        }
        return true;
    }

    public boolean validateLastName(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etLastName.getText().toString())) {
            if (errorShouldShow)
                ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_last_name), false);
            return false;
        }
        return true;
    }


    public boolean validateEmail(boolean errorShouldShow) {
        if (!ApplicationUtils.validateEmail(etEmail.getText().toString())) {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_email), false);
            return false;
        }
        return true;
    }

    public boolean validatePhone(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etFirstPhone.getText().toString())) {

            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_phone), false);
            return false;
        }
        return true;
    }


    public boolean validateZipCode(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etZipCode.getText().toString())) {

            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_zipcode), false);
            return false;
        }
        return true;
    }


    public boolean validateCountry(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etCountry.getText().toString())) {

            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_country), false);
            return false;
        }
        return true;
    }


    public boolean validateState(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etState.getText().toString())) {

            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_state), false);
            return false;
        }
        return true;
    }

    public boolean validateCity(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etCity.getText().toString())) {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_city), false);
            return false;
        }
        return true;
    }

    public boolean validateAddress(boolean errorShouldShow) {
        if (!ApplicationUtils.validateName(etAddress.getText().toString())) {
            ApplicationUtils.showToast(mActivityContext, getResources().getString(R.string.tv_err_msg_address), false);
            return false;
        }
        return true;
    }


    private void validateData() {

        if (validateFirstName(true) == false)
            return;
        if (validateLastName(true) == false)
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

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError(mActivityContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }
        try {
            saveData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void saveData() {

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


        RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), etFirstName.getText().toString());
        RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), etLastName.getText().toString());

        RequestBody phone_number = RequestBody.create(MediaType.parse("text/plain"), etFirstPhone.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), etAddress.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), lat + "");
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), lon + "");

        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), countryId + "");
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), stateId + "");
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), cityId + "");
        RequestBody zip_code = RequestBody.create(MediaType.parse("text/plain"), etZipCode.getText().toString());
        RequestBody about_me = RequestBody.create(MediaType.parse("text/plain"), etAboutMe.getText().toString());
        MultipartBody.Part imageBodyPart = null;
        if (profileImgFile != null) {
            RequestBody image = RequestBody.create(
                    MediaType.parse("image/jpeg"),
                    profileImgFile
            );
            imageBodyPart = MultipartBody.Part.createFormData("image", profileImgFile.getName(), image);
        }


        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;

        Call<ResultLogin> call = api.updateProfile(token, first_name, last_name, phone_number, address, latitude, longitude, country, state, city, imageBodyPart, zip_code, about_me);
        showProgressBar(true);
        call.enqueue(new Callback<ResultLogin>() {
            @Override
            public void onResponse(Call<ResultLogin> call,
                                   final Response<ResultLogin> response) {
                try {
                    showProgressBar(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getUser() != null) {
                            ApplicationUtils.showToast(mActivityContext, response.body().getMessage(), true);
                            SharedPreference.saveProfileDefaults(mContext, response.body().getUser());


                            mListener.onProfileUpdated(true);


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
            public void onFailure(Call<ResultLogin> call, Throwable t) {
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

        if (!ApplicationUtils.isOnline(mContext)) {
            DialogFactory.showDropDownNotificationError((Activity) mContext,
                    mContext.getString(R.string.alert_information),
                    mContext.getString(R.string.alert_internet_connection));
            return;
        }


        String token = SharedPreference.getSharedPrefValue(getActivity(), Constants.USER_TOKEN);
        token = "Bearer " + token;

        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        Call<ResultLogin> call = api.getUserDetail(token);

        call.enqueue(new Callback<ResultLogin>() {
            @Override
            public void onResponse(Call<ResultLogin> call,
                                   final Response<ResultLogin> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getUser() != null) {

                            SharedPreference.saveProfileDefaults(mContext, response.body().getUser());

                            User user = response.body().getUser();
                            if (user != null) {
                                setUserDetail(user);
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
            public void onFailure(Call<ResultLogin> call, Throwable t) {
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


//    TextWatcher phoneTextChangedListeners = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (s.toString().length() > 0) {
//                inputLayoutPhone.setHint("Phone");
//            } else {
//                inputLayoutPhone.setHint("Phone");
//            }
//        }
//    };


}
