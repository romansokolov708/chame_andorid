package com.odelan.chama.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.odelan.chama.MyApplication;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class ChangePasswordActivity extends BaseNavigationActivity {

    @BindView(R.id.currentPasswordET)
    EditText currentPasswordET;

    @BindView(R.id.newPasswordET)
    EditText newPasswordET;

    @BindView(R.id.confirmPasswordET)
    EditText confirmPasswordET;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_change_password;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.change_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.updateBtn)
    public void onUpdate() {
        if (newPasswordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
            showLoading();
            AndroidNetworking.post(BASE_API_URL + "users/changePassword")
                    .addBodyParameter("phone", MyApplication.sUser.phone)
                    .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                    .addBodyParameter("role", MyApplication.sUser.role)
                    .addBodyParameter("cur_password", currentPasswordET.getText().toString())
                    .addBodyParameter("new_password", newPasswordET.getText().toString())
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
                                Boolean success =  response.getBoolean("success");
                                if (success == true) {
                                    String message = response.getString("message");
                                    showToast(message);
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
        else {
            showToast("New password doesn't match");
        }
    }
}
