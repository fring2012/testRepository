package com.example.administrator.ota_demo.engine;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.abupdate.iot_libs.OtaAgentPolicy;
import com.abupdate.iot_libs.info.VersionInfo;
import com.abupdate.iot_libs.inter.IDownSimpleListener;
import com.abupdate.iot_libs.inter.IRebootUpgradeCallBack;
import com.abupdate.iot_libs.policy.PolicyManager;
import com.abupdate.iot_libs.utils.SPFTool;
import com.abupdate.trace.Trace;
import com.example.administrator.ota_demo.constant.Const;
import com.example.administrator.ota_demo.gloable.App;
import com.example.administrator.ota_demo.util.ActivityCollector;
import com.example.administrator.ota_demo.util.NetUtil;
import com.example.administrator.ota_demo.util.Utils;
import com.example.administrator.ota_demo.view.activity.DialogActivity;
import com.example.administrator.ota_demo.R;
import java.io.File;

public class UpdateEngine {
    private static final String TAG = "UpdateEngine";
    private static volatile UpdateEngine mInstance;

    //同时接收多个OTA任务但只执行一个
    private boolean processing = false;

    private UpdateEngine() {
    }
    public static UpdateEngine getInstance(){
        if(mInstance == null){
            synchronized (VersionInfo.class) {
                mInstance = new UpdateEngine();
            }
        }
        return mInstance;
    }
    public void updateExecute() {
        if (processing) {
            //一次接受一次任务
            return;
        }
        new Thread(new OtaRun()).start();
    }

    private class OtaRun implements Runnable {

        @Override
        public void run() {
            processing = true;
            Pair<Integer, VersionInfo> versionInfoPair = OtaAgentPolicy.checkVersionExecute();
            if (!Const.isSuccess(versionInfoPair.first)) {
                return;
            }
            Log.d(TAG,"准备下载！！！ ");
            saveCheckInfo();
            if (!PolicyManager.INSTANCE.isDownloadForce()) {
                //如果没有配置强制升级 -> 判断wifi策略
                if (PolicyManager.INSTANCE.is_request_wifi() && !NetUtil.NETWORK_WIFI.equals(NetUtil.getNetworkState(App.context))) {
                    //配置了仅wifi，但当前不在wifi状态下 -> 不下载
                    return;
                }
            }
            if (!PolicyManager.INSTANCE.is_storage_space_enough(OtaAgentPolicy.getConfig().updatePath)) {
                //最小升级空间策略：空间不足 -> 不下载
                return;
            }
            download();
            processing = false;

        }
    }
    private void download() {
        Log.d(TAG,"download()");
        OtaAgentPolicy.downloadExecute(new IDownSimpleListener() {
            public void on_start() {
                Log.d(TAG,"IDownSimpleListener. on_start()");
            }
            /**
             * 取消下载回调
             */
            public void onCancel() {
                Log.d(TAG,"onCancel()");
            }
            @Override
            public void onCompleted(File file) {
                super.onCompleted(file);
                Log.d(TAG,"onCompleted(File file)");
                if (PolicyManager.INSTANCE.is_force_install()) {
                    //配置了强制升级策略 -> 判断电量是否满足
                    if (!PolicyManager.INSTANCE.is_battery_enough(App.context)) {
                        //配置了最低电量策略：电量不足 -> 不升级
                        Log.d(TAG,"配置了最低电量策略：电量不足 -> 不升级");
                        return;
                    }
                    //升级
                    rebootUpdate();
                } else {
                    Log.d(TAG," 没有配置强制升级 -> 判断是否弹框提醒");
                    //没有配置强制升级 -> 判断是否弹框提醒
                    if (shouldShowDialog()) {
                        Log.d(TAG,"showNewVersionDialog");
                        //没有在前台显示 -> 弹框提醒
                        showNewVersionDialog();
                    } else {
                        Log.d(TAG,"show_notification");
                        //通知栏提醒
                        show_notification(App.context.getString(R.string.notify_tips),App.context);
                    }
                }

            }
            public void onDownloadProgress(long downSize, long totalSize, int progress) {
                Log.d(TAG,"downSize:" + downSize + "totalSize:" + totalSize);
            }
            @Override
            public void onFailed(int error) {
                Log.d(TAG,"onFailed(int error)" + error);
            }
        });
    }
    public void showNewVersionDialog() {
        ActivityCollector.removeAllActivity();
        Intent intent1 = new Intent("fota.intent.action.update.notify").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra(DialogActivity.ACTION_TYPE, DialogActivity.ACTION_OTA_HAVE_NEW_VERSION);
        App.context.startActivity(intent1);
    }
    /**
     * 如果已经应用正在进行OTA操作或者新版本弹框在前台显示，那么没有必要再进行弹框显示了
     *
     * @return
     */
    private boolean shouldShowDialog() {
        if (Utils.isAppForeground()) {
            return false;
        }
        return true;
    }
    private void rebootUpdate() {
        OtaAgentPolicy.rebootUpgrade(new IRebootUpgradeCallBack() {
            @Override
            public boolean rebootConditionPrepare() {
                return true;
            }

            @Override
            public void onError(int i) {
            }
        });

    }
    public static void saveCheckInfo(){
        //将更新提醒存储,用作升级提醒
        saveContent();

        //检测周期策略
        int check_cycle = PolicyManager.INSTANCE.get_check_cycle();
        if (check_cycle > 0) {
            //开启周期检测任务(优先以后台下发的检测周期为周期)
            CheckPeriod.setCheckPeriod(check_cycle);
            CheckPeriod.reset_period();
        }
    }

    //通知栏显示更新消息
    public void show_notification(String msg, Context context) {
        Trace.d(TAG, "show_notification():" + msg);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.iot_main_title))
                        .setContentText(msg);
        Intent resultIntent = new Intent(context, Utils.getLaunchActivity());

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        mBuilder.setContentIntent(contentIntent);
        @SuppressLint("WrongConstant") NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
    private static void saveContent() {
//        if (TextUtils.isEmpty(VersionInfo.getInstance().content)) {
//            return;
//        }
//        SPFTool.putString(DialogActivity.SP_UPDATE_CONTENT, VersionInfo.getInstance().getReleaseNoteByCurrentLanguage());
    }
}
