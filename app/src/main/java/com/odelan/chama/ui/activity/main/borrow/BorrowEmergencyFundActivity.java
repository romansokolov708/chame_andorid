package com.odelan.chama.ui.activity.main.borrow;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.LoanMD;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.base.BaseNavigationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class BorrowEmergencyFundActivity extends BaseNavigationActivity implements OnCountryPickerListener {

    @BindView(R.id.maxAmountTV)
    TextView maxAmountTV;

    @BindView(R.id.amountET)
    EditText amountET;

    @BindView(R.id.currencyET)
    EditText currencyET;

    @BindView(R.id.spinner1)
    Spinner spinner1;

    @BindView(R.id.confirmAmountET)
    EditText confirmAmountET;

    @BindView(R.id.confirmCurrencyET)
    EditText confirmCurrencyET;

    @BindView(R.id.spinner2)
    Spinner spinner2;

    /*@BindView(R.id.rateTV)
    TextView rateTV;*/

    @BindView(R.id.repayAmountTV)
    TextView repayAmountTV;

    List<String> mCycles = new ArrayList<>();
    List<String> mPayments = new ArrayList<>();
    private CountryPicker countryPicker;

    boolean isCurrencySelected = true;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_borrow_emergency_fund;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.borrow);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToggleBtn();
        showBackButton();

        init();
    }

    private void init() {
        maxAmountTV.setText("(Max Amount: " + Integer.toString(MyApplication.contribution_amount * MyApplication.sUser.emergency_fund / 100) + " KES)");
        mCycles = new ArrayList<>();
        mPayments = new ArrayList<>();

        countryPicker =
                new CountryPicker.Builder().with(mContext)
                        .listener(this)
                        .build();

        mCycles.add("1 week");
        mCycles.add("2 weeks");
        mCycles.add("3 weeks");
        mCycles.add("Monthly");

        ArrayAdapter<String> cycleAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, mCycles);
        cycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(cycleAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Not selected");
            }
        });

        mPayments.add("MPESA");

        ArrayAdapter<String> paymentsAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, mPayments);
        paymentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(paymentsAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Not selected");
            }
        });
        currencyET.setText("KES");
        confirmCurrencyET.setText("KES");

        currencyET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCurrencySelected = true;
                countryPicker.showDialog("currency",mContext.getSupportFragmentManager());
            }
        });

        confirmCurrencyET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCurrencySelected = false;
                countryPicker.showDialog("currency",mContext.getSupportFragmentManager());
            }
        });

        //rateTV.setText("*Please note your interest rate is 2%");
        repayAmountTV.setText("*Repayment amount will be : 0 KES");

        amountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int amount = count == 0 ? 0 : Integer.parseInt(s.toString());
                repayAmountTV.setText("*Repayment amount will be : " + Integer.toString(amount * (100 + MyApplication.interest_rate) / 100) +" KES");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.borrowBtn) public void onBorrow() {
        String amountS = amountET.getText().toString();
        String confirm = confirmAmountET.getText().toString();
        int maxAmount = MyApplication.contribution_amount * MyApplication.sUser.emergency_fund / 100;
        if (amountS.length() == 0) {
            showToast("Enter borrow amount");
            return;
        }
        if (amountS.compareTo(confirm) != 0) {
            showToast("Borrow amount doesn't match");
            return;
        }
        int amount = Integer.parseInt(amountET.getText().toString());
        if (amount > maxAmount) {
            showToast("Don't exceed max amount!");
            return;
        }
        AndroidNetworking.post(BASE_API_URL + "transactions/addNewPendingTransaction")
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("amount", Integer.toString(amount))
                .addBodyParameter("type", LoanMD.TYPE_EMERGENCY_FUND)
                .addBodyParameter("duration", spinner1.getSelectedItem().toString())
                .setTag("newPending")
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
                                showToast("Successfully request transaction");
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



    @Override
    public void onSelectCountry(Country country) {
        if (isCurrencySelected) {
            currencyET.setText(country.getCurrency());
        } else {
            confirmCurrencyET.setText(country.getCurrency());
        }
    }
}
