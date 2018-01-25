package com.kmitl.itl.enableandroid.ui.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.kmitl.itl.enableandroid.MyApp;
import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.http.HttpManager;
import com.kmitl.itl.enableandroid.model.Bus;
import com.kmitl.itl.enableandroid.ui.activity.base.BaseActivity;
import com.kmitl.itl.enableandroid.databinding.ActivityBusStationDetailBinding;
import com.kmitl.itl.enableandroid.model.PlaceSearchResponse.PlaceResult;
import com.kmitl.itl.enableandroid.ui.adapter.BusAdapter;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class BusStationDetailActivity extends BaseActivity<ActivityBusStationDetailBinding> {

    List<Bus> mBus = Arrays.asList(
//            new Bus("0", "A1", 2, 30, "time"),
//            new Bus("1", "A2", 7, 30, "time"),
//            new Bus("2", "A3", 5, 30, "time"),
//            new Bus("3", "A4", 4, 30, "time"),
//            new Bus("4", "A5", 1, 30, "time")
    );

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bus_station_detail;
    }

    @Override
    protected void initInstances() {
        HttpManager.getInstance().getService().getBus("B01001")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bus>() {
                    @Override
                    public void accept(Bus bus) throws Exception {
                        Log.i("eiei",""+bus.getBusno());
                    }
                });
        PlaceResult placeResult = Parcels.unwrap(getIntent().getParcelableExtra("bus_station"));
        mBinding.tvBusStation.setText(placeResult.getName());
        BusAdapter adapter = new BusAdapter(mBus, new BusAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Bus bus) {
                MyApp.subscibeBus(bus.getBusno());
            }
        });
        mBinding.rvBus.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvBus.setItemAnimator(new DefaultItemAnimator());
        mBinding.rvBus.setAdapter(adapter);
    }

}
