package com.chen.drawimage.single;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-01-31 20:01
 * 描述:
 */

public class SingleView extends View {

    private Paint mPaint;

    private List<Point> mPoints;


    public SingleView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        setBackgroundColor(Color.WHITE);
        mPoints = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mPoints.add(new Point((int)event.getX(),(int)event.getY()));
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
        for (Point p : mPoints) {
            canvas.drawCircle(p.x,p.y,50,mPaint);
        }
    }
}
