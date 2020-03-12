package com.yizhitong.wisdombuilding.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.manager.SessionManager;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.LoginUserModel;
import com.yizhitong.wisdombuilding.modle.PrivilegesModel;
import com.yizhitong.wisdombuilding.utils.MD5Utils;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;

public class LoginActivity extends BaseActivity {

    //账户账号
    @BindView(R.id.et_userName)
    protected EditText etUserName;
    //账户密码
    @BindView(R.id.et_password)
    protected EditText etPassword;
    //登录按钮
    @BindView(R.id.btn_login)
    protected Button btnLogin;
    //清除账号
    @BindView(R.id.iv_unameClear)
    protected ImageView ivUnameClear;
    //清除密码
    @BindView(R.id.iv_pwdClear)
    protected ImageView ivPwdClear;

    private MD5Utils md5Uutils = new MD5Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ImageView ic = findViewById(R.id.iv_icon);
        final Drawable drawable = getResources().getDrawable(R.drawable.login_top);
        Bitmap bitmap = ImageUtils.drawable2Bitmap(drawable);
        Bitmap mbitmap = ImageUtils.toRoundCorner(bitmap, 20);
        ic.setImageBitmap(mbitmap);
        MyUiUtils.hideNavKey(this);
        ButterKnife.bind(this);
//        //初始化清除事件
//        EditTextClearTools.addClearListener(etUserName, ivUnameClear);
//        EditTextClearTools.addClearListener(etPassword, ivPwdClear);

    }

    //登录点击事件
    @OnClick(R.id.btn_login)
    public void clickLongin(View view) {
        if (preventDoubleClick()) {
            return;
        }
        if (!checkInputOK()) {
            return;
        }
        login();
    }

    private boolean checkInputOK() {
        if (StringUtils.isEmpty(etUserName.getText().toString().trim())) {
            ToastUtil.showShort(getResources().getString(R.string.account_hint));
            return false;
        }
        if (StringUtils.isEmpty(etPassword.getText().toString().trim())) {
            ToastUtil.showShort(getResources().getString(R.string.password_hint));
            return false;
        }

        return true;
    }

    private void login() {

        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordMD5 = md5Uutils.string2MD5(password);

        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<LoginUserModel>> call = apiService.loginUser(userName, passwordMD5, 0);

        startApiCall(call, new ApiResultHandler<LoginUserModel>() {
            @Override
            String defaultErrorMessage() {
                return getString(R.string.sign_in_failed);
            }

            @Override
            void apiSucceed(LoginUserModel loginUserModel) {
                if (loginUserModel.getCode() == 0) {
                    loginSucceed(loginUserModel);
                } else {
                    ToastUtil.showShort(loginUserModel.getMsg());
                }
            }
        });
    }

    private void loginSucceed(LoginUserModel loginUserModel) {
        SessionManager.shared().signIn(loginUserModel);
        String username = etUserName.getText().toString().trim();
        SPUtils.getInstance().put(Constants.KeySavedUsername, username);
//        getPrivileges(loginUserModel.getData().getId());
        showMainActivity();

    }

    private void showMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //获取菜单权限
    private void getPrivileges(int id ) {
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<PrivilegesModel>> call = apiService.getSystemPrivileges(id, 0);

        startApiCall(call, new ApiResultHandler<PrivilegesModel>() {
            @Override
            String defaultErrorMessage() {
                return null;
            }

            @Override
            void apiSucceed(PrivilegesModel privilegesModel) {
                if (privilegesModel.getCode() == 0) {
                    SessionManager.shared().savePrivileges(privilegesModel);
                }
            }
        });
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }
}

