package com.yizhitong.wisdombuilding.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;

import com.next.easynavigation.view.EasyNavigationBar;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.bottomnav.HomeFragment;
import com.yizhitong.wisdombuilding.fragment.bottomnav.MineFragment;
import com.yizhitong.wisdombuilding.fragment.bottomnav.PersonnelFragment;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.VersionModel;
import com.yizhitong.wisdombuilding.service.AppUpdateService;
import com.yizhitong.wisdombuilding.utils.MyUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class MainActivity extends BaseActivity {

    @BindView(R.id.navBar)
    EasyNavigationBar navBar;

    private Context mContext;
    private String mDownloadUrl;


    private static final int REQUEST_CODE_UNKNOWN_APP = 2001;

    private static final String TAG = "MainActivity";

    public static final String MESSAGE_RECEIVED_ACTION = "com.hujiang.wisdomsite.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_ALIAS = "alias";

    public static final int SET_ALIAS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        setNavigationBar();
//        if (SPUtils.getInstance().getBoolean(Constants.ISSHOWUWHITE, true)) {
//            showUWhiteDialog();
//        }
//        getVersionService();
//        EventBus.getDefault().register(this);
    }

    private void setNavigationBar() {
        String[] tabText = {"首页", "人员管理", "我的"};
        //未选中icon
        int[] normalIcon = {R.drawable.icon_home_unselected, R.drawable.icon_two_system_unselected, R.drawable.icon_mine_unselected};
        //选中时icon
        int[] selectIcon = {R.drawable.icon_home_selected, R.drawable.icon_two_system_selected, R.drawable.icon_mine_selected};
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new PersonnelFragment());
        fragments.add(new MineFragment());
        navBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .normalTextColor(getResources().getColor(R.color.text_gray))
                .selectIconItems(selectIcon)
                .selectTextColor(getResources().getColor(R.color.text_blue))
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .lineHeight(1)
                .lineColor(getResources().getColor(R.color.gray))
                .msgPointSize(16)
                .msgPointLeft(-10)
                .msgPointTop(-10)
                .msgPointTextSize(8)
                .canScroll(true)
                .build();
    }

    public void getVersionService() {
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<VersionModel>> call = apiService.getVersion();
        startApiCall(call, new ApiResultHandler<VersionModel>() {
            @Override
            void apiSucceed(VersionModel model) {
                if (model.getCode() == 0) {
                    if (MyUtils.compareVersion(model.getData().getVersionName(), MyUtils.getVersion(mContext)) == 1) {
                        SPUtils.getInstance().put(Constants.ISUPDATE,true);
                        if (SPUtils.getInstance().getBoolean(model.getData().getVersionName(), true)){
                            showUpdateDialog(model);
                        }
                    }
                } else {
                    ToastUtil.showShort(model.getMsg());
                }
            }
        });
    }

    private void showUpdateDialog(final VersionModel model) {
        if (model.getData().isMandatoryUpgrade()) {
            new MaterialDialog.Builder(this)
                    .title("版本更新")
                    .content(model.getData().getVersionContent())
                    .positiveText("立即更新")
                    .positiveColor(getResources().getColor(R.color.text_blue))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mDownloadUrl = model.getData().getUrl();
                            SPUtils.getInstance().put(Constants.MDOWNLOADURL,mDownloadUrl);
                            downloadAPK();
                        }
                    })
                    .negativeColor(getResources().getColor(R.color.text_gray))
                    .negativeText("取消")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            if (model.getData().isMandatoryUpgrade()) {
                                finish();
                            }
                        }
                    })
                    .cancelable(false)
                    .show();
        } else {
            new MaterialDialog.Builder(this)
                    .title("版本更新")
                    .content(model.getData().getVersionContent())
                    .positiveText("立即更新")
                    .positiveColor(getResources().getColor(R.color.text_blue))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mDownloadUrl = model.getData().getUrl();
                            SPUtils.getInstance().put(Constants.MDOWNLOADURL,mDownloadUrl);
                            downloadAPK();
                        }
                    })
                    .negativeColor(getResources().getColor(R.color.text_gray))
                    .negativeText("取消")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            if (model.getData().isMandatoryUpgrade()) {
                                finish();
                            }
                        }
                    })
                    .neutralColor(getResources().getColor(R.color.text_gray))
                    .neutralText("不再提醒")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SPUtils.getInstance().put(model.getData().getVersionName(), false);
                        }
                    })
                    .cancelable(false)
                    .show();
        }
    }



    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            Log.e(TAG, resultCode + "");
            downloadAPK();
        }
    }

    //下载apk安装
    private void downloadAPK() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                AppUpdateService.start(mContext, mDownloadUrl);//安装应用的逻辑
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            AppUpdateService.start(mContext, mDownloadUrl);
        }
    }

    private void showUWhiteDialog() {
        new MaterialDialog.Builder(this)
                .title("温馨提示")
                .content("为了更好的使用APP预警推送功能，需要开启自启权限，下一步将继续请求权限")
                .positiveText("下一步")
                .positiveColor(getResources().getColor(R.color.text_blue))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PermissionUtils.launchAppDetailsSettings();
                    }
                })
                .negativeColor(getResources().getColor(R.color.text_gray))
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .neutralColor(getResources().getColor(R.color.text_gray))
                .neutralText("不再提醒")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SPUtils.getInstance().put(Constants.ISSHOWUWHITE, false);
                    }
                })
                .cancelable(false)
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
