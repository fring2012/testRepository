package com.example.administrator.ota_sdk.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.ota_sdk.R;
import com.example.administrator.ota_sdk.presenter.Presenter;

import butterknife.BindView;
import butterknife.OnClick;

public class SotiryActivity extends BaseActivity{
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.resultInfo)
    TextView textView;

    @Override
    public int getContentView() {
        return R.layout.activity_sotiry_layout;
    }

    @Override
    public void setPresenter(Presenter presenter) {

    }
    @OnClick({R.id.check,R.id.update,R.id.download,R.id.cleanCache})
    public void onClick(View view){
        int requestCode = 100;
        switch (view.getId()){
            case R.id.check:
                requestCode = 0;
                break;
            case R.id.download:
                requestCode = 1;
                break;
            case R.id.update:
                requestCode = 2;
                break;
            case R.id.cleanCache:
                requestCode = 3;
                break;
        }
        sendBroadcast(requestCode);
        
    }

    @Override
    public void init() {

    }


    public void sendBroadcast(int requestCode){

        Intent intent = new Intent();
        intent.putExtra("requestCode",requestCode);
        setParams(intent);
        intent.setComponent(new ComponentName("com.abupdate.apk_up_receiver",
                "com.abupdate.apk_up_receiver.broadcast.ApkUpInfoReceiver"));
        intent.setAction("broadcast.ApkUpInfoReceiver");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
        Log.d(TAG,"checkVersion() end");
    }
    public void setParams(Intent intent){
        String packageName = getApplicationContext().getPackageName();

        intent.putExtra("packageName",packageName);
        intent.putExtra("receiverName", "CheckAndDownloadInfoReceiver");
        intent.putExtra("receiverClass","com.example.administrator.ota_sdk.receiver.CheckAndDownloadInfoReceiver");
        intent.putExtra("mid","4325");
        intent.putExtra("deviceType","phone");
        intent.putExtra("device","MI3C");
        intent.putExtra("oem","212");
        intent.putExtra("platform","BRCM23550");
        intent.putExtra("productId","1525594277");
        intent.putExtra("productSecret","c380eaa48f0248f9878a14857844d133");
    }
}
