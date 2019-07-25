package com.odelan.chama.ui.activity.main.creater;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.ChamaGroupMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import android.provider.ContactsContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class SetDefaultValuesActivity extends BaseNavigationActivity {

    public static final String FROM_REGISTER = "register";
    public static final String FROM_MENU = "menu";
    public static String sFrom = FROM_MENU;

    @BindView(R.id.chamaNameET)
    EditText chamaNameET;

    @BindView(R.id.yourNameET)
    EditText yourNameET;

    @BindView(R.id.phoneET)
    EditText phoneET;

    @BindView(R.id.dateET)
    EditText dateET;

    @BindView(R.id.maxMemberET)
    EditText maxMemberET;

    @BindView(R.id.kesET)
    EditText kesET;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.emergencyFundET)
    EditText emergencyFundET;

    @BindView(R.id.save3xET)
    EditText save3xET;

    @BindView(R.id.merryET)
    EditText merryET;

    List<String> mCycles = new ArrayList<String>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_set_default_values;
    }

    @Override
    protected String getViewTitle() {
        return "Chama Rules";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToggleBtn();

        if (sFrom.equals(FROM_REGISTER)) {
            hideBackButton();
        } else {
            showBackButton();
        }

        mCycles = new ArrayList<String>();
        mCycles.add("1 week");
        mCycles.add("2 weeks");
        mCycles.add("3 weeks");
        mCycles.add("Monthly");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mCycles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast(mCycles.get((int) id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Not selected");
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onDatePick();
            }
        });

        chamaNameET.setText(MyApplication.sUser.chama_name);
        yourNameET.setText(MyApplication.sUser.user_name);
        phoneET.setText(MyApplication.sUser.phone);
        dateET.setText(MyApplication.sUser.chama_code);

        if (MyApplication.sUser.account_status.equals("Active")) {
            maxMemberET.setText(MyApplication.sUser.max_members.toString());
            kesET.setText(MyApplication.sUser.contribution.toString());
            if (MyApplication.sUser.contribution_cycle.equals("1 week")) {
                spinner.setSelection(0);
            } else if (MyApplication.sUser.contribution_cycle.equals("2 weeks")) {
                spinner.setSelection(1);
            } else if (MyApplication.sUser.contribution_cycle.equals("3 weeks")) {
                spinner.setSelection(2);
            } else {
                spinner.setSelection(3);
            }

            emergencyFundET.setText(MyApplication.sUser.emergency_fund.toString());
            save3xET.setText(MyApplication.sUser.my_saving.toString());
            merryET.setText(MyApplication.sUser.merry_go_round.toString());
        }
    }

    private void onDatePick() {
        Calendar now = Calendar.getInstance();
        new android.app.DatePickerDialog(
                this,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int m = month + 1;
                        String monthStr = String.valueOf(m);
                        if (m < 10) {
                            monthStr = "0" + m;
                        }

                        String dayStr = String.valueOf(dayOfMonth);
                        if (dayOfMonth < 10) {
                            dayStr = "0" + dayStr;
                        }

                        String date = year + "-" + monthStr + "-" + dayStr;
                        dateET.setText(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    /*@Override
    public void onBackPressed() {

    }*/

    @OnClick(R.id.submitBtn)
    public void onSubmit() {
        if (isEmpty(maxMemberET)) {
            showToast("Please input max number of members");
            return;
        }

        if (isEmpty(kesET)) {
            showToast("Please input contribution amount");
            return;
        }

        if (isEmpty(emergencyFundET)) {
            showToast("Please input Emergency Fund percentage");
            return;
        }

        if (isEmpty(save3xET)) {
            showToast("Please input 3x My Savings percentage");
            return;
        }

        if (isEmpty(merryET)) {
            showToast("Please input Merry Go Round percentage");
            return;
        }

        submit();
    }

    void submit() {
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "groups/updateChamaGroup")
                .addBodyParameter("chama_code", dateET.getText().toString())
                .addBodyParameter("chama_name", chamaNameET.getText().toString())
                .addBodyParameter("max_members", maxMemberET.getText().toString())
                .addBodyParameter("contribution", kesET.getText().toString())
                .addBodyParameter("contribution_cycle", mCycles.get(spinner.getSelectedItemPosition()))
                .addBodyParameter("emergency_fund", emergencyFundET.getText().toString())
                .addBodyParameter("my_saving", save3xET.getText().toString())
                .addBodyParameter("merry_go_round", merryET.getText().toString())
                .setTag("update_chama_group")
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
//                                JSONObject data = response.getJSONObject("doc");
                                MyApplication.sUser.chama_name = chamaNameET.getText().toString();
                                MyApplication.sUser.max_members = Integer.parseInt(maxMemberET.getText().toString());
                                MyApplication.sUser.contribution = Integer.parseInt(kesET.getText().toString());
                                MyApplication.sUser.contribution_cycle = mCycles.get(spinner.getSelectedItemPosition());
                                MyApplication.sUser.emergency_fund = Integer.parseInt(emergencyFundET.getText().toString());
                                MyApplication.sUser.my_saving = Integer.parseInt(save3xET.getText().toString());
                                MyApplication.sUser.merry_go_round = Integer.parseInt(merryET.getText().toString());
//                                MyApplication.sUser = LoganSquare.parse(data.toString(), User.class);
                                showToast("Saved");
                                startActivityWithAnim(new Intent(mContext, HomeActivity.class));
                            } else {
                                showToast("Something went wrong");
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

    @OnClick(R.id.inviteBtn)
    public void onInvite() {
        Intent intent = new Intent(Intent.ACTION_DEFAULT, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

}
