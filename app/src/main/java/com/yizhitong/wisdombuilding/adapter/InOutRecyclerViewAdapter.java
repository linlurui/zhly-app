package com.yizhitong.wisdombuilding.adapter;



import android.view.View;

import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.modle.PersonnelInOutModel;
import com.yizhitong.wisdombuilding.modle.PersonnelListModel;

import java.util.List;

public class InOutRecyclerViewAdapter extends BaseQuickAdapter<PersonnelInOutModel.DataBean, BaseViewHolder> {
        /**
         * 构造器，用来初始化TestAdapter
         *
         * @param data 我们的列表数据
         */
        public InOutRecyclerViewAdapter(@Nullable List<PersonnelInOutModel.DataBean> data) {
            super(R.layout.item_inout_fragment, data);
        }

        /**
         * 增加一个构造方法，便于没有数据时候初始化适配器
         */
        public InOutRecyclerViewAdapter() {
            super(R.layout.item_inout_fragment);
        }

        /**
         * 继承BaseQuickAdapter后需要重写的方法
         *
         * @param helper view持有者，为重用view而设计，减少每次创建view的内存消耗
         * @param data   我们的列表数据
         */
    @Override
    protected void convert(BaseViewHolder helper, PersonnelInOutModel.DataBean data) {
        if (data.getDirection()!=null){
            helper.setText(R.id.in_out, data.getDirection().equals("in")?"进":"出");
        }
        helper.setText(R.id.in_time,data.getPassedTime()+"/"+data.getTemperature());
    }
}
