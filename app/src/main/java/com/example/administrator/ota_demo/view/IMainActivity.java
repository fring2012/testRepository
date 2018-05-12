package com.example.administrator.ota_demo.view;

public interface IMainActivity {
    /**
     * 检测版本前ui状态
     */
    void before_check_version_ui();
    /**
     * 检测新版本
     */
    void check_version_ui();

    /**
     * 检测版本成功后ui状态
     */
    void after_success_check_version_ui();

    /**
     * 检测版本失败后ui状态
     * @param status 错误码
     * @param info 错误信息
     */
    void after_fail_check_version_ui(int status,String info);

    /**
     * 开始下载
     */
    void start_download_ui();
    /**
     * 下载进度
     * @param progress
     */
    void setDownloadProgress(int progress);

    /**
     *可以升级
     */
    void can_update_ui();
    /**
     * 等待中
     */
    void waiting_ui();
}
