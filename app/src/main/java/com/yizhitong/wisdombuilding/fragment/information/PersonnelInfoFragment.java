package com.yizhitong.wisdombuilding.fragment.information;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.fragment.BaseFragment;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.PersonnelDetailModel;
import com.yizhitong.wisdombuilding.modle.PersonnelListModel;
import com.yizhitong.wisdombuilding.utils.ToastUtil;
import com.yizhitong.wisdombuilding.view.LabelTextRow;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

import static com.blankj.utilcode.util.PhoneUtils.call;

@BindEventBus
public class PersonnelInfoFragment extends BaseFragment {


    @BindView(R.id.personnel_id)
    LabelTextRow personnel_id;
    @BindView(R.id.personnel_name)
    LabelTextRow personnel_name;
    @BindView(R.id.personnel_sex)
    LabelTextRow personnel_sex;
    @BindView(R.id.personnel_nation)
    LabelTextRow personnel_nation;
    @BindView(R.id.personnel_birthday)
    LabelTextRow personnel_birthday;
    @BindView(R.id.personnel_address)
    LabelTextRow personnel_address;
    @BindView(R.id.personnel_agency)
    LabelTextRow personnel_agency;
    @BindView(R.id.personnel_valid)
    LabelTextRow personnel_valid;

    @BindView(R.id.img_photo)
    ImageView img_photo;
    @BindView(R.id.img_front)
    ImageView img_front;
    @BindView(R.id.img_back)
    ImageView img_back;

    private static PersonnelInfoFragment fragment;

    public static PersonnelInfoFragment getInstance(Bundle bundle){
        if (fragment==null){
            fragment= new PersonnelInfoFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.fragment_personnel_details, null);
        return view;
    }

    @Override
    protected void initViewOrData() {
        super.initViewOrData();
        network();
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
        personnel_id.getValueView().setText(dataBean.getIdCode());
        personnel_name.getValueView().setText(dataBean.getEmpName());
        personnel_address.getValueView().setText(dataBean.getIdAddress());
        personnel_agency.getValueView().setText(dataBean.getIdAgency());
        personnel_birthday.getValueView().setText(dataBean.getDateOfBirth());
        personnel_nation.getValueView().setText(dataBean.getEmpNation());
        personnel_sex.getValueView().setText(dataBean.getEmpSex());
        personnel_valid.getValueView().setText(dataBean.getIdValiddate());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true) //不加入内存缓存，默认会加入
                .diskCacheStrategy(DiskCacheStrategy.NONE); //磁盘缓存只缓存转换过后的图片
        Glide.with(getActivity()).asBitmap()
                .load(dataBean.getFaceUrl())
                .apply(requestOptions)
                .into(img_photo);
        Glide.with(getActivity()).asBitmap()
                .load(dataBean.getIdphotoScan())
                .apply(requestOptions)
                .into(img_front);
        Glide.with(getActivity()).asBitmap()
                .load(dataBean.getIdphotoScan2())
                .apply(requestOptions)
                .into(img_back);
    }
}
