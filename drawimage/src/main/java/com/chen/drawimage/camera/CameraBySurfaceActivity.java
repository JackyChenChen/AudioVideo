package com.chen.drawimage.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.chen.drawimage.R;
import java.io.IOException;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-02-26 15:09
 * 描述:
 *
 *      Camera的使用
 */

public class CameraBySurfaceActivity extends Activity implements View.OnClickListener{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private ImageView imageView;
    private int viewWidth,viewHeight;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        initView();
    }

    private void initView(){
        imageView = findViewById(R.id.imageView);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        //surfaceView不需要自己的缓冲区
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                releaseCamera();
            }
        });
        surfaceView.setOnClickListener(this);
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        try{
            camera = Camera.open();
        }catch (RuntimeException e){
            camera = Camera.open(Camera.getNumberOfCameras()-1);
        }

        //摄像头进行90度旋转
        camera.setDisplayOrientation(90);
        if (camera != null){
            try {
                Camera.Parameters parameters = camera.getParameters();
                //设置预览照片大小
                parameters.setPreviewSize(viewWidth,viewHeight);
                //设置相机预览照片帧数
                parameters.setPreviewFpsRange(4,10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片质量
                parameters.set("jpeg-quality",90);
                //设置照片的大小
                parameters.setPictureSize(viewWidth,viewHeight);
                //通过surfaceview显示预览
                camera.setPreviewDisplay(surfaceHolder);
                //开始预览
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera(){
        if (camera != null){
            camera.stopPreview();
            camera.release();
        }
    }


    @Override
    public void onClick(View view) {
        if (camera == null){
            return;
        }
        //自动对焦后拍照
        camera.autoFocus(autoFocusCallback);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (surfaceView != null){
            viewWidth = surfaceView.getWidth();
            viewHeight = surfaceView.getHeight();
        }
    }

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            //对焦成功
            if (b){
                camera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {

                    }
                },pictureCallback);
            }
        }
    };

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource == null) {
                Toast.makeText(CameraBySurfaceActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();
            }
            final Matrix matrix = new Matrix();
            matrix.setRotate(90);
            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
            if (bitmap != null && imageView != null && imageView.getVisibility() == View.GONE) {
                camera.stopPreview();
                imageView.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.GONE);
                Toast.makeText(CameraBySurfaceActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                imageView.setImageBitmap(bitmap);
            }

        }
    };

}
