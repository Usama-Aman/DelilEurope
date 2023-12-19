package com.dalileuropeapps.dalileurope.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dalileuropeapps.dalileurope.R;
import com.stripe.android.PaymentConfiguration;


public class AppController extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context context;

    public static boolean isStrip = false;
    public static String stripToken = "";
    public static boolean isPayPal = false;


    public static Context getAppContext() {
        return AppController.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppController.context = getApplicationContext();

        PaymentConfiguration.init(
                AppController.context,
                getResources().getString(R.string.stip_pb_key)
        );

    }

    public static Context getInstance() {
        return context;
    }

    public static void setInstance(Context context) {
        AppController.context = context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}