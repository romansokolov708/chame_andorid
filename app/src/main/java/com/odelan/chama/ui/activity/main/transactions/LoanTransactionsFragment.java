package com.odelan.chama.ui.activity.main.transactions;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.ui.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class LoanTransactionsFragment extends BaseFragment {

    final int COLUMN_COUNT = 3;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.totalAmountTV)
    TextView totalAmountTV;

    List<TransactionMD> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_loan_transactions;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        setLayout();
        getData();
    }

    private void getData() {
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/getTransactionsByPhone")
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .setTag("LoanTransaction")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            mDatas = new ArrayList<>();
                            int total = 0;
                            JSONArray data = response.getJSONArray("doc");
                            for (int i = 0 ; i < data.length() ; i ++) {

                                JSONObject obj = data.getJSONObject(i);
                                TransactionMD item = new TransactionMD();
                                item.username = obj.getString("user_name");
                                item.amount = Integer.toString(obj.getInt("amount"));
                                item.currency = obj.getString("currency");

                                String created_at = obj.getString("created_at");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date date_created_at = formatter.parse(created_at.replaceAll("Z$", "+0000"));

                                item.duedate = date_format.format(date_created_at);
                                item.type = obj.getString("type");
                                if (item.type.equals(TransactionMD.TYPE_LOANS)) {
                                    mDatas.add(item);
                                    total += obj.getInt("amount");
                                }
                            }
                            totalAmountTV.setText(Integer.toString(total) + "KES");
                            recyclerView.setAdapter(new RecyclerViewAdapter(mDatas));
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

        List<TransactionMD> mList;

        public RecyclerViewAdapter(List<TransactionMD> list) {
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
            holder.nameTV.setText("Paid back ");
            holder.amountTV.setText(holder.mItem.getAmount());
            holder.dateTV.setText(holder.mItem.duedate);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            @BindView(R.id.nameTV)
            TextView nameTV;

            @BindView(R.id.amountTV)
            TextView amountTV;

            @BindView(R.id.dateTV)
            TextView dateTV;

            public TransactionMD mItem;

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
