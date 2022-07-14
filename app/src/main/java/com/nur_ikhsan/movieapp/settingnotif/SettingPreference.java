package com.nur_ikhsan.movieapp.settingnotif;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {

    private static final String PREFS_NAME = "setting_pref";
    private static final String RELEASE_REMINDER = "isRelease";
    private final SharedPreferences mSharedPreferences;

    public SettingPreference(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void setReleaseReminder(boolean isActive) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(RELEASE_REMINDER, isActive);
        editor.apply();
    }


    public boolean getReleaseReminder() {
        return mSharedPreferences.getBoolean(RELEASE_REMINDER, false);
    }
}
