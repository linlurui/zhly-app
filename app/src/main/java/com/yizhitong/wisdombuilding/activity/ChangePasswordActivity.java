package com.yizhitong.wisdombuilding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.DataModel;
import com.yizhitong.wisdombuilding.utils.MD5Utils;
import com.yizhitong.wisdombuilding.utils.MyUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        if (preventDoubleClick()) {
            return;
        }
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword)) {
            ToastUtil.showShort("密码不能为空");
            return;
        }

        if (!StringUtils.equals(newPassword, confirmPassword)) {
            ToastUtil.showShort("新密码两次输入不一致");
            return;
        }
        String oldPasswordMD5 = MD5Utils.string2MD5(oldPassword);
        String newPasswordMD5 = MD5Utils.string2MD5(newPassword);
        String confirmPasswordMD5 = MD5Utils.string2MD5(confirmPassword);
        int id = SPUtils.getInstance().getInt("id");
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<DataModel>> call = apiService.changePassword(id, oldPasswordMD5, newPasswordMD5, confirmPasswordMD5);

        startApiCall(call, new ApiResultHandler<DataModel>() {

            @Override
            void apiSucceed(DataModel model) {
                if (model.getCode() == 0) {
                    ToastUtil.showShort(model.getMsg());
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    ToastUtil.showShort(model.getMsg());
                }
            }
        });
    }
}
