package com.yizhitong.wisdombuilding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.modle.PersonnelListModel;
import com.yizhitong.wisdombuilding.modle.PersonnelModel;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;

import java.util.List;

public class InfoExpandableListAdapter extends BaseExpandableListAdapter {

    //Model：定义的数据
    private List<PersonnelListModel.DataBean> groups;

    private List<List<PersonnelModel>> childs;
    private Context context;

    public InfoExpandableListAdapter(Context context, List<PersonnelListModel.DataBean> groups, List<List<PersonnelModel>> childs) {
        this.context = context;
        this.groups = groups;
        this.childs = childs;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.parent_text = convertView.findViewById(R.id.parent_text);
            groupViewHolder.parent_image = convertView.findViewById(R.id.parent_image);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.parent_text.setText(groups.get(groupPosition).getCompanyName() + "（" + groups.get(groupPosition).getSize() + "）");
        if (isExpanded) {
            groupViewHolder.parent_image.setSelected(true);
        } else {
            groupViewHolder.parent_image.setSelected(false);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.child_name = convertView.findViewById(R.id.child_name);
            childViewHolder.child_img = convertView.findViewById(R.id.child_img);
            childViewHolder.child_ll_in = convertView.findViewById(R.id.child_ll_in);
            childViewHolder.child_ll_out = convertView.findViewById(R.id.child_ll_out);
            childViewHolder.in_time = convertView.findViewById(R.id.in_time);
            childViewHolder.out_time = convertView.findViewById(R.id.out_time);
            childViewHolder.child_detail = convertView.findViewById(R.id.child_detail);
            childViewHolder.personnel_name = convertView.findViewById(R.id.personnel_name);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.child_ll_in.setVisibility(View.GONE);
        childViewHolder.child_ll_out.setVisibility(View.GONE);
        childViewHolder.personnel_name.setText(childs.get(groupPosition).get(childPosition).getEmpName() + "\n" + childs.get(groupPosition).get(childPosition).getSubordinate());
        childViewHolder.child_name.setVisibility(View.GONE);

        RoundedCorners roundedCorners = new RoundedCorners(20);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners);
        requestOptions.skipMemoryCache(true) //不加入内存缓存，默认会加入
                .diskCacheStrategy(DiskCacheStrategy.NONE); //磁盘缓存只缓存转换过后的图片
        Glide.with(context).asBitmap()
                .load(childs.get(groupPosition).get(childPosition).getFaceUrl())
                .apply(requestOptions)
                .into(childViewHolder.child_img);
        childViewHolder.child_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUiUtils.showPersonnelTabActivity(context, childs.get(groupPosition).get(childPosition).getId());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView parent_text;
        ImageView parent_image;
    }

    static class ChildViewHolder {
        TextView personnel_name;
        TextView child_name;
        ImageView child_img;
        TextView in_time;
        TextView out_time;
        TextView child_detail;
        LinearLayout child_ll_in;
        LinearLayout child_ll_out;

    }

}
