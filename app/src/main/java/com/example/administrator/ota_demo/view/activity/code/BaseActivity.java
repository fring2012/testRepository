package com.example.administrator.ota_demo.view.activity.code;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity{
    public boolean onRunning = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRunning = true;
    }

    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    };

    protected  abstract int getContentView();
}
