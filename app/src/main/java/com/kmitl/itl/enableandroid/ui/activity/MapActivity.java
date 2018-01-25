package com.kmitl.itl.enableandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kmitl.itl.enableandroid.http.HttpManager;
import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.ui.activity.base.BaseActivity;
import com.kmitl.itl.enableandroid.databinding.ActivityMapBinding;
import com.kmitl.itl.enableandroid.model.PlaceSearchResponse.PlaceResult;

import org.parceler.Parcels;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapActivity extends BaseActivity<ActivityMapBinding> implements OnMapReadyCallback {

    private static final String TAG = MapActivity.class.getClass().getSimpleName();
    private static final int MAP_DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    public static final double MAP_DEFAULT_LATITUDE = 13.7369667;
    public static final double MAP_DEFAULT_LONGTUDE = 100.5374572;
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private DatabaseReference mDataBase;
    private FusedLocationProviderClient mLocationClient;
    private Disposable mDispoable;
    private Location mLastKnowLocation;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initInstances() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mBinding.cvSearch.setOnClickListener(v -> performPlaceSearch());
        mBinding.fabMyLocation.setOnClickListener(v -> getLastLocation(false));

        mDataBase = FirebaseDatabase.getInstance().getReference().child("User Destinations");
    }

    private void performPlaceSearch() {
        try {
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setCountry("TH")
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(autocompleteFilter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateMap();
    }

    private void performSearchBusStation(Location place) {
        String location = place.getLatitude() + "," + place.getLongitude();
        long radius = 1000;

        mDispoable = HttpManager.getInstance().getService()
                .searchBusStation(location, radius)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeSearchResponse -> {
                    for (PlaceResult ps :
                            placeSearchResponse.getPlaceResult()) {
                        Marker marker = mMap.addMarker(
                                new MarkerOptions()
                                        .position(ps.getGeometry().getLatLng())
                                        .title(ps.getName()));
                        marker.setTag(ps);
                    }
                }, throwable -> Log.e(TAG, throwable.getMessage()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mLocationPermissionGranted) {
            checkLocationPermission();
        } else {
            mLocationClient.requestLocationUpdates(LocationRequest.create(), mLocationCallback, Looper.myLooper());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDispoable != null && !mDispoable.isDisposed()) {
            mDispoable.dispose();
        }

        mLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mBinding.tvPlaceName.setText("ปลายทาง : " + place.getName());
                mDataBase.push().setValue(place.getName());
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
//                mMap.addMarker(new MarkerOptions()
//                        .position(place.getLatLng())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                performSearchBusStation(place);
                if (mLastKnowLocation != null) {
                    performSearchBusStation(mLastKnowLocation);
                    LatLng latLng = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    showToast("ไม่สามารถหาตำแหน่งของคุณได้ กรุณาตรวจสอบการตั้งค่าตำแหน่งของคุณอีกครั้ง");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                mBinding.tvPlaceName.setText("");
                mMap.clear();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
        updateMap();
        getLastLocation(true);
    }

    private void initMap() {
        if (mMap == null) {
            return;
        }

        mMap.setMinZoomPreference(7);
        mMap.setMaxZoomPreference(20);
        mMap.setOnInfoWindowClickListener(marker -> {
            PlaceResult placeResult = (PlaceResult) marker.getTag();
            Intent intent = new Intent(this, BusStationDetailActivity.class);
            intent.putExtra("bus_station", Parcels.wrap(placeResult));
            startActivity(intent);

        });
    }

    private void updateMap() {
        if (mMap == null) {
            return;
        }

        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mBinding.fabMyLocation.setVisibility(View.VISIBLE);
            } else {
                mMap.setMyLocationEnabled(false);
                mBinding.fabMyLocation.setVisibility(View.GONE);
                checkLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(boolean isInitMap) {
        if (!mLocationPermissionGranted) {
            moveCameraToDefault();
            return;
        }

        mLocationClient.getLastLocation()
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        Location lastLocation = task.getResult();
                        if (lastLocation != null) {
                            mLastKnowLocation = lastLocation;
                            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_DEFAULT_ZOOM));
                        } else {
                            showToast("ไม่สามารถหาตำแหน่งของคุณได้ กรุณาตรวจสอบการตั้งค่าตำแหน่งของคุณอีกครั้ง");
                            if (isInitMap) {
                                moveCameraToDefault();
                            }
                        }
                    } else if (isInitMap) {
                        moveCameraToDefault();
                    }
                });
    }

    private void moveCameraToDefault() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGTUDE), MAP_DEFAULT_ZOOM));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            if (lastLocation != null) {
                mLastKnowLocation = lastLocation;
            }
        }
    };
}
