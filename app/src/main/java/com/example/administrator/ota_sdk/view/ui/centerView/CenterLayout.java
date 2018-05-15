package com.example.administrator.ota_sdk.view.ui.centerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ota_sdk.R;
import com.zhy.android.percent.support.PercentRelativeLayout;

public class CenterLayout extends PercentRelativeLayout implements View.OnClickListener{

    private CenterCircleView centerCircleView;
    private TextView progressText;
    private TextView resultInfo;
    private ImageView arrowImage;

    private String state = CHECK_VERSION;

    public static final String CHECK_VERSION = "check_version_state";
    public static final String DOWNLOAD_VERSION = "download_version_state";
    public static final String UPDATE_VERSION = "update_version_state";

    private OnClickListener internal_Listener;

    public CenterLayout(Context context) {
        super(context);
    }

    public CenterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.center_layout,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        centerCircleView = (CenterCircleView)findViewById(R.id.centerCircleView);
        progressText = (TextView)findViewById(R.id.progressText);
        resultInfo = (TextView)findViewById(R.id.resultInfo);
        arrowImage = (ImageView)findViewById(R.id.arrowImage);
    }

    public void before_check_version_ui(){
        centerCircleView.setProgress(100);
        arrowImage.setVisibility(VISIBLE);
        arrowImage.setRotation(360);
        resultInfo.setText("检测");
        progressText.setText("");
    }

    public void after_succes_check_version_ui(String versionName){
        centerCircleView.setProgress(100);
        arrowImage.setVisibility(VISIBLE);
        arrowImage.setRotation(360);
        resultInfo.setText("最新版本:" + versionName);
        progressText.setText("");
    }

    public void after_error_check_version_ui(int error){
        centerCircleView.setProgress(100);
        arrowImage.setVisibility(VISIBLE);
        arrowImage.setRotation(360);
        resultInfo.setText("错误信息:" + error);
        progressText.setText("");
    }

    public void wait_ui(){
        centerCircleView.setProgress(10);
        centerCircleView.startAnim();
        arrowImage.setVisibility(GONE);
        resultInfo.setText("");
        progressText.setText("");
    }
    public void setOnClickListener(@Nullable OnClickListener listener) {
        this.internal_Listener = listener;
        super.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (state){
            case CHECK_VERSION :
                internal_Listener.check_version();
                break;
            case DOWNLOAD_VERSION :
                internal_Listener.download_latest_version();
                break;
            case UPDATE_VERSION :
                internal_Listener.update_laterst_version();
                break;
        }
    }

    public interface OnClickListener {
        void check_version();

        void download_latest_version();

        void update_laterst_version();

    }
}
