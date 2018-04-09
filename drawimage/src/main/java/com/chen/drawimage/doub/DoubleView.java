package com.chen.drawimage.doub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-01-31 20:00
 * 描述:
 *
 *      双缓冲区:在显示缓冲区的基础上再创建一个Canvas和对应的Bitmap,
 *      在onDraw方法中通过显示缓冲区的Canvas绘制生成好的Bitmap
 *
 */

public class DoubleView  extends View{

    private Paint mPaint;
    private Canvas mBufferCanvas;
    private Bitmap mBufferBitmap;

    public DoubleView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (mBufferBitmap == null) {
                    mBufferBitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
                    mBufferCanvas = new Canvas(mBufferBitmap);
                }
                mBufferCanvas.drawCircle((int)event.getX(),(int)event.getY(),50,mPaint);
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBufferBitmap == null) {
            return;
        }
        canvas.drawBitmap(mBufferBitmap,0,0,null);
    }
}
