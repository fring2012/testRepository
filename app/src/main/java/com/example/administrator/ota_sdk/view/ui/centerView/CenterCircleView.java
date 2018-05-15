package com.example.administrator.ota_sdk.view.ui.centerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.administrator.ota_sdk.R;

public class CenterCircleView extends View {
    private final String TAG = "CenterCircleView";
    private int r;
    private int progress = 10;
    private Paint paint ;
    private int progressColor = Color.WHITE;
    private int max = 100;
    private int rWidth = 10;
    private Animation animation;



    public CenterCircleView(Context context) {
        super(context);
    }

    public CenterCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d(TAG,"开始绘制");
        int center = this.getWidth()/2;
        int r = getMeasuredWidth()/2;
        paint.setStrokeWidth(rWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setColor(progressColor);
        RectF oval = new RectF(0 + rWidth/2,0 + rWidth/2 ,2 * center - rWidth/2,2*center -  rWidth/2);
        canvas.drawArc(oval, 270, (360 * progress / max), false, paint); //逆时针转为负;

        super.onDraw(canvas);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if(this.progress < 0)
            return;
        invalidate();
    }
    public void startAnim(){
        animation.cancel();
        this.setAnimation(animation);
    }






}
