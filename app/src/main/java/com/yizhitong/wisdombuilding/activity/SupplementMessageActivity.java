package com.yizhitong.wisdombuilding.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.manager.SessionManager;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.CompanyModel;
import com.yizhitong.wisdombuilding.modle.DataModel;
import com.yizhitong.wisdombuilding.modle.ImageCardInfoModel;
import com.yizhitong.wisdombuilding.modle.InfoEntryModel;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;
import com.yizhitong.wisdombuilding.view.LabelEditRow;
import com.yizhitong.wisdombuilding.view.LabelTextRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import retrofit2.Call;


public class SupplementMessageActivity extends BaseActivity {

    @BindView(R.id.supplement_company)
    protected LabelTextRow company;
    @BindView(R.id.supplement_department)
    protected LabelEditRow department;
    @BindView(R.id.supplement_storey)
    protected LabelEditRow storey;
    @BindView(R.id.supplement_remarks)
    protected EditText remarks;

    private int companyId;
    private int type;
    private int pid;
    private List<CompanyModel.DataBean> data;
    private OptionPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplement);
        type = getIntent().getIntExtra(Constants.TAG, 1);
        pid = SPUtils.getInstance().getInt("projectId");
        if (type == 2) {
            company.setLabel("拜访公司");
            department.setLabelText("拜访对象");
        }
        getCompanyMsg();
    }

    private void showDialog() {
        List<String> names = new ArrayList<>();
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getCompanyName() != null) {
                    names.add(data.get(i).getCompanyName());
                }
            }
            picker = new OptionPicker(this, names);
            picker.setCanceledOnTouchOutside(true);
            picker.setSelectedIndex(0);
            picker.setCycleDisable(true);

            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int index, String item) {
                    company.setText(item);
                    companyId = data.get(index).getId();
                }
            });
            picker.show();
        } else {
            ToastUtil.showShort("暂无公司");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        picker.dismiss();
    }

    @OnClick({R.id.confirm, R.id.supplement_company})
    protected void onClicked(View view) {
        switch (view.getId()) {
            case R.id.supplement_company:
                showDialog();
                break;
            case R.id.confirm:
                submit();
                break;
        }
    }

    private void getCompanyMsg() {
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<CompanyModel>> call = apiService.selectList(pid);
        startApiCall(call, new ApiResultHandler<CompanyModel>() {
            @Override
            void apiSucceed(CompanyModel model) {

                if (model.getCode() == 0) {
                    data = model.getData();
                } else {
                    ToastUtil.showShort(model.getMsg());
                }
            }

            @Override
            void apiFailed() {

            }
        });
    }

    private void submit() {
        ImageCardInfoModel.DataBean front = InfoEntryModel.shared().idFrontInfo.getData();
        ImageCardInfoModel.DataBean back = InfoEntryModel.shared().idBackInfo.getData();
        ImageCardInfoModel.DataBean face = InfoEntryModel.shared().faceResource.getData();
        Map<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("type", type + "");
        map.put("empName", front.getTitle());
        map.put("idCode", front.getIdNumber());
        map.put("faceUrl", face.getUrl());
        map.put("idphotoScan", front.getUrl());
        map.put("idphotoScan2", back.getUrl());
        map.put("empPhon", "");
        map.put("empSex", front.getSex());
        map.put("empNation", front.getNation());
        map.put("idAddress", front.getAddress());
        map.put("idAgency", back.getIssue());
        map.put("idValiddate", back.getIdCardStartdate());
        map.put("dateOfBirth", front.getDateOfBirth());
        map.put("companyId", companyId);
        map.put("companyName", company.getText().toString());
        map.put("floor", storey.getEditText().getText().toString());
        map.put("subordinate", department.getEditText().getText().toString());
        map.put("bz", remarks.getText().toString());
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<DataModel>> call = apiService.insertPersonnel(map);
        startApiCall(call, new ApiResultHandler<DataModel>() {
            @Override
            boolean showLoading() {
                return true;
            }

            @Override
            void apiSucceed(DataModel model) {

                if (model.getCode() == 0) {
                    finish();
                    ToastUtil.showShort(model.getMsg());
                } else {
                    ToastUtil.showShort(model.getMsg());
                }
            }

            @Override
            void apiFailed() {

            }
        });
    }
}
