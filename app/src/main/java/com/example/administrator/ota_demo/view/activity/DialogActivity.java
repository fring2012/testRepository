package com.example.administrator.ota_demo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;


import com.abupdate.trace.Trace;
import com.example.administrator.ota_demo.R;
import com.example.administrator.ota_demo.view.activity.code.BaseActivity;

import butterknife.BindView;

/**
 *
 * @author fighter_lee
 * @date 2017/8/23
 */

public class DialogActivity extends BaseActivity {

    private static final String TAG = "UpdateResultActivity";
    public static final String ACTION_TYPE = "action_type";
    public static final String UPDATE_RESULT = "update_result";
    public static final String UPDATE_CONTENT = "update_content";
    public static final String SP_UPDATE_CONTENT = "sp_update_content";

    /**
     * 显示升级结果
     */
    public static final String ACTION_OTA_RESULT = "action_ota_result";

    /**
     * 显示有新版本提醒
     */
    public static final String ACTION_OTA_HAVE_NEW_VERSION = "action_ota_have_new_version";
    @BindView(R.id.dialog_fl)
    FrameLayout dialogFl;




//    protected void initView(Bundle savedInstanceState) {
//        Intent intent = getIntent();
//        String stringExtra = intent.getStringExtra(ACTION_TYPE);
//        switch (stringExtra) {
//            case ACTION_OTA_RESULT:
//                boolean result = intent.getBooleanExtra(UPDATE_RESULT, true);
//                String content = intent.getStringExtra(UPDATE_CONTENT);
//                Trace.d(TAG, "onCreate() " + content);
//                if (TextUtils.isEmpty(content)) {
//                    content = getString(R.string.default_content);
//                }
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(UPDATE_RESULT,result);
//                bundle.putString(UPDATE_CONTENT,content);
//                OtaResultFragment otaResultFragment = new OtaResultFragment();
//                otaResultFragment.setArguments(bundle);
//                FragmentUtil.addFragmentWithoutAnim(this,R.id.dialog_fl,otaResultFragment);
//                break;
//
//            case ACTION_OTA_HAVE_NEW_VERSION:
//                OtaNewVersionFragment newVersionFragment = new OtaNewVersionFragment();
//                FragmentUtil.addFragmentWithoutAnim(this,R.id.dialog_fl,newVersionFragment);
//                break;
//        }
//    }

    @Override
    protected int getContentView() {
        return R.layout.activity_dialog;
    }

}
