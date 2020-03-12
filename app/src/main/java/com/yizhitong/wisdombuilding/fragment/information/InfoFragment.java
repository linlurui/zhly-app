package com.yizhitong.wisdombuilding.fragment.information;

import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;


import com.blankj.utilcode.util.SPUtils;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.adapter.InfoExpandableListAdapter;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;

import com.yizhitong.wisdombuilding.modle.PersonnelListModel;
import com.yizhitong.wisdombuilding.modle.PersonnelModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

@BindEventBus
public class InfoFragment extends BaseFragment {

    @BindView(R.id.expandable_list)
    ExpandableListView expandable_list;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;

    InfoExpandableListAdapter adapter;
    List<PersonnelListModel.DataBean> groups = new ArrayList<>();
    List<List<PersonnelModel>> childs = new ArrayList<>();

    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_info, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        network();
    }

    @OnClick(R.id.search)
    public void onClick(View view) {
        network();
    }

    private void network() {
        int pid = SPUtils.getInstance().getInt("projectId");
        String name = editTextSearch.getText().toString();
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<PersonnelListModel>> call = apiService.selectPersonnelCompany(pid, name);
        startApiCall(call, new ApiResultHandler<PersonnelListModel>() {

            @Override
            protected void apiSucceed(PersonnelListModel model) {
                if (model.getCode() == 0) {
                    groups.clear();
                    childs.clear();
                    updateView(model);
                }
            }
        });
    }

    private void updateView(PersonnelListModel model) {
        for (PersonnelListModel.DataBean dataBean : model.getData()) {
            groups.add(dataBean);
            childs.add(dataBean.getpList());
        }
        adapter = new InfoExpandableListAdapter(getActivity(), groups, childs);
        expandable_list.setAdapter(adapter);
    }

}
