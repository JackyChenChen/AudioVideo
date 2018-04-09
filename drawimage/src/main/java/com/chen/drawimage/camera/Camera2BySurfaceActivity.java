package com.chen.drawimage.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.drawimage.R;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-03-01 20:34
 * 描述:
 *
 *      Camera2的基本使用
 *
 */

public class Camera2BySurfaceActivity extends Activity implements View.OnClickListener {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static{
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView imageView;
    private CameraManager cameraManager;
    private Handler childHanler,mainHandler;
    private String cameraID;
    private ImageReader imageReader;
    private CameraCaptureSession cameraCaptureSession;
    private CameraDevice cameraDevice;

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
        surfaceView.setOnClickListener(this);
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                initCamera2();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                releaseCamera2();
            }
        });
    }

    /**
     * 初始化相机
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initCamera2(){
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHanler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        cameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;
        imageReader = ImageReader.newInstance(1080,1920, ImageFormat.JPEG,1);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                cameraDevice.close();
                surfaceView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Image image = imageReader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//由缓冲区存入字节数组
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        },mainHandler);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开摄像头
            cameraManager.openCamera(cameraID, stateCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void releaseCamera2(){
        if (null != cameraDevice){
            cameraDevice.close();
            cameraDevice = null;
        }
    }


    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            Camera2BySurfaceActivity.this.cameraDevice = cameraDevice;
            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            releaseCamera2();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            Toast.makeText(Camera2BySurfaceActivity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();

        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePreview(){

        try {
            // 创建预览需要的CaptureRequest.Builder
            final CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(surfaceHolder.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()), new CameraCaptureSession.StateCallback()
            {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == cameraDevice) return;
                    // 当摄像头已经准备好时，开始显示预览
                    Camera2BySurfaceActivity.this.cameraCaptureSession = cameraCaptureSession;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        Camera2BySurfaceActivity.this.cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHanler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(Camera2BySurfaceActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                }
            }, childHanler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        takePicture();
    }


    /**
     * 拍照
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePicture() {
        if (cameraDevice == null) return;
        // 创建拍照需要的CaptureRequest.Builder
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(imageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            cameraCaptureSession.capture(mCaptureRequest, null, childHanler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
