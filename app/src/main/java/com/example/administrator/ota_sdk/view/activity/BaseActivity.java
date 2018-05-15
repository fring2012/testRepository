package com.example.administrator.ota_sdk.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.ota_sdk.presenter.Presenter;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {
    protected final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        init();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        Log.d(TAG,"ButterKnife.bind(this)");
    }

    public abstract int getContentView();

    public abstract void setPresenter(Presenter presenter);
    public abstract void init();
}
