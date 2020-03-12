package com.yizhitong.wisdombuilding.fragment.information;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.activity.PersonnelInOutActivity;
import com.yizhitong.wisdombuilding.adapter.InOutExpandableListAdapter;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.DateEntity;
import com.yizhitong.wisdombuilding.modle.InOutItemModel;
import com.yizhitong.wisdombuilding.modle.InOutModel;
import com.yizhitong.wisdombuilding.modle.InOutTotalModel;
import com.yizhitong.wisdombuilding.modle.PersonnelInOutModel;
import com.yizhitong.wisdombuilding.utils.DataUtils;
import com.yizhitong.wisdombuilding.utils.LogUtil;
import com.yizhitong.wisdombuilding.utils.TextStyle;
import com.yizhitong.wisdombuilding.view.DataView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;


@BindEventBus
public class InOutFragment extends BaseFragment {

    @BindView(R.id.data_view)
    DataView dataView;
    @BindView(R.id.tv_zz)
    TextView tv_zz;
    @BindView(R.id.tv_fk)
    TextView tv_fk;
    @BindView(R.id.expandable_list)
    ExpandableListView expandable_list;

    List<String> groups = new ArrayList<>();
    List<List<InOutItemModel>> childs = new ArrayList<>();
    private InOutExpandableListAdapter adapter;
    private String time;
    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_inout, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        time = DataUtils.getCurrDate(Constants.DateFormat_10);
        network();
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                time=date.date;
                network();
            }
        });
        dataView.getData(DataUtils.getCurrDate(Constants.DateFormat_10));
    }

    private void network() {
        int pid = SPUtils.getInstance().getInt("projectId");
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<InOutModel>> call = apiService.getPersonnelRecord(pid, time);
        startApiCall(call, new ApiResultHandler<InOutModel>() {

            @Override
            protected void apiSucceed(InOutModel model) {
                if (model.getCode() == 0) {
                    updateView(model);
                }
            }


        });
    }

    private void updateView(InOutModel model) {
        groups.clear();
        childs.clear();
        groups.add(model.getData().getZzMap().getZzSize() + "");
        groups.add(model.getData().getFkMap().getFkSize() + "");
        childs.add(model.getData().getZzMap().getZzList());
        childs.add(model.getData().getFkMap().getFkList());
        adapter = new InOutExpandableListAdapter(getActivity(), groups, childs);
        expandable_list.setAdapter(adapter);
        expandable_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), PersonnelInOutActivity.class);
                intent.putExtra(Constants.EMPLOYEEID,childs.get(groupPosition).get(childPosition).getId());
                intent.putExtra(Constants.TIME,time);
                startActivity(intent);
                return false;
            }
        });
        TextStyle ts = new TextStyle(getResources().getColor(R.color.text_blue), 26);
        tv_zz.setText(ts
                .clear()
                .span("办公人数：")
                .spanColorAndSize(model.getData().getZzMap().getZzSize()+"")
                .span("(人)")
                .getText());
        tv_fk.setText(ts
                .clear()
                .span("访客人数：")
                .spanColorAndSize(model.getData().getFkMap().getFkSize()+"")
                .span("(人)")
                .getText());
    }
}
