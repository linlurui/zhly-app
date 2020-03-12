package com.yizhitong.wisdombuilding.activity;

import android.os.Bundle;

import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.fragment.information.PersonnelInOutFragment;



public class PersonnelInOutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inout_personnel);
        int employeeId = getIntent().getIntExtra(Constants.EMPLOYEEID, 0);
        String time = getIntent().getStringExtra(Constants.TIME);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EMPLOYEEID, employeeId);
        bundle.putString(Constants.TIME,time);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_fragment, PersonnelInOutFragment.getInstance(bundle))
                .commit();
    }
}
