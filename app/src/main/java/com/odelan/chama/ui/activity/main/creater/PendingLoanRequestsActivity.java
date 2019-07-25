package com.odelan.chama.ui.activity.main.creater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.LoanMD;
import com.odelan.chama.data.model.OutstandingMD;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.activity.main.loans.fragment.LoansFragment;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import com.odelan.chama.utils.DlgHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.odelan.chama.info.Info.BASE_API_URL;
import static com.odelan.chama.info.Info.BASE_API_URL_B2C;

public class PendingLoanRequestsActivity extends BaseNavigationActivity {

    final int COLUMN_COUNT = 3;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.totalAmountTV)
    TextView totalAmountTV;

    List<LoanMD> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_pending_loan_requests;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.pending_loan_requests);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToggleBtn();
        showBackButton();

        init();
    }

    public void init() {
        setLayout();
        getData();
    }

    private void getData() {
        mDatas = new ArrayList<>();
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/getPendingTransactionsByGroup")
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
                                int total = 0;

                                for (int i = 0 ; i < data.length() ; i ++) {
                                    JSONObject obj = data.getJSONObject(i);

                                    LoanMD item = new LoanMD();
                                    item.id = obj.getInt("id");
                                    item.username = obj.getString("user_name");
                                    item.amount = Integer.toString(obj.getInt("amount"));
                                    total += obj.getInt("amount");
                                    item.currency = obj.getString("currency");
                                    item.type = obj.getString("type");

                                    String created_at = obj.getString("created_at");
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                    SimpleDateFormat date_format = new SimpleDateFormat("dd MMM, yyyy");
                                    Date date_created_at = formatter.parse(created_at.replaceAll("Z$", "+0000"));

                                    item.duedate = date_format.format(date_created_at);
                                    mDatas.add(item);
                                }

                                totalAmountTV.setText(Integer.toString(total) + " KES");
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

        recyclerView.setAdapter(new RecyclerViewAdapter(mDatas));
    }

    private void setLayout() {
        RecyclerView.LayoutManager layoutManager;

        if (GRID_LAYOUT) {
            layoutManager = new GridLayoutManager(mContext, COLUMN_COUNT);
        } else {
            layoutManager = new LinearLayoutManager(mContext);
        }

        recyclerView.setLayoutManager(layoutManager);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<LoanMD> mList;

        public RecyclerViewAdapter(List<LoanMD> list) {
            mList = list;
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_merry_layout, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int position) {
            final RecyclerViewAdapter.ViewHolder holder = (RecyclerViewAdapter.ViewHolder) mholder;

            holder.mItem = mList.get(position);
            holder.nameTV.setText(holder.mItem.username);
            holder.amountTV.setText(holder.mItem.getAmount());
            holder.dateTV.setText(holder.mItem.duedate);

            if (MyApplication.sUser.role.compareTo(User.TYPE_MEMBER) != 0) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = "";
                        if (holder.mItem.type.equals(LoanMD.TYPE_EMERGENCY_FUND)) {
                            message = holder.mItem.username + " is requesting " + holder.mItem.currency + ". " + holder.mItem.amount + " emergency fund payable by " + holder.mItem.duedate;
                        } else if (holder.mItem.type.equals(LoanMD.TYPE_3X_MY_SAVING)) {
                            message = holder.mItem.username + " is requesting " + holder.mItem.currency + ". " + holder.mItem.amount + " 3x my saving payable by " + holder.mItem.duedate;
                        }

                        new DlgHelper().showDialog(mContext,
                                "",
                                message,
                                "APPROVE",
                                "DECLINE",
                                new DlgHelper.OnDialogBtnClickListener() {
                                    @Override
                                    public void onClickProc(View view) {
                                        showLoading();
                                        AndroidNetworking.post(BASE_API_URL_B2C + "transactions/approvePendingTransaction")
                                                .addBodyParameter("id", holder.mItem.id.toString())
                                                .addBodyParameter("role", MyApplication.sUser.role)
                                                .addBodyParameter("transaction_fee", MyApplication.transaction_fee.toString())
                                                .addBodyParameter("interest_rate", MyApplication.interest_rate.toString())
                                                .setTag("approvePending")
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
                                                                showToast("Successfully approved");
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
                                },
                                new DlgHelper.OnDialogBtnClickListener() {
                                    @Override
                                    public void onClickProc(View view) {
                                        showLoading();
                                        AndroidNetworking.post(BASE_API_URL + "transactions/declinePendingTransaction")
                                                .addBodyParameter("id", holder.mItem.id.toString())
                                                .setTag("declinePending")
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
                                                                showToast("Successfully declined");
                                                                startActivityWithAnim(new Intent(mContext, GroupProfileActivity.class));
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
                                });
                    }
                });
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            @BindView(R.id.nameTV)
            TextView nameTV;

            @BindView(R.id.amountTV)
            TextView amountTV;

            @BindView(R.id.dateTV)
            TextView dateTV;

            public LoanMD mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                ButterKnife.bind(this, view);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }
    }
}
