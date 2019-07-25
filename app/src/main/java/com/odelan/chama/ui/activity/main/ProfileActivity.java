package com.odelan.chama.ui.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mukesh.countrypicker.Countries;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.User;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import com.odelan.chama.utils.FileUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import android.util.Base64;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class ProfileActivity extends BaseNavigationActivity implements OnCountryPickerListener {

    @BindView(R.id.codeET)
    EditText codeET;

    @BindView(R.id.phoneET)
    EditText phoneET;

    @BindView(R.id.nameET)
    EditText nameET;

    @BindView(R.id.chamaNameTV)
    TextView chamaNameTV;

    @BindView(R.id.chamaCodeTV)
    TextView chamaCodeTV;

    @BindView(R.id.ruleTV)
    TextView ruleTV;

    @BindView(R.id.statusTV)
    TextView statusTV;

    @BindView(R.id.photoIV)
    RoundedImageView photoIV;

    private CountryPicker countryPicker;
    private Bitmap mBmp = null;

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

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_profile;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.profile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameET.setText(MyApplication.sUser.user_name);
        chamaNameTV.setText(MyApplication.sUser.chama_name);
        chamaCodeTV.setText(MyApplication.sUser.chama_code);
        ruleTV.setText(MyApplication.sUser.role);
        statusTV.setText(MyApplication.sUser.account_status);
        codeET.setText(MyApplication.sUser.phone.substring(0,3));
        phoneET.setText(MyApplication.sUser.phone.substring(3));
        if (MyApplication.sUser.avatar.length() > 0) {
            mBmp = StringToBitMap(MyApplication.sUser.avatar);
            photoIV.setImageBitmap(mBmp);
        }
//        countryPicker =
//                new CountryPicker.Builder().with(this)
//                        .listener(this)
//                        .build();
//        countryPicker.setCountries(Countries.getCountriesByCountryNames(Countries.APP_COUNTRIES));
    }

    @Override
    public void onSelectCountry(Country country) {
        codeET.setText(country.getDialCode());
    }

    @OnClick(R.id.codeET) public void onCodeET() {
        countryPicker.showDialog(getSupportFragmentManager());
    }

    @OnClick(R.id.saveBtn) public void onSave() {
        String avatar = "";

        if (mBmp != null) {
            avatar = BitMapToString(mBmp);
        }

        AndroidNetworking.post(BASE_API_URL + "users/userProfileSave")
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("role", MyApplication.sUser.role)
                .addBodyParameter("user_name", nameET.getText().toString())
                .addBodyParameter("avatar", avatar)
                .setTag("userProfileSave")
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
                                JSONObject data = response.getJSONObject("doc");

                                MyApplication.sUser.user_name = data.getString("user_name");
                                MyApplication.sUser.avatar = data.getString("avatar");

                                showToast("Saved");
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

    @OnClick(R.id.photoIV) public void onPhoto() {
        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoIV.setImageURI(result.getUri());
                mBmp = FileUtils.getBitmapFromFileUri(mContext, result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showToast(getString(R.string.cropping_failed) + " " + result.getError());
            }
        }
    }
}
