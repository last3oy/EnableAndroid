package com.kmitl.itl.enableandroid.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.databinding.ActivitySplashBinding;
import com.kmitl.itl.enableandroid.ui.activity.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends Activity {
    private Disposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisposable = Completable.timer(2000, TimeUnit.MILLISECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Intent intent = new Intent(this, MapActivity.class);
                    startActivity(intent);
                    finish();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSubscription();
    }

    private void clearSubscription() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
