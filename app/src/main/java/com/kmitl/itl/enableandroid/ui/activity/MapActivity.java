package com.kmitl.itl.enableandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kmitl.itl.enableandroid.PrefsManager;
import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.databinding.ActivityMapBinding;
import com.kmitl.itl.enableandroid.http.HttpManager;
import com.kmitl.itl.enableandroid.model.PlaceSearchResponse.PlaceResult;
import com.kmitl.itl.enableandroid.ui.activity.base.BaseActivity;

import org.parceler.Parcels;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MapActivity extends BaseActivity<ActivityMapBinding> implements OnMapReadyCallback {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_SETTING_REQUEST_CODE = 200;
    private static final int PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE = 99;
    private static final int MAP_DEFAULT_ZOOM = 15;

    private static final double MAP_DEFAULT_LATITUDE = 13.7369667;
    private static final double MAP_DEFAULT_LONGITUDE = 100.5374572;

    private static final LatLng DEFAULT_LAT_LNG = new LatLng(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE);

    private static final String TAG = MapActivity.class.getClass().getSimpleName();

    private boolean mLocationPermissionGranted;
    private boolean mHasLocationSetting;

    private GoogleMap mMap;
    private DatabaseReference mDataBase;
    private FusedLocationProviderClient mLocationClient;
    private Disposable mDispoable;
    private Location mLastKnowLocation;
    private Place mDestinationPlace;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initInstances() {
        mLocationPermissionGranted = PrefsManager.getInstance().getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mBinding.cvSearch.setOnClickListener(v -> performPlaceSearch());
        mBinding.fabMyLocation.setOnClickListener(v -> getLastLocation());

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
            case PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE: {
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
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDispoable != null && !mDispoable.isDisposed()) {
            mDispoable.dispose();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                handleSearchResult(data);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                mBinding.tvPlaceName.setText("");
                clearMarker();
            }
        } else if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mHasLocationSetting = true;
            } else {
                mHasLocationSetting = false;
            }
        }
    }

    private void handleSearchResult(Intent data) {
        clearMarker();
        if (mLastKnowLocation == null) {
            showToast("ไม่สามารถแสดงข้อมูลได้เนื่องจากไม่มีตำแหน่งของคุณ กรุณาเปิดตำแหน่ง"); // Mock message
            requestLocationSetting();
            return;
        }

        performSearchBusStation(mLastKnowLocation);
        mDestinationPlace = PlaceAutocomplete.getPlace(this, data);
        mBinding.tvPlaceName.setText("ปลายทาง : " + mDestinationPlace.getName());
        mDataBase.push().setValue(mDestinationPlace.getName());
        LatLngBounds.Builder builder = LatLngBounds.builder();
        LatLng lastKnowLatLng = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
        builder.include(mDestinationPlace.getLatLng());
        builder.include(lastKnowLatLng);
        mMap.addMarker(new MarkerOptions()
                .position(mDestinationPlace.getLatLng())
                .title(mDestinationPlace.getName().toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 250));
    }

    private void clearMarker() {
        mMap.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
        checkLocationPermission();
        updateMap();
        requestLocationSetting();
        getLastLocation();
    }

    private void requestLocationSetting() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, mLocationSettingSuccess);
        task.addOnFailureListener(this, mLocationSettingFailure);
    }

    private void initMap() {
        if (mMap == null) {
            return;
        }

        mMap.setMinZoomPreference(7);
        mMap.setMaxZoomPreference(20);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LAT_LNG, MAP_DEFAULT_ZOOM));
        mMap.setOnInfoWindowClickListener(this::goToBusStationDetail);

    }

    private void goToBusStationDetail(Marker marker) {
        PlaceResult placeResult = (PlaceResult) marker.getTag();
        if (mDestinationPlace != null && placeResult != null) {
            Intent intent = new Intent(this, BusStationDetailActivity.class);
            intent.putExtra("destination_lat_lng", mDestinationPlace.getLatLng());
            intent.putExtra("bus_station", Parcels.wrap(placeResult));
            startActivity(intent);
        }
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
            PrefsManager.getInstance().saveLocationPermission();
            updateMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (!mLocationPermissionGranted) {
            checkLocationPermission();
            return;
        }

        if (!mHasLocationSetting) {
            requestLocationSetting();
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
                            return;
                        }
                    }
                    showToast("ไม่สามารถหาตำแหน่งของคุณได้ กรุณาตรวจสอบการตั้งค่าตำแหน่งของคุณอีกครั้ง");
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private OnSuccessListener<LocationSettingsResponse> mLocationSettingSuccess = new OnSuccessListener<LocationSettingsResponse>() {
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            mHasLocationSetting = true;
            getLastLocation();
        }
    };

    private OnFailureListener mLocationSettingFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(MapActivity.this,
                            LOCATION_SETTING_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {
                    Log.e(TAG, sendEx.getMessage());
                }
            }
        }
    };
}
