package com.loycompany.hopesoninter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.loycompany.hopesoninter.fragments.main.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsFragment = new SettingsFragment("SettingsActivity");

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, settingsFragment).commit();
    }
}