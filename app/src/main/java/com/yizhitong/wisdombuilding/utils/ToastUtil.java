package com.yizhitong.wisdombuilding.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtil {

    public static Context sContext = null;
    private static Toast sToast = null;

    /**
     * 短时间显示Toast
     *
     * @param message 信息
     */
    public static void showShort(CharSequence message) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.setGravity(Gravity.CENTER,0,0);
        sToast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param message 信息
     */
    public static void showShort(int message) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message 信息
     */
    public static void showLong(CharSequence message) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, message, Toast.LENGTH_LONG);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message 信息
     */
    public static void showLong(int message) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, message, Toast.LENGTH_LONG);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message  信息
     * @param duration 时长
     */
    public static void showDuration(CharSequence message, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, message, duration);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message  信息
     * @param duration 时长
     */
    public static void showDuration(int message, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, message, duration);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }
}
