package com.odelan.chama.ui.activity.main;

import android.os.Bundle;

import com.odelan.chama.R;
import com.odelan.chama.ui.base.BaseNavigationActivity;

public class SettingsActivity extends BaseNavigationActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_settings;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.settings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
