package com.example.administrator.ota_demo.view.ui.centerView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.example.administrator.ota_demo.R;

public class CommonCircleView extends View {
    public final static String TAG = "CommonCircleView";
    private final Paint paint;
    private final Context mContext;
    private Resources resources;
    private int max = 100;
    private int progress = 0;
    private int ringWidth;
    // 圆环的颜色
    private int ringColor;
    // 进度条颜色
    private int progressColor;

    private String textProgress;
    //控件宽度
    private int mWidth;
    //控件半径
    private int mDiameter;

    private Animation animation;
//    public CommonCircleView(Context context, Context mContext) {
//        super(context);
//        this.mContext = mContext;
//        paint = new Paint();
//    }

    public CommonCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.paint = new Paint();
        this.resources = context.getResources();
        this.paint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.iot_custom_view);

        this.ringColor = a.getColor(R.styleable.iot_custom_view_iot_ringColor, Color.WHITE);
        this.progressColor = a.getColor(R.styleable.iot_custom_view_iot_progressColor,Color.WHITE);
        this.ringWidth = dip2px(context,a.getInt(R.styleable.iot_custom_view_iot_integerSize,0));
        this.mDiameter = dip2px(context,a.getInt(R.styleable.iot_custom_view_iot_diameter,0));
        this.progress = a.getInt(R.styleable.iot_custom_view_iot_progressSize,0);
        a.recycle();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 设置加载进度，取值范围在0~之间
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress >= 0 && progress <= max) {
            this.progress = progress;
            //刷新view
            invalidate();
        }
    }

    /**
     * 获取当前进度值
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置圆环背景色
     *
     * @param ringColor
     */
    public void setRingColor(int ringColor) {
        this.ringColor = resources.getColor(ringColor);
    }


    /**
     * 通过不断画弧的方式更新界面，实现进度增加
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int center = this.getWidth() / 2;
        int radios = center - ringWidth / 2;//半径

        // 绘制圆环
        this.paint.setStyle(Paint.Style.STROKE); // 绘制空心圆
        this.paint.setColor(ringColor);
        this.paint.setStrokeWidth(ringWidth);//圆圈的粗细
        canvas.drawCircle(center, center, radios, this.paint);
        RectF oval = new RectF(center - radios, center - radios, center
                + radios, center + radios);
        this.paint.setColor(progressColor);


        //画有弧度的
        if (progress > 0) {
            this.paint.setAntiAlias(true);//设置抗锯齿，如果不设置，加载位图的时候可能会出现锯齿状的边界，如果设置，边界就会变的稍微有点模糊，锯齿就看不到了。
            this.paint.setDither(true);//设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawArc(oval, 270, (360 * progress / max), false, paint); //逆时针转为负
        }

        super.onDraw(canvas);
    }
    public void loadXmlAnim(int animXml){
        animation = AnimationUtils.loadAnimation(mContext,animXml);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
    }

    public void stopAnim(){
        if(animation != null){
            this.clearAnimation();
        }
    }
    public void startAnim(){
        if(animation != null){
            this.clearAnimation();
            this.startAnimation(animation);
        }
    }
}
