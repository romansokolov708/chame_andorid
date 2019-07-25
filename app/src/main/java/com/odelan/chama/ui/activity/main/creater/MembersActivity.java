package com.odelan.chama.ui.activity.main.creater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.base.BaseNavigationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class MembersActivity extends BaseNavigationActivity {

    public static final String USER_MEMBER = "members";
    public static final String USER_NON_CONTRIBUTED = "non_contributed";
    public static String sType = MembersActivity.USER_MEMBER;

    final int COLUMN_COUNT = 3;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<User> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_members;
    }

    @Override
    protected String getViewTitle() {
        if (MembersActivity.sType.equals(MembersActivity.USER_NON_CONTRIBUTED)) {
            return getString(R.string.pending_member_contributions);
        }

        return getString(R.string.members);
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
                                JSONArray members = response.getJSONArray("members");

                                for (int i = 0 ; i < members.length() ; i ++) {
                                    JSONObject member = members.getJSONObject(i);
                                    User item = new User();
                                    item.user_name = member.getString("user_name");
                                    item.phone = member.getString("phone");
                                    item.role = member.getString("role");
                                    item.avatar = member.getString("avatar");
                                    item.chama_name = MyApplication.sUser.chama_name;
                                    item.chama_code = MyApplication.sUser.chama_code;
                                    item.account_status = member.getString("account_status");
                                    mDatas.add(item);
                                }

                                recyclerView.setAdapter(new RecyclerViewAdapter(mDatas));
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

        List<User> mList;

        public RecyclerViewAdapter(List<User> list) {
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
                    .inflate(R.layout.item_user_layout, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view) {
            };
        }

        public Bitmap StringToBitMap(String encodedString) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int position) {
            final RecyclerViewAdapter.ViewHolder holder = (RecyclerViewAdapter.ViewHolder) mholder;

            holder.mItem = mList.get(position);
            holder.nameTV.setText(holder.mItem.user_name);
            holder.roleTV.setText(holder.mItem.role);

            if (holder.mItem.avatar.length() > 0) {
                Bitmap mBmp = StringToBitMap(holder.mItem.avatar);
                holder.photoIV.setImageBitmap(mBmp);
            }
            else {
                holder.photoIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_placeholder));
            }

            if (holder.mItem.account_status.equals(User.STATUS_ACTIVE)) {
                holder.blockMaskIV.setVisibility(View.INVISIBLE);
            } else {
                holder.blockMaskIV.setVisibility(View.VISIBLE);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberDetailActivity.mUser = holder.mItem;
                    startActivityWithAnim(new Intent(mContext, MemberDetailActivity.class));
                }
            });

            //Picasso.get().load(holder.mItem.photo).into(holder.photoIV);
//            Glide.with(mContext).load(holder.mItem.avatar).into(holder.photoIV);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            @BindView(R.id.nameTV)
            TextView nameTV;

            @BindView(R.id.roleTV)
            TextView roleTV;

            @BindView(R.id.photoIV)
            RoundedImageView photoIV;

            @BindView(R.id.blockMaskIV)
            RoundedImageView blockMaskIV;

            public User mItem;

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
