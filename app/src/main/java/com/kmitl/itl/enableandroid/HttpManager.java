package com.kmitl.itl.enableandroid;

import com.google.android.gms.common.api.Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class HttpManager {
    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager();
        }
        return instance;
    }

    private ApiService mService;

    public ApiService getService() {
        return mService;
    }

    private HttpManager() {
        //TODO: add base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.google.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        mService = retrofit.create(ApiService.class);
    }
}
