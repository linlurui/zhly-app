package com.yizhitong.wisdombuilding.fragment.information;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.PersonnelDetailModel;
import com.yizhitong.wisdombuilding.view.LabelEditRow;

import butterknife.BindView;
import retrofit2.Call;

@BindEventBus
public class PersonnelCompanyFragment extends BaseFragment {


    @BindView(R.id.personnel_company)
    LabelEditRow personnel_company;
    @BindView(R.id.personnel_department)
    LabelEditRow personnel_department;
    @BindView(R.id.personnel_storey)
    LabelEditRow personnel_storey;
    @BindView(R.id.personnel_remarks)
    EditText personnel_remarks;

    private static PersonnelCompanyFragment fragment;

    public static PersonnelCompanyFragment getInstance(Bundle bundle){
        if (fragment==null){
            fragment= new PersonnelCompanyFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_personnel_company, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        network();
        personnel_company.setEnabled(false);
        personnel_department.setEnabled(false);
        personnel_storey.setEnabled(false);
        personnel_remarks.setEnabled(false);
    }

    private void network() {
        int personnelId = getArguments().getInt(Constants.EMPLOYEEID);
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<PersonnelDetailModel>> call = apiService.selectPersonnelById(personnelId);
        startApiCall(call, new ApiResultHandler<PersonnelDetailModel>() {

            @Override
            protected void apiSucceed(PersonnelDetailModel model) {
                if (model.getCode() == 0) {
                    updateView(model.getData());
                }
            }
        });
    }

    private void updateView(PersonnelDetailModel.DataBean dataBean) {
        personnel_company.getEditText().setText(dataBean.getCompanyName());
        personnel_department.getEditText().setText(dataBean.getSubordinate());
        personnel_storey.getEditText().setText(dataBean.getFloor());
        personnel_remarks.setText(dataBean.getBz());
    }
}
