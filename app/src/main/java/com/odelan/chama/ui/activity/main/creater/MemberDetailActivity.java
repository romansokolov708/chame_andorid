package com.odelan.chama.ui.activity.main.creater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.base.BaseNavigationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.MyApplication.OPTIONS;
import static com.odelan.chama.MyApplication.ROLES;
import static com.odelan.chama.info.Info.BASE_API_URL;

public class MemberDetailActivity extends BaseNavigationActivity {

    public static User mUser = null;

    @BindView(R.id.photoIV)
    RoundedImageView photoIV;

    @BindView(R.id.roleSpinner)
    Spinner roleSpinner;

    @BindView(R.id.roleTV)
    TextView roleTV;

    @BindView(R.id.statusSpinner)
    Spinner statusSpinner;

    @BindView(R.id.statusTV)
    TextView statusTV;

    @BindView(R.id.nameTV)
    TextView nameTV;

    @BindView(R.id.phoneTV)
    TextView phoneTV;

    @BindView(R.id.chamaNameTV)
    TextView chamaNameTV;

    @BindView(R.id.chamaCodeTV)
    TextView chamaCodeTV;

    @BindView(R.id.saveBtn)
    Button saveBtn;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_member_detail;
    }

    @Override
    protected String getViewTitle() {
        if (mUser != null && mUser.user_name != null) {
            return mUser.user_name;
        } else {
            return null;
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToggleBtn();
        showBackButton();

        init();
    }

    public void init() {
//        Glide.with(mContext).load(mUser.avatar).into(photoIV);
        nameTV.setText(mUser.user_name);
        phoneTV.setText(mUser.phone);
        chamaNameTV.setText(mUser.chama_name);
        chamaCodeTV.setText(mUser.chama_code);
        roleTV.setText(mUser.role);
        statusTV.setText(mUser.account_status);

        if (mUser.avatar.length() > 0) {
            Bitmap mBmp = StringToBitMap(mUser.avatar);
            photoIV.setImageBitmap(mBmp);
        }
        else {
            photoIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_placeholder));
        }


        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, ROLES);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Not selected");
            }
        });

        roleTV.setVisibility(View.GONE);
        statusTV.setVisibility(View.GONE);
        roleSpinner.setVisibility(View.VISIBLE);
        statusSpinner.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.VISIBLE);

        if (!MyApplication.sUser.role.equals(User.TYPE_CHAIRPERSON) || mUser.role.equals(User.TYPE_CHAIRPERSON)) {
            roleSpinner.setVisibility(View.GONE);
            statusSpinner.setVisibility(View.GONE);
            roleTV.setVisibility(View.VISIBLE);
            statusTV.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
        } else if (mUser.role.equals(User.TYPE_SECRETARY)) {
            roleSpinner.setSelection(0);
        } else if (mUser.role.equals(User.TYPE_TREASURE)) {
            roleSpinner.setSelection(1);
        } else if (mUser.role.equals(User.TYPE_MEMBER)) {
            roleSpinner.setSelection(2);
        }

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, OPTIONS);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Not selected");
            }
        });

        if (mUser.account_status.equals(User.STATUS_ACTIVE)) {
            statusSpinner.setSelection(0);
        } else if (mUser.account_status.equals(User.STATUS_SUSPEND)) {
            statusSpinner.setSelection(1);
        } else {
            statusSpinner.setSelection(2);
        }
    }

    @OnClick(R.id.saveBtn) public void onSave() {
        AndroidNetworking.post(BASE_API_URL + "users/updateUserStatus")
                .addBodyParameter("phone", mUser.phone)
                .addBodyParameter("chama_code", mUser.chama_code)
                .addBodyParameter("role", roleSpinner.getSelectedItem().toString())
                .addBodyParameter("account_status", statusSpinner.getSelectedItem().toString())
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
}
