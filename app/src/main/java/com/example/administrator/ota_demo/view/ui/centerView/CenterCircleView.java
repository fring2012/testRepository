package com.example.administrator.ota_demo.view.ui.centerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ota_demo.R;


public class CenterCircleView extends RelativeLayout implements View.OnClickListener {
    private static final int STATE_CHECK_VERSION = 0X1;
    private static final int STATE_START_DOWNLOAD = 0X2;
    private static final int STATE_UPGRADE = 0X3;
    private static final int STATE_IDLE = 0X4; //无法点击的状态
    private static final String TAG = "CommonCirleView";
    private Context mCx;

    private int m_state = STATE_CHECK_VERSION;

    private CommonCircleView m_circle_view;   //进度条
    private ImageView m_arrow;  //箭头
    private TextView m_progress_view;  //下载进度数值
    private TextView m_state_detail_view;


    /**
     * LayoutInflater.from(getContext()).inflate(R.layout.viewgroup_circle, this);后调用此方法
     */
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        m_circle_view = (CommonCircleView)findViewById(R.id.view_circle) ;
        m_arrow = (ImageView)findViewById(R.id.image_arrow);
        m_progress_view = (TextView)findViewById(R.id.tv_progress);
        m_state_detail_view = (TextView)findViewById(R.id.tv_update_state_detail) ;

        m_circle_view.loadXmlAnim(R.anim.iot_rotate);

    }

    private OnClickListener m_listener;  //监听对象

    public CenterCircleView(Context context) {
        super(context);
    }

    public CenterCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.viewgroup_circle, this);
        this.mCx = context;
    }

    public void before_check_version_ui(){
        m_state = STATE_CHECK_VERSION;
        m_arrow.setVisibility(VISIBLE);
        m_arrow.setRotation(0);
        m_circle_view.stopAnim();
        m_circle_view.setProgress(100);
        m_progress_view.setVisibility(GONE);
        m_state_detail_view.setVisibility(VISIBLE);
        m_state_detail_view.setText("检测版本");
    }

    public  void check_version_ui(){
        waiting_ui();
    }

    /**
     * 检测版本成功后ui
     * @param versionName 版本名称
     */
    public void after_success_check_version_ui(String versionName){
        m_state = STATE_START_DOWNLOAD; //开始下载状态
        m_circle_view.stopAnim();
        m_circle_view.setProgress(100);
        m_progress_view.setVisibility(GONE);
        m_arrow.setVisibility(VISIBLE);
        m_arrow.setRotation(180);
        m_state_detail_view.setVisibility(VISIBLE);
        m_state_detail_view.setText(String.format(getResources().getString(R.string.new_version),versionName));
    }

    /**
     * 检测版本失败后ui
     * @param status 返回失败状态码
     */
    public void after_fail_check_version_ui(int status,String info){
        m_circle_view.stopAnim();
        m_circle_view.setProgress(100);
        m_arrow.setVisibility(VISIBLE);
        m_progress_view.setVisibility(GONE);
        m_state_detail_view.setVisibility(VISIBLE);
        m_state_detail_view.setText(String.format(getResources().getString(R.string.error_info),status + "",info));
    }

    /**
     * 开始下载ui
     */
    public void start_download_ui(){
        m_circle_view.stopAnim();
        m_state_detail_view.setText("");
        m_circle_view.setProgress(0);
        m_progress_view.setText(0 + "%");
        m_arrow.setVisibility(GONE);
    }

    /**
     * 设置下载进度条
     * @param progress 下载进度值
     */
    public void setDownloadProgress(int progress){
        m_circle_view.setProgress(progress);
        m_state_detail_view.setText("");
        m_progress_view.setVisibility(VISIBLE);
        m_progress_view.setText(progress + "%");
    }

    /**
     * 可以更新的ui状态
     */
    public void can_update_ui(){
        m_state = STATE_UPGRADE;
        m_circle_view.setVisibility(VISIBLE);
        m_circle_view.setProgress(100);
        m_arrow.setVisibility(VISIBLE);
        m_arrow.setRotation(360);
        m_progress_view.setVisibility(GONE);
        m_state_detail_view.setVisibility(VISIBLE);
        m_state_detail_view.setText("升级");

    }

    /**
     * 等待中ui
     */
    public void waiting_ui(){
        m_circle_view.setProgress(10);
        m_circle_view.startAnim();
        m_arrow.setVisibility(GONE);
        m_state_detail_view.setText("");
        m_progress_view.setText("");
    }
    @Override
    public void onClick(View v) {
        if(m_listener == null)
            return;
        switch (m_state){
            case STATE_CHECK_VERSION:
                m_listener.on_check_version();
                break;
            case STATE_START_DOWNLOAD:
                m_listener.on_start_download();
                 break;
            case STATE_UPGRADE:
                m_listener.on_reboot_upgrade();
                break;
            case STATE_IDLE:
                break;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        m_listener = onClickListener;
        super.setOnClickListener(this);
    }

    public interface OnClickListener{
        void on_check_version();

        void on_start_download();

        void on_reboot_upgrade();
    }

}
