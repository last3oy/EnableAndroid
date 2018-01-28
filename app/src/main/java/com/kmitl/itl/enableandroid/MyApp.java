package com.kmitl.itl.enableandroid;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;


public class MyApp extends Application {

    private static String mBusTopic;
    private static SharedPreferences mPref;


    @Override
    public void onCreate() {
        super.onCreate();

        PrefsManager.getInstance().init(this);
    }

    public static void unsubscribeBus() {
        if (mBusTopic == null) {
            return;
        }
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        Log.i("unsub", mBusTopic);
        firebaseMessaging.unsubscribeFromTopic(mBusTopic);
        mBusTopic = null;
    }

    public static void subscibeBus(String busTopic) {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        if (mBusTopic != null) {
            unsubscribeBus();
        }

        if (busTopic != null) {
            mBusTopic = busTopic;
            Log.i("sub", busTopic);
            firebaseMessaging.subscribeToTopic(busTopic);
        }
    }
}
