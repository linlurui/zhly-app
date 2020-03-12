package com.yizhitong.wisdombuilding.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yizhitong.wisdombuilding.Constants;

public class CommonActivity extends BaseActivity {
    private static final String EXA_NAME = "name";
    private static final String EXA_ARGS = "args";
    private static String title;
    /**
     * 启动公用的Activity
     *
     * @param context  上下文对象
     * @param fragment Fragment
     * @param args     Fragment参数
     */
    public static void start(Context context, Class<?> fragment, Bundle args) {
        context.startActivity(configIntent(context, fragment, args));
    }

    public static Intent configIntent(Context context, Class<?> fragment, Bundle args) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(EXA_NAME, fragment.getName());
        intent.putExtra(EXA_ARGS, args);
        title= args.getString(Constants.TITLE);
        return intent;
    }

    @Override
    protected CharSequence toolbarTitle() {
        return title;
    }

    @Override
    protected void showLoadingDialog() {
    }

}
