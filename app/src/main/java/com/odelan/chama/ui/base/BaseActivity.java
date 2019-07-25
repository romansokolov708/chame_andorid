package com.odelan.chama.ui.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.chama.R;
import com.odelan.chama.utils.Common;
import com.odelan.chama.utils.MultiLanguageHelper;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 7/18/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity mContext;
    public String TAG = "BaseActivity";

    public MultiLanguageHelper multiLanguageHelper;

    protected abstract int getRootLayoutResID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getRootLayoutResID());

        if (!(this instanceof BaseNavigationActivity)) {
            ButterKnife.bind(this);
        }

        mContext = this;
        multiLanguageHelper = new MultiLanguageHelper(mContext);
    }

    public void setLanguage(String language){
        multiLanguageHelper.setLanguage(mContext.getClass(), language);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void saveKeyValue(String key, String value) {
        Common.saveInfoWithKeyValue(mContext, key, value);
    }

    public String getValueFromKey(String key) {
        return Common.getInfoWithValueKey(mContext, key);
    }

    private Toast mToast;

    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private KProgressHUD kProgressHUD = null;
    public void showLoading() {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.please_wait))
                    .setWindowColor(Color.parseColor("#DDDDDDDD"))
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f);
        }

        kProgressHUD.show();
    }

    public void showLoading(String message) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setWindowColor(Color.parseColor("#DDDDDDDD"))
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f);
        } else {
            kProgressHUD.setLabel(message);
        }

        kProgressHUD.show();
    }

    public void dismissLoading() {
        if(kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    public void showKeyboard(View view) {
        Common.showSoftKeyboard(mContext, view);
    }

    public void hideKeyboard(View view) {
        Common.hideSoftKeyboard(mContext, view);
    }

    @Override
    public void finish() {
        super.finish();

        animToRight();
    }

    public void startActivityWithAnim(Intent intent) {
        startActivity(intent);
        animToLeft();
    }

    public void animToLeft() {
        // appear new activity with animation (right to left)
        overridePendingTransition(R.anim.rightin_activity, R.anim.not_move_activity);
    }

    public void animToRight() {
        // disappear old activity with animation (left to right)
        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
    }

    public boolean isEmpty(EditText et) {
        return et.getText().toString().isEmpty() ? true : false;
    }
}
