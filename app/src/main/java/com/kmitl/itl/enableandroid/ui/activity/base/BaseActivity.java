package com.kmitl.itl.enableandroid.ui.activity.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected T mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        mBinding = DataBindingUtil.setContentView(this, layoutId);
        initInstances();
    }

    protected abstract int getLayoutId();

    protected abstract void initInstances();
}
