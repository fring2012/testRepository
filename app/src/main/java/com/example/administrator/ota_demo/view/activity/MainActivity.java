package com.example.administrator.ota_demo.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abupdate.iot_libs.info.DeviceInfo;
import com.example.administrator.ota_demo.R;
import com.example.administrator.ota_demo.presenter.IPresenter;
import com.example.administrator.ota_demo.presenter.impl.PresenterImpl;
import com.example.administrator.ota_demo.util.CleanUtils;
import com.example.administrator.ota_demo.view.IMainActivity;
import com.example.administrator.ota_demo.view.activity.code.BaseActivity;
import com.example.administrator.ota_demo.view.ui.centerView.CenterCircleView;
import com.example.administrator.ota_demo.view.ui.centerView.CenterCircleView.OnClickListener;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainActivity{
    private final static String TAG = "MainActivity";

    @BindView(R.id.view_center_circle)
    CenterCircleView centerCircleView;
    @BindView(R.id.button_control)
    Button buttonControl;
    @BindView(R.id.iot_version_detail)
    TextView currentVersion;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationview)
    NavigationView navigationView;

    private IPresenter presenter;
    private String versionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,getApplicationContext().toString());
        presenter = new PresenterImpl();
        presenter.setView(this);
        before_check_version_ui();
        centerCircleView.setOnClickListener(new OnClickListener() {
            @Override
            public void on_check_version() {
                presenter.check_version();
            }

            @Override
            public void on_start_download() {
                presenter.start_down();
            }

            @Override
            public void on_reboot_upgrade() {
                presenter.update();
            }
        });

        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deleteFile:
                        CleanUtils.cleanAll();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        before_check_version_ui();
                        break;
                }
                return false;
            }
        });
    }
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        //Environment.getExtemalStorageState() 获取SDcard的状态
        //Environment.MEDIA_MOUNTED 手机装有SDCard,并且可以进行读写
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @OnClick({R.id.button_control,R.id.toolbar_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_control:
                presenter.down_cancel();
                break;
            case R.id.toolbar_back:
                drawerLayout.openDrawer(Gravity.LEFT);

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void before_check_version_ui() {
        centerCircleView.before_check_version_ui();
        currentVersion.setText(String.format(this.getString(R.string.current_version), DeviceInfo.getInstance().version));
        buttonControl.setVisibility(View.GONE);
    }

    @Override
    public void check_version_ui() {
        centerCircleView.check_version_ui();
    }

    @Override
    public void after_success_check_version_ui() {
        centerCircleView.after_success_check_version_ui(versionName);
        buttonControl.setVisibility(View.GONE);
    }

    @Override
    public void after_fail_check_version_ui(int status ,String info) {
        centerCircleView.after_fail_check_version_ui(status , info);
    }

    @Override
    public void start_download_ui() {
        centerCircleView.start_download_ui();
        buttonControl.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDownloadProgress(int progress) {
        centerCircleView .setDownloadProgress(progress);
    }

    @Override
    public void can_update_ui() {
        centerCircleView.can_update_ui();
        buttonControl.setVisibility(View.GONE);
    }


    @Override
    public void waiting_ui() {
        centerCircleView.waiting_ui();
    }


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
