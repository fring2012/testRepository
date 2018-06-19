package com.example.administrator.ota_sdk.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abupdate.trace.Trace;
import com.example.administrator.ota_sdk.view.activity.MainActivity;

public class CheckAndDownloadInfoReceiver extends BroadcastReceiver {
    private static final String TAG = "CheckAndDownloadInfoReceiver";
    private MainActivity mainActivity;
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive(Context context, Intent intent)");
        int state = intent.getIntExtra("state",0);
        switch (state) {
            case 0:
                int resultState = intent.getIntExtra("resultState", 0);
                if (resultState == 1000) {
                    String versionName = intent.getStringExtra("versionName");
                    String content = intent.getStringExtra("content");
                    mainActivity.after_succes_check_version_ui(versionName);
                    Trace.d(TAG, "resultState:" + resultState + "versionName:" + versionName + "content:" + content);
                } else {
                    String error = intent.getStringExtra("error");
                    Trace.d(TAG, "resultState:" + resultState + "error:" + error);
                }
                break;
            case 1:
                int progress =intent.getIntExtra("progress",0);
                Trace.d(TAG, "progress:" + progress);
                break;
            case 3:
                String policyException = intent.getStringExtra("exception");
                Trace.d(TAG, "exception:" + policyException);
        }
    }

    public void setView(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
}
