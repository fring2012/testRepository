package com.example.administrator.ota_demo.engine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abupdate.iot_libs.policy.PolicyManager;
import com.abupdate.trace.Trace;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmManagerr {

    public static final String TAG = "AlarmManagerr:";
    private static AlarmManagerr info = null;
    public static final String ACTION_CHECK_VERSION = "action_check_version";

    private Context mContext;
    public PendingIntent operation;

    public static AlarmManagerr getInstance(Context context) {
        if (info == null) {
            info = new AlarmManagerr(context);
        }
        return info;
    }

    private AlarmManagerr(Context ctx) {
        mContext = ctx;
    }

    public void startCycleCheck() {
        //加上2s保证触发闹钟时,检测一定会触发
        long repeatTime = CheckPeriod.getCheckPeriod() + 2000;
        long triggerTime = repeatTime+ System.currentTimeMillis();
        setRepeatingAlarm(repeatTime, 20 * 1000 ,AlarmManagerr.ACTION_CHECK_VERSION);
        Log.d(TAG,"repeatTime:" + repeatTime  + "/triggerTime:" + triggerTime);
    }


    //repeating alarm
    public AlarmManager setRepeatingAlarm(long repeatTime, long triggerTime, String action) {
        Trace.d(TAG, "setRepeatingAlarm() "+repeatTime);
        operation = PendingIntent.getBroadcast(mContext, 0, new Intent(action), 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        am.cancel(operation);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime,repeatTime,operation);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime,repeatTime,operation);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Trace.d(TAG, "serAlarm enter, current time is:  " + format.format(System.currentTimeMillis()));
        Trace.d(TAG, "setRepeatingAlarm() first time is:"+format.format(triggerTime));
        return am;
    }

}
