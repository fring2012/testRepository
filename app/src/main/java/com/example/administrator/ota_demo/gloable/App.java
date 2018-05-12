package com.example.administrator.ota_demo.gloable;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.abupdate.iot_libs.OtaAgentPolicy;
import com.abupdate.iot_libs.info.DeviceInfo;
import com.abupdate.iot_libs.info.ProductInfo;
import com.abupdate.iot_libs.security.FotaException;
import com.example.administrator.ota_demo.engine.AlarmManagerr;


/**
 * @author fighter_lee
 * @date 2017/5/11
 */

public class App extends Application {

    private final String TAG = "App";
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        initFota();
//        registerFOTAReceiver();
//        initPolicyConfig();
        startCycleCheck();
    }

    /**
     * 初始化fota数据(必要)
     */
    private void initFota() {
        try {
            OtaAgentPolicy.init(context)
                    .setMid("4325")
                    .commit();
            DeviceInfo.getInstance().deviceType = "phone";
            DeviceInfo.getInstance().mid = "4325";
            DeviceInfo.getInstance().models = "MI3C";
            DeviceInfo.getInstance().oem = "212";
            DeviceInfo.getInstance().platform = "BRCM23550";
            ProductInfo.getInstance().productId = "1525594277";
            ProductInfo.getInstance().productSecret = "c380eaa48f0248f9878a14857844d133";

//            if (Utils.buildConfigValid()) {
//                /**
//                 * 默认初始化方法，读取build.prop中的信息
//                 */
//                OtaAgentPolicy.init(context)
//                        .setMid(CustomConfig.getInstance().mid)
//                        .commit();
//            } else {
//                /**
//                 * 自定义DeviceInfo的初始化方法。
//                 */
//                OtaAgentPolicy.init(context)
//                        .setMid(CustomConfig.getInstance().mid)
//                        .setCustomDeviceInfo(new CustomDeviceInfo()
//                                .setVersion(CustomConfig.getInstance().version)
//                                .setOem(CustomConfig.getInstance().oem)
//                                .setModels(CustomConfig.getInstance().models)
//                                .setDeviceType(CustomConfig.getInstance().deviceType)
//                                .setPlatform(CustomConfig.getInstance().platform)
//                        )
//                        .commit();
//            }


        } catch (FotaException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 注册广播
//     * 开机接收升级结果（可定制化）
//     * 升级推送广播（可定制化）
//     * <p>
//     * 注：请在程序启动第一时间注册此广播，或者静态注册，否则可能存在收不到升级结果的广播
//     */
//    private void registerFOTAReceiver() {
//        LocalReceiver receiver = new LocalReceiver();
//        //升级结果广播
//        context.registerReceiver(receiver, new IntentFilter(BroadcastConsts.ACTION_FOTA_UPDATE_RESULT), BroadcastConsts.PERMISSION_FOTA_UPDATE, null);
//        //升级推送广播
//        context.registerReceiver(receiver, new IntentFilter(BroadcastConsts.ACTION_FOTA_NOTIFY), BroadcastConsts.PERMISSION_FOTA_UPDATE, null);
//    }
//
//    /**
//     * 初始化后台策略（可定制化）
//     */
//    private void initPolicyConfig() {
//        //配置后台策略信息,请根据设备实际情况进行选择
//        PolicyConfig.getInstance()
//                .request_wifi(true)//下载只能在wifi网络
//                .request_check_cycle(true)//检测版本周期
//                .request_download_force(true)//是否强制下载
//                .request_storage_path(true)//升级包下载路径
//                .request_storage_size(true)//本地存储最小空间 单位Byte
//                .request_battery("iot".equals(BuildConfig.APP_TYPE))//升级时，电量最小值
//                .request_install_force(true)//强制升级
//                .request_reboot_update_force(false)//重启强制升级
//                .request_remind_cycle(false)//提醒周期
//                .request_install_free_time(false)//闲时安装
//                .parsePolicyYourself(OtaConstants.PolicyType.TYPE_DOWNLOAD_FORCE, new PolicyForceDownload())//自定义解析强制下载策略
//                .parsePolicyYourself(OtaConstants.PolicyType.TYPE_DOWNLOAD_REQUEST_WIFI, new PolicyDLWifi())//自定义解析仅wifi下载策略
//                .parsePolicyYourself(OtaConstants.PolicyType.TYPE_INSTALL_FORCE, new PolicyInstallForce())//自定义强制升级策略
//        ;
//    }
//
    /**
     * 开启周期检测(可定制化)
     */
    private void startCycleCheck() {
        Log.d(TAG,"startCycleCheck()");
        AlarmManagerr.getInstance(this).startCycleCheck();
    }

}
