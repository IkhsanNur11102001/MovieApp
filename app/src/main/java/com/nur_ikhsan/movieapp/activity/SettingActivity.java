package com.nur_ikhsan.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.nur_ikhsan.movieapp.R;

import com.nur_ikhsan.movieapp.notifikasi.NotificationReleaseReceiver;
import com.nur_ikhsan.movieapp.settingnotif.SettingPreference;

public class SettingActivity extends AppCompatActivity {
    private Switch switchRelease;


    private NotificationReleaseReceiver notificationReleaseReceiver;
    private SettingPreference settingPreference;
    private Button button;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchRelease = findViewById(R.id.swRealeseToday);
        button = findViewById(R.id.btnChangeLanguage);


        notificationReleaseReceiver = new NotificationReleaseReceiver();

        settingPreference = new SettingPreference(this);

        setSwitchRelease();

        // Switch Release OnClick
        switchRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchRelease.isChecked()) {
                    notificationReleaseReceiver.setReleaseAlarm(getApplicationContext());
                    settingPreference.setReleaseReminder(true);
                    Toast.makeText(getApplicationContext(), "Pengingat rilis diaktifkan", Toast.LENGTH_SHORT).show();
                } else {
                    notificationReleaseReceiver.cancelAlarm(getApplicationContext());
                    settingPreference.setReleaseReminder(false);
                    Toast.makeText(getApplicationContext(), "Pengingat rilis dinonaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Button Change Language OnClick
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
            }
        });
    }

    private void setSwitchRelease() {
        if (settingPreference.getReleaseReminder()) switchRelease.setChecked(true);
        else switchRelease.setChecked(false);
    }
}