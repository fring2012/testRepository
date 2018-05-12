package com.example.administrator.ota_demo.constant;


import com.abupdate.iot_libs.constant.Error;
import com.example.administrator.ota_demo.R;
import com.example.administrator.ota_demo.gloable.App;

/**
 * Created by fighter_lee on 2017/5/24.
 */

public class Const {

    public static final String CHECK_VERSION = "check version";
    public static final String DOWNLOAD = "download";
    public static final String UPDATE = "update";
    public static final String DELETE_FILE = "delete_file";
    public static final String REGISTER = "register";
    public static final String CONNECT = "connect";
    public static final String ERROR = "Error";
    public static final String DISCONNECT = "disconnect";
    public static final String OTA_NOTIFY = "notify";
    public static final String OTA_LOGIN = "login";
    public static final String OTA_LOGOUT = "logout";
    public static final String OTA_REPORT_DEVICE = "report device status";

    public static boolean isNetError(int error) {
        if (error == Error.SERVER_DATA_ERROR ||
                error == Error.DOWNLOADING_NET_EXCEPTION) {
            return true;
        }
        return false;
    }

    public static String getDlTipByErrorCode(int error) {
        String tips = "";
        switch (error){
            case Error.DOWNLOADING_MEMORY_NOT_ENOUGH:
                tips = App.context.getString(R.string.tips_download_memory_not_enough);
                break;
            case Error.DOWNLOADING_NOT_WIFI:
                tips = App.context.getString(R.string.tips_only_wifi_download);
                break;
            default:
                tips = App.context.getString(R.string.net_error_tips);
        }
        return tips;
    }

    public static String getUpdateTipByErrorCode(int error) {
        String tips = "";
        switch (error) {
            case Error.UPGRADE_BATTERY_NOT_ENOUGH:
                tips = App.context.getString(R.string.tips_battery_not_enough);
                break;
            default:
                tips = App.context.getString(R.string.enter_recovery_fail);
        }
        return tips;
    }

    public static boolean isSuccess(int code) {
        return 1000 == code;
    }


}
