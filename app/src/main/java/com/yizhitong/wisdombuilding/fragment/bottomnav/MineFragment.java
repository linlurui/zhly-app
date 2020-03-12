package com.yizhitong.wisdombuilding.fragment.bottomnav;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.activity.ChangePasswordActivity;
import com.yizhitong.wisdombuilding.activity.SystemSettingActivity;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.manager.SessionManager;
import com.yizhitong.wisdombuilding.modle.LoginUserModel;

import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;


import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.PhoneUtils.call;

@BindEventBus
public class MineFragment extends BaseFragment {

    @BindView(R.id.account)
    TextView account;

    private static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码

    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_mine, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        LoginUserModel userModel = SessionManager.shared().getCurrentUser();
        if (userModel != null) {
            String userName = userModel.getData().getUserName();
            account.setText(userName);
        }

    }

    @OnClick({R.id.mani_customer, R.id.mani_feedback, R.id.mani_setting, R.id.mani_password})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mani_customer:
                showPhoneDialog();
                break;
            case R.id.mani_feedback:

                break;
            case R.id.mani_setting:
                intent.setClass(getActivity(), SystemSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mani_password:
                intent.setClass(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void showPhoneDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("客服电话")
                .content("400-8008778")
                .positiveText("立即呼叫")
                .positiveColor(getResources().getColor(R.color.text_blue))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + "400-8008778");
                            intent.setData(data);
                            startActivity(intent);
                        }
                    }
                })
                .negativeColor(getResources().getColor(R.color.text_gray))
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 判断是否有某项权限
     *
     * @param string_permission 权限
     * @param request_code      请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(getActivity(), string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showShort("请允许拨号权限后再试");
                    return;
                }
                call("tel:" + "400-8008778");
                break;
        }
    }

}
