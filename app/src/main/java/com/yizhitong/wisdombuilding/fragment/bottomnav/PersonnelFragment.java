package com.yizhitong.wisdombuilding.fragment.bottomnav;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.activity.CardCameraActivity;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.InOutTotalModel;
import com.yizhitong.wisdombuilding.utils.DataUtils;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.MyUtils;
import com.yizhitong.wisdombuilding.utils.TextStyle;
import com.yizhitong.wisdombuilding.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

@BindEventBus
public class PersonnelFragment extends BaseFragment {

    @BindView(R.id.workerAttencePercentage)
    TextView workerAttencePercentage;
    @BindView(R.id.workerTotalNum)
    TextView workerTotalNum;
    @BindView(R.id.workerPresentNum)
    TextView workerPresentNum;
    @BindView(R.id.todayAttenceWorkerNum)
    TextView todayAttenceWorkerNum;
    @BindView(R.id.managerPercentage)
    TextView managerPercentage;
    @BindView(R.id.totalManager)
    TextView totalManager;
    @BindView(R.id.managerPresent)
    TextView managerPresent;
    @BindView(R.id.todayAttenceManagerNum)
    TextView todayAttenceManagerNum;
    @BindView(R.id.weekTv)
    TextView weekTv;
    @BindView(R.id.dateTv)
    TextView dateTv;
    @BindView(R.id.projectNameTv)
    TextView projectNameTv;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smart_refresh;

    private static final int REQUEST_CAMERA_PERMISSION = 10111; //相机请求码
    private int tag;
    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_personnel_manage, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        network();
        //当前日期星期
        String weekStr = MyUtils.StringData(true);
        String dateStr = MyUtils.StringData(false);
        weekTv.setText(weekStr);
        dateTv.setText(dateStr);
        TextStyle tsy = new TextStyle(getResources().getColor(R.color.white), 24);
        projectNameTv.setText(tsy
                .clear()
                .spanSize("智慧楼宇")
                .span("\n一种全新的智能楼宇\n管理模式")
                .getText());
        smartRefreshView();
    }

    private void smartRefreshView() {
        smart_refresh.setEnableLoadMore(false);
        smart_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                network();
                refreshLayout.finishRefresh();
            }

        });
    }

    @OnClick({R.id.face_attendance, R.id.real_name_registration, R.id.personnel_info})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.face_attendance:
                if (showCheckPermissions() && !checkReadPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)) {
                    return;
                }
                tag=1;
                showCardCameraActivity();
                break;
            case R.id.real_name_registration:
                if (showCheckPermissions() && !checkReadPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)) {
                    return;
                }
                tag=2;
                showCardCameraActivity();
                break;
            case R.id.personnel_info:
                MyUiUtils.showInfoTabActivity(getActivity());
                break;
        }
    }

    /**
     * 是否应该检查权限
     *
     * @return
     */
    private boolean showCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(getActivity(), string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{string_permission}, request_code);
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCardCameraActivity();
                } else {
                    ToastUtil.showShort("请允许相机权限后再试");
                    return;
                }
                break;
        }
    }

    private void showCardCameraActivity() {
        Intent intent = new Intent();
        intent.putExtra(Constants.TAG,tag);
        intent.setClass(getActivity(), CardCameraActivity.class);
        startActivity(intent);
    }

    //获取数据信息
    private void network() {
        int pid = SPUtils.getInstance().getInt("projectId");
        String time = DataUtils.getCurrDate(Constants.DateFormat_10);
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<InOutTotalModel>> call = apiService.getPersonnelDT(pid, time);
        startApiCall(call, new ApiResultHandler<InOutTotalModel>() {

            @Override
            protected void apiSucceed(InOutTotalModel model) {
                if (model.getCode() == 0) {
                    updateView(model);
                }
            }
        });
    }

    private void updateView(InOutTotalModel model) {
        int textColor = getResources().getColor(R.color.text_blue);
        TextStyle ts = new TextStyle(textColor, 20);
        workerTotalNum.setText(ts.clear()
                .spanColorAndSize(model.getData().getZz().getZzryzs() + "")
                .getText());

        workerPresentNum.setText(ts.clear()
                .spanColorAndSize(model.getData().getZz().getZzryin() + "")
                .getText());

        todayAttenceWorkerNum.setText(ts.clear()
                .spanColorAndSize(model.getData().getZz().getZzryout() + "")
                .getText());

        totalManager.setText(ts.clear()
                .spanColorAndSize(model.getData().getFk().getFkryzs() + "")
                .getText());

        managerPresent.setText(ts.clear()
                .spanColorAndSize(model.getData().getFk().getFkryin() + "")
                .getText());

        todayAttenceManagerNum.setText(ts.clear()
                .spanColorAndSize(model.getData().getFk().getFkryout() + "")
                .getText());

    }

}
