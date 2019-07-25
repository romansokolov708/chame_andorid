package com.odelan.chama.ui.activity.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.odelan.chama.R;
import com.odelan.chama.ui.activity.main.borrow.fragment.BorrowFragment;
import com.odelan.chama.ui.activity.main.contribute.fragment.ContributeFragment;
import com.odelan.chama.ui.activity.main.loans.fragment.LoansFragment;
import com.odelan.chama.ui.base.BaseFragment;
import com.odelan.chama.ui.base.BaseNavigationActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;

public class HomeActivity extends BaseNavigationActivity implements BaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabBarLayout)
    ViewGroup tabBarLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    SmartTabLayout viewPagerTabLayout;
    FragmentPagerItemAdapter adapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    protected String getViewTitle() {
        return getString(R.string.home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void onBackPressed() {

    }

    public void init() {
        tabBarLayout.addView(LayoutInflater.from(this).inflate(R.layout.smart_tab_layout, tabBarLayout, false));
        viewPagerTabLayout = findViewById(R.id.viewPagerTabLayout);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of(getString(R.string.contribute), ContributeFragment.class, new Bundler().putString("title", getString(R.string.contribute)).get()));
        pages.add(FragmentPagerItem.of(getString(R.string.borrow), BorrowFragment.class, new Bundler().putString("title", getString(R.string.borrow)).get()));
        pages.add(FragmentPagerItem.of(getString(R.string.loans), LoansFragment.class, new Bundler().putString("title", getString(R.string.loans)).get()));

        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTabLayout.setViewPager(viewPager);
        viewPagerTabLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener () {
            @Override
            public void onPageSelected(int position) {
                /*if (position == 0) {
                    setTitleText(getString(R.string.vault_holdings));
                } else if (position == 1) {
                    setTitleText(getString(R.string.mastercard_account));
                    if (cardHomeFragment != null) {
                        cardHomeFragment.init();
                    }
                } else {
                    setTitleText(getString(R.string.vault_holdings));
                    if (retirementHomeFragment != null) {
                        retirementHomeFragment.init();
                    }
                }*/
            }
        });
    }

    @Override
    public void onFragmentInteraction(BaseFragment fragment) {

    }
}
