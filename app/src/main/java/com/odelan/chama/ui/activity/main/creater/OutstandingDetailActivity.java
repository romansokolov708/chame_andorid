package com.odelan.chama.ui.activity.main.creater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.odelan.chama.R;
import com.odelan.chama.data.model.OutstandingMD;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class OutstandingDetailActivity extends BaseNavigationActivity {

    public static OutstandingMD data = null;

    @BindView(R.id.amountTV)
    TextView amountTV;

    @BindView(R.id.daysTV)
    TextView daysTV;

    @BindView(R.id.nameTV)
    TextView nameTV;

    @BindView(R.id.roleTV)
    TextView roleTV;

    @BindView(R.id.borrowAmountTV)
    TextView borrowAmountTV;

    @BindView(R.id.loanAmountTV)
    TextView loanAmountTV;

    @BindView(R.id.borrowDateTV)
    TextView borrowDateTV;

    @BindView(R.id.loanDateTV)
    TextView loanDateTV;

    @BindView(R.id.photoIV)
    RoundedImageView photoIV;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_outstanding_detail;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        amountTV.setText(data.loan_amount + " " + data.currency);
        if (data.days < 0) {
            daysTV.setText("(" + Long.toString(-data.days) + (data.days == -1 ? " day passed)" : " days passed)"));
        } else {
            daysTV.setText("(" + Long.toString(data.days) + (data.days == 1 ? " day left)" : " days left)"));
        }
        nameTV.setText(data.username);
        roleTV.setText("(" + data.role + ")");
        borrowAmountTV.setText(data.loan_amount + " " + data.currency);
        loanAmountTV.setText(data.borrowed_amount + " " + data.currency);
        borrowDateTV.setText(data.borrow_date);
        loanDateTV.setText(data.schedule_loan_date);

        hideToggleBtn();
        showBackButton();
    }

    @OnClick(R.id.suspendBtn)
    public void onSuspend() {
        AndroidNetworking.post(BASE_API_URL + "users/updateUserStatus")
                .addBodyParameter("phone", data.phone)
                .addBodyParameter("chama_code", data.chama_code)
                .addBodyParameter("role", data.role)
                .addBodyParameter("account_status", "Suspend")
                .setTag("updateUserStatus")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            String message = response.getString("message");
                            showToast(message);
                            onBack();
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

    @OnClick(R.id.remindBtn)
    public void onRemind() {
        onBack();
    }
}