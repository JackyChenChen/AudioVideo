package com.chen.drawimage.single;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-01-31 19:16
 * 描述:
 *     继承View
 *     单缓冲区 -- 显示缓冲区
 *
 *     Paint.ANTI_ALIAS_FLAG            ---  消除锯齿
 *     Paint.DITHER_FLAG                ---  抗抖动
 *
 *     Paint.Style.STROKE               ---  只绘制图形轮廓(描边)
 *     Paint.Style.FILL                 ---  只绘制图形内容
 *     Paint.Style.FILL_AND_STROKE      ---  既绘制轮廓也绘制内容
 *
 *
 *     说明在onDraw执行完成之后，才回把数据交给GPU去处理展示---显示缓冲区
 *
 */

public class SingleImage extends View{

    //画笔
    private Paint mPaint;

    private Rect rect = new Rect(100,100,800,400);


    public SingleImage(Context context) {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(rect,mPaint);

        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        canvas.drawCircle(450,1000,400,mPaint);

    }

}
