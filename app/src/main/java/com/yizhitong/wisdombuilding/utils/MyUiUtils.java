package com.yizhitong.wisdombuilding.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.blankj.utilcode.util.ImageUtils;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.activity.CardCameraActivity;
import com.yizhitong.wisdombuilding.activity.FaceActivity;
import com.yizhitong.wisdombuilding.activity.InfoTabActivity;
import com.yizhitong.wisdombuilding.activity.LoginActivity;
import com.yizhitong.wisdombuilding.activity.OcrInfoActivity;
import com.yizhitong.wisdombuilding.activity.PersonnelTabActivity;
import com.yizhitong.wisdombuilding.manager.SessionManager;


import java.util.Calendar;
import java.util.List;

//import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
//import cn.qqtheme.framework.picker.SinglePicker;
//import cn.qqtheme.framework.widget.WheelView;

public class MyUiUtils {


    /**
     * 计算两个手指头之间的中心点的位置
     * x = (x1+x2)/2;
     * y = (y1+y2)/2;
     *
     * @param event 触摸事件
     * @return 返回中心点的坐标
     */
    public static PointF midPoint(MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    public static float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//两点间距离公式
    }

    /**
     * public static void showYearMonthDayPicker(Activity activity, String selectedDate, DatePicker.OnYearMonthDayPickListener listener) {
     * Calendar startCalendar = Calendar.getInstance();
     * int startYear = startCalendar.get(Calendar.YEAR);
     * <p>
     * Calendar selectedCalendar = Calendar.getInstance();
     * selectedCalendar.setTime(MyUtils.dateFromString(selectedDate));
     * int selectedYear = selectedCalendar.get(Calendar.YEAR);
     * int selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1;
     * int selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
     * <p>
     * int minYear = Math.min(startYear, selectedYear);
     * int maxYear = Math.max(startYear, selectedYear);
     * <p>
     * final DatePicker picker = new DatePicker(activity);
     * picker.setCanceledOnTouchOutside(true);
     * picker.setUseWeight(true);
     * picker.setTopPadding(20);
     * picker.setRangeStart(minYear - 5, 1, 1);
     * picker.setRangeEnd(maxYear + 5, 12, 31);
     * picker.setSelectedItem(selectedYear, selectedMonth, selectedDay);
     * picker.setResetWhileWheel(false);
     * picker.setOnDatePickListener(listener);
     * picker.setOnWheelListener(new DatePicker.OnWheelListener() {
     *
     * @Override public void onYearWheeled(int index, String year) {
     * picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
     * }
     * @Override public void onMonthWheeled(int index, String month) {
     * picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
     * }
     * @Override public void onDayWheeled(int index, String day) {
     * picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
     * }
     * });
     * picker.show();
     * }
     * <p>
     * public static <T> void showPicker(Activity activity, List<T> data, int selectedIndex, SinglePicker.OnItemPickListener<T> onItemPickListener) {
     * if (data == null || data.size() == 0) {
     * return;
     * }
     * if (selectedIndex < 0 || data.size() <= selectedIndex) {
     * selectedIndex = 0;
     * }
     * SinglePicker<T> picker = new SinglePicker<T>(activity, data);
     * picker.setCanceledOnTouchOutside(true);
     * picker.setSelectedIndex(selectedIndex);
     * picker.setCycleDisable(true);
     * if (onItemPickListener != null) {
     * picker.setOnItemPickListener(onItemPickListener);
     * }
     * picker.show();
     * }
     * <p>
     * public static void showPickerData(Activity activity, List<String> items) {
     * OptionPicker picker = new OptionPicker(activity, items);
     * picker.setCanceledOnTouchOutside(false);
     * picker.setDividerRatio(WheelView.DividerConfig.FILL);
     * picker.setShadowColor(Color.RED, 60);
     * picker.setSelectedIndex(1);
     * picker.setCycleDisable(true);
     * picker.setTextSize(14);
     * <p>
     * picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
     * @Override public void onOptionPicked(int index, String item) {
     * <p>
     * <p>
     * }
     * });
     * picker.show();
     * }
     * <p>
     * public static void showCardCameraActivity(Context context, MyConstants.ScanType scanType, int id) {
     * Intent intent = new Intent(context, CardCameraActivity.class);
     * intent.putExtra(MyConstants.ExtraScanType, scanType.ordinal());
     * intent.putExtra(MyConstants.ExtraId, id);
     * context.startActivity(intent);
     * }
     * <p>
     * public static void changeFaceDetectActivity(Context context, boolean useFrontCamera, boolean isChangeFace) {
     * Intent intent = new Intent(context, FaceActivity.class);
     * Bundle bundle = new Bundle();
     * bundle.putBoolean("isChangeFace", isChangeFace);
     * intent.putExtras(bundle);
     * //        intent.putExtra("jsonFace",jsonFace);
     * intent.putExtra(MyConstants.ExtraFrontCamera, useFrontCamera);
     * context.startActivity(intent);
     * }
     * <p>
     * <p>
     * public static void showOcrInfoActivity(Context context, MyConstants.ScanType scanType, int id) {
     * Intent intent = new Intent(context, OcrInfoActivity.class);
     * intent.putExtra(MyConstants.ExtraScanType, scanType.ordinal());
     * intent.putExtra(MyConstants.ExtraId, id);
     * context.startActivity(intent);
     * }
     */

    public static void showOptionInfoPicker(Activity activity, List data, int selectedIndex, OptionPicker.OnOptionPickListener onOptionPickListener) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (selectedIndex < 0 || data.size() <= selectedIndex) {
            selectedIndex = 0;
        }

        OptionPicker picker = new OptionPicker(activity, data);
        picker.setCanceledOnTouchOutside(true);
        picker.setSelectedIndex(selectedIndex);
        picker.setCycleDisable(true);
        if (onOptionPickListener != null) {
            picker.setOnOptionPickListener(onOptionPickListener);
        }
        picker.show();
    }


    public static void showCardCameraActivity(Context context, Constants.ScanType scanType, int tag) {
        Intent intent = new Intent(context, CardCameraActivity.class);
        intent.putExtra(Constants.ExtraScanType, scanType.ordinal());
        intent.putExtra(Constants.TAG, tag);
        context.startActivity(intent);
    }

    public static void showOptionPicker(Activity activity, String[] data, int selectedIndex, OptionPicker.OnOptionPickListener onOptionPickListener) {
        if (data == null || data.length == 0) {
            return;
        }
        if (selectedIndex < 0 || data.length <= selectedIndex) {
            selectedIndex = 0;
        }

        OptionPicker picker = new OptionPicker(activity, data);
        picker.setCanceledOnTouchOutside(true);
        picker.setSelectedIndex(selectedIndex);
        picker.setCycleDisable(true);
        if (onOptionPickListener != null) {
            picker.setOnOptionPickListener(onOptionPickListener);
        }
        picker.show();
    }

    public static void showPersonnelTabActivity(Context context, int employeeId) {
        Intent intent = new Intent(context, PersonnelTabActivity.class);
        intent.putExtra(Constants.EMPLOYEEID, employeeId);
        context.startActivity(intent);
    }

    public static void showFaceDetectActivity(Context context, boolean useFrontCamera, boolean isFaceOfIdcard, int tag) {
        Intent intent = new Intent(context, FaceActivity.class);
        intent.putExtra("isFaceOfIdcard", isFaceOfIdcard);
        intent.putExtra(Constants.ExtraFrontCamera, useFrontCamera);
        intent.putExtra(Constants.TAG, tag);
        context.startActivity(intent);
    }

    public static void showOcrInfoActivity(Context context, Constants.ScanType scanType, int tag) {
        Intent intent = new Intent(context, OcrInfoActivity.class);
        intent.putExtra(Constants.ExtraScanType, scanType.ordinal());
        intent.putExtra(Constants.TAG, tag);
        context.startActivity(intent);
    }

    public static void showInfoTabActivity(Context context) {
        Intent intent = new Intent(context, InfoTabActivity.class);
        context.startActivity(intent);
    }

    public static void signOut(Activity activity) {
        SessionManager.shared().signOut();
        Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void scrollToBottom(ScrollView scrollView) {
        View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
        int sy = scrollView.getScrollY();
        int sh = scrollView.getHeight();
        int delta = bottom - (sy + sh);
        scrollView.smoothScrollBy(0, delta);
    }

    public static void hideNavKey(Context context) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = ((Activity) context).getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = ((Activity) context).getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void circularBitmap(ImageView imageView, Drawable drawable) {
        Bitmap bitmap = ImageUtils.drawable2Bitmap(drawable);
        Bitmap mbitmap = ImageUtils.toRoundCorner(bitmap, 20);
        imageView.setImageBitmap(mbitmap);
    }
}
