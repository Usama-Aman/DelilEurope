package com.dalileuropeapps.dalileurope.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.activities.SignInActivity;
import com.dalileuropeapps.dalileurope.api.retrofit.AdType;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.notifications.NotificationConfig;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;


public class SharedPreference {
    private static final String SHARED_PREFERENCES_KEY = "DalileuropeSharedPrefs";

    public static int count = 0;
    public static int tempInt = 0;


    public static void saveSharedPrefValue(Context mContext, String key, String value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putString(key, scrambleText(value));
        edit.commit();
    }

    public static void setAppLanguage(Context mContext, String lang) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_LANGUAGE, lang);
        editor.commit();

    }

    public static void savePrefValue(Context mContext, String key, String value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void saveProfileDefaults(Context mContext, User jsonData) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(jsonData);
        editor.putString(Constants.USERDEFAULT_USER_DATA, json);

        editor.commit();

    }

    public static void saveBoolSharedPrefValue(Context mContext, String key, boolean value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void saveIntegerSharedPrefValue(Context mContext, String key, int value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    //Read from Shared Preferance
    public static int readSharedPreferenceInt(Context mContext, String spName, String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return tempInt = sharedPreferences.getInt(key, 0);
    }

    //write shared preferences in integer
    public static void writeSharedPreference(Context mContext, int ammount, String spName, String key) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();

        editor.putInt(key, ammount);
        editor.commit();
    }

    /****************************
     * @param cxt
     * @param key
     * @return
     ****************************/
    public static boolean getBoolSharedPrefValue(Context cxt, String key, boolean defaultValue) {
        SharedPreferences userSharedPrefs = cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        return userSharedPrefs.getBoolean(key, defaultValue);
    }

    public static String getSharedPrefValue(Context mContext, String key) {
        String value = null;
        if (mContext != null) {
            SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
            value = userSharedPrefs.getString(key, null);
        }
        return value;
    }

    public static SharedPreferences getSharedPref(Context cxt) {
        return cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
    }

    public static int getIntSharedPrefValue(Context cxt, String shared_pref_key, int defaultValue) {
        SharedPreferences userSharedPrefs = cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        return userSharedPrefs.getInt(shared_pref_key, defaultValue);
    }

    /******************************
     * @param mContext
     * @return
     *****************************/
    public static boolean isUserLoggedIn(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.USERDEFAULT_ISLOGGEDIN, false);
    }

    private static String scrambleText(String text) {
        try {
            Random r = new Random();
            String prefix = String.valueOf(r.nextInt(90000) + 10000);
            String suffix = String.valueOf(r.nextInt(90000) + 10000);
            text = prefix + text + suffix;

            byte[] bytes = text.getBytes("UTF-8");
            byte[] newBytes = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte) (bytes[i] - 1);
            }
            return new String(newBytes, "UTF-8");
        } catch (Exception e) {
            return text;
        }
    }

    public static String unScrambleText(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            byte[] newBytes = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte) (bytes[i] + 1);
            }
            String textVal = new String(newBytes, "UTF-8");
            return textVal.substring(5, textVal.length() - 5);
        } catch (Exception e) {
            return text;
        }
    }


    public static String getAppLanguage(Context mContext) {
        if (SharedPreference.getSharedPrefValue(mContext, Constants.PREF_LANGUAGE) != null) {
            return SharedPreference.unScrambleText(SharedPreference.getSharedPrefValue(mContext, Constants.PREF_LANGUAGE));
        }
        return "en";
    }


    public static void saveLoginDefaults(Context mContext, User jsonData, String sessionId, Boolean saveLogin) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(jsonData);
        editor.putString(Constants.USERDEFAULT_USER_DATA, json);
        if (saveLogin)
            editor.putBoolean(Constants.USERDEFAULT_ISLOGGEDIN, true);
        else
            editor.putBoolean(Constants.USERDEFAULT_ISLOGGEDIN, false);

        ArrayList<AdType> adTypes = new ArrayList<>();

        if (jsonData.getAdsTypes() != null) {
            AdType adTypeNone = new AdType();
            adTypeNone.setType(mContext.getResources().getString(R.string.tv_none));
            adTypeNone.setCurrency("$");
            adTypeNone.setAdFee(0);
            adTypes.add(adTypeNone);

            AdType adTypeFeatured = new AdType();
            adTypeFeatured.setType(mContext.getResources().getString(R.string.tv_featured_price));
            adTypeFeatured.setCurrency("$");
            if (ApplicationUtils.isSet(jsonData.getFeatureFee()))
                adTypeFeatured.setAdFee(Double.parseDouble(jsonData.getFeatureFee()));
            adTypes.add(adTypeFeatured);


            AdType adTypeHighlighted = new AdType();
            adTypeHighlighted.setType(mContext.getResources().getString(R.string.tv_highlighted));
            adTypeHighlighted.setCurrency("$");
            if (ApplicationUtils.isSet(jsonData.getHighlightedFee()))
                adTypeHighlighted.setAdFee(Double.parseDouble(jsonData.getHighlightedFee()));
            adTypes.add(adTypeHighlighted);
        }

        editor.putString(Constants.USERDEFAULT_ADSTYPES, (new Gson()).toJson(adTypes));

        if (jsonData.getIsBusiness().equals("1")) {
            editor.putBoolean(Constants.USERDEFAULT_ISBUSINESSACCOUNT, true);
            if (jsonData.getBusiness() != null && jsonData.getBusiness().size() != 0) {
                if (jsonData.getBusiness().get(0).getPaymentStatus().equals("paid")) {
                    editor.putBoolean(Constants.USERDEFAULT_PAIDSUBCRIPTION, true);
                    if (jsonData.getBusiness().get(0).getBusiness_subscriptions() != null) {
                        editor.putString(Constants.USERDEFAULT_SUBSCRIPTION_INFO, (new Gson()).toJson(jsonData.getBusiness().get(0).getBusiness_subscriptions()));
                    }
                } else {
                    editor.putBoolean(Constants.USERDEFAULT_PAIDSUBCRIPTION, false);
                }
            }
        } else
            editor.putBoolean(Constants.USERDEFAULT_ISBUSINESSACCOUNT, false);


        editor.putString(Constants.USERDEFAULT_CURRENCY, jsonData.getCurrency());
        editor.putString(Constants.USER_TOKEN, sessionId);

        editor.commit();

    }

    public static User getUserData(Context mContext) {
        String val = getSharedPrefValue(mContext, Constants.USERDEFAULT_USER_DATA);
        Gson gson = new Gson();
        User user = gson.fromJson(val, User.class);
        return user;
    }

    public static void logoutDefaults(Context mContext) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();

        String languageCode = SharedPreference.unScrambleText(SharedPreference.getSharedPrefValue(mContext, Constants.PREF_LANGUAGE));
        editor.remove(Constants.USERDEFAULT_ISLOGGEDIN);
        editor.remove(Constants.USERDEFAULT_USER_DATA);
        editor.remove(NotificationConfig.FCM_ID_SENT);
        editor.commit();

        editor.putString(Constants.PREF_LANGUAGE, scrambleText(languageCode));
        editor.commit();
        Intent act = new Intent(mContext, SignInActivity.class);
        act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(act);
        ((Activity) mContext).finish();

    }


}
