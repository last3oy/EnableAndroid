package com.kmitl.itl.enableandroid.http;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new GoogleServiceInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://servicetest0362.somee.com/")
                .addConverterFactory(
                        new AnnotatedConverterFactory.Builder()
                                .add(Gson.class, GsonConverterFactory.create())
                                .add(SimpleXml.class, SimpleXmlConverterFactory.create())
                                .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        mService = retrofit.create(ApiService.class);
    }
}
