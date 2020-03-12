package com.yizhitong.wisdombuilding.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.modle.ImageCardInfoModel;
import com.yizhitong.wisdombuilding.modle.InfoEntry;
import com.yizhitong.wisdombuilding.modle.InfoEntryModel;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;


public class OcrInfoActivity extends BaseActivity {
    @BindView(R.id.imageView)
    protected ImageView imageView;

    @BindView(R.id.idFrontLayout)
    protected View idFrontLayout;

    @BindView(R.id.idBackLayout)
    protected View idBackLayout;

    @BindView(R.id.nextButton)
    protected Button nextButton;

    @BindView(R.id.nameTextView)
    protected TextView nameTextView;

    @BindView(R.id.idNoTextView)
    protected TextView idNoTextView;

    @BindView(R.id.sexTextView)
    protected TextView sexTextView;

    @BindView(R.id.nationTextView)
    protected TextView nationTextView;

    @BindView(R.id.birthdayTextView)
    protected TextView birthdayTextView;

    @BindView(R.id.addressTextView)
    protected TextView addressTextView;

    @BindView(R.id.idIssuerTextView)
    protected TextView idIssuerTextView;

    @BindView(R.id.idDateTextView)
    protected TextView idDateTextView;


    private Constants.ScanType scanType = Constants.ScanType.IdCardFront;

    private List<InfoEntry.DataBean> loadedBankNames;
    private int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra(Constants.ExtraScanType, 0);
        tag =getIntent().getIntExtra(Constants.TAG, 1);
        scanType = Constants.ScanType.values()[type];
        setContentView(R.layout.activity_ocr_info);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        setupLayout();
        setupImageView();
        setupInfo();
        if (isAddBankCard()) {
            nextButton.setText(R.string.confirm);
        } else {
            nextButton.setText(R.string.next);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CharSequence toolbarTitle() {
        if (isScanIdFront()) {
            return getString(R.string.id_card_front_info);
        } else if (isScanIdBack()) {
            return getString(R.string.id_card_back_info);
        } else if (isScanBankCard() || isAddBankCard()) {
            return getString(R.string.bank_card_info);
        } else {
            return null;
        }
    }
//
//    @Subscribe
//    public void onBackToMainEvent(BackToMainEvent event) {
//        finish();
//    }

    private boolean isScanIdFront() {
        return scanType == Constants.ScanType.IdCardFront;
    }

    private boolean isScanIdBack() {
        return scanType == Constants.ScanType.IdCardBack;
    }

    private boolean isScanBankCard() {
        return scanType == Constants.ScanType.BankCard;
    }

    private boolean isAddBankCard() {
        return scanType == Constants.ScanType.AddBankCard;
    }

    private void setupLayout() {
        idFrontLayout.setVisibility(isScanIdFront() ? View.VISIBLE : View.GONE);
        idBackLayout.setVisibility(isScanIdBack() ? View.VISIBLE : View.GONE);
    }

    private void setupImageView() {
        File imageFile = null;
        if (isScanIdFront()) {
            imageFile = InfoEntryModel.shared().idFrontImage;
        } else if (isScanIdBack()) {
            imageFile = InfoEntryModel.shared().idBackImage;
        } else {
            imageFile = InfoEntryModel.shared().bankImage;
        }

        if (imageFile != null) {
            Glide.with(this).load(imageFile).into(imageView);
        }
    }

    private void setupInfo() {
        if (isScanIdFront()) {
            setupFrontInfo();
        } else if (isScanIdBack()) {
            setupBackInfo();
        }
    }

    private void setupFrontInfo() {
        ImageCardInfoModel info = InfoEntryModel.shared().idFrontInfo;
        if (info == null) {
            return;
        }
        nameTextView.setText(info.getData().getTitle());
        idNoTextView.setText(info.getData().getIdNumber());
        sexTextView.setText(info.getData().getSex());
        nationTextView.setText(info.getData().getNation());
        birthdayTextView.setText(info.getData().getDateOfBirth());
        addressTextView.setText(info.getData().getAddress());
    }

    private void setupBackInfo() {
        ImageCardInfoModel info = InfoEntryModel.shared().idBackInfo;
        if (info == null) {
            return;
        }
        idIssuerTextView.setText(info.getData().getIssue());
        idDateTextView.setText(info.getData().getIdCardStartdate());
    }


    private void updateTextView(TextView textView, String text, @StringRes int stringRes) {
        if (StringUtils.isEmpty(text)) {
            textView.setText(stringRes);
            textView.setTextColor(getResources().getColor(R.color.text_dark_gray));
        } else {
            textView.setText(text);
            textView.setTextColor(getResources().getColor(R.color.text_dark_gray));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rescan_exit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_rescan:
                rescanInfo();
                break;
            case R.id.menu_item_cancel:
                showCancelRegisterAlert();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void showCancelRegisterAlert() {
        showAlertDialog(R.drawable.icon_info, null, getString(R.string.cancel_register_tip), getString(R.string.exit), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                finish();
//                EventBus.getDefault().post(new BackToMainEvent());
            }
        }, getString(R.string.cancel), null, null);
    }


    @OnClick(R.id.nextButton)
    protected void nextButtonClicked(View view) {
        if (preventDoubleClick()) {
            return;
        }
        if (!isInfoOK()) {
            showAlertDialog(R.drawable.icon_warning, getString(R.string.ocr_info_invalid_tip));
            return;
        }
        infoOKClickNext();
    }

    private boolean isInfoOK() {
        if (isScanIdFront()) {
            return InfoEntryModel.shared().isIdFrontInfoOK();
        } else if (isScanIdBack()) {
            return InfoEntryModel.shared().isIdBackInfoOK();
        } else if (isScanBankCard() || isAddBankCard()) {
            return InfoEntryModel.shared().isBankInfoOK();
        }

        return false;
    }

    private void infoOKClickNext() {
        if (isScanIdFront()) {
            MyUiUtils.showFaceDetectActivity(this, true, true,tag);
            finish();
        } else if (isScanIdBack()) {
            Intent intent = new Intent(this, SupplementMessageActivity.class);
            intent.putExtra(Constants.TAG, tag);//1，在职人员 ，2，访客，
            startActivity(intent);
            finish();
//            showNeedBankCardAlert();
        } else if (isAddBankCard()) {
//            updateBankCard();
        } else {
            showAdditionalInfoActivity();
        }
    }

    private void rescanInfo() {
        MyUiUtils.showCardCameraActivity(this, scanType,tag);
    }

    private void showAdditionalInfoActivity() {
//        startActivity(new Intent(this, InfoEntryActivity.class));
    }

    @OnClick(R.id.idNoInfo)
    protected void changeIdNo() {
        if (preventDoubleClick()) {
            return;
        }
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.id_no)
                .input(getString(R.string.id_no), InfoEntryModel.shared().idFrontInfo.getData().getIdNumber(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();
                        if (str.length() == 18 || str.length() == 15) {
                            InfoEntryModel.shared().idFrontInfo.getData().setIdNumber(str);
                            setupFrontInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_CLASS_TEXT)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

    @OnClick(R.id.nameInfo)
    protected void changeName() {
        if (preventDoubleClick()) {
            return;
        }

        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.name)
                .input(getString(R.string.name), InfoEntryModel.shared().idFrontInfo.getData().getTitle(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();

                        if (0 < str.length()) {
                            InfoEntryModel.shared().idFrontInfo.getData().setTitle(str);
                            setupFrontInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

    @OnClick(R.id.sexInfo)
    protected void changeSex() {
        if (preventDoubleClick()) {
            return;
        }

        int selectedIndex = StringUtils.equals(InfoEntryModel.shared().idFrontInfo.getData().getSex(), "男") ? 0 : 1;
        MyUiUtils.showOptionPicker(this, getResources().getStringArray(R.array.sex), selectedIndex, new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                InfoEntryModel.shared().idFrontInfo.getData().setSex(item);
                setupFrontInfo();
            }
        });
    }

    @OnClick(R.id.nationInfo)
    protected void changeNation() {
        if (preventDoubleClick()) {
            return;
        }

        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.nation)
                .input(getString(R.string.nation), InfoEntryModel.shared().idFrontInfo.getData().getNation(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();
                        if (0 < str.length()) {
                            InfoEntryModel.shared().idFrontInfo.getData().setNation(str);
                            setupFrontInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_CLASS_TEXT)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

    @OnClick(R.id.birthdayInfo)
    protected void changeBirthday() {
        if (preventDoubleClick()) {
            return;
        }

        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.birthday)
                .input(getString(R.string.birthday), InfoEntryModel.shared().idFrontInfo.getData().getDateOfBirth(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();
                        if (10 == str.length()) {
                            InfoEntryModel.shared().idFrontInfo.getData().setDateOfBirth(str);
                            setupFrontInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_DATETIME_VARIATION_NORMAL)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

    @OnClick(R.id.addressInfo)
    protected void changeAddress() {
        if (preventDoubleClick()) {
            return;
        }

        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.live_address)
                .input(getString(R.string.live_address), InfoEntryModel.shared().idFrontInfo.getData().getAddress(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();
                        if (0 < str.length()) {
                            InfoEntryModel.shared().idFrontInfo.getData().setAddress(str);
                            setupFrontInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_CLASS_TEXT)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

    @OnClick(R.id.issuerInfo)
    protected void changeIssuer() {
        if (preventDoubleClick()) {
            return;
        }
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.id_issuer)
                .input(getString(R.string.id_issuer), InfoEntryModel.shared().idBackInfo.getData().getIssue(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();
                        if (0 < str.length()) {
                            InfoEntryModel.shared().idBackInfo.getData().setIssue(str);
                            setupBackInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_CLASS_TEXT)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

    @OnClick(R.id.idDateInfo)
    protected void changeIdDate() {
        if (preventDoubleClick()) {
            return;
        }

        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.icon_info)
                .title(R.string.id_valid_date)
                .input(getString(R.string.id_valid_date), InfoEntryModel.shared().idBackInfo.getData().getIdCardStartdate(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String str = input.toString();
                        if (str.length() == 21) {
                            InfoEntryModel.shared().idBackInfo.getData().setIdCardStartdate(str);
                            setupBackInfo();
                        } else {
                            ToastUtil.showShort(R.string.input_invalid);
                        }
                    }
                })
                .inputType(InputType.TYPE_DATETIME_VARIATION_NORMAL)
                .negativeText(R.string.cancel)
                .positiveText(R.string.confirm)
                .show();
    }

}
