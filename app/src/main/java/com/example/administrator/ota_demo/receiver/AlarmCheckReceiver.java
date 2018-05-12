package com.example.administrator.ota_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abupdate.iot_libs.OtaAgentPolicy;
import com.example.administrator.ota_demo.engine.UpdateEngine;

public class AlarmCheckReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmCheckReceiver";
    public final static String ALARM_CHECK_RECEIVER_NAME = "action_check_version";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive(Context context, Intent intent)");
        UpdateEngine.getInstance().updateExecute();

    }
}
