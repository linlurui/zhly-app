package com.yizhitong.wisdombuilding.activity;

import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.utils.AppUtil;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.TextStyle;

import butterknife.BindView;
import butterknife.OnClick;


public class SystemSettingActivity extends BaseActivity {

    @BindView(R.id.msg)
    TextView msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        TextStyle ts = new TextStyle(getResources().getColor(R.color.text_blue), 32);
        msg.setText(ts.clear()
                .spanColorAndSize(AppUtil.getAppName(this))
                .span("\n\n\n\n当前版本：" + AppUtil.getVersionName(this)).getText());
    }

    @OnClick({R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                logout();
                break;
        }
    }

    //退出登录
    private void logout() {
        if (preventDoubleClick()) {
            return;
        }
        showAlertDialog(R.drawable.icon_warning, null, getString(R.string.sign_out_tip), getString(R.string.sign_out), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                MyUiUtils.signOut(SystemSettingActivity.this);
            }
        }, getString(R.string.cancel), null, null);
    }
}
