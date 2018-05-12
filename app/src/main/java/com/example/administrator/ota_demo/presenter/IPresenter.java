package com.example.administrator.ota_demo.presenter;

import com.example.administrator.ota_demo.presenter.code.Presenter;
import com.example.administrator.ota_demo.view.activity.MainActivity;

public interface IPresenter extends Presenter<MainActivity>{
    /**
     * 检测版本
     */
    void check_version();
    /**
     * 开始下载
     */
    void start_down();

    /**
     *升级
     */
    void update();

    /**
     * 下载取消
     */
    void down_cancel();


}
