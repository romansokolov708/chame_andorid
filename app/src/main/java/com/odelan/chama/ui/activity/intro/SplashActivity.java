package com.odelan.chama.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.odelan.chama.R;
import com.odelan.chama.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            });
        }
    };

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_intro;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        handler.postDelayed(runnable, 2000);
    }
}
