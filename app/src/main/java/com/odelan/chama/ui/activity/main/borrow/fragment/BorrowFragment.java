package com.odelan.chama.ui.activity.main.borrow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.borrow.BorrowEmergencyFundActivity;
import com.odelan.chama.ui.activity.main.borrow.BorrowSavingsActivity;
import com.odelan.chama.ui.activity.main.borrow.MerryActivity;
import com.odelan.chama.ui.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class BorrowFragment extends BaseFragment {

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_borrow;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
    }

    @OnClick(R.id.fundBtn) public void onEmergencyFund() {
        if (MyApplication.sUser.account_status.compareTo(User.STATUS_SUSPEND) == 0) {
            showToast("Your account is suspended.");
            return;
        }
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/checkBorrowStatusByGroup")
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .setTag("check")
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
                                startActivityWithAnim(new Intent(mContext, BorrowEmergencyFundActivity.class));
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

    @OnClick(R.id.saveBtn) public void on3xMySavings() {
        if (MyApplication.sUser.account_status.compareTo(User.STATUS_SUSPEND) == 0) {
            showToast("Your account is suspended.");
            return;
        }
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/checkBorrowStatusByGroup")
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .setTag("check")
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
                                startActivityWithAnim(new Intent(mContext, BorrowSavingsActivity.class));
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

    @OnClick(R.id.merryBtn) public void onMerry() {
        if (MyApplication.sUser.account_status.compareTo(User.STATUS_SUSPEND) == 0) {
            showToast("Your account is suspended.");
            return;
        }
        startActivityWithAnim(new Intent(mContext, MerryActivity.class));
    }
}
