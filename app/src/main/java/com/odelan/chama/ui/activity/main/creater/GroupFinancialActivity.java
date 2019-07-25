package com.odelan.chama.ui.activity.main.creater;

import android.os.Bundle;

import com.odelan.chama.R;
import com.odelan.chama.ui.base.BaseNavigationActivity;

public class GroupFinancialActivity extends BaseNavigationActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_group_financial;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.group_financial);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
