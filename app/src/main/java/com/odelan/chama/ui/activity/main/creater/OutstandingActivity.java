package com.odelan.chama.ui.activity.main.creater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.OutstandingMD;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.ui.activity.main.transactions.BorrowTransactionsFragment;
import com.odelan.chama.ui.base.BaseNavigationActivity;

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

public class OutstandingActivity extends BaseNavigationActivity {

    final int COLUMN_COUNT = 3;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<OutstandingMD> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_outstanding;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.outstanding);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToggleBtn();
        showBackButton();

        init();
    }

    private void init() {
        setLayout();
        getData();
    }

    private void getData() {
        mDatas = new ArrayList<>();
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/getOutstandingTransactionsByGroup")
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
                                for (int i = 0 ; i < data.length() ; i ++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    OutstandingMD item = new OutstandingMD();
                                    item.username = obj.getString("user_name");
                                    item.phone = obj.getString("phone");
                                    item.chama_code = MyApplication.sUser.chama_code;
                                    item.borrowed_amount = Integer.toString(obj.getInt("amount"));
                                    item.loan_amount = Integer.toString(obj.getInt("amount") * 100 / (100 + MyApplication.interest_rate));
                                    item.currency = obj.getString("currency");
                                    item.role = obj.getString("role");

                                    String created_at = obj.getString("created_at");
                                    String end_at = obj.getString("end_at");
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                    SimpleDateFormat date_format = new SimpleDateFormat("dd MMM, yyyy");
                                    Date date_created_at = formatter.parse(created_at.replaceAll("Z$", "+0000"));
                                    Date date_end_at = formatter.parse(end_at.replaceAll("Z$", "+0000"));
                                    Date current = new Date();

                                    long diff = date_end_at.getTime() - current.getTime();
                                    item.days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                    item.borrow_date = date_format.format(date_created_at);
                                    item.schedule_loan_date = date_format.format(date_end_at);
                                    mDatas.add(item);

                                }
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

        List<OutstandingMD> mList;

        public RecyclerViewAdapter(List<OutstandingMD> list) {
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
                    .inflate(R.layout.item_outstanding_layout, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int position) {
            final RecyclerViewAdapter.ViewHolder holder = (RecyclerViewAdapter.ViewHolder) mholder;

            holder.mItem = mList.get(position);
            holder.nameTV.setText(holder.mItem.username + " Borrowed");
            holder.amountTV.setText(holder.mItem.getBorrowedAmount());
            holder.dateTV.setText(holder.mItem.getDuringDays());
            if (holder.mItem.days < 0) {
                holder.warningIV.setVisibility(View.VISIBLE);
                holder.daysTV.setText("(" + Long.toString(- holder.mItem.days)  + (holder.mItem.days == -1 ? " day passed)" : " days passed)"));
            } else {
                holder.warningIV.setVisibility(View.INVISIBLE);
                holder.daysTV.setText("(" + Long.toString(holder.mItem.days ) + (holder.mItem.days == 1 ? " day left)" :" days left)"));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OutstandingDetailActivity.data = holder.mItem;
                    startActivityWithAnim(new Intent(mContext, OutstandingDetailActivity.class));
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            @BindView(R.id.nameTV)
            TextView nameTV;

            @BindView(R.id.amountTV)
            TextView amountTV;

            @BindView(R.id.dateTV)
            TextView dateTV;

            @BindView(R.id.warningIV)
            ImageView warningIV;

            @BindView(R.id.daysTV)
            TextView daysTV;

            public OutstandingMD mItem;

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
