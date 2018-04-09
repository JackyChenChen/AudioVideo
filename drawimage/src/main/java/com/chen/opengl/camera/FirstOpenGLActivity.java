package com.chen.opengl.camera;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-04-09 15:49
 * 描述:
 *
 *      OpenGL  ---  Open Graphics Library
 *
 *
 */

public class FirstOpenGLActivity extends Activity{

    FirstSurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surfaceView = new FirstSurfaceView(this);

        setContentView(surfaceView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }
}
