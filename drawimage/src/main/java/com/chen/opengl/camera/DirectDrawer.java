package com.chen.opengl.camera;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-04-09 07:58
 * 描述:
 *
 *      FloatBuffer:
 *
 *
 *      负责将SurfaceTexture(纹理句柄) 内容绘制到屏幕上
 *
 */

public class DirectDrawer {

    private FloatBuffer vertexBuffer, mTextureCoordsBuffer;

    private ShortBuffer drawListBuffer;

    private int mProgram;

    private int mPositionHandle;

    private int mTextureCoordHandle;

    private int mMVPMatrixHandle;

    private short drawOrder[] = {0, 2, 1, 0, 3, 2}; // order to draw vertices

    // number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = 2;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private float mVertices[] = new float[8];

    private float mTextureCoords[] = new float[8];

    private float mTextHeightRatio = 0.1f;

    private int texture;
    public float[] mMVP = new float[16];

//    public void resetMatrix() {
//        mat4f_LoadOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, mMVP);
//    }
}
