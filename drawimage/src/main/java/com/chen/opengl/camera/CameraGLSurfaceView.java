package com.chen.opengl.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.TextureView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-04-09 07:49
 * 描述:
 *
 *      GLSurfaceView继承了SurfaceView，用来专门显示OpenGL渲染
 *      GLSurfaceView可以用来显示视频 图像 3D模型等视图
 *      GLSurfaceView能够真正做到数据和显示分离，我们可以在此基础做一些视频数据的处理
 *
 *      GLSurfaceView.Renderer
 *          如果说GLSurfaceView是画布 ，那Renderer就是画笔，主要包含3个抽象函数：
 *          onSurfaceCreated onDrawFrame onSurfaceChanged
 *
 *
 */

public class CameraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer,SurfaceTexture.OnFrameAvailableListener {

    private Context context;

    private SurfaceTexture surfaceTexture;

    private int mTextureID = -1;

    private DirectDrawer mDirectDrawer;

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }
}
