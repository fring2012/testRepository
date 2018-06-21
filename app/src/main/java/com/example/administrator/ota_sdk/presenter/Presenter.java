package com.example.administrator.ota_sdk.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abupdate.iot_libs.OtaAgentPolicy;
import com.abupdate.iot_libs.info.VersionInfo;
import com.abupdate.iot_libs.inter.ICheckVersionCallback;
import com.example.administrator.ota_sdk.view.activity.MainActivity;

public class Presenter {
    private final String TAG = "Presenter";
    private MainActivity activity;
    public Presenter(MainActivity activity){
        this.activity =  activity;
        this.activity.setPresenter(this);
    }
    public void check_version(){
        activity.wait_ui();
        Intent intent = new Intent();
        setParams(intent);
        intent.setComponent(new ComponentName("com.abupdate.apk_up_receiver",
                "com.abupdate.apk_up_receiver.broadcast.ApkUpInfoReceiver"));
        intent.setAction("broadcast.ApkUpInfoReceiver");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        activity.sendBroadcast(intent);
        Log.d(TAG,"checkVersion() end");
    }
    public void setParams(Intent intent){
        String packageName = activity.getApplicationContext().getPackageName();
        intent.putExtra("packageName",packageName);
        intent.putExtra("receiverName", "CheckAndDownloadInfoReceiver");
        intent.putExtra("receiverClass","com.example.administrator.ui_make.receiver.CheckAndDownloadInfoReceiver");
        intent.putExtra("mid","4325");
        intent.putExtra("deviceType","phone");
        intent.putExtra("device","MI3C");
        intent.putExtra("oem","212");
        intent.putExtra("platform","BRCM23550");
        intent.putExtra("productId","1525594277");
        intent.putExtra("productSecret","c380eaa48f0248f9878a14857844d133");
    }
}
