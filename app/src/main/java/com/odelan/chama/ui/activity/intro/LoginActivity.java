package com.odelan.chama.ui.activity.intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.github.javiersantos.appupdater.AppUpdater;
import com.mukesh.countrypicker.Countries;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.activity.main.creater.SetDefaultValuesActivity;
import com.odelan.chama.ui.base.BaseActivity;
import com.odelan.chama.utils.Common;
import com.odelan.chama.utils.DlgHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class LoginActivity extends BaseActivity implements OnCountryPickerListener {

    private CountryPicker countryPicker;
    boolean isRemember = false;

    @BindView(R.id.chamaNameET)
    EditText chamaNameET;

    @BindView(R.id.codeET)
    EditText codeET;

    @BindView(R.id.phoneET)
    EditText phoneET;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.lock)
    ImageView lockBtn;

    @BindView(R.id.checkBox)
    CheckBox checkBox;

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 9999;
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasPermissionsGranted(PERMISSIONS)) {
            requestPermissions(PERMISSIONS);
        } else {
            init();
        }
    }

    private void init() {
        codeET.setText("254");
        phoneET.setText(getValueFromKey("Phone"));
        passwordET.setText(getValueFromKey("Password"));

        if (getValueFromKey("isRemember").equals("true")) {
            checkBox.setChecked(true);
            isRemember = true;
        }

        countryPicker =
                new CountryPicker.Builder().with(this)
                        .listener(this)
                        .build();
        countryPicker.setCountries(Countries.getCountriesByCountryNames(Countries.APP_COUNTRIES));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemember = isChecked;
            }
        });

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordET.getInputType() == 129) {
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT);
                    lockBtn.setImageResource(R.drawable.invisible);
                } else {
                    passwordET.setInputType(129);
                    lockBtn.setImageResource(R.drawable.visible);
                }
            }
        });
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        boolean hasPermision = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermision = false;
            }
        }

        return hasPermision;
    }

    private void showPermissionError() {
        new DlgHelper().showDialog(mContext, getString(R.string.warning), getString(R.string.permission_denied));
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(mContext, permissions, STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasPermissionsGranted(PERMISSIONS)) {
                        init();
                    } else {
                        showPermissionError();
                    }
                } else {
                    showPermissionError();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onSelectCountry(Country country) {
        codeET.setText(country.getDialCode());
    }

    @OnClick(R.id.codeET)
    public void onCodeET() {
        countryPicker.showDialog(getSupportFragmentManager());
    }

    @OnClick(R.id.forgotPasswordLL)
    public void onForgotPassword() {
        if (phoneET.getText().toString().isEmpty()) {
            showToast("Please input phone number");
            return;
        }

        if (chamaNameET.getText().toString().isEmpty()) {
            showToast("Please input chama name");
            return;
        }

        String phone = codeET.getText().toString() + phoneET.getText().toString();

        AndroidNetworking.post(BASE_API_URL + "users/resetPassword")
                .addBodyParameter("phone", phone)
                .addBodyParameter("chama_name", chamaNameET.getText().toString())
                .setTag("resetPassword")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success == true) {
                                String message = response.getString("message");
                                showToast("Please use your new password sent to your phone to login again.");
                            } else {
                                String message = response.getString("message");
                                showToast(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("Response parse error");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dismissLoading();
                        showToast("Network Error");
                    }
                });
    }

    @OnClick(R.id.loginBtn)
    public void onLogin() {

        if (codeET.getText().toString().isEmpty()) {
            showToast("Please choose country");
            return;
        }

        if (phoneET.getText().toString().isEmpty()) {
            showToast("Please input phone number");
            return;
        }

        if (passwordET.getText().toString().isEmpty()) {
            showToast("Please input password");
            return;
        }

        if (isRemember) {
            saveKeyValue("CountryDialCode", codeET.getText().toString());
            saveKeyValue("Phone", phoneET.getText().toString());
            saveKeyValue("Password", passwordET.getText().toString());
            saveKeyValue("isRemember", "true");
        } else {
            saveKeyValue("CountryDialCode", "");
            saveKeyValue("Phone", "");
            saveKeyValue("Password", "");
            saveKeyValue("isRemember", "false");
        }

        login();
    }

    @OnClick(R.id.signupBtn)
    public void onSignup() {
        startActivityWithAnim(new Intent(mContext, SignupActivity.class));
    }

    public void login() {
        showLoading();
        String phone = codeET.getText().toString() + phoneET.getText().toString();
        AndroidNetworking.post(BASE_API_URL + "users/loginUser")
                .addBodyParameter("chama_name", chamaNameET.getText().toString())
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", passwordET.getText().toString())
                .addBodyParameter("dev_id", "")
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success == true) {
                                JSONObject data = response.getJSONObject("doc");
                                MyApplication.sUser = LoganSquare.parse(data.toString(), User.class);
                                MyApplication.interest_rate = response.getInt("interest_rate");
                                MyApplication.transaction_fee = response.getInt("transaction_fee");

                                JSONArray transactions = response.getJSONArray("transactions");
                                int total = 0;
                                int contribute = 0;
                                int borrow = 0;
                                int loan = 0;
                                for (int i = 0; i < transactions.length(); i++) {
                                    JSONObject cur = transactions.getJSONObject(i);
                                    if (cur.getString("type").equals(TransactionMD.TYPE_CONTRIBUTE))
                                        contribute += cur.getInt("amount");
                                    else if (cur.getString("type").equals(TransactionMD.TYPE_BORROW))
                                        borrow += cur.getInt("amount");
                                    else if (cur.getString("type").equals(TransactionMD.TYPE_LOANS))
                                        loan += cur.getInt("amount");

                                    if (cur.getString("type").equals(TransactionMD.TYPE_BORROW))
                                        total -= cur.getInt("amount");
                                    else
                                        total += cur.getInt("amount");
                                }
                                MyApplication.contribution_amount = contribute;
                                MyApplication.borrow_amount = borrow;
                                MyApplication.loan_amount = loan;

                                //////////////*******************  Socket *********************\\\\\\\\\\\\\\\\\\\\\\\\\\\
                                JSONObject sData = new JSONObject();
                                sData.put("chama_code", MyApplication.sUser.chama_code);
                                sData.put("phone", MyApplication.sUser.phone);
                                MyApplication.mSocket.emit("login", sData);

                                startActivityWithAnim(new Intent(mContext, HomeActivity.class));
                            } else {
                                String message = response.getString("message");
                                showToast(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("Response parse error");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dismissLoading();
                        showToast("Network Error");
                    }
                });
    }
}
