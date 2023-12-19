package com.dalileuropeapps.dalileurope.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Retrieve current location using Google Play Services Location API
 * Based on "https://github.com/googlesamples/android-play-location/tree/master/LocationUpdates"
 */
public abstract class FusedLocationActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private AppCompatActivity mContext;
    protected static final String TAG = "loc-updates-activity";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private final static String LOCATION_KEY = "location-key";
    private final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates = false;
    private String mLastUpdateTime = "";
    /* Lahore = 31.5546° N, 74.3572° E*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());


        mContext = (AppCompatActivity) this;
        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();

        if (!isPlayServicesAvailable(this)) return;
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        } else {
            return;
        }

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    protected abstract int getLayoutResourceId();

    /*******************************
     * Fused location Api methods
     ******************************/
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
            }

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }

            updateUI();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        //mLocationRequest.setSmallestDisplacement(5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startUpdatesButtonHandler(View view) {
        clearUI();
        if (!isPlayServicesAvailable(this)) return;
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        } else {
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            setButtonsEnabledState();
            startLocationUpdates();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setButtonsEnabledState();
            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates");
        try {


            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            if (builder != null) {

                if (mGoogleApiClient != null) {
                    if (mGoogleApiClient.isConnected()) {

                        PendingResult<LocationSettingsResult> result =
                                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                        if (result != null) {
                            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                                @Override
                                public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                                    if (mGoogleApiClient != null)
                                        if (mGoogleApiClient.isConnected()) {
                                            final Status status = locationSettingsResult.getStatus();

                                            switch (status.getStatusCode()) {
                                                case LocationSettingsStatusCodes.SUCCESS:
                                                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                                        if (mGoogleApiClient != null) {
                                                            if (mGoogleApiClient.isConnected()) {
                                                                try {
                                                                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, FusedLocationActivity.this);
                                                                } catch (Exception e) {
                                                                    e.getMessage();
                                                                }
                                                            } else {
                                                                mGoogleApiClient.connect();
                                                            }
                                                        } else {
                                                            buildGoogleApiClient();
                                                            mGoogleApiClient.connect();
                                                        }
                                                    }
                                                    break;
                                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                                    try {
                                                        status.startResolutionForResult(FusedLocationActivity.this, REQUEST_CHECK_SETTINGS);
                                                    } catch (IntentSender.SendIntentException e) {
                                                        // Ignore the error.
                                                    }
                                                    break;
                                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                                    // Location settings are not satisfied. However, we have no way
                                                    // to fix the settings so we won't show the dialog.
                                                    break;
                                            }
                                        } else {
                                            buildGoogleApiClient();
                                        }
                                }
                            });
                        }
                    } else {
                        mGoogleApiClient.connect();
                    }
                } else {
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                }

            }


        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            //mBinding.startUpdatesButton.setEnabled(false);
            //mBinding.stopUpdatesButton.setEnabled(true);
        } else {
            //mBinding.startUpdatesButton.setEnabled(true);
            //mBinding.stopUpdatesButton.setEnabled(false);
        }
    }

    private void clearUI() {
        //mBinding.latitudeText.setText("");
        ///mBinding.longitudeText.setText("");
        //mBinding.lastUpdateTimeText.setText("");
    }

    private void updateUI() {
        if (mCurrentLocation == null) return;
        //mBinding.latitudeText.setText(String.format("%s: %f", mLatitudeLabel,mCurrentLocation.getLatitude()));
        //mBinding.longitudeText.setText(String.format("%s: %f", mLongitudeLabel,mCurrentLocation.getLongitude()));
        //mBinding.lastUpdateTimeText.setText(String.format("%s: %s", mLastUpdateTimeLabel,mLastUpdateTime));
    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates");
        try {
            // The final argument to {@code requestLocationUpdates()} is a LocationListener
            // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setButtonsEnabledState();
                    startLocationUpdates();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        mRequestingLocationUpdates = false;
                        //Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                    } else {
                        showRationaleDialog();
                    }
                }
                break;
            }
        }
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(FusedLocationActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(FusedLocationActivity.this, "You can`t access location.", Toast.LENGTH_SHORT).show();
                        mRequestingLocationUpdates = false;
                    }
                })
                .setCancelable(false)
                .setMessage("Location permission required. Please allow the location permission.")
                .show();
    }

    public static boolean isPlayServicesAvailable(Context context) {
        // Google Play Service APK
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isPlayServicesAvailable(this);

        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (!isPlayServicesAvailable(this)) return;
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        } else {
            return;
        }
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } else if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        } else if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {


        if (mCurrentLocation != null) {
            int distance = getDistance(Float.parseFloat(String.valueOf(mCurrentLocation.getLatitude())),
                    Float.parseFloat(String.valueOf(mCurrentLocation.getLongitude())),
                    Float.parseFloat(String.valueOf(location.getLatitude())),
                    Float.parseFloat(String.valueOf(location.getLongitude())));
            // Toast.makeText(this, "onLocationChanged: " + "----" + distance + "----" + location.distanceTo(mCurrentLocation), Toast.LENGTH_SHORT).show();
            if (distance > 5) {
                mCurrentLocation = location;
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateUI();
            }
        } else {

            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    public String getAddressFromLocation(double latitude, double longitude) {
        String message = "";
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.US);
            final List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            final Address address = addresses.get(0);

            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    StringBuilder sb = new StringBuilder();
                    sb.append(address.getAddressLine(0));
                    if (addresses.size() > 1) {
                        sb.append(", ");
                        sb.append(address.getAddressLine(1));
                    }
                    if (addresses.size() > 2) {
                        sb.append(", ");
                        sb.append(address.getAddressLine(2));
                    }
                    if (!TextUtils.isEmpty(address.getLocality())) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(address.getLocality());
                    }
                    if (!TextUtils.isEmpty(address.getCountryName())) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(address.getCountryName());
                    }
                    String message = sb.toString();

                    // etToolbarSearch.setText("" + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    public static int getDistance(float lat_a, float lng_a, float lat_b, float lng_b) {
        // earth radius is in mile
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(lat_a))
                * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2)
                * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        double meterConversion = 1609;
        double kmConvertion = 1.6093;

        int result = 0;
        result = (int) (distance * meterConversion);

        // return new Float(distance * meterConversion).floatValue();
        //return String.format("%.2f", new Float(distance * kmConvertion).floatValue()); // distance in kilometers


        return result;
    }


}