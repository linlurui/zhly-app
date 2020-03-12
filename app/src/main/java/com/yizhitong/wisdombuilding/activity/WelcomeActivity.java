package com.yizhitong.wisdombuilding.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.manager.SessionManager;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;


public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView ic = findViewById(R.id.ic_logo);
        Drawable drawable = getResources().getDrawable(R.drawable.login_top);
        MyUiUtils.circularBitmap(ic, drawable);
        MyUiUtils.hideNavKey(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainOrSignInActivity();
            }
        }, 2000);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    private void showMainOrSignInActivity() {
        if (SessionManager.shared().isSignIn()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
