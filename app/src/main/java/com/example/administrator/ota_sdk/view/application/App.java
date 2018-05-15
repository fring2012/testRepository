package com.example.administrator.ota_sdk.view.application;

import android.app.Application;
import android.util.Log;

import com.abupdate.iot_libs.OtaAgentPolicy;
import com.abupdate.iot_libs.info.DeviceInfo;
import com.abupdate.iot_libs.info.ProductInfo;
import com.abupdate.iot_libs.security.FotaException;

public class App extends Application {
    private final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Log.d(TAG,"App.onCreate()");
            OtaAgentPolicy.init(this.getApplicationContext())
                    .setMid("4325").commit();
            DeviceInfo.getInstance().version = "0.0.8";
            DeviceInfo.getInstance().deviceType = "phone";
            DeviceInfo.getInstance().mid = "4325";
            DeviceInfo.getInstance().models = "MI3C";
            DeviceInfo.getInstance().oem = "212";
            DeviceInfo.getInstance().platform = "BRCM23550";
            ProductInfo.getInstance().productId = "1525594277";
            ProductInfo.getInstance().productSecret = "c380eaa48f0248f9878a14857844d133";

        } catch (FotaException e) {
            e.printStackTrace();
        }
    }
}
