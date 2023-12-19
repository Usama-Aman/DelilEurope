package com.dalileuropeapps.dalileurope.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.dalileuropeapps.dalileurope.utils.Constants.MY_PERMISSIONS_REQUEST_LOCATION;


public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    View locationButton;
    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static LatLng shippingLatlng;
    public static String shippingAddress = "";
    private View mapView;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private Button btnDoneMap;
    private ImageView back;
    private MarkerOptions markerOptions;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private AppCompatActivity mContext;
    ImageView ivMyLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        mContext = (AppCompatActivity) this;
        //transparentStatusBar();

        try {
            if (bundle.getString("address") != "") {
                shippingAddress = bundle.getString("address");
            }
            double lat = bundle.getDouble("lat");
            double lon = bundle.getDouble("lon");
            shippingLatlng = new LatLng(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        initialize();
    }

    private void initialize() {
        Places.initialize(MapsActivity.this, getResources().getString(R.string.places_api_billing_key));

        back = findViewById(R.id.ivBack);
        back.setOnClickListener(this);
        btnDoneMap = findViewById(R.id.btnDoneMap);
        btnDoneMap.setOnClickListener(this);
        ivMyLoc = (ImageView) findViewById(R.id.ivMyLoc);
        ivMyLoc.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to ble used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mapView = mapFragment.getView();

        autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().
                findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                shippingLatlng = place.getLatLng();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(shippingLatlng, 15);
                mMap.animateCamera(cameraUpdate);

                updateMarker();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "onError: Auto Complete" + status);
            }

        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap == null) {
            return;
        }


        // For zooming automatically to the location of the marker
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = (int) (marker.getTag());
                ApplicationUtils.hideKeyboard(mContext);
                return false;
            }
        });

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            locationButton.setVisibility(View.GONE);
        }


        configureCameraIdle();
        googleMap.setOnCameraIdleListener(onCameraIdleListener);

        if (mapView != null)
            ApplicationUtils.enableGPS(mContext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        googleMap.setMyLocationEnabled(true);


        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            locationButton.setVisibility(View.GONE);
        }


    }


    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                shippingLatlng = mMap.getCameraPosition().target;
                if (shippingLatlng.latitude != 0 && shippingLatlng.longitude != 0) {
                    new GetAddressAsync(shippingLatlng.latitude, shippingLatlng.longitude).execute();
                }
                //  getAddress(shippingLatlng.latitude, shippingLatlng.longitude);
            }
        };
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!shippingAddress.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    mMap.setMyLocationEnabled(true);
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(shippingLatlng, 15);
            mMap.animateCamera(cameraUpdate);

            // getAddress(shippingLatlng.latitude, shippingLatlng.longitude);
            new GetAddressAsync(shippingLatlng.latitude, shippingLatlng.longitude).execute();
            // updateMarker();

        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        getCurrentLocation();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getCurrentLocation() throws Exception {

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {

            shippingLatlng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(shippingLatlng, 15);
            mMap.animateCamera(cameraUpdate);

            // getAddress(shippingLatlng.latitude, shippingLatlng.longitude);
            new GetAddressAsync(shippingLatlng.latitude, shippingLatlng.longitude).execute();
            //   updateMarker();

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleLocation(location);
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);

                        if (!shippingAddress.equals("") && !shippingAddress.equals("pickupAddress")) {
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(shippingLatlng, 15);
                            mMap.animateCamera(cameraUpdate);
                            //  getAddress(shippingLatlng.latitude, shippingLatlng.longitude);

                            new GetAddressAsync(shippingLatlng.latitude, shippingLatlng.longitude).execute();
                            //updateMarker();

                        } else {
                            try {
                                getCurrentLocation();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } else {
                    ApplicationUtils.showToast(MapsActivity.this, getResources().getString(R.string.permission_denied), false);
                }
                return;
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocation(location);
    }

    private void handleLocation(Location location) {
        shippingLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(shippingLatlng, 15);
        mMap.animateCamera(cameraUpdate);
        getAddress(location.getLatitude(), location.getLongitude());
        // updateMarker();

    }

    private void updateMarker() {
    /*    markerOptions = new MarkerOptions();
        markerOptions.position(shippingLatlng);
        markerOptions.draggable(true);
        mMap.clear();
        mMap.addMarker(markerOptions);*/
    }


    class GetAddressAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();
        double LATITUDE;
        double LONGITUDE;

        public GetAddressAsync(double LATITUDE, double LONGITUDE) {

            this.LATITUDE = LATITUDE;
            this.LONGITUDE = LONGITUDE;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute", "On pre Exceute......");
        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            shippingAddress = "";
            shippingAddress = getAddress(LATITUDE, LONGITUDE);
            return shippingAddress;
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String latLngAddress) {
            super.onPostExecute(latLngAddress);

            if (ApplicationUtils.isSet(latLngAddress)) {
                autocompleteSupportFragment.setText(latLngAddress); // Here is you AutoCompleteTextView where you want to set your string address (You can remove it if you not need it)
            }
        }
    }

    private String getAddress(double latitude, double longitude) {
        Address address;
        StringBuilder result = new StringBuilder();
        try {

            System.out.println("get address");
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                System.out.println("size====" + addresses.size());
                address = addresses.get(0);

                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i) + ",");
                    }
                }
                System.out.println("ad==" + address);
                System.out.println("result---" + result.toString());

                return result.toString();
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return "";
    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

                mGoogleApiClient.disconnect();
            }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnDoneMap) {

            //   new GetAddressAsync(shippingLatlng.latitude, shippingLatlng.longitude).execute();
            //  System.out.println(shippingAddress);


            Intent intent = new Intent();
            intent.putExtra("address", shippingAddress);
            intent.putExtra("lat", shippingLatlng.latitude);
            intent.putExtra("lon", shippingLatlng.longitude);

            setResult(RESULT_OK, intent);
            finish();
        } else if (view.getId() == R.id.ivBack) {
            setResult(RESULT_CANCELED);
            this.finish();
        } else if (view.getId() == R.id.ivMyLoc) {

            ApplicationUtils.hideKeyboard(mContext);

            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
            if (mMap != null)
                mMap.setMyLocationEnabled(true);

            if (mapView != null &&
                    mapView.findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                if (locationButton != null)
                    locationButton.setVisibility(View.GONE);
            }
            if (mapView != null) {
                if (locationButton != null) {
                    ApplicationUtils.enableGPS(mContext);
                    locationButton.callOnClick();
                }
            }

        }
    }

}





