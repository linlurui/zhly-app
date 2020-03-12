package com.yizhitong.wisdombuilding.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.android.cameraview.CameraImpl;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.ImageCardInfoModel;
import com.yizhitong.wisdombuilding.modle.InfoEntryModel;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.MyUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.pqpo.smartcameralib.MaskView;
import me.pqpo.smartcameralib.SmartCameraView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CardCameraActivity extends BaseActivity {
    @BindView(R.id.cameraView)
    protected SmartCameraView cameraView;
    @BindView(R.id.scanTitle)
    protected TextView scanTitle;

    private Constants.ScanType scanType = Constants.ScanType.IdCardFront;
    private File currentImageFile;
    private int  tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_camera);
        ButterKnife.bind(this);
        int type = getIntent().getIntExtra(Constants.ExtraScanType, 0);
        tag = getIntent().getIntExtra(Constants.TAG, 1);
        scanType = Constants.ScanType.values()[type];
        setupTitle();
        setupMaskView();
        setupCameraCallback();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraAndScan();
    }

    @Override
    protected void onPause() {
        stopCameraAndScan();
        super.onPause();
    }

    private void startCameraAndScan() {
        cameraView.start();
        cameraView.startScan();

        MaskView maskView = (MaskView) cameraView.getMaskView();
        maskView.setShowScanLine(true);
    }

    private void stopCameraAndScan() {
        cameraView.stopScan();
        cameraView.stop();

        MaskView maskView = (MaskView) cameraView.getMaskView();
        maskView.setShowScanLine(false);
    }

    private void setupTitle() {
        if (isScanIdFront()) {
            scanTitle.setText(getString(R.string.take_id_card_front));
        } else if (isScanIdBack()) {
            scanTitle.setText(getString(R.string.take_id_card_back));
        } else if (isScanBankCard()||isAddBankCard()) {
            scanTitle.setText(getString(R.string.take_bank_card_front));
        } else {
            scanTitle.setText(getString(R.string.take_card));
        }
    }

    private boolean isScanIdFront() {
        return scanType == Constants.ScanType.IdCardFront;
    }

    private boolean isScanIdBack() {
        return scanType == Constants.ScanType.IdCardBack;
    }

    private boolean isScanBankCard() {
        return scanType == Constants.ScanType.BankCard ;
    }
    private boolean isAddBankCard() {
        return scanType == Constants.ScanType.AddBankCard;
    }
    @OnClick(R.id.cancelButton)
    protected void cancelButtonClicked(View view) {
        finish();
    }

    private void setupMaskView() {
        final MaskView maskView = (MaskView) cameraView.getMaskView();
        maskView.setMaskLineColor(Color.GREEN);
        maskView.setScanLineGradient(0xff2ecc71, 0x0027ae60);
        maskView.setShowScanLine(true);
        maskView.setScanSpeed(6);
        maskView.setMaskLineWidth(2);
        maskView.setScanGradientSpread(10);
        cameraView.post(new Runnable() {
            @Override
            public void run() {
                int height = cameraView.getHeight();
                maskView.setMaskSize((int) (height * 0.7f / 0.63f), (int) (height * 0.7f));
                maskView.setMaskOffset(0, 30);
            }
        });
        cameraView.setMaskView(maskView);
    }

    private void setupCameraCallback() {
        cameraView.addCallback(new CameraImpl.Callback() {
            @Override
            public void onPictureTaken(CameraImpl camera, byte[] data) {
                super.onPictureTaken(camera, data);
                cameraView.cropJpegImage(data, new SmartCameraView.CropCallback() {
                    @Override
                    public void onCropped(Bitmap bitmap) {
                        if (bitmap != null) {
                            cropCardSucceed(bitmap);
                            stopCameraAndScan();
                        } else {
                            startCameraAndScan();
                        }
                    }
                });
            }
        });
    }

    private void cropCardSucceed(Bitmap bitmap) {
        File originalFile = MyUtils.newBitmapFile(this);
        ImageUtils.save(bitmap, originalFile, Bitmap.CompressFormat.JPEG);
        compressImageFile(originalFile);
    }

    private void compressImageFile(final File imageFile) {
        Luban.with(this)
                .load(imageFile)
                .ignoreBy(0)
                .setTargetDir(imageFile.getParent())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        FileUtils.deleteFile(imageFile);
                        if (isScanIdFront()) {
                            uploadImageFile(file, "face");
                        } else if (isScanIdBack()) {
                            uploadImageFile(file, "back");
                        } else {
                            uploadImageFile(file, "card");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
//                        uploadImageFile(imageFile);
                        if (isScanIdFront()) {
                            uploadImageFile(imageFile, "face");
                        } else if (isScanIdBack()) {
                            uploadImageFile(imageFile, "back");
                        } else {
                            uploadImageFile(imageFile, "card");
                        }
                    }
                }).launch();
    }

    private void uploadImageFile(File imageFile, String scanType) {
        currentImageFile = imageFile;
        showLoadingDialog(getString(R.string.ocr_loading));

        MultipartBody.Part part = MyUtils.imageMultipartFile(imageFile);
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<ImageCardInfoModel>> call = apiService.uploadCardImage(part, scanType);
        startApiCall(call, new ApiResultHandler<ImageCardInfoModel>() {
            @Override
            boolean showLoading() {
                return false;
            }

            @Override
            void apiSucceed(ImageCardInfoModel imageCardInfoModel) {
                dismissDialogAndRestart();
                if (imageCardInfoModel.getCode() == 0) {
                    if (StringUtils.isEmpty(imageCardInfoModel.getData().getUrl())) {
                        dismissDialogAndRestart();
                    } else {
                        ocrSucceed(imageCardInfoModel);
                    }
                } else {
                    ToastUtil.showShort(imageCardInfoModel.getMsg());
                }
            }

            @Override
            void apiFailed() {
                dismissDialogAndRestart();
            }
        });
    }

    private void dismissDialogAndRestart() {
        dismissLoadingDialog();
        startCameraAndScan();
    }

    private void ocrSucceed(ImageCardInfoModel cardOcrInfo) {
        dismissLoadingDialog();
        if (isScanIdFront()) {
            InfoEntryModel.shared().idFrontImage = currentImageFile;
            InfoEntryModel.shared().idFrontInfo = cardOcrInfo;
            InfoEntryModel.shared().idFrontHeader = cardOcrInfo;
            MyUiUtils.showOcrInfoActivity(this, Constants.ScanType.IdCardFront,tag);

        } else if (isScanIdBack()) {
            InfoEntryModel.shared().idBackImage = currentImageFile;
            InfoEntryModel.shared().idBackInfo = cardOcrInfo;
            MyUiUtils.showOcrInfoActivity(this, Constants.ScanType.IdCardBack,tag);

        } else if (isScanBankCard()) {
            InfoEntryModel.shared().bankImage = currentImageFile;
            InfoEntryModel.shared().bankInfo = cardOcrInfo;
            MyUiUtils.showOcrInfoActivity(this, Constants.ScanType.BankCard,tag);

        }
//        else if (isAddBankCard()) {
//            int id = getIntent().getIntExtra(Constants.ExtraId,0);
//            InfoEntryModel.shared().bankImage = currentImageFile;
//            InfoEntryModel.shared().bankInfo = cardOcrInfo;
//            MyUiUtils.showOcrInfoActivity(this, Constants.ScanType.AddBankCard,id);
//
//        }
        finish();
    }
}
