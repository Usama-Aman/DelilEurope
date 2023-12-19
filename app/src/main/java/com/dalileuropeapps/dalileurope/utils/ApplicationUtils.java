package com.dalileuropeapps.dalileurope.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.notifications.NotificationConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import static com.dalileuropeapps.dalileurope.utils.Constants.dateFormator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApplicationUtils {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static boolean validatePassword(String password, int requiredLength) {
        if (password.trim().isEmpty() || password.length() < requiredLength)
            return false;
        return true;
    }


    public static boolean validateConfirmPassword(String password, String newPassword) {
        if (newPassword.trim().isEmpty() || !password.toLowerCase().equalsIgnoreCase(newPassword.toLowerCase()))
            return false;
        return true;
    }


    public static int getDeviceStatusBarHeight(Context context) {

        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0)
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);

        System.out.println("Its value of status bar height " + statusBarHeight);
        return statusBarHeight;
    }


    public static String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        return dateFormator.format(currentDate);
    }


    public static boolean validateEmail(String email) {
        if (email.isEmpty() || !isValidEmail(email))
            return false;
        return true;
    }

    public static boolean validateName(String name) {
        if (name.isEmpty() || name.length() == 0)
            return false;
        return true;
    }

    public static boolean isSet(String value) {
        if (value == null || value.length() == 0)
            return false;
        return true;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view != null) {
            if (view.hasFocus())
                view.clearFocus();
            inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }


    public static void hideKeyboardOnButtonClick(Activity context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    public static void loadImageWithGlide(Context context, String imgURL, ImageView iv, int drawable) {
        try {
            Glide.with(context)
                    .load(imgURL)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(drawable)
                            .error(drawable)
                            .centerCrop())
                    .into(iv);
        } catch (Exception e) {

        }

    }

    public static void showImageWithGlideBitmap(Context context, Bitmap bitmap, final ImageView imageView, int placeholder) {
        Glide.with(context)
                .asBitmap()
                .load(bitmap)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    /*******************************
     * Method for network is in working state or not.
     ******************************/
    public static boolean isOnline(Context cntext) {
        ConnectivityManager cm = (ConnectivityManager) cntext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    public static void postFCMToken(final Context ctx, String deviceToken, String deviceID, String deviceType, String appMode) {
        ApiClient apiClient = ApiClient.getInstance();

        apiClient.postFCMToken(deviceToken, deviceID, deviceType, appMode, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, final Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus()) {

                        SharedPreference.saveBoolSharedPrefValue(ctx, NotificationConfig.FCM_ID_SENT, true);
                    } else {
                        if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                            URLogs.m("FCM POST FAILURE: " + response.body().getMessage());
                        }
                    }
                } else {
                    if (response.code() == 401) {
                        URLogs.m("FCM POST FAILURE: 401 Error");
                    }
                    if (response.code() == 404) {
                        URLogs.m("FCM POST FAILURE: 404 Error");
                    }
                    if (response.code() == 500) {
                        URLogs.m("FCM POST FAILURE: 500 Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

                if (t instanceof SocketTimeoutException)
                    URLogs.m("FCM POST FAILURE: SocketTimeoutException");
                else if (t instanceof IOException)
                    URLogs.m("FCM POST FAILURE: IOException");
                else
                    URLogs.m("FCM POST FAILURE");

            }
        });
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;
        try {
            address = geoCoder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0)
                return null;
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }


    static Map<String, String> daysOrDates = new HashMap<>();

    public static void getDatesList() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String outputDate = simpleDateFormat.format(calendar.getTime());

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        Boolean found = true;
        String day = " ";
        while (found) {
            if (outputDate.equalsIgnoreCase(currentDate)) {
                day = sdf.format(calendar.getTime());

                daysOrDates.put(outputDate, "Today");
                found = false;
                break;
            } else {
                day = sdf.format(calendar.getTime());
                daysOrDates.put(outputDate, day);
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                outputDate = simpleDateFormat.format(calendar.getTime());
            }

        }
    }

    public static String formatDateFromDateTime(String dt) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(dt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String msgDate = sdf.format(date);
            return msgDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String formatDateFromDate(String dt) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(dt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String msgDate = sdf.format(date);
            return msgDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String formatDateMonthDateYear(String dt) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(dt);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);
            String msgDate = sdf.format(date);
            return msgDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String formatDateAsYear(String dt) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(dt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            String msgDate = sdf.format(date);
            return msgDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }


    public static String getDayOrDate(String dayOrDate) {
        String dayOrDt = dayOrDate;
        if (dayOrDate.length() <= 0) {
            getDatesList();
        }
        if (daysOrDates.containsKey(dayOrDate)) {
            dayOrDt = daysOrDates.get(dayOrDate);
        }
        return dayOrDt;
    }


    public static String getDtAndTime(String dt) {
        String formatedDate = "";
        try {
//

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = dateFormat.parse(dt);
            String currentDateTimeStr = ApplicationUtils.getCurrentDate();
            Date currentDateTime = dateFormat.parse(currentDateTimeStr);

            SimpleDateFormat msgDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String msgDate = msgDateFormatter.format(date);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());

            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());


            long dateDiff = currentDateTime.getTime() - date.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (msgDate.equalsIgnoreCase(currentDate)) {

           /*     if (dateDiff < MINUTE_MILLIS) {
                    return "just now";
                } else if (dateDiff < 2 * MINUTE_MILLIS) {
                    return "a minute ago";
                } else if (dateDiff < 50 * MINUTE_MILLIS) {
                    return dateDiff / MINUTE_MILLIS + " minutes ago";
                } else if (dateDiff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (dateDiff < 24 * HOUR_MILLIS) {
                    return dateDiff / HOUR_MILLIS + " hours ago";
                } else {*/
                String strDateFormat = "HH:mm a";
                SimpleDateFormat timeFormate = new SimpleDateFormat(strDateFormat);
                formatedDate = timeFormate.format(date);
                // }


            } else {
                SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                String year = yearFormatter.format(date);
                if (year.equalsIgnoreCase(currentYear)) {
                    SimpleDateFormat mothDateFormatter = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
                    formatedDate = mothDateFormatter.format(date);
                } else {
                    SimpleDateFormat yearMonthDateFormatter = new SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH);
                    formatedDate = yearMonthDateFormatter.format(date);
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;

    }


    //Function to hide the keyboard if open
    public static void hideKeyboard(AppCompatActivity activity) {

        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Activity activity, String message, boolean isSuccess) {

        if (isSuccess) {
            DialogFactory.showDropDownNotificationSuccess(
                    activity,
                    activity.getString(R.string.success),
                    message);

        } else {
            DialogFactory.showDropDownNotificationError(
                    activity,
                    activity.getString(R.string.error),
                    message);
        }
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    // 29-12-2019

    public static UserMessages fromJson(Context context, String json) {
        Gson gson = new Gson();
        UserMessages obj = gson.fromJson(json, UserMessages.class);
        return obj;
    }

    public static void toJson(Context context, UserMessages data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
    }


    public static UserMessages fromJsonNotificationData(Context context, String json) {
        Gson gson = new Gson();
        UserMessages obj = gson.fromJson(json, UserMessages.class);
        return obj;
    }

    public static void toNotificationDataJson(Context context, UserMessages data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
    }

    public static boolean removeFileFromDisk(File path) {
        boolean result = true;
        if (path.exists()) {
            if (path.isDirectory()) {
                for (File child : path.listFiles()) {
                    result &= removeFileFromDisk(child);
                }
                result &= path.delete(); // Delete empty directory.
            } else if (path.isFile()) {
                result &= path.delete();
            }
            return result;
        } else {
            return false;
        }
    }

    public static String get30DaysToDate(String date, boolean before, int days) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!before)
            c.add(Calendar.DATE, days);
        else
            c.add(Calendar.DATE, -days);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String output = sdf1.format(c.getTime());
        return output;
    }

    public static void enableGPS(Context ctx) {
        final LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            DialogFactory.showDropDownNotificationError((Activity) ctx,
                    ctx.getString(R.string.alert_information),
                    ctx.getString(R.string.msg_enable_gps));
        }

    }

    public static boolean isEnableGPS(Context ctx) {
        final LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    public static void hideKeyboardForOverlay(Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            try {
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    /***********************************
     * Get city and Country name method
     ***********************************/
    public static String getAddress(Context context, double latitude, double longitude) {
        StringBuilder results = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if (ApplicationUtils.isSet(address.getLocality()))
                    results.append(address.getLocality()).append(" ,");
                if (ApplicationUtils.isSet(address.getAddressLine(0)))
                    results.append(address.getAddressLine(0)).append(" ,");
                if (ApplicationUtils.isSet(address.getCountryName()))
                    results.append(address.getCountryName());
            }
        } catch (IOException e) {
            //  Log.e("tag", e.getMessage());
            return "";
        }
        return results.toString();
    }

    /***********************************************************
     * Below code Working for scale image as aspect ratio:
     Bitmap bitmapImage = BitmapFactory.decodeFile("Your path");
     int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
     Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
     your_imageview.setImageBitmap(scaled);
     Compress your image without losing quality like Whatsapp
     **********************************************************/
    public static String compressImage(String filePath) {
        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        //max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        //width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        //setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        //this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        //check the rotation of the image and display it properly
        try {
            ExifInterface exif = new ExifInterface(filePath); //imgFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
            e.getStackTrace();
        }


        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), Constants.DEFAULT_IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public static Date fromStrToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long fromDateToLong(Date dt) {
        if (dt != null)
            return dt.getTime();
        return 0;
    }


    public static boolean isUserSubscriptionExpired(String strExpire) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date endDate = sdf.parse(strExpire);

            if (new Date().after(endDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public static boolean isCurrentDateAfterDate(String strExpire) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date endDate = sdf.parse(strExpire);
            Date newDate = new Date();

            if (newDate.compareTo(endDate) == 1 || newDate.compareTo(endDate) == 0) {

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public static void clearFocus(EditText et) {
        if (et != null)
            et.clearFocus();
    }

    public static Calendar convertStrDateToCalendar(String strDate) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            cal.setTime(sdf.parse(strDate));
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long getDaysDiffBtwDates(String strDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date dateSTr = sdf.parse(strDate);
            Date dateEndDate = sdf.parse(endDate);
            long diff = dateEndDate.getTime() - dateSTr.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String removeLastCharacterFromString(String str) {

        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;

    }
}
