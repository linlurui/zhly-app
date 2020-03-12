package com.yizhitong.wisdombuilding.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.yizhitong.wisdombuilding.R;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyUtils {
//    public static final String MobileRegex = "^1\\d{10}$";
//    public static final String DateFormat_8 = "yyyyMMdd";

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    /**
     * sp或者 dp 装换为 px
     */
    public static int dpToPx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dpValue * scale);
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 版本号比较
     * 0代表相等，1代表version1大于version2，-1代表version1小于version2
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }

    }


    //获取当前的日期
    public static String StringData(boolean flag) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }

        if (flag == true) {
            return "星期" + mWay;
        } else {
            return mMonth + "月" + mDay + "日";
        }
    }

    public static File newBitmapFile(Context context) {
        String timestamp = TimeUtils.date2String(new Date(), new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()));
        String filename = timestamp + ".jpg";

        File imageDir = appImagesDir(context);
        FileUtils.createOrExistsDir(imageDir);

        return new File(imageDir, filename);
    }

    public static File appImagesDir(Context context) {
        File base = context.getFilesDir();
        return new File(base, "app_images");
    }
    public static MultipartBody.Part imageMultipartFile(File imageFile) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        return MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
    }

    public static MultipartBody.Part imageMultipart(File imageFile) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), imageFile);
        return MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
    }


    /**

    public static void changeStatusBarTextColor(boolean isBlack, Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
    }

    public static String convertIdCardBirthDate(String dateString) {
        if (!StringUtils.isEmpty(dateString)) {
            String ymd = DateFormat_10;
            if (dateString.length() == 8) {
                ymd = DateFormat_8;
            }

            try {
                SimpleDateFormat format = new SimpleDateFormat(ymd, Locale.getDefault());
                Date date = format.parse(dateString);

                SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                return serverFormat.format(date);
            } catch (Exception ex) {
                // do nothing
            }
        }
        return dateString;
    }

    public static Date dateFromString(String string) {
        if (StringUtils.isEmpty(string)) {
            return new Date();
        }

        String formatString = null;
        int length = StringUtils.length(string);
        if (length == 8) {
            formatString = DateFormat_8;
        } else if (length == 10) {
            formatString = DateFormat_10;
        }

        if (StringUtils.isEmpty(formatString)) {
            return new Date();
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
            return format.parse(string);
        } catch (Exception ex) {
            // do nothing
        }

        return new Date();
    }

    public static String stripTimeFromDate(String dateTimeString) {
        if (StringUtils.isEmpty(dateTimeString)) {
            return "";
        }

        int index = dateTimeString.indexOf(" ");
        if (0 < index) {
            return dateTimeString.substring(0, index);
        }

        return dateTimeString;
    }

    public static boolean isValidMobile(String phone) {
        if (phone == null) {
            return false;
        }

        return phone.matches(MobileRegex);
    }


    public static RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    //根据传进来的Object对象来判断是String还是File类型的参数
    public static Map<String, RequestBody> addParameter(Map<String, RequestBody> params, String key, Object o) {
        RequestBody body = null;
        if (o instanceof String) {
            body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), (String) o);
        } else if (o instanceof File) {
            body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) o);
        }
        params.put(key, body);
        return params;
    }


    public static boolean isEmpty(String str) {
        if (str == null || str.equals("")||str.equals("null")) {
            return true;
        }
        return false;
    }
//    获取本机唯一识别码

//     @return 本机唯一识别码MD5值

    public static String getDeviceUUID(Context context) {
        String uuid = SPUtils.getInstance().getString("device_uuid");
        if (StringUtils.isEmpty(uuid)) {
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (deviceId.equals("9774d56d682e549c")) {
                Random random = new Random();
                deviceId = Integer.toHexString(random.nextInt())
                        + Integer.toHexString(random.nextInt())
                        + Integer.toHexString(random.nextInt());
            }
            SPUtils.getInstance().put("device_uuid", MD5Uutils.string2MD5(deviceId));
            return MD5Uutils.string2MD5(deviceId);
        }
        return uuid;
    }*/

     //获得独一无二的Pseudo Device ID
     public static String getUniquePseudoDeviceID() {
     String serial = null;
     String m_szDevIDShort = "35" +
     Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
     Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
     Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
     Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
     Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
     Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
     Build.USER.length() % 10; //13 位
     try {
     //API>=9 使用serial号
     serial = Build.class.getField("SERIAL").get(null).toString();
     } catch (Exception exception) {
     //serial需要一个初始化
     serial = "serial"; // 随便一个初始化
     }
     //使用硬件信息拼凑出来的15位号码
     return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
     }

}
