package com.example.administrator.ota_sdk.presenter;

import android.app.Activity;
import android.content.Context;
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
        OtaAgentPolicy.checkVersionAsync(new ICheckVersionCallback() {
            @Override
            public void onCheckSuccess(VersionInfo versionInfo) {
                Log.d(TAG,"最新版本:" + versionInfo.versionName);
                activity.after_succes_check_version_ui(versionInfo.versionName);
            }

            @Override
            public void onCheckFail(int status) {
                Log.d(TAG,"错误信息:" + status);
                activity.after_error_check_version_ui(status);
            }
        });
    }
}
