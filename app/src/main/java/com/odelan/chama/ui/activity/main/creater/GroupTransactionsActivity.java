package com.odelan.chama.ui.activity.main.creater;

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
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.ui.activity.main.transactions.AllTransactionsFragment;
import com.odelan.chama.ui.base.BaseNavigationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class GroupTransactionsActivity extends BaseNavigationActivity {

    public static final String TRANSACTION_ALL = "all";
    public static final String TRANSACTION_CONTRIBUTE = "contribute";
    public static final String TRANSACTION_BORROW = "borrow";
    public static final String TRANSACTION_LOAN = "loan";

    public static String sType = GroupTransactionsActivity.TRANSACTION_ALL;


    final int COLUMN_COUNT = 3;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.totalTV)
    TextView totalTV;

    @BindView(R.id.totalAmountTV)
    TextView totalAmountTV;

    List<TransactionMD> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_group_transactions; // all case
    }

    @Override
    protected String getViewTitle() {
        if (sType.equals(GroupTransactionsActivity.TRANSACTION_CONTRIBUTE)) {
            return "Group Contributions";
        } else if (sType.equals(GroupTransactionsActivity.TRANSACTION_BORROW)) {
            return "Group Borrows";
        } else if (sType.equals(GroupTransactionsActivity.TRANSACTION_LOAN)) {
            return "Group Repaid Loans";
        }
        return getString(R.string.group_transactions);
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
        showLoading();
        mDatas = new ArrayList<>();
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

                                    TransactionMD item = new TransactionMD();
                                    item.username = cur.getString("user_name");
                                    item.type = cur.getString("type");
                                    item.amount = Integer.toString(cur.getInt("amount"));
                                    item.currency = cur.getString("currency");

                                    String created_at = cur.getString("created_at");
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date date_created_at = formatter.parse(created_at.replaceAll("Z$", "+0000"));

                                    item.duedate = date_format.format(date_created_at);
                                    if (sType.equals(TRANSACTION_ALL) || item.type.equals(sType)) {
                                        mDatas.add(item);
                                    }
                                }
                                if (sType.equals(GroupTransactionsActivity.TRANSACTION_CONTRIBUTE)) {
                                    totalAmountTV.setText(Integer.toString(contribute) + " KES");
                                } else if (sType.equals(GroupTransactionsActivity.TRANSACTION_BORROW)) {
                                    totalAmountTV.setText(Integer.toString(borrow) + " KES");
                                } else if (sType.equals(GroupTransactionsActivity.TRANSACTION_LOAN)) {
                                    totalAmountTV.setText(Integer.toString(loan) + " KES");
                                } else {
                                    totalAmountTV.setText(Integer.toString(total) + " KES");
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
            String typeStr = "Contributed";
            if (holder.mItem.type.equals(TransactionMD.TYPE_CONTRIBUTE)) {
                typeStr = holder.mItem.username + " Contributed";
            } else if (holder.mItem.type.equals(TransactionMD.TYPE_BORROW)) {
                typeStr = holder.mItem.username + " Borrowed";
            } else if (holder.mItem.type.equals(TransactionMD.TYPE_LOANS)) {
                typeStr = holder.mItem.username + " Paid back";
            }
            holder.nameTV.setText(typeStr);
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
