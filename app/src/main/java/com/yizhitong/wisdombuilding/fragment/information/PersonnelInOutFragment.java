package com.yizhitong.wisdombuilding.fragment.information;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.adapter.InOutRecyclerViewAdapter;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.DateEntity;
import com.yizhitong.wisdombuilding.modle.PersonnelDetailModel;
import com.yizhitong.wisdombuilding.modle.PersonnelInOutModel;

import com.yizhitong.wisdombuilding.utils.DataUtils;
import com.yizhitong.wisdombuilding.utils.LogUtil;
import com.yizhitong.wisdombuilding.view.DataView;

import butterknife.BindView;
import retrofit2.Call;


@BindEventBus
public class PersonnelInOutFragment extends BaseFragment {


    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.data_view)
    DataView dataView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smart_refresh;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private int employeeId;
    private String time;
    private InOutRecyclerViewAdapter myAdapter;
    private static PersonnelInOutFragment fragment;

    public static PersonnelInOutFragment getInstance(Bundle bundle) {
        if (fragment == null) {
            fragment = new PersonnelInOutFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_inout_personnel, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        employeeId = getArguments().getInt(Constants.EMPLOYEEID);
        time =getArguments().getString(Constants.TIME);
        network();
        smartRefreshView();
        refreshView();
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                LogUtil.e("--------------", date.toString());
                time = date.date;
                network();
            }
        });
        dataView.getData(time);
    }

    /**
     * 刷新消息列表
     */
    private void refreshView() {
        //1,加载空布局文件，便于第五步适配器在没有数据的时候加载
        View emptyView = View.inflate(getActivity(), R.layout.empty_view, null);
        //2，设置LayoutManager,LinearLayoutManager表示竖直向下
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //3，初始化一个无数据的适配器
        myAdapter = new InOutRecyclerViewAdapter();
        //4，绑定recyclerView和适配器
        recyclerView.setAdapter(myAdapter);
        //5，给recyclerView设置空布局
        myAdapter.setEmptyView(emptyView);
    }

    private void smartRefreshView() {
        smart_refresh.setEnableLoadMore(true);
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

    private void network() {
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<PersonnelInOutModel>> call = apiService.selectPersonnelRecord(employeeId, time);
        startApiCall(call, new ApiResultHandler<PersonnelInOutModel>() {

            @Override
            protected void apiSucceed(PersonnelInOutModel model) {
                if (model.getCode() == 0) {
                    myAdapter.setNewData(model.getData());
                }
            }
        });
        Call<ApiResp<PersonnelDetailModel>> call2 = apiService.selectPersonnelById(employeeId);
        startApiCall(call2, new ApiResultHandler<PersonnelDetailModel>() {

            @Override
            protected void apiSucceed(PersonnelDetailModel model) {
                if (model.getCode() == 0 && model.getData() != null) {
                    tv_name.setText(model.getData().getEmpName());
                }
            }
        });
    }

}
