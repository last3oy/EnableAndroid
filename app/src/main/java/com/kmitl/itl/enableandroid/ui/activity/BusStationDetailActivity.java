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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BusStationDetailActivity extends BaseActivity<ActivityBusStationDetailBinding> {

    private List<Bus> mBus = new ArrayList<>();
    private BusAdapter mAdapter;
    private Disposable mDisposable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bus_station_detail;
    }

    @Override
    protected void initInstances() {
        PlaceResult placeResult = Parcels.unwrap(getIntent().getParcelableExtra("bus_station"));
        mAdapter = new BusAdapter(mBus, new BusAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Bus bus) {
                MyApp.subscibeBus(bus.getBusno());
            }
        });
        mBinding.rvBus.setAdapter(mAdapter);
        mBinding.tvBusStation.setText(placeResult.getName());
        mBinding.rvBus.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvBus.setItemAnimator(new DefaultItemAnimator());

        mDisposable = HttpManager.getInstance().getService().getBus("B01001")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bus>() {
                    @Override
                    public void accept(Bus bus) throws Exception {
                        mBus.clear();
                        mBus.add(bus);
                        mAdapter.setBus(mBus);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(BusStationDetailActivity.this, "Cann't connect the Server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
