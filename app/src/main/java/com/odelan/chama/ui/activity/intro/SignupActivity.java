package com.odelan.chama.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.mukesh.countrypicker.Countries;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.activity.main.creater.SetDefaultValuesActivity;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import com.odelan.chama.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class SignupActivity extends BaseNavigationActivity implements OnCountryPickerListener {

    @BindView(R.id.nameET1)
    EditText nameET1;

    @BindView(R.id.nameET2)
    EditText nameET2;

    @BindView(R.id.codeET1)
    EditText codeET1;

    @BindView(R.id.codeET2)
    EditText codeET2;

    @BindView(R.id.phoneET1)
    EditText phoneET1;

    @BindView(R.id.phoneET2)
    EditText phoneET2;

    @BindView(R.id.passwordET1)
    EditText passwordET1;

    @BindView(R.id.passwordET2)
    EditText passwordET2;

    @BindView(R.id.chamaCodeET)
    EditText chamaCodeET;

    @BindView(R.id.chamaNameET)
    EditText chamaNameET;

    private CountryPicker countryPicker;
    boolean isCodeET1 = true;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_signup;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToggleBtn();
        showBackButton();

        codeET1.setText("254");
        codeET2.setText("254");

        countryPicker =
                new CountryPicker.Builder().with(this)
                        .listener(this)
                        .build();
        countryPicker.setCountries(Countries.getCountriesByCountryNames(Countries.APP_COUNTRIES));
    }

    @OnClick(R.id.backBtn) public void onBack() {
        finish();
    }

    @OnClick(R.id.submitBtn1) public void onSubmit1() {
        //startActivityWithAnim(new Intent(mContext, HomeActivity.class));

        if (nameET1.getText().toString().isEmpty()) {
            showToast("Please input full name");
            return;
        }

        if (codeET1.getText().toString().isEmpty()) {
            showToast("Please select dial code");
            return;
        }

        if (phoneET1.getText().toString().isEmpty()) {
            showToast("Please input your phone number");
            return;
        }

        if (passwordET1.getText().toString().isEmpty()) {
            showToast("Please input password");
            return;
        }

        if (chamaCodeET.getText().toString().isEmpty()) {
            showToast("Please input Chama Code");
            return;
        }

        signupAsJoiner();
    }

    @OnClick(R.id.submitBtn2) public void onSubmit2() {
        //startActivityWithAnim(new Intent(mContext, SetDefaultValuesActivity.class));

        if (nameET2.getText().toString().isEmpty()) {
            showToast("Please input full name");
            return;
        }

        if (codeET2.getText().toString().isEmpty()) {
            showToast("Please select dial code");
            return;
        }

        if (phoneET2.getText().toString().isEmpty()) {
            showToast("Please input your phone number");
            return;
        }

        if (passwordET2.getText().toString().isEmpty()) {
            showToast("Please input password");
            return;
        }

        if (chamaNameET.getText().toString().isEmpty()) {
            showToast("Please input Chama Name");
            return;
        }

        signupAsLeader();
    }

    @OnClick(R.id.codeET1) public void onCodeET1() {
        isCodeET1 = true;
        countryPicker.showDialog(getSupportFragmentManager());
    }

    @OnClick(R.id.codeET2) public void onCodeET2() {
        isCodeET1 = false;
        countryPicker.showDialog(getSupportFragmentManager());
    }

    @Override
    public void onSelectCountry(Country country) {
        if (isCodeET1) {
            codeET1.setText(country.getDialCode());
        } else {
            codeET2.setText(country.getDialCode());
        }
    }

    public void signupAsJoiner() {
        String phone = codeET1.getText().toString() + phoneET1.getText().toString();
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "users/signUpJoiner")
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", passwordET1.getText().toString())
                .addBodyParameter("user_name", nameET1.getText().toString())
                .addBodyParameter("chama_code", chamaCodeET.getText().toString())
                .addBodyParameter("dev_id", "")
                .setTag("signupAsJoiner")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            Boolean success =  response.getBoolean("success");
                            if (success == true) {
                                JSONObject userObject = response.getJSONObject("doc");
                                MyApplication.sUser = LoganSquare.parse(userObject.toString(), User.class);
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

    public void signupAsLeader() {
        showLoading();
        String phone = codeET2.getText().toString() + phoneET2.getText().toString();
        AndroidNetworking.post(BASE_API_URL + "users/signUpLeader")
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", passwordET2.getText().toString())
                .addBodyParameter("user_name", nameET2.getText().toString())
                .addBodyParameter("chama_name", chamaNameET.getText().toString())
                .addBodyParameter("dev_id", "")
                .setTag("signupAsLeader")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            Boolean success =  response.getBoolean("success");
                            if (success == true) {
                                JSONObject userObject = response.getJSONObject("doc");
                                MyApplication.sUser = LoganSquare.parse(userObject.toString(), User.class);
                                startActivityWithAnim(new Intent(mContext, SetDefaultValuesActivity.class));
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
