package com.yizhitong.wisdombuilding.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.fragment.information.PersonnelCompanyFragment;
import com.yizhitong.wisdombuilding.fragment.information.PersonnelInOutFragment;
import com.yizhitong.wisdombuilding.fragment.information.PersonnelInfoFragment;
import com.yizhitong.wisdombuilding.utils.DataUtils;
import com.yizhitong.wisdombuilding.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonnelTabActivity extends BaseActivity {
    static final int NUM_ITEMS = 3;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private String[] strings = new String[]{"所属公司","个人信息","进出信息"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tab);
        Bundle bundle = new Bundle();
        int employeeId = getIntent().getIntExtra(Constants.EMPLOYEEID,0);
        bundle.putInt(Constants.EMPLOYEEID,employeeId);
        bundle.putString(Constants.TIME, DataUtils.getCurrDate(Constants.DateFormat_10));
        fragmentList.add(PersonnelCompanyFragment.getInstance(bundle));
        fragmentList.add(PersonnelInfoFragment.getInstance(bundle));
        fragmentList.add(PersonnelInOutFragment.getInstance(bundle));
        initView();
    }

    private void initView(){
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.tab_viewpager);
        MyAdapter fragmentAdater = new  MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdater);
        tab_layout.setupWithViewPager(viewPager);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return strings[position];
        }
    }
}
