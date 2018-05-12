package com.example.administrator.ota_demo.engine;


import com.abupdate.iot_libs.utils.SPFTool;
import com.abupdate.trace.Trace;
import com.example.administrator.ota_demo.gloable.App;
import com.example.administrator.ota_demo.util.NetUtil;

/**
 * 管理检测周期是否到达
 * 3中方式触发:
 * 1. 开机广播
 * 2. 网络状态改变
 * 3. 闹钟
 * Created by raise.yang on 17/10/25.
 */

public class CheckPeriod {

    //单位:毫秒,检测周期默认25小时
    private static final int DEFAULT_CHECK_VERSION_PERIOD = 25 * 60 * 60 * 1000;

    private static final String KEY_PERIOD_INTERVAL_TIME = "key_period_interval_time";
    private static final String KEY_PREVIOUS_TIME = "key_previous_time";

    /**
     * 是否到达检测时间
     * 若到达,将自动重置本次检测时间
     * 没有网络,将直接判断没有到达,等待下次条件触发
     *
     * @return true if arrived
     */
    public static boolean isArrived() {
        if (!NetUtil.hasNetwork(App.context)) {
            return false;
        }
        boolean is_arrived = false;
        long pre_check_time = SPFTool.getLong(KEY_PREVIOUS_TIME, -1);
        long cur_time = System.currentTimeMillis();
        long interval_time = getCheckPeriod();
        if (cur_time - pre_check_time > interval_time) {
            //时间差大于检测周期时间
            is_arrived = true;
            reset_period();
        }
        return is_arrived;
    }

    /**
     * 重置闹钟和检测时间
     */
    public static void reset_period() {
        SPFTool.putLong(KEY_PREVIOUS_TIME, System.currentTimeMillis());
        AlarmManagerr.getInstance(App.context).startCycleCheck();
    }

    /**
     * 设置检测周期<br/>
     * @param period 单位：分
     */
    public static void setCheckPeriod(long period) {
        period = period * 60 * 1000;
        long pre_period = SPFTool.getLong(KEY_PERIOD_INTERVAL_TIME, DEFAULT_CHECK_VERSION_PERIOD);
        if (pre_period != period) {
            SPFTool.putLong(KEY_PERIOD_INTERVAL_TIME, period);
            Trace.i("CheckPeriod", "setCheckPeriod() %s", period);
        }
    }

    /**
     * 获取检测周期
     * @return 单位：毫秒
     */
    public static long getCheckPeriod() {
        return SPFTool.getLong(KEY_PERIOD_INTERVAL_TIME, DEFAULT_CHECK_VERSION_PERIOD);
    }
}