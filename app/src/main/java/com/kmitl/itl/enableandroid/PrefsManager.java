package com.kmitl.itl.enableandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private static PrefsManager INSTANCE;

    public static PrefsManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrefsManager();
        }
        return INSTANCE;
    }

    private PrefsManager() {

    }

    private SharedPreferences mPreferences;

    public void init(Context context) {
        mPreferences = context.getSharedPreferences("Enable", Context.MODE_PRIVATE);
    }

    public void saveLocationPermission() {
        mPreferences.edit()
                .putBoolean("KEY_PERMISSION", true)
                .apply();
    }

    public boolean getLocationPermission() {
        return mPreferences.getBoolean("KEY_PERMISSION", false);
    }
}
