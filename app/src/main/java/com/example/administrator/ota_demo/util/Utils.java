package com.example.administrator.ota_demo.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;
import com.abupdate.trace.Trace;
import com.example.administrator.ota_demo.BuildConfig;
import com.example.administrator.ota_demo.data.Installation;
import com.example.administrator.ota_demo.gloable.App;
import com.example.administrator.ota_demo.view.activity.code.BaseActivity;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * Created by fighter_lee on 2017/3/31.
 */

public class Utils {

    public static String setFotaLog(Context context) {

        String path;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //大于6.0 存放于Android/data/data/包名/cache
            path = context.getExternalCacheDir() + "/iport_log.txt";
        } else {
            //小于6.0 存放于内置存储卡根目录
            path = Environment.getExternalStorageDirectory() + "/iport_log.txt";
        }
        return path;
    }

    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 传入时间是否在区间内
     * 传入的时间格式为 10:10
     *
     * @param currentTime
     * @param fromTime
     * @param toTime
     * @return
     */
    public static boolean timeCompare(String currentTime, String fromTime, String toTime) {
        Trace.d("utils", "timeCompare() cur:" + currentTime + ",form:" + fromTime + ",to:" + toTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date current = sdf.parse(currentTime);
            Date from = sdf.parse(fromTime);
            Date to = sdf.parse(toTime);
            Date twenty_four = sdf.parse("24:00");
            Date zero = sdf.parse("00:00");
            if (from.after(to)) {
                //隔夜
                if (current.after(from) && current.before(twenty_four) ||
                        current.after(zero) && current.before(to)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (current.after(from) && current.before(to)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isScreenOn(Context context) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm.isScreenOn()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断App是否处于前台
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground() {
        try {
            for (Map.Entry<Class<?>, Activity> classActivityEntry : ActivityCollector.activities.entrySet()) {
                BaseActivity activity = (BaseActivity) classActivityEntry.getValue();
                if (activity.onRunning) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static Class getLaunchActivity() {
        try {
            Class<?> aClass = Class.forName(BuildConfig.APP_LAUNCH_ACTIVITY);
            return aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断build.prop文件中fota配置是否可用
     * @return
     */
    public static boolean buildConfigValid() {
        try {
            ClassLoader cl = App.context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;

            Method get = SystemProperties.getMethod("get", paramTypes);

            String version = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.version"});
            String oem = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.oem"});
            String models = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.device"});
            String platform = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.platform"});
            String deviceType = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.type"});

            if (!TextUtils.isEmpty(version) &&
                    !TextUtils.isEmpty(oem) &&
                    !TextUtils.isEmpty(models) &&
                    !TextUtils.isEmpty(platform) &&
                    !TextUtils.isEmpty(deviceType)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取 IMEI 码
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return IMEI 码
     */
    public static String getIMEI() {
//        TelephonyManager tm =
//                (TelephonyManager) App.context.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm != null ? tm.getDeviceId() : null;
        return null;
    }

    /**
     * 获取SN号
     *
     * @return SN 号
     */
    public static String getSN() {
        return Build.SERIAL;
    }

    /**
     * 获取Installation ID
     * @return
     */
    public static String getInstallationId() {
        return Installation.id(App.context);
    }
}
