package com.yizhitong.wisdombuilding.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.interfaces.BindEventBus;
import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.utils.LogUtil;
import com.yizhitong.wisdombuilding.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class BaseFragment extends Fragment {

    /**
     * 公用的传参key
     */
    public static final String ARGS_ID = "id";
    /**
     * 由getContentView初始化，子类的业务View
     */
    protected View mContentView;
    protected Context mContext;

    private long lastClickTime = 0;
    protected boolean isVisible = false;// 对用户界面是否可见
    private int backCount;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private AppCompatActivity mActivity;
    private Unbinder unbinder;

    // loading dialog
    private MaterialDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果savedInstanceState不为空，那么可以获取在onSaveInstanceState方法中保存的值。
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);// 获取保存fragment前的可视状态
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (isHidden) {
                // 如果原来fragment是隐藏状态，那么就hide
                transaction.hide(this);
            } else {
                // 如果原来fragment是显示状态，那么就show
                transaction.show(this);
            }
            transaction.commit();
        }
        //判断是否需要注册
//        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
//            EventBus.getDefault().unregister(this);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_base, container, false);
        ViewGroup contentContainer = layout.findViewById(R.id.contentContainer);
        mContentView = getContentView();
        if (mContentView == null) {
            final int contentRes = getContentRes();
            if (contentRes > 0) {
                mContentView = View.inflate(mContext, contentRes, null);
            }
        }
        if (mContentView != null) {
            contentContainer.addView(mContentView, 0);
        }
        unbinder = ButterKnife.bind(this, layout);
        initViewOrData();
        return layout;
    }


    /**
     * 获取Fragment需要显示的View
     *
     * @return
     */
    protected View getContentView() {
        return null;
    }

    /**
     * 获取Fragment需要显示的View的资源文件
     *
     * @return
     */
    protected int getContentRes() {
        return 0;
    }

    /**
     * 处理特殊的初始化需求，无需求可不重写
     */
    protected void initViewOrData() {

    }

    /**
     * 调用该方法用来保存fragment当前的动态状态。
     * 可以用bundle对象保存一些值，然后可以在onCreate方法中获取保存的值。
     *
     * @param outState Bundle对象
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());// 保存当前fragment的可视状态
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backCount = mActivity.getSupportFragmentManager().getBackStackEntryCount();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }

    }

    protected boolean preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }


    protected void showLoadingDialog(String text) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }
        loadingDialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.loading_dialog, false)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();
        if (!StringUtils.isEmpty(text)) {
            View view = loadingDialog.getCustomView();
            TextView textView = view.findViewById(R.id.progressTextView);
            textView.setText(text);
        }
        try {
            loadingDialog.show();
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
        }
        // must set after showing the dialog
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected void showLoadingDialog() {
        showLoadingDialog(null);
    }

    public abstract class ApiResultHandler<T> {
        boolean showLoading() {
            return true;
        }

        boolean showError() {
            return true;
        }

        String defaultErrorMessage() {
            return null;
        }

        void apiFailed() {
        }

        protected abstract void apiSucceed(T t);
    }

    protected <T> void startApiCall(Call<ApiResp<T>> call, final BaseFragment.ApiResultHandler<T> handler) {
        final boolean showLoading = (handler != null && handler.showLoading());
        if (showLoading) {
            showLoadingDialog();
        }
        call.enqueue(new Callback<ApiResp<T>>() {
            @Override
            public void onResponse(Call<ApiResp<T>> call, Response<ApiResp<T>> response) {
                if (showLoading) {
                    dismissLoadingDialog();
                }

                if (handler == null) {
                    return;
                }

                ApiResp<T> theResp = response.body();
                if (response.isSuccessful() && theResp != null) {
                    handler.apiSucceed(theResp.getData());
                } else {
                    String message;
                    if (theResp != null && !StringUtils.isEmpty(theResp.getMessage())) {
                        message = theResp.getMessage();
                    } else {
                        message = getErrorResponseMessage(response.errorBody());
                    }

                    if (StringUtils.isEmpty(message)) {
                        message = handler.defaultErrorMessage();
                    }

                    apiError(handler, message);
                }
            }

            @Override
            public void onFailure(Call<ApiResp<T>> call, Throwable t) {
                if (showLoading) {
                    dismissLoadingDialog();
                }

                if (handler == null) {
                    return;
                }

                apiError(handler, handler.defaultErrorMessage());
            }

            private String getErrorResponseMessage(ResponseBody errorBody) {
                try {
                    String str = errorBody.string();
                    if (!StringUtils.isEmpty(str)) {
                        JSONObject obj = new JSONObject(str);
                        return obj.optString("message");
                    }
                } catch (Exception ex) {
                    // do nothing
                }
                return "";
            }

            private void apiError(final BaseFragment.ApiResultHandler<T> handler, String message) {
                if (handler.showError()) {
                    if (StringUtils.isEmpty(message)) {
                        message = getString(R.string.server_response_failure);
                    }
                    ToastUtil.showShort(message);
//                    showAlertDialog(R.drawable.icon_error, null, message, null, null, null, null, new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            handler.apiFailed();
//                        }
//                    });

                } else {
                    handler.apiFailed();
                }
            }

            protected MaterialDialog showAlertDialog(@DrawableRes int iconRes, CharSequence title, CharSequence message, CharSequence positive, MaterialDialog.SingleButtonCallback positiveCallback, CharSequence negative, MaterialDialog.SingleButtonCallback negativeCallback, MaterialDialog.OnDismissListener onDismissListener) {

                if (StringUtils.isEmpty(positive)) {
                    positive = getString(R.string.ok);
                }
                if (StringUtils.isEmpty(title)) {
                    title = getString(R.string.kind_tip);
                }
                if (message == null) {
                    message = "";
                }

                MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                        .title(title)
                        .content(message)
                        .positiveText(positive)
                        .positiveColor(getResources().getColor(R.color.app_blue))
                        .negativeColor(getResources().getColor(R.color.app_dark_gray));

                if (iconRes != 0) {
                    builder.iconRes(iconRes);
                }

                if (positiveCallback != null) {
                    builder.onPositive(positiveCallback);
                }
                if (negative != null) {
                    builder.negativeText(negative);
                }
                if (negativeCallback != null) {
                    builder.onNegative(negativeCallback);
                }
                if (onDismissListener != null) {
                    builder.dismissListener(onDismissListener);
                }

                try {
                    return builder.show();
                } catch (Exception ex) {
                    return builder.build();
                }
            }
        });
    }

}
