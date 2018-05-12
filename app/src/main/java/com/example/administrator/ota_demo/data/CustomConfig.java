package com.example.administrator.ota_demo.data;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.abupdate.iot_libs.info.DeviceInfo;
import com.abupdate.iot_libs.utils.SPFTool;
import com.example.administrator.ota_demo.gloable.App;
import com.example.administrator.ota_demo.util.FileUtils;
import com.example.administrator.ota_demo.util.Utils;


import org.json.JSONObject;

import java.io.InputStream;

/**
 * @author fighter_lee
 * @date 2018/2/27
 * 自定义配置，方便构建差异化配置
 */
public class CustomConfig {

    private static CustomConfig mInstance;
    private static final String TAG = "CustomConfig";
    public String mid = "4325";
    public boolean requestPush = true;
    public boolean localUpdate = true;
    public String screenOrientation = ScreenOrientation.SCREEN_PORTRAIT.getType();

    public String oem;
    public String models;
    public String platform;
    public String deviceType;
    public String version;

    public static CustomConfig getInstance() {
        if (mInstance == null) {
            synchronized (CustomConfig.class) {
                if (mInstance == null) {
                    mInstance = new CustomConfig();
                }
            }
        }
        return mInstance;
    }

    public CustomConfig() {
        getConfigFromConfigProp();
    }

    private void getConfigFromConfigProp() {
        try {
            AssetManager assetManager = App.context.getAssets();
            InputStream open = assetManager.open("CustomConfig.properties");
            int len;
            StringBuilder configMsg = new StringBuilder();
            byte[] buff = new byte[1024];
            while ((len = open.read(buff)) != -1) {
                configMsg.append(new String(buff));
            }
            FileUtils.closeIO(open);
            String config = configMsg.toString().trim();
            JSONObject configJson = new JSONObject(config);

            String midType = TYPE_SN;
            if (configJson.has(KEY_CONFIG_MID_TYPE)) {
                midType = configJson.getString(KEY_CONFIG_MID_TYPE);
            }
            genMid(midType);

            //获取是否需要升级推送功能
            if (configJson.has(KEY_CONFIG_PUSH)) {
                requestPush = configJson.getBoolean(KEY_CONFIG_PUSH);
            }

            //获取是否需要本地升级功能
            if (configJson.has(KEY_CONFIG_LOCAL_UPDATE)) {
                localUpdate = configJson.getBoolean(KEY_CONFIG_LOCAL_UPDATE);
            }

            //获取特定屏幕方向
            if (configJson.has(KEY_CONFIG_ORIENTATION)) {
                if (ScreenOrientation.valid(configJson.getString(KEY_CONFIG_ORIENTATION))) {
                    screenOrientation = configJson.getString(KEY_CONFIG_ORIENTATION);
                }
            }

            if (configJson.has(KEY_CONFIG_OEM)) {
                oem = configJson.getString(KEY_CONFIG_OEM);
            }

            if (configJson.has(KEY_CONFIG_MODELS)) {
                models = configJson.getString(KEY_CONFIG_MODELS);
            }

            if (configJson.has(KEY_CONFIG_PLATFORM)) {
                platform = configJson.getString(KEY_CONFIG_PLATFORM);
            }

            if (configJson.has(KEY_CONFIG_DEVICETYPE)) {
                deviceType = configJson.getString(KEY_CONFIG_DEVICETYPE);
            }

            if (configJson.has(KEY_CONFIG_VERSION)) {
                version = configJson.getString(KEY_CONFIG_VERSION);
            } else {
                version = android.os.Build.VERSION.RELEASE;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genMid(String midType) {
        String genMid = "";
        try {
            genMid = SPFTool.getString(DeviceInfo.KEY_MID_BACK, "");
            if (TextUtils.isEmpty(genMid)) {
                switch (midType) {
                    case TYPE_IMEI:
                        genMid = Utils.getIMEI();
                        break;
                    case TYPE_SN:
                        genMid = Utils.getSN();
                        break;
                    default:
                        genMid = Utils.getSN();
                        break;
                }

                if (!TextUtils.isEmpty(genMid) &&
                        !"unknown".equalsIgnoreCase(genMid) &&
                        genMid.length() >= 4) {
                    SPFTool.putString(DeviceInfo.KEY_MID_BACK, genMid);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (TextUtils.isEmpty(genMid) ||
                    "unknown".equalsIgnoreCase(genMid) ||
                    genMid.length() < 4) {
                genMid = Utils.getInstallationId();
                SPFTool.putString(DeviceInfo.KEY_MID_BACK, genMid);
            }

            mid = genMid;
        }
    }

    private static final String TYPE_IMEI = "imei";
    private static final String TYPE_SN = "sn";

    private static final String TYPE_SCREEN_PORTRAIT = "portrait";
    private static final String TYPE_SCREEN_LANDSCAPE = "landscape";

    private static final String KEY_CONFIG_MID_TYPE = "ro.fota.midType";
    private static final String KEY_CONFIG_PUSH = "ro.fota.push";
    private static final String KEY_CONFIG_LOCAL_UPDATE = "ro.fota.localUpdate";
    private static final String KEY_CONFIG_ORIENTATION = "ro.fota.orientation";

    private static final String KEY_CONFIG_OEM = "ro.fota.oem";
    private static final String KEY_CONFIG_MODELS = "ro.fota.device";
    private static final String KEY_CONFIG_PLATFORM = "ro.fota.platform";
    private static final String KEY_CONFIG_DEVICETYPE = "ro.fota.type";
    private static final String KEY_CONFIG_VERSION = "ro.fota.version";

    public enum ScreenOrientation {
        SCREEN_PORTRAIT(TYPE_SCREEN_PORTRAIT), SCREEN_LANDSCAPE(TYPE_SCREEN_LANDSCAPE);
        private String type;

        ScreenOrientation(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static boolean valid(String type) {
            if (type.equals(SCREEN_LANDSCAPE.getType()) || type.equals(SCREEN_PORTRAIT.getType())) {
                return true;
            }
            return false;
        }
    }

}
