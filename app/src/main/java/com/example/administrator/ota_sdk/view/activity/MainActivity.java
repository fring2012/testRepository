package com.example.administrator.ota_sdk.view.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.abupdate.iot_libs.info.DeviceInfo;
import com.example.administrator.ota_sdk.R;
import com.example.administrator.ota_sdk.presenter.Presenter;
import com.example.administrator.ota_sdk.receiver.CheckAndDownloadInfoReceiver;
import com.example.administrator.ota_sdk.view.ui.centerView.CenterLayout;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.centerLayout)
    CenterLayout centerLayout;
    @BindView(R.id.leftmenu)
    ImageView imageView;
    @BindView(R.id.navigation)
    NavigationView navigationView;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.currentVersionInfo)
    TextView currentVersionInfo;


    private Presenter presenter;
    private CheckAndDownloadInfoReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager packageManager = getPackageManager();
        Log.d(TAG,getPackageName());
        centerLayout.setOnClickListener(new CenterLayout.OnClickListener() {
            @Override
            public void check_version() {
                presenter.check_version();
            }

            @Override
            public void download_latest_version() {

            }

            @Override
            public void update_laterst_version() {

            }
        });

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        currentVersionInfo.setText("当前版本:" + DeviceInfo.getInstance().version);
    }

    @Override
    protected void onStart() {
        super.onStart();
        before_check_version_ui();
        registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }

    protected void registerReceiver(){
        Intent intent = new Intent();
        mBroadcastReceiver = new CheckAndDownloadInfoReceiver();
        mBroadcastReceiver.setView(this);
        intent.setAction("CheckAndDownloadInfoReceiver");
        registerReceiver();
    }


    public void before_check_version_ui(){
        centerLayout.before_check_version_ui();
    }

    public void after_succes_check_version_ui(String versionName){
        centerLayout.after_succes_check_version_ui(versionName);
    }
    public void after_error_check_version_ui(int error){
        centerLayout.after_error_check_version_ui(error);
    }
    public void wait_ui(){
        centerLayout.wait_ui();
    }
    @Override
    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void init() {
        setPresenter(new Presenter(this));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }



}
