package com.yizhitong.wisdombuilding.fragment.bottomnav;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.activity.BaseActivity;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.InOutTotalModel;
import com.yizhitong.wisdombuilding.modle.LoginUserModel;
import com.yizhitong.wisdombuilding.utils.DataUtils;
import com.yizhitong.wisdombuilding.utils.MyUtils;
import com.yizhitong.wisdombuilding.utils.TextStyle;
import com.yizhitong.wisdombuilding.utils.ToastUtil;


import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

@BindEventBus
public class HomeFragment extends BaseFragment {

    @BindView(R.id.homeGridView)
    GridView homeGridView;

    @BindView(R.id.week)
    TextView week;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.tv_worker_online)
    TextView worker_online;
    @BindView(R.id.tv_worker_in)
    TextView worker_in;
    @BindView(R.id.tv_manager_online)
    TextView manager_online;
    @BindView(R.id.tv_manager_in)
    TextView manager_in;
    @BindView(R.id.tv_project_synopsis)
    TextView project_synopsis;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smart_refresh;

    //定义图标数组
    private int[] imageRes = {R.drawable.personnel_manage, R.drawable.video_monitor,
            R.drawable.intelligent_parking, R.drawable.fire_control};
    //定义标题数组
    private String[] itemName = {"人员管理", "视频监控", "智能停车", "消防巡检"};


    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_home, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        TextStyle tsy = new TextStyle(getResources().getColor(R.color.white), 24);
        project_synopsis.setText(tsy
                .clear()
                .spanSize("智慧楼宇")
                .span("\n一种全新的智能楼宇\n管理模式")
                .getText());
        //当前日期星期
        String weekStr = MyUtils.StringData(true);
        String dateStr = MyUtils.StringData(false);
        week.setText(weekStr);
        date.setText(dateStr);
        smartRefreshView();
        network();
    }

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
    private void updateView(InOutTotalModel model){
        int textColor = getResources().getColor(R.color.text_blue);
        TextStyle ts = new TextStyle(textColor, 20);
        worker_online.setText(ts.clear()
                .spanColorAndSize(model.getData().getZz().getZzryzs() + "")
                .span("\n办公人数")
                .getText());

        worker_in.setText(ts.clear()
                .spanColorAndSize(model.getData().getZz().getZzryin() + "")
                .span("\n出入人数")
                .getText());

        manager_online.setText(ts.clear()
                .spanColorAndSize(model.getData().getFk().getFkryzs() + "")
                .span("\n访客人数")
                .getText());

        manager_in.setText(ts.clear()
                .spanColorAndSize(model.getData().getFk().getFkryin() + "")
                .span("\n访客出入")
                .getText());

    }
//    @OnClick({R.id.group})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.group:
//                getActivity().finish();
//                break;
//        }
//    }

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


    @Override
    protected void showLoadingDialog() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String weekStr = MyUtils.StringData(true);
        String dateStr = MyUtils.StringData(false);
        week.setText(weekStr);
        date.setText(dateStr);

        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int length = itemName.length;
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImageView", imageRes[i]);
            map.put("ItemTextView", itemName[i]);
            data.add(map);
        }
        //为itme.xml添加适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
                data, R.layout.item_home, new String[]{"ItemImageView", "ItemTextView"}, new int[]{R.id.ItemImageView, R.id.ItemTextView});
        homeGridView.setAdapter(simpleAdapter);
        //为mGridView添加点击事件监听器
        homeGridView.setOnItemClickListener(new GridViewItemOnClick());
    }


    //定义点击事件监听器
    public class GridViewItemOnClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    ToastUtil.showShort(getResources().getString(R.string.not_open));
                    break;
                case 1:
                    ToastUtil.showShort(getResources().getString(R.string.not_open));
                    break;
                case 2:
                    ToastUtil.showShort(getResources().getString(R.string.not_open));
                    break;
                case 3:
                    ToastUtil.showShort(getResources().getString(R.string.not_open));
                    break;
                case 4:
                    ToastUtil.showShort(getResources().getString(R.string.not_open));
                    break;

            }
        }
    }
}
