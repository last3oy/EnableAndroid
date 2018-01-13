package com.kmitl.itl.enableandroid.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleServiceInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String requestHost = originalRequest.url().host();
        if ("maps.googleapis.com".equalsIgnoreCase(requestHost)) {
            HttpUrl url = originalRequest.url().newBuilder()
                    .addQueryParameter("key", "AIzaSyDvP8SXRLQVhmujhnn-ecqrWdUFkC0QBhk")
                    .build();

            Request newRequest = originalRequest.newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
