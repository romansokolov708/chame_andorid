package com.odelan.chama.ui.activity.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.ui.base.BaseNavigationActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.SUPPORT_EMAIL;
import static com.odelan.chama.info.Info.SUPPORT_PHONE;

public class ContactActivity extends BaseNavigationActivity {

    @BindView(R.id.phoneTV)
    TextView phoneTV;

    @BindView(R.id.emailTV)
    TextView emailTV;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_contact;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.contact_us);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phoneTV.setText(SUPPORT_PHONE);
        emailTV.setText(SUPPORT_EMAIL);
    }

    @OnClick(R.id.phoneTV) public void onCall() {
        /*Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
        startActivity(intent);*/

        String contact = SUPPORT_PHONE; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = mContext.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            showToast("Whatsapp app not installed in your phone");
            e.printStackTrace();
        }
    }

    @OnClick(R.id.emailTV) public void onEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }
    }
}
