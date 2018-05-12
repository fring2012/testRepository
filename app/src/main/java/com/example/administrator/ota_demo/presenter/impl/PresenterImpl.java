package com.example.administrator.ota_demo.presenter.impl;

import android.util.Log;
import android.view.View;

import com.abupdate.iot_libs.OtaAgentPolicy;
import com.abupdate.iot_libs.info.VersionInfo;
import com.abupdate.iot_libs.inter.ICheckVersionCallback;
import com.abupdate.iot_libs.inter.IDownloadListener;
import com.abupdate.iot_libs.inter.IRebootUpgradeCallBack;
import com.abupdate.trace.Trace;
import com.example.administrator.ota_demo.constant.Const;
import com.example.administrator.ota_demo.presenter.IPresenter;
import com.example.administrator.ota_demo.presenter.code.BasePresenterImpl;
import com.example.administrator.ota_demo.view.activity.MainActivity;


public class PresenterImpl extends BasePresenterImpl<MainActivity> implements IPresenter{
    public final static String TAG = "PresenterImpl";
    @Override
    public void check_version() {
        view.check_version_ui();
        OtaAgentPolicy.checkVersionAsync(new ICheckVersionCallback() {
            @Override
            public void onCheckSuccess(VersionInfo versionInfo) {
                view.setVersionName(versionInfo.versionName);
                view.after_success_check_version_ui();
            }

            @Override
            public void onCheckFail(int status) {
                Log.d(TAG,status + "");
                view.after_fail_check_version_ui(status,status+"");
            }
        });
    }

    @Override
    public void start_down() {
        Log.d(TAG, "start_down");
        OtaAgentPolicy.downloadAsync(new IDownloadListener() {
            @Override
            public void onPrepare() {
                view.start_download_ui();
            }

            @Override
            public void onDownloadProgress(long downSize, long totalSize) {
                Log.d(TAG,downSize + "/" + totalSize);
                view.setDownloadProgress((int)(downSize*100/totalSize));
            }

            @Override
            public void onFailed(int error) {

            }

            @Override
            public void onCompleted() {
                view.can_update_ui();
            }

            @Override
            public void onCancel() {
                view.after_success_check_version_ui();
            }
        });
    }
    @Override
    public void update() {
        IRebootUpgradeCallBack iRebootUpgradeCallBack = new IRebootUpgradeCallBack() {
            @Override
            public boolean rebootConditionPrepare() {
                //是否已经为重启升级准备完成
                return true;
            }

            @Override
            public void onError(int error) {
                //进入recovery前的错误回掉
                Trace.d(TAG + ":" + error, Const.getUpdateTipByErrorCode(error));
            }
        };

        OtaAgentPolicy.rebootUpgrade(iRebootUpgradeCallBack);
    }
    @Override
    public void down_cancel() {
        OtaAgentPolicy.downloadCancel();
    }


}
