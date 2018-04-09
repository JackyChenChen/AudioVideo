package com.chen.drawimage.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-01-31 20:44
 * 描述:
 *
 *      绘制正弦函数
 *
 *      取值有Cap.ROUND(圆形线帽)、Cap.SQUARE(方形线帽)、Paint.Cap.BUTT(无线帽)
 *
 *      Join.MITER（结合处为锐角） Join.Round(结合处为圆弧) Join.BEVEL(结合处为直线)
 *
 */

public class SinView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private int x;
    private int y;
    private Path mPath;
    private Paint mPaint;

    public SinView(Context context) {
        super(context);
        init();
    }

    public SinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }


    //在创建时激发，一般在这里调用画图的线程。
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsDrawing = true;
        mPath.moveTo(0, 400);
        new Thread(this).start();
    }

    //在surface的大小发生改变时激发
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    //销毁时激发，一般在这里将画图的线程停止、释放。
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            draw();
            x += 3;
            y = (int) (100*Math.sin(x * Math.PI / 180) + 400);
            mPath.lineTo(x, y);
        }
    }


    private void draw() {
        try {
            // 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
            mCanvas = mHolder.lockCanvas();
            // SurfaceView背景
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                // 结束锁定画图，并提交改变。
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
