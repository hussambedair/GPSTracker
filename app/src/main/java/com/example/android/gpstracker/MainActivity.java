package com.example.android.gpstracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.gpstracker.Base.BaseActivity;
import com.example.android.gpstracker.Utils.MyLocationProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends BaseActivity
        implements LocationListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION_CODE = 500;

    MapView mapView;

    GoogleMap googleMap;
    Marker currentLocationMarker;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker to the user's current location and move the camera
        changeUserMarkerLocation();



    }

    public void changeUserMarkerLocation () {
        if (currentLocation != null && googleMap != null) {

            LatLng latLng = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
            if (currentLocationMarker == null) {
                currentLocationMarker= googleMap.addMarker(markerOptions);
            } else {
                currentLocationMarker.setPosition(latLng);
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        if (isLocationPermissionAllowed()) {
            // do your function
            getUserLocation();
        } else {
            //request permission
            requestLocationPermission();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    MyLocationProvider locationProvider;
    Location currentLocation;
    public void getUserLocation () {
        locationProvider = new MyLocationProvider(activity, this);
        currentLocation = locationProvider.getCurrentLocation();
        if (currentLocation != null) {
            Toast.makeText(activity,
                    currentLocation.getLatitude()+" "+currentLocation.getLongitude() ,
                    Toast.LENGTH_SHORT).show();

        }

    }

    private void requestLocationPermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            showConfirmationMessage(R.string.warning,
                    R.string.permission_explaination,
                    R.string.ok, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION_CODE);

                        }
                    });
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION_CODE);

            // MY_PERMISSIONS_REQUEST_LOCATION_CODE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }




    public boolean isLocationPermissionAllowed () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        // permission is granted
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // call your function
                    getUserLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(activity,"We cannot get your nearest friend. Permission is denied!",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        changeUserMarkerLocation();
        Toast.makeText(activity,
                currentLocation.getLatitude()+" "+currentLocation.getLongitude() ,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("provider enabled!", provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("provider disabled!", provider);

    }
}
