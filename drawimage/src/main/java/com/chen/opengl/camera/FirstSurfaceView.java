package com.chen.opengl.camera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-04-09 15:52
 * 描述:
 *
 * Renderer的渲染模式
 *
 *      GLSurfaceView.RENDERMODE_CONTINUOUSLY
 *      不间断的绘制，默认渲染模式是这种
 *      GLSurfaceView.RENDERMODE_WHEN_DIRTY
 *      不会主动绘制，调用GLSurface的requestRender()方法后才会执行一次（第一次运行时候会自动执行一次）
 *
 */

public class FirstSurfaceView extends GLSurfaceView {

    FirstRenderer renderer;


    public FirstSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init(){
        //设置一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        //创建渲染器实例
        renderer = new FirstRenderer();
        //设置渲染器
        setRenderer(renderer);
        //设置渲染模式
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
