package com.dalileuropeapps.dalileurope.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;


public class SplashActivity extends BaseActivity implements FullCallback {

    int SPLASH_TIME_OUT = 100;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String lang = SharedPreference.getAppLanguage(SplashActivity.this);

        if (lang.equalsIgnoreCase(Constants.english)) {

            changeLanguageDefault(Constants.english);
        } else if (lang.equalsIgnoreCase(Constants.swedish)) {

            changeLanguageDefault(Constants.swedish);
        } else if (lang.equalsIgnoreCase(Constants.germany)) {

            changeLanguageDefault(Constants.germany);
        } else if (lang.equalsIgnoreCase(Constants.arabic)) {

            changeLanguageDefault(Constants.arabic);
        }
        transparentStatusBar();
        initViews();
        getData();
        openActivity();

    }

    public void initViews() {
    }

    public void getData() {

    }

    private void changeLanguageDefault(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);
//        finishAffinity();

    }

    public void openActivity() {
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermission()) {
                        //PermissionEnum.CAMERA,
                        PermissionManager.with(SplashActivity.this)
                                .permission(PermissionEnum.ACCESS_FINE_LOCATION,
                                        PermissionEnum.ACCESS_COARSE_LOCATION,
                                        PermissionEnum.READ_EXTERNAL_STORAGE,
                                        PermissionEnum.WRITE_EXTERNAL_STORAGE,
                                        PermissionEnum.CALL_PHONE)
                                .askagain(true)
                                .askagainCallback(new AskagainCallback() {
                                    @Override
                                    public void showRequestPermission(UserResponse response) {
                                        showDialog(response);
                                    }
                                })
                                .callback(SplashActivity.this)
                                .ask();
                    } else {
                        setData();
                    }
                } else {
                    setData();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    /***************************************
     * Marshmallow Or Above Permissions Code
     ***************************************/
    private boolean checkPermission() {
        PermissionEnum permissionEnum = PermissionEnum.ACCESS_FINE_LOCATION;
        boolean granted = PermissionUtils.isGranted(SplashActivity.this, PermissionEnum.ACCESS_FINE_LOCATION);
        if (granted == true) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    @Override
    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
        List<String> msg = new ArrayList<>();
        for (PermissionEnum permissionEnum : permissionsGranted) {
            msg.add("Location Permission is Granted");
        }
        for (PermissionEnum permissionEnum : permissionsDenied) {
            msg.add(permissionEnum.toString() + " is Denied");
        }
        for (PermissionEnum permissionEnum : permissionsDeniedForever) {
            msg.add(permissionEnum.toString() + " is DeniedForever");
        }
        for (PermissionEnum permissionEnum : permissionsAsked) {
            msg.add(" You asked for ...");
        }
        String[] items = msg.toArray(new String[msg.size()]);

        setData();
    }

    private void showDialog(final AskagainCallback.UserResponse response) {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle(getResources().getString(R.string.tv_permission_needed))
                .setMessage(getResources().getString(R.string.tv_permission_detail))
                .setPositiveButton(getResources().getString(R.string.tv_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        response.result(true);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.tv_not), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        response.result(false);
                    }
                })
                .show();
    }


    public void setData() {
        if (SharedPreference.isUserLoggedIn(getApplicationContext())) {
            openActivity(MainActivity.class);
        } else {
            openActivity(SignInActivity.class);
        }
    }

    private void openActivity(final Class<?> cls) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashActivity.this, cls);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
