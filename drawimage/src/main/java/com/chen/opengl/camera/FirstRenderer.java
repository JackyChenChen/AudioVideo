package com.chen.opengl.camera;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-04-09 15:57
 * 描述:
 */

public class FirstRenderer implements GLSurfaceView.Renderer {


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //设置清屏颜色
        GLES20.glClearColor(1f, 0f, 0f, 0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        //设置窗口大小
        GLES20.glViewport(0,0,i,i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //调用glClear(GL10.GL_COLOR_BUFFER_BIT)方法清除屏幕颜色,执行这个方法之后
        //屏幕就会渲染之前通过glClearColor设置的清屏颜色.
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }
}
