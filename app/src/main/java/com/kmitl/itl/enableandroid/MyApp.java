package com.kmitl.itl.enableandroid;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;


public class MyApp extends Application {

    private static MyApp INSTANCE;
    private String mBusTopic;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static MyApp getInstance() {
        return INSTANCE;
    }

    public void unsubscribeBus() {

    }

    public void subscibeBus(String busTopic) {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        if (mBusTopic != null) {
            firebaseMessaging.unsubscribeFromTopic(mBusTopic);
        }

        if (busTopic != null) {
            mBusTopic = busTopic;
            firebaseMessaging.subscribeToTopic(busTopic);
        }
    }
}
