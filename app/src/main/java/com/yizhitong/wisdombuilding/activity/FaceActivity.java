package com.yizhitong.wisdombuilding.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.baidu.aip.ImageFrame;
import com.baidu.aip.face.CameraImageSource;
import com.baidu.aip.face.DetectRegionProcessor;
import com.baidu.aip.face.FaceDetectManager;
import com.baidu.aip.face.FaceFilter;
import com.baidu.aip.face.PreviewView;
import com.baidu.aip.face.TexturePreviewView;
import com.baidu.aip.face.camera.ICameraControl;
import com.baidu.aip.face.camera.PermissionCallback;
import com.baidu.idl.facesdk.FaceInfo;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.api.ApiService;
import com.yizhitong.wisdombuilding.api.BaseApiServer;
import com.yizhitong.wisdombuilding.manager.SessionManager;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.ImageCardInfoModel;
import com.yizhitong.wisdombuilding.modle.InfoEntryModel;
import com.yizhitong.wisdombuilding.utils.BrightnessTools;
import com.yizhitong.wisdombuilding.utils.MyUiUtils;
import com.yizhitong.wisdombuilding.utils.MyUtils;
import com.yizhitong.wisdombuilding.utils.ToastUtil;


import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;

public class FaceActivity extends BaseActivity {
    Toast toast = null;
    @BindView(R.id.preview_view)
    protected TexturePreviewView previewView;

    @BindView(R.id.texture_view)
    protected TextureView textureView;

    @BindView(R.id.scanTitle)
    protected TextView scanTitle;

    private static final String CameraFailedAction = "camera_failed";

    private boolean mDetectStopped = false;
    private FaceDetectManager faceDetectManager;
    private CameraImageSource cameraImageSource;
    private DetectRegionProcessor cropProcessor = new DetectRegionProcessor();
    private int mScreenW;
    private int mScreenH;
    private Paint paint = new Paint();
    private Handler mHandler = new Handler();
    private int mRound = 2;

    private boolean useFrontCamera = true;
    private Date lastAttendTime = null;
    private boolean isChangeFace = false;
    private boolean isFaceOfIdcard = false;
    private int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁用横屏
        useFrontCamera = getIntent().getBooleanExtra(Constants.ExtraFrontCamera, true);
        faceDetectManager = new FaceDetectManager(this);

        Intent intent = getIntent();
        tag = intent.getIntExtra(Constants.TAG,1);
        isFaceOfIdcard = intent.getBooleanExtra("isFaceOfIdcard", isFaceOfIdcard);
        Bundle dataBundle = this.getIntent().getExtras();
        if (dataBundle != null) {
            isChangeFace = dataBundle.getBoolean("isChangeFace");
        }

        if (isFaceOfIdcard) {
            scanTitle.setText(R.string.face_id_verify);
        } else {
            if (!isChangeFace) {
                scanTitle.setText(R.string.face_attend);
                setupSoundPool();
            } else {
                scanTitle.setText(R.string.change_face_id_verify);
            }
        }

//        scanTitle.setText(R.string.face_attend);

        initScreen();
        initView();
        //setupSoundPool();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startDetect();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
        }

        if (toast != null) {
            toast.cancel();
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (CameraFailedAction.equals(action)) {
                faceDetectManager.stop();
                faceDetectManager.start();
            }
        }
    };

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @OnClick(R.id.cancelButton)
    protected void cancelButtonClicked(View view) {
        finish();
    }

    @OnClick(R.id.cameraButton)
    protected void cameraButtonClicked(View view) {
        switchCamera(!useFrontCamera);
    }

    /**
     * 获取屏幕参数
     */
    private void initScreen() {
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mScreenW = outMetrics.widthPixels;
        mScreenH = outMetrics.heightPixels;
        mRound = getResources().getDimensionPixelSize(R.dimen.face_round);
    }

    /**
     * 初始化view
     */
    private void initView() {
        textureView.setOpaque(false);
        // 不需要屏幕自动变黑。
        textureView.setKeepScreenOn(true);

        cameraImageSource = new CameraImageSource(this);
        cameraImageSource.setPreviewView(previewView);

        faceDetectManager.setImageSource(cameraImageSource);
        faceDetectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(final int retCode, FaceInfo[] infos, ImageFrame frame) {
                if (infos == null || infos.length == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clearFaceRect();
                        }
                    });
                }
            }
        });
        faceDetectManager.setOnTrackListener(new FaceFilter.OnTrackListener() {
            @Override
            public void onTrack(final FaceFilter.TrackedModel trackedModel) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showFrame(trackedModel);
                        verifyFaceTrackModel(trackedModel);
//                        witnessComparison(trackedModel);
                    }
                });

            }
        });

        cameraImageSource.getCameraControl().setPermissionCallback(new PermissionCallback() {
            @Override
            public boolean onRequestPermission() {
                ActivityCompat.requestPermissions(FaceActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 100);
                return true;
            }
        });


        ICameraControl control = cameraImageSource.getCameraControl();
        control.setPreviewView(previewView);
        // 设置检测裁剪处理器
        faceDetectManager.addPreProcessor(cropProcessor);

        int orientation = getResources().getConfiguration().orientation;
        boolean isPortrait = (orientation == Configuration.ORIENTATION_PORTRAIT);

        if (isPortrait) {
            previewView.setScaleType(PreviewView.ScaleType.FIT_WIDTH);
        } else {
            previewView.setScaleType(PreviewView.ScaleType.FIT_HEIGHT);
        }

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        cameraImageSource.getCameraControl().setDisplayOrientation(rotation);
        switchCamera(useFrontCamera);

        initBrightness();
        initPaint();
    }

    /**
     * 设置相机亮度，不够200自动调整亮度到200
     */
    private void initBrightness() {
        int brightness = BrightnessTools.getScreenBrightness(this);
        if (brightness < 200) {
            BrightnessTools.setBrightness(this, 200);
        }
    }

    /**
     * 启动人脸检测
     */
    private void startDetect() {
        RectF newDetectedRect = new RectF(0, 0, mScreenW, mScreenH);
        cropProcessor.setDetectedRect(newDetectedRect);
        faceDetectManager.start();
        mDetectStopped = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        faceDetectManager.stop();
        mDetectStopped = true;
        if (toast != null) {
            toast.cancel();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(CameraFailedAction);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(broadcastReceiver, intentFilter);

        if (mDetectStopped) {
            faceDetectManager.start();
            mDetectStopped = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void clearFaceRect() {
        Canvas canvas = textureView.lockCanvas();
        if (canvas == null) {
            return;
        }
        // 清空canvas
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        textureView.unlockCanvasAndPost(canvas);
    }

    /**
     * 绘制人脸框。
     *
     * @param model 追踪到的人脸
     */
    private void showFrame(FaceFilter.TrackedModel model) {
        Canvas canvas = textureView.lockCanvas();
        if (canvas == null) {
            return;
        }
        // 清空canvas
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if (model != null) {
            FaceInfo info = model.getInfo();
            model.getImageFrame().retain();
            RectF rectCenter = new RectF(info.mCenter_x - 2 - info.mWidth * 3 / 5,
                    info.mCenter_y - 2 - info.mWidth * 4 / 5,
                    info.mCenter_x + 2 + info.mWidth * 3 / 5,
                    info.mCenter_y + 2 + info.mWidth * 3 / 5);
            previewView.mapFromOriginalRect(rectCenter);
            // 绘制框
            paint.setStrokeWidth(mRound);
            paint.setAntiAlias(true);

            if (model.meetCriteria()) {
                // 符合检测要求，绘制绿框
                paint.setColor(Color.GREEN);
            } else {
                paint.setColor(Color.TRANSPARENT);
            }

            canvas.drawRect(rectCenter, paint);
        }
        textureView.unlockCanvasAndPost(canvas);
    }

    private void switchCamera(boolean useFront) {
        useFrontCamera = useFront;

        if (useFront) {
            cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_FRONT);
            previewView.setMirrored(true);
        } else {
            cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_BACK);
            previewView.setMirrored(false);
        }

        if (!mDetectStopped) {
            faceDetectManager.stop();
            faceDetectManager.start();
        }
    }

    private boolean isVerifying = false;

    private void verifyFaceTrackModel(FaceFilter.TrackedModel model) {
        if (model == null || isVerifying) {
            return;
        }
        final Bitmap face = model.cropFace();
        if (face == null) {
            return;
        }
        isVerifying = true;

        File imageFile = MyUtils.newBitmapFile(this);
        ImageUtils.save(face, imageFile, Bitmap.CompressFormat.JPEG);

        InfoEntryModel.shared().faceImage = imageFile;
        MultipartBody.Part part = MyUtils.imageMultipartFile(imageFile);
        ApiService apiService = BaseApiServer.createService();
        Call<ApiResp<ImageCardInfoModel>> call = null;

        if (isFaceOfIdcard) {//人证对比
            String idCardUrl = InfoEntryModel.shared().idFrontInfo.getData().getUrl();
            call = apiService.queryWitnessComparison(part, idCardUrl);

        }
        //      else {
//            //isChangeFace为true，修改人脸头像
//            if (isChangeFace) {
//                int id = InfoEntryModel.shared().workerId;
//                call = apiService.changeFace(part, id);
//            } else {
//
//                Bundle bd = this.getIntent().getExtras();
//                String commute = bd.getString("commute");
//                int projectId = SPUtils.getInstance().getInt("projectId");
//                String deviceSn = MyUtils.getDeviceUUID(this);
//                call = apiService.attendanceRecord(part, projectId, commute, "Android", deviceSn);
//                long interval = SessionManager.shared().getAttendTimeInterval() * 1000;
//                Date now = new Date();
//                if (lastAttendTime != null && (now.getTime() - lastAttendTime.getTime()) < interval) {
//                    delayFaceAttend(attendDelay);
//                    return;
//                }
//            }

//        }

        startApiCall(call, new ApiResultHandler<ImageCardInfoModel>() {
            @Override
            boolean showLoading() {
                return false;
            }
            @Override
            void apiSucceed(ImageCardInfoModel imageResource) {
                if (isFaceOfIdcard) {
                    if (imageResource.getCode() == 0) {//人证对比
                        if (imageResource.getData() != null) {
                            boolean result = Boolean.valueOf(imageResource.getData().getResult());
                            if (result == true) {
                                faceRegisterSucceed(imageResource);
                            } else {
                                verifyOrAttendFailed(imageResource.getMsg());
                            }
                        }
                    } else {
                        ToastUtil.showShort(imageResource.getMsg());
                    }
                }
//                else {
//                    if (!isChangeFace) {//人脸考勤
//                        if (imageResource.getCode() == 0) {
//                            attendSucceed(imageResource.getMsg());
//                            lastAttendTime = new Date();
//                        } else {
//                            verifyOrAttendFailed(imageResource.getMsg());
//                            lastAttendTime = null;
//                        }
//                    } else {//修改人脸
//                        if (imageResource.getCode() == 0) {
//                            ToastUtil.showShort(imageResource.getMsg());
//                            faceDetectManager.stop();
//                            mDetectStopped = true;
//                            finish();
//                        } else {
//                            Toasty.error(FaceActivity.this,imageResource.getMsg()).show();
//                            verifyOrAttendFailed(imageResource.getMsg());
//                        }
//                    }
//                }
            }

            @Override
            void apiFailed() {
                verifyOrAttendFailed(getString(R.string.net_unknown_error));
            }
        });
    }

    private void faceRegisterSucceed(ImageCardInfoModel faceResource) {
        InfoEntryModel.shared().faceResource = faceResource;
        faceDetectManager.stop();
        mDetectStopped = true;

        showAlertDialog(R.drawable.icon_success, null, getString(R.string.face_id_card_verify_succeed), getString(R.string.next), null, null, null, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MyUiUtils.showCardCameraActivity(FaceActivity.this, Constants.ScanType.IdCardBack,tag);
                finish();
            }
        });
    }

//    private void attendSucceed(String uname) {
//        String message = getString(R.string.face_attend_succeed_score_format);
//        //考勤成功后震动两次
//        TipHelper.Vibrate(this, new long[]{300, 500, 300, 500,}, false);
//        ToastMessage(true, uname);
//        playAttendSound(true);
//        delayFaceAttend(attendDelay);
//    }

    private void delayFaceAttend(long time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isVerifying = false;
            }
        }, time);
    }

    private static final long registerDelay = 2000;
    private static final long attendDelay = 5000;

    private void verifyOrAttendFailed(String msg) {
        long delay;
        if (isFaceOfIdcard) {
            delay = registerDelay;
            ToastMessage(false, getString(R.string.face_id_card_not_match_tip));
//            ToastMessage(false, msg);
        } else {
            delay = registerDelay;
            if (isChangeFace) {
                ToastMessage(false, getString(R.string.face_id_face_not_match_tip));
//                ToastMessage(false, msg);
            } else {
                ToastMessage(false, msg);
                playAttendSound(false);
            }

        }

        delayFaceAttend(delay);
    }

    // Play sound
    private SoundPool soundPool;
    private int succeedSoundId;
    private int failedSoundId;
    private boolean loadedSucceed = false;
    private boolean loadedFailed = false;

    private void setupSoundPool() {
        // Set the hardware buttons to control the music
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (sampleId == succeedSoundId) {
                    loadedSucceed = true;
                } else if (sampleId == failedSoundId) {
                    loadedFailed = true;
                }
            }
        });
        succeedSoundId = soundPool.load(this, R.raw.face_succeed, 1);
        failedSoundId = soundPool.load(this, R.raw.face_failed, 1);
    }

    private void playAttendSound(boolean succeed) {
        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        // Is the sound loaded already?
        if (succeed) {
            if (loadedSucceed) {
                soundPool.play(succeedSoundId, volume, volume, 1, 0, 1f);
            }
        } else {
            if (loadedFailed) {
                soundPool.play(failedSoundId, volume, volume, 1, 0, 1f);
            }
        }
    }

    /**
     * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     */
    private void ToastMessage(boolean flag, String messages) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/dialog_create_picture/下的xml布局文件，并且实例化
        LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.toast_style, null); //加載layout下的布局
        //view.getBackground().setAlpha(1);
        TextView text = view.findViewById(R.id.tvTextToast);
        ImageView icon = view.findViewById(R.id.tvImageToast);
        if (!flag) {
            icon.setImageResource(R.drawable.icon_error);
            text.setTextColor(getResources().getColor(R.color.text_red));
        } else {
            icon.setImageResource(R.drawable.icon_success);
            text.setTextColor(getResources().getColor(R.color.text_dark_gray));
        }
        text.setText(messages); //toast内容
        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 50);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

}
