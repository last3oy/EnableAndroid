package com.kmitl.itl.enableandroid.ui.activity;

import android.widget.Toast;

import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.ui.activity.base.BaseActivity;
import com.kmitl.itl.enableandroid.databinding.ActivityBusStationDetailBinding;
import com.kmitl.itl.enableandroid.model.PlaceSearchResponse.PlaceResult;

import org.parceler.Parcels;

public class BusStationDetailActivity extends BaseActivity<ActivityBusStationDetailBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bus_station_detail;
    }

    @Override
    protected void initInstances() {
        PlaceResult placeResult = Parcels.unwrap(getIntent().getParcelableExtra("bus_station"));
        Toast.makeText(this, "" + placeResult.getId(), Toast.LENGTH_SHORT).show();
    }

}
