package com.odelan.chama.ui.activity.main.creater;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.base.BaseNavigationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class GroupProfileActivity extends BaseNavigationActivity {

    @BindView(R.id.membersTV)
    TextView membersTV;

    @BindView(R.id.balanceTV)
    TextView balanceTV;

    @BindView(R.id.contributeTV)
    TextView contributeTV;

    @BindView(R.id.borrowTV)
    TextView borrowTV;

    @BindView(R.id.loanTV)
    TextView loanTV;

    @BindView(R.id.outstandingTV)
    TextView outstandingTV;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_group_profile;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.group_profile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/getProfileByGroup")
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .setTag("transactionByGroup")
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
                                JSONArray data = response.getJSONArray("doc");
                                JSONArray members = response.getJSONArray("members");
                                int total = 0;
                                int contribute = 0;
                                int borrow = 0;
                                int loan = 0;
                                for (int i = 0 ; i < data.length() ; i ++) {
                                    JSONObject cur = data.getJSONObject(i);
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
                                membersTV.setText(Integer.toString(members.length()));
                                balanceTV.setText(Integer.toString(total) + " KES");
                                contributeTV.setText(Integer.toString(contribute) + " KES");
                                borrowTV.setText(Integer.toString(borrow) + " KES");
                                loanTV.setText(Integer.toString(loan) + " KES");
                                outstandingTV.setText(Integer.toString(borrow - loan) + " KES");
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

    @OnClick(R.id.ruleIV) public void onRule() {
        if (MyApplication.sUser.role.compareTo(User.TYPE_CHAIRPERSON) == 0)
            startActivityWithAnim(new Intent(mContext, SetDefaultValuesActivity.class));
    }

    @OnClick(R.id.membersIV) public void onMembers() {
        MembersActivity.sType = MembersActivity.USER_MEMBER;
        startActivityWithAnim(new Intent(mContext, MembersActivity.class));
    }

    @OnClick(R.id.balanceIV) public void onBalance() {
        GroupTransactionsActivity.sType = GroupTransactionsActivity.TRANSACTION_ALL;
        startActivityWithAnim(new Intent(mContext, GroupTransactionsActivity.class));
    }

    @OnClick(R.id.contributeIV) public void onContribute() {
        GroupTransactionsActivity.sType = GroupTransactionsActivity.TRANSACTION_CONTRIBUTE;
        startActivityWithAnim(new Intent(mContext, GroupTransactionsActivity.class));
    }

    @OnClick(R.id.borrowIV) public void onBorrow() {
        GroupTransactionsActivity.sType = GroupTransactionsActivity.TRANSACTION_BORROW;
        startActivityWithAnim(new Intent(mContext, GroupTransactionsActivity.class));
    }

    @OnClick(R.id.loanIV) public void onLoan() {
        GroupTransactionsActivity.sType = GroupTransactionsActivity.TRANSACTION_LOAN;
        startActivityWithAnim(new Intent(mContext, GroupTransactionsActivity.class));
    }

    @OnClick(R.id.outstandingIV) public void onOutstanding() {
        startActivityWithAnim(new Intent(mContext, OutstandingActivity.class));
    }

    @OnClick(R.id.loanRequestIV) public void onPendingLoanRequests() {
        startActivityWithAnim(new Intent(mContext, PendingLoanRequestsActivity.class));
    }

    @OnClick(R.id.nonContributedIV) public void onNoncontributed() {
        MembersActivity.sType = MembersActivity.USER_NON_CONTRIBUTED;
        startActivityWithAnim(new Intent(mContext, MembersActivity.class));
    }
}
