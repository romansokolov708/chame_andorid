package com.odelan.chama.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.odelan.chama.BuildConfig;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.info.Info;
import com.odelan.chama.ui.activity.intro.SplashActivity;
import com.odelan.chama.ui.activity.main.ChangePasswordActivity;
import com.odelan.chama.ui.activity.main.ContactActivity;
import com.odelan.chama.ui.activity.main.HomeActivity;
import com.odelan.chama.ui.activity.main.ProfileActivity;
import com.odelan.chama.ui.activity.main.TransactionsActivity;
import com.odelan.chama.ui.activity.main.creater.GroupProfileActivity;
import com.odelan.chama.utils.DlgHelper;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.chama.MyApplication.isProduction;
import static com.odelan.chama.info.Info.MODE;
import static com.odelan.chama.info.Info.MODE_LIVE;
import static com.odelan.chama.info.Info.MODE_TEST;

public abstract class BaseNavigationActivity extends BaseActivity {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    FrameLayout contentLayout;
    ImageView toggleBtn;
    ImageView backBtn;
    TextView titleTV;

    @BindView(R.id.versionTV)
    TextView versionTV;

    @BindView(R.id.photoIV)
    RoundedImageView photoIV;

    @BindView(R.id.nameTV)
    TextView nameTV;

    final int CLICK_LIMIT_COUNT = 30;
    int clickCount = 0;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_base_navigation;
    }

    protected abstract int getLayoutResID();
    protected abstract String getViewTitle();

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        navigationView = findViewById(R.id.nav_view);
        contentLayout = findViewById(R.id.contentLayout);
        toggleBtn = findViewById(R.id.toggleBtn);
        backBtn = findViewById(R.id.backBtn);
        titleTV = findViewById(R.id.titleTV);

        View mContainerView = LayoutInflater.from(
                this.getBaseContext()).inflate(getLayoutResID(), null, false);
        contentLayout.addView(mContainerView);

        ButterKnife.bind(this);

        if (MyApplication.sUser != null) {
            nameTV.setText(MyApplication.sUser.user_name);

            if (MyApplication.sUser.avatar.length() > 0) {
                Bitmap mBmp = StringToBitMap(MyApplication.sUser.avatar);
                photoIV.setImageBitmap(mBmp);
            }
        }

        setTitleText(getViewTitle());
    }

    private void setVersionLabel() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        String appMode = "Live";
        if (MODE.equals(Info.MODE_TEST)) {
            appMode = "Sandbox";
        }

        String apkMode = "Debug";
        if (!BuildConfig.DEBUG) {
            // release apk
            apkMode = "Release";
        }

        versionTV.setText(apkMode + " " + appMode + " Version: " + versionName + " (" + versionCode + ")");
    }

    @OnClick(R.id.versionTV) public void onClickVersion () {
        clickCount ++;
        if (clickCount == CLICK_LIMIT_COUNT) {
            String mode = getString(R.string.sandbox);
            if (!isProduction) {
                mode = getString(R.string.live);
            }
            String msg = getString(R.string.switch_mode) + " " + mode + "?";
            new DlgHelper(mContext).showDialog(
                    getString(R.string.alert),
                    msg,
                    getString(R.string.dialog_ok),
                    getString(R.string.cancel),
                    new DlgHelper.OnDialogBtnClickListener() {
                        @Override
                        public void onClickProc(View view) {
                            if (isProduction) {
                                MODE = MODE_TEST;
                            } else {
                                MODE = MODE_LIVE;
                            }

                            isProduction = !isProduction;
                            logout();
                        }
                    }
            );
        }
    }

    @OnClick(R.id.editTV) public void onEdit() {
        drawer.closeDrawer(GravityCompat.START);
        startActivityWithAnim(new Intent(mContext, ProfileActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void hideToggleBtn() {
        toggleBtn.setVisibility(View.INVISIBLE);
    }

    public void showToggleBtn() {
        toggleBtn.setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        backBtn.setVisibility(View.INVISIBLE);
    }

    public void showBackButton() {
        backBtn.setVisibility(View.VISIBLE);
    }

    public void setTitleText (String title) {
        titleTV.setText(title);
    }

    @OnClick(R.id.homeLL) public void onHome() {
        if (!(mContext instanceof HomeActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            startActivityWithAnim(new Intent(mContext, HomeActivity.class));
        }
    }

    @OnClick(R.id.transLL) public void onTransctions() {
        if (!(mContext instanceof TransactionsActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            startActivityWithAnim(new Intent(mContext, TransactionsActivity.class));
        }
    }

    @OnClick(R.id.profileLL) public void onProfile() {
        if (!(mContext instanceof ProfileActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            startActivityWithAnim(new Intent(mContext, ProfileActivity.class));
        }
    }

    @OnClick(R.id.groupLL) public void onGroup() {
        if (!(mContext instanceof GroupProfileActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            startActivityWithAnim(new Intent(mContext, GroupProfileActivity.class));
        }
    }

    @OnClick(R.id.passwordLL) public void onChangePassword() {
        if (!(mContext instanceof ChangePasswordActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            startActivityWithAnim(new Intent(mContext, ChangePasswordActivity.class));
        }
    }

    @OnClick(R.id.contactLL) public void onContact() {
        if (!(mContext instanceof ContactActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            startActivityWithAnim(new Intent(mContext, ContactActivity.class));
        }
    }

    private void logout() {
        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivityWithAnim(intent);
        mContext.finish();
    }

    @OnClick(R.id.logoutLL) public void onLogout() {
        drawer.closeDrawer(GravityCompat.START);
        logout();
    }

    @OnClick(R.id.toggleBtn) public void onToggle() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
            setVersionLabel();
        }
    }

    @OnClick(R.id.backBtn) public void onBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
