package com.example.administrator.ota_demo.presenter.code;

import com.example.administrator.ota_demo.view.activity.code.BaseActivity;

public interface Presenter<T extends BaseActivity> {
     void setView(T view);
     T getView();
}
