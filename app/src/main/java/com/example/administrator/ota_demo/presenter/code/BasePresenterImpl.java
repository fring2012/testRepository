package com.example.administrator.ota_demo.presenter.code;


import android.view.View;

import com.example.administrator.ota_demo.view.activity.code.BaseActivity;

public class BasePresenterImpl<T extends BaseActivity> implements Presenter<T>{
    protected T view;

    @Override
    public void setView(T view){
        this.view = view;
    }

    @Override
    public T getView() {
        if(view == null){
            throw new RuntimeException("请先用setView板顶view!");
        }
        return view;
    }

}
