package com.odelan.chama.ui.activity.main.borrow;

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
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonArray;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.MerryDueMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.activity.main.creater.SetDefaultValuesActivity;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;
import static com.odelan.chama.info.Info.BASE_API_URL_B2C;

public class MerryActivity extends BaseNavigationActivity {

    final int COLUMN_COUNT = 3;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.amountTV)
    TextView amountTV;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<MerryDueMD> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_merry;
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
        setLayout();
        getData();
    }

    @OnClick(R.id.acceptBtn) public void onAccept() {
        showLoading();
        AndroidNetworking.post(BASE_API_URL_B2C + "transactions/updateMerryGoRound")
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("accept_status", "accept")
                .setTag("merry-go-rounds")
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
                                onBack();
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

    @OnClick(R.id.declineBtn) public void onDecline() {
        showLoading();
        AndroidNetworking.post(BASE_API_URL_B2C + "transactions/updateMerryGoRound")
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("accept_status", "decline")
                .setTag("merry-go-rounds")
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
                                onBack();
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

    private void getData() {
        mDatas = new ArrayList<>();

        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/getMerryGoRounds")
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .setTag("merry-go-rounds")
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
                                JSONArray rounds = response.getJSONArray("doc");
                                Integer waiting = response.getInt("waiting");

                                for (int i = 0 ; i < rounds.length() ; i ++) {
                                    JSONObject round = rounds.getJSONObject(i);
                                    MerryDueMD item = new MerryDueMD();
                                    item.username = round.getString("user_name");
                                    item.amount = Integer.toString(round.getInt("amount"));
                                    item.currency = "KES";
                                    String created_at = round.getString("duedate");
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                    SimpleDateFormat date_format = new SimpleDateFormat("dd MMMM, yyyy");
                                    Date date_created_at = formatter.parse(created_at.replaceAll("Z$", "+0000"));

                                    item.duedate = date_format.format(date_created_at);
                                    mDatas.add(item);
                                }

                                recyclerView.setAdapter(new RecyclerViewAdapter(mDatas));

                                if (waiting > 0) {
                                    amountTV.setText("Amount: " + waiting.toString() + " KES");
                                    Button btn = (Button) findViewById(R.id.acceptBtn);
                                    btn.setEnabled(true);

                                    Button btn1 = (Button) findViewById(R.id.declineBtn);
                                    btn1.setEnabled(true);
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

        List<MerryDueMD> mList;

        public RecyclerViewAdapter(List<MerryDueMD> list) {
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
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            @BindView(R.id.nameTV)
            TextView nameTV;

            @BindView(R.id.amountTV)
            TextView amountTV;

            @BindView(R.id.dateTV)
            TextView dateTV;

            public MerryDueMD mItem;

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
