package com.dalileuropeapps.dalileurope.activities;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessDetails;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessSubscriptions;
import com.dalileuropeapps.dalileurope.api.retrofit.LanguageResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.fragments.AddListingFragment;
import com.dalileuropeapps.dalileurope.fragments.BusinessProfileFragment;
import com.dalileuropeapps.dalileurope.fragments.ChangePasswordActivity;
import com.dalileuropeapps.dalileurope.fragments.ContentPagesFragment;
import com.dalileuropeapps.dalileurope.fragments.DashboardFragment;
import com.dalileuropeapps.dalileurope.fragments.FavrouteAddListingFragment;
import com.dalileuropeapps.dalileurope.fragments.MessagesListFragment;
import com.dalileuropeapps.dalileurope.fragments.ProfileFragment;
import com.dalileuropeapps.dalileurope.interfaces.ActivityListener;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.network.ApiInterface;
import com.dalileuropeapps.dalileurope.network.RetrofitClient;
import com.dalileuropeapps.dalileurope.notifications.NotificationConfig;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.DeviceUuidFactory;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dalileuropeapps.dalileurope.utils.ApplicationUtils.showToast;

//import com.examples.dalileurope.models.MessageData;


public class MainActivity extends FusedLocationActivity implements View.OnClickListener, ProfileFragment.OnProfileFragmentInteractionListener, MessagesListFragment.OnListFragmentInteractionListener, ActivityListener {

    RelativeLayout rlContentMain;
    DrawerLayout dlMain;
    NavigationView nvMain;
    ImageView ivToolbar;

    ImageView ivUserImage;
    TextView tvUserName;
    TextView tvAddress;
    LinearLayout llLang;
    AppCompatActivity mContext;

    static String userId;
    static String userName;
    static String userEmail;
    static String userLocation;
    static String userImage;
    static double userLAT = 0.0;
    static double userLNG = 0.0;
    View container;
    DashboardFragment dashboardFragment;

    public static boolean isBusinessAccount = false;
    public static boolean isBusinessAccountPaid = false;
    BusinessSubscriptions businessSubscriptions = null;
    String businessExpiry;
    boolean isExpiredSubscription = false;


    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    // public static final String CONFIG_CLIENT_ID = "AftjBeEHDz9lIATv4-XE1-CJzsjJeIo7eOC2vGOhI8GucdYJs5uzxBtqsI3UkulJKm98wXYRDXyvFzJe";
    public static final String CONFIG_CLIENT_ID = "AVr1ED419UPA_3IvsMMYOVhcJlWYMzAuiYxFcAoaebgDNyQRTW0Mwwpw5I25-gG1d-SEnKO8yOUTGzAz";

    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_PAYMENT_PAYPAL = 301;
    public static final int REQUEST_CODE_PAYMENT_STRIP = 302;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    public static final int REQUEST_CODE_PROFILE_SHARING = 3;
    public static int REQ_CODE_MAP_PROFILEFRAGMENT = 101;
    public static int REQ_CODE_MAP_BUSINESSFRAGMENT = 103;
    public static final int AUTOCOMPLETE_REQUEST_CODE_DASHBOARD = 102;

    public static PayPalConfiguration paymentConfig = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        mContext = this;
        statusBarTextColorWhite();
        initUserProfileData();
        initViews();
        setData();
        registerFCMUser();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paymentConfig);
        startService(intent);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    ProgressBar progressBar;

    public void initViews() {

        dlMain = findViewById(R.id.dlMain);
        rlContentMain = findViewById(R.id.rlContentMain);
        nvMain = findViewById(R.id.nvMain);
        nvMain.setItemIconTintList(null);
        ivToolbar = findViewById(R.id.ivToolbar);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progressBar);
        initHeaderView();
    }

    String userAddress = "";

    private void initUserProfileData() {

        User userModel = SharedPreference.getUserData(mContext);
        if (userModel != null) {
            userId = String.valueOf(userModel.getId());
            userName = userModel.getFirstName();
            userEmail = userModel.getEmail();
            userImage = userModel.getThumbImage();
            if (userModel.getLatitude() != null && userModel.getLatitude() != null) {
                userLAT = userModel.getLatitude();
                userLNG = userModel.getLongitude();
            } else {
                userLAT = 0;
                userLNG = 0;
            }
            userAddress = userModel.getAddress();


            isBusinessAccount = SharedPreference.getBoolSharedPrefValue(mContext, Constants.USERDEFAULT_ISBUSINESSACCOUNT, false);
            isBusinessAccountPaid = SharedPreference.getBoolSharedPrefValue(mContext, Constants.USERDEFAULT_PAIDSUBCRIPTION, false);

            if (isBusinessAccount && isBusinessAccountPaid) {
                if (ApplicationUtils.isSet(SharedPreference.getSharedPrefValue(mContext, Constants.USERDEFAULT_SUBSCRIPTION_INFO))) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BusinessSubscriptions>() {
                    }.getType();
                    businessSubscriptions = gson.fromJson(SharedPreference.getSharedPrefValue(mContext, Constants.USERDEFAULT_SUBSCRIPTION_INFO), type);
                    if (ApplicationUtils.isSet(businessSubscriptions.getExpiredAt())) {
                        businessExpiry = businessSubscriptions.getExpiredAt();
                        isExpiredSubscription = ApplicationUtils.isUserSubscriptionExpired(businessExpiry);
                    }
                }
            }

        }
    }

    TextView tvLng;
    ImageView ivLang;

    public void initHeaderView() {
        View header = nvMain.getHeaderView(0);
        ivUserImage = header.findViewById(R.id.ivUserImage);

        tvUserName = header.findViewById(R.id.tvUserName);
        tvAddress = header.findViewById(R.id.tvAddress);
        llLang = header.findViewById(R.id.llLang);

        tvLng = header.findViewById(R.id.tvLang);
        ivLang = header.findViewById(R.id.ivLang);
//        SharedPreference.setAppLanguage(MainActivity.this,language);
        String lang = SharedPreference.getAppLanguage(MainActivity.this);
//        Toast.makeText(getApplicationContext(), lang, Toast.LENGTH_SHORT).show();
        if (lang.equalsIgnoreCase(Constants.english)) {
            ivLang.setBackgroundResource(R.drawable.ic_flag_english_layer);
            tvLng.setText(Constants.english);
            changeLanguageDefault(Constants.english);
        } else if (lang.equalsIgnoreCase(Constants.swedish)) {
            ivLang.setBackgroundResource(R.drawable.ic_flag_swedish_layer);
            tvLng.setText(Constants.swedish);
            changeLanguageDefault(Constants.swedish);
        } else if (lang.equalsIgnoreCase(Constants.germany)) {
            ivLang.setBackgroundResource(R.drawable.ic_flag_german_layer);
            tvLng.setText(Constants.germany);
            changeLanguageDefault(Constants.germany);
        } else if (lang.equalsIgnoreCase(Constants.arabic)) {
            ivLang.setBackgroundResource(R.drawable.ic_arabic_2);
            tvLng.setText(Constants.arabic);
            changeLanguageDefault(Constants.arabic);
        }


        if (ApplicationUtils.isSet(userName)) {
            tvUserName.setText(userName);
            tvUserName.setVisibility(View.VISIBLE);
        }
        if (ApplicationUtils.isSet(userAddress)) {
            tvAddress.setText(userAddress);
        } else {
            tvAddress.setText("");
        }

        if (ApplicationUtils.isSet(userImage)) {
            Glide.with(mContext)
                    .load(userImage)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                    .error(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                    .into(ivUserImage);
        }

        llLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupMenu(tvLng, true, R.style.MyPopupStyle);
            }
        });
    }


    void setNameAndAddress() {
        User userModel = SharedPreference.getUserData(mContext);
        userName = userModel.getFirstName();
        userAddress = userModel.getAddress();
        userImage = userModel.getThumbImage();
        if (ApplicationUtils.isSet(userName)) {
            tvUserName.setText(userName);
            tvUserName.setVisibility(View.VISIBLE);
        }
        if (ApplicationUtils.isSet(userAddress)) {
            tvAddress.setText(userAddress);
        } else {
            tvAddress.setText("");
        }

        if (ApplicationUtils.isSet(userImage)) {
            Glide.with(mContext)
                    .load(userImage)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                    .error(mContext.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait))
                    .into(ivUserImage);
        }

        if (userModel.getLatitude() != null && userModel.getLatitude() != null) {
            userLAT = userModel.getLatitude();
            userLNG = userModel.getLongitude();
        } else {
            userLAT = 0;
            userLNG = 0;
        }

//        Toast.makeText(getApplicationContext(), userName + "  " + userAddress, Toast.LENGTH_SHORT).show();
    }

    private void changeLanguage(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);
        SharedPreference.setAppLanguage(MainActivity.this, language);
        String lang = SharedPreference.getAppLanguage(MainActivity.this);

        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();

    }


    private void showPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.lang_menu, popup.getMenu());
        popup.setGravity(RelativeLayout.ALIGN_START);

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuEnglish:
                        updateLanguage(Constants.english);
                        changeLanguage(Constants.english);
                        ivLang.setBackgroundResource(R.drawable.ic_flag_english_layer);
                        tvLng.setText(Constants.english);
//                        Toast.makeText(MainActivity.this, "Contact us clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuGermany:
                        updateLanguage(Constants.germany);
                        changeLanguage(Constants.germany);
                        ivLang.setBackgroundResource(R.drawable.ic_flag_german_layer);
                        tvLng.setText(Constants.germany);
//                        Toast.makeText(MainActivity.this, "Terms and Conditions clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuSwedish:
                        updateLanguage(Constants.swedish);
                        changeLanguage(Constants.swedish);
                        ivLang.setBackgroundResource(R.drawable.ic_flag_swedish_layer);
                        tvLng.setText(Constants.swedish);
//                        Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuArabic:
                        updateLanguage(Constants.arabic);
                        changeLanguage(Constants.arabic);
                        ivLang.setBackgroundResource(R.drawable.ic_arabic_2);
                        tvLng.setText(Constants.arabic);
//                        Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();

    }

    void showPopUp(LinearLayout imgDotts) {
        PopupMenu popup = new PopupMenu(MainActivity.this, tvLng);
        //inflating menu from xml resource
        popup.inflate(R.menu.lang_menu);
        popup.setGravity(RelativeLayout.ALIGN_LEFT);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuEnglish:
                        //handle menu1 click
                        break;
                    case R.id.menuGermany:
                        //handle menu2 click

                        break;
                    case R.id.menuSwedish:
                        //handle menu3 click

                        break;
                }
                return false;
            }
        });
        //displaying the popup

        popup.show();
    }


    void changeLanguageDefault(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);
//        finishAffinity();

    }


    void updateLanguage(String language) {
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);

        String token = SharedPreference.getSharedPrefValue(MainActivity.this, Constants.USER_TOKEN);
        token = "Bearer " + token;


        Call<LanguageResponse> call = api.updateLanguage(token, language);

        call.enqueue(new Callback<LanguageResponse>() {
            @Override
            public void onResponse(Call<LanguageResponse> call, Response<LanguageResponse> response) {


                try {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            showToast(MainActivity.this, response.body().getMessage() + "", true);

                        } else {
                            showToast(MainActivity.this, response.body().getMessage() + "", false);

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        showToast(MainActivity.this, jsonObject.getString("message") + "", false);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LanguageResponse> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void changeLocale(Context context, String lang) {
        Configuration config = context.getResources().getConfiguration();
//        String lang = SharedPreference.getAppLanguage(context);
        Toast.makeText(context, lang, Toast.LENGTH_SHORT).show();
        Locale locale;
        if (!(config.locale.getLanguage().equals(lang))) {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }


    public void setData() {
        dashboardFragment = new DashboardFragment();
        setFragment(dashboardFragment, Constants.dashboardFragmentStack);
        //tvToolbarTitle.setText("");

        initializingTheDrawer();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(mContext,
                dlMain, null, R.string.openDrawer, R.string.closeDrawer) {
            private float scaleFactor = 4f;

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
               /* float slideX = (drawerView.getWidth() - 150) * slideOffset;
                container.setTranslationX(slideX);
                container.setScaleX(1 - (slideOffset / scaleFactor));*/
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        dlMain.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    ProfileFragment profileFragment;
    BusinessProfileFragment businessProfileFragment;
    int REQ_CODE_MAP2 = 107;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQ_CODE_MAP_PROFILEFRAGMENT) {
            if (profileFragment != null)
                profileFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQ_CODE_MAP2 || requestCode == REQUEST_CODE_PAYMENT) {

            if (businessProfileFragment != null)
                businessProfileFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DASHBOARD) {
            dashboardFragment.onActivityResult(requestCode, resultCode, data);
        } else if (profileFragment != null) {

            if (profileFragment.isProfileActive)
                profileFragment.onActivityResult(requestCode, resultCode, data);
            if (businessProfileFragment != null)
                if (businessProfileFragment.isActive != null)
                    if (businessProfileFragment.isActive)
                        businessProfileFragment.onActivityResult(requestCode, resultCode, data);

        } else if (businessProfileFragment != null) {

            if (businessProfileFragment.isActive)
                businessProfileFragment.onActivityResult(requestCode, resultCode, data);
            if (profileFragment != null)
                if (profileFragment.isProfileActive)
                    profileFragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void setFragment(Fragment fragment, String stack) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, stack).commit();
    }

    int dashboardClick = 0;

    private void initializingTheDrawer() {

        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);
                dlMain.closeDrawers();
                // businessProfileFragment.isActive = false;
                // profileFragment.isProfileActive = false;
                dashboardFragment.isDashboardActive = false;
                switch (menuItem.getItemId()) {
                    case R.id.tv_toolbar_home:
                        dashboardFragment.isDashboardActive = true;
                        ivToolbar.setImageResource(R.drawable.bg_main_toolbar);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(dashboardFragment, Constants.dashboardFragmentStack);
                        if (dashboardClick == 1) {
                            dashboardFragment.resetUI();
                            dashboardFragment.onRefresh();
                        }
                        dashboardClick = 1;
                        return true;
                    case R.id.tv_toolbar_messages:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(new MessagesListFragment(), Constants.messageFragmentStack);
                        return true;
                    case R.id.tv_toolbar_my_ads:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(new AddListingFragment(), Constants.favaddFragmentStack);
                        return true;
                    case R.id.tv_toolbar_profile:
                        dashboardClick = 0;
                        profileFragment = new ProfileFragment();
                        profileFragment.isProfileActive = true;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(profileFragment, Constants.profileFragmentStack);
                        return true;
                    case R.id.tv_toolbar_business_profile:
                        dashboardClick = 0;
                        businessProfileFragment = new BusinessProfileFragment();
                        businessProfileFragment.isActive = true;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(businessProfileFragment, Constants.businessprofileFragmentStack);
                        return true;
                    case R.id.tv_toolbar_my_favourites:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(new FavrouteAddListingFragment(), Constants.myaddFragmentStack);
                        return true;
                    case R.id.tv_toolbar_contact_us:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(ContentPagesFragment.newInstance("contact-us", getResources().getString(R.string.contact_us)), Constants.contactusFragmentStack);
                        return true;
                    case R.id.tv_toolbar_about_us:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(ContentPagesFragment.newInstance("about-us", getResources().getString(R.string.about_us)), Constants.contactusFragmentStack);
                        return true;
                    case R.id.tv_toolbar_advertise_us:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(ContentPagesFragment.newInstance("advertise-with-use", getResources().getString(R.string.advertise_with_us)), Constants.contactusFragmentStack);
                        return true;
                    case R.id.tv_toolbar_questions_answers:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(ContentPagesFragment.newInstance("question-and-answers", getResources().getString(R.string.questions__and_answers)), Constants.contactusFragmentStack);
                        return true;

                    case R.id.tv_toolbar_privacy_settings:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(ContentPagesFragment.newInstance("privacy-policy", getResources().getString(R.string.privacy_police)), Constants.contactusFragmentStack);
                        return true;
                    case R.id.tv_toolbar_logout:
                        dashboardClick = 0;
                        logoutBottomSheet();
                        return true;
                    case R.id.tv_toolbar_post_ad:
                        dashboardClick = 0;
                        ApplicationUtils.hideKeyboard(mContext);
                        getUserDetails();
                        return true;
                    case R.id.tv_toolbar_change_password:
                        dashboardClick = 0;
                        ivToolbar.setImageResource(R.drawable.bg_header);
                        ivToolbar.setVisibility(View.VISIBLE);
                        setFragment(new ChangePasswordActivity(), Constants.changePasswordFragmentStack);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

    }


    public void registerFCMUser() {

        String v = SharedPreference.getSharedPrefValue(mContext, NotificationConfig.FCM_ID);
        boolean b = SharedPreference.isUserLoggedIn(mContext);
        boolean s = SharedPreference.getBoolSharedPrefValue(mContext, NotificationConfig.FCM_ID_SENT, false);
        String devasdiceToken = SharedPreference.unScrambleText(SharedPreference.getSharedPrefValue(mContext, NotificationConfig.FCM_ID));

//        if (SharedPreference.getSharedPrefValue(mContext, NotificationConfig.FCM_ID) != null &&
//                SharedPreference.isUserLoggedIn(mContext) &&
//                !SharedPreference.getBoolSharedPrefValue(mContext, NotificationConfig.FCM_ID_SENT, false)) {
//
//            String deviceToken = SharedPreference.unScrambleText(SharedPreference.getSharedPrefValue(mContext, NotificationConfig.FCM_ID));
//            DeviceUuidFactory uuidFactory = new DeviceUuidFactory(this);
//            String deviceID = uuidFactory.getDeviceUuid().toString();
//            String deviceType = "android";
//            String appMode = "development";
//            ApplicationUtils.postFCMToken(mContext, deviceToken, deviceID, deviceType, appMode);
//        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String deviceToken = task.getResult().getToken();
                DeviceUuidFactory uuidFactory = new DeviceUuidFactory(this);
                String deviceID = uuidFactory.getDeviceUuid().toString();
                String deviceType = "android";
                String appMode = "development";

                Log.d("FirebaseTOken", devasdiceToken + "{"+ deviceID);

                ApplicationUtils.postFCMToken(mContext, deviceToken, deviceID, deviceType, appMode);

            }
        });

    }


    private void logoutBottomSheet() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mContext);
        View sheetView = this.getLayoutInflater().inflate(R.layout.logout_bottomsheet_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        TextView tvLogOut = sheetView.findViewById(R.id.tvLogOut);
        Button btnCancel = sheetView.findViewById(R.id.btnCancel);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                mBottomSheetDialog.dismiss();
                SharedPreference.logoutDefaults(mContext);
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
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btn_toolbar_right:
                break;
            case R.id.btn_toolbar_back:
                if (dlMain.isDrawerVisible(GravityCompat.START)) {
                    dlMain.closeDrawer(GravityCompat.START);
                } else {
                    dlMain.openDrawer(GravityCompat.START);
                }
                break;

        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        ApplicationUtils.hideKeyboard(MainActivity.this);
        return true;
    }

    @Override
    public void onListFragmentInteraction(UserMessages item) {
        ApplicationUtils.hideKeyboard(((Activity) mContext));
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("thread_id", item.getId());
        intent.putExtra("name", item.getTo_user_details().getFirstName());
        intent.putExtra("to_user_id", item.getTo_user_details().getId() + "");
        startActivity(intent);
    }

    @Override
    public void callToolbarBack(boolean isMenu) {
        if (isMenu) {
            if (dlMain.isDrawerVisible(GravityCompat.START)) {
                dlMain.closeDrawer(GravityCompat.START);
            } else {
                dlMain.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void callHomeReset() {
        dashboardFragment.isDashboardActive = true;
        ivToolbar.setImageResource(R.drawable.bg_main_toolbar);
        setFragment(dashboardFragment, Constants.dashboardFragmentStack);

    }


    @Override
    public void onResume() {
        super.onResume();
        initUserProfileData();
        //getUserDetails();
    }

    public static String getConfigEnvironment() {
        return CONFIG_ENVIRONMENT;
    }

    public static String getConfigClientId() {
        return CONFIG_CLIENT_ID;
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void showProgressBar(final boolean progressVisible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void getUserDetails() {
        isBusinessAccount = false;
        isBusinessAccountPaid = false;
        showProgressBar(true);
        String token = SharedPreference.getSharedPrefValue(mContext, Constants.USER_TOKEN);
        token = "Bearer " + token;
        ApiClient apiClient = ApiClient.getInstance();
        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
        Call<BusinessProfileResponse> call = api.getUserBusinesProfile(token);

        call.enqueue(new Callback<BusinessProfileResponse>() {
            @Override
            public void onResponse(Call<BusinessProfileResponse> call,
                                   final Response<BusinessProfileResponse> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    if (response != null && response.body() != null && response.body().getStatus()) {
                        if (response != null && response.body() != null && response.body().getData() != null) {

                            isBusinessAccount = false;
                            isBusinessAccountPaid = false;
                            BusinessDetails user = response.body().getData().getData();
                            if (user != null) {
                                isBusinessAccount = true;

                                if (user.getPaymentStatus().equals("paid")) {
                                    isBusinessAccountPaid = true;
                                }
                            } else {
                                isBusinessAccount = false;
                            }

                        }
                    }

                    moveTOPostAdd();
                }
            }

            @Override
            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {
                showProgressBar(false);
                moveTOPostAdd();
            }
        });
    }

    void moveTOPostAdd() {
        if (!isBusinessAccount) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_error),
                    mContext.getString(R.string.msg_no_business_account));
            return;
        }
        if (!isBusinessAccountPaid) {
            DialogFactory.showDropDownNotificationError(mContext,
                    mContext.getString(R.string.alert_error),
                    mContext.getString(R.string.msg_business_subscription_expired));
            return;
        }

        Intent postAd = new Intent(mContext, PostAdActivity.class);
        startActivity(postAd);
    }

    @Override
    public void onProfileUpdated(Boolean updted) {
        setNameAndAddress();
    }


//    private void getUserDetails() {
//        String token = SharedPreference.getSharedPrefValue(MainActivity.this, Constants.USER_TOKEN);
//        token = "Bearer " + token;
//        ApiClient apiClient = ApiClient.getInstance();
//        ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
//        Call<BusinessProfileResponse> call = api.getUserBusinesProfile(token);
//
//        call.enqueue(new Callback<BusinessProfileResponse>() {
//            @Override
//            public void onResponse(Call<BusinessProfileResponse> call,
//                                   final Response<BusinessProfileResponse> response) {
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (response.isSuccessful()) {
//                    if (response != null && response.body() != null && response.body().getStatus()) {
//                        if (response != null && response.body() != null && response.body().getData() != null) {
//
//
//                            isBusinessAccount = false;
//                            isBusinessAccountPaid = false;
//                            BusinessDetails user = response.body().getData().getData();
//                            if (user != null) {
//                                isBusinessAccount = true;
//
//
//                                if (ApplicationUtils.isSet(user.getPaymentStatus()))
//                                    if (user.getPaymentStatus().equals("paid")) {
//
//                                        isBusinessAccountPaid = true;
//                                    }
//
//                            }
//
//
//                        }
//                    } else {
//                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
//                            DialogFactory.showDropDownNotificationError(
//                                    MainActivity.this,
//                                    mContext.getString(R.string.alert_error),
//                                    response.body().getMessage());
//                        }
//                    }
//                } else {
//                    if (response.code() == 401)
//                        DialogFactory.showDropDownNotificationError(
//                                MainActivity.this,
//                                mContext.getString(R.string.alert_information),
//                                mContext.getString(R.string.error_msg_unauthorized_user));
//                    if (response.code() == 404)
//                        DialogFactory.showDropDownNotificationError(
//                                MainActivity.this,
//                                mContext.getString(R.string.alert_information),
//                                mContext.getString(R.string.alert_api_not_found));
//                    if (response.code() == 500)
//                        DialogFactory.showDropDownNotificationError(
//                                MainActivity.this,
//                                mContext.getString(R.string.alert_information),
//                                mContext.getString(R.string.alert_internal_server_error));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BusinessProfileResponse> call, Throwable t) {
//                try {
////                    showProgressBar(false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (t instanceof SocketTimeoutException)
//                    DialogFactory.showDropDownNotificationError(
//                            MainActivity.this,
//                            mContext.getString(R.string.alert_information),
//                            mContext.getString(R.string.alert_request_timeout));
//                else if (t instanceof IOException)
//                    DialogFactory.showDropDownNotificationError(
//                            MainActivity.this,
//                            mContext.getString(R.string.alert_information),
//                            mContext.getString(R.string.alert_api_not_found));
//                else
//                    DialogFactory.showDropDownNotificationError(
//                            MainActivity.this,
//                            mContext.getString(R.string.alert_information),
//                            t.getLocalizedMessage());
//            }
//        });
//    }

}
