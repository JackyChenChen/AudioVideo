package com.chen.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.chen.drawimage.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-03-05 21:41
 * 描述:
 *
 *      视频分离
 *
 */

public class MediaExtractorActivity extends Activity {

    private String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

    MediaExtractor mediaExtractor;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_extractor);
        mediaExtractor = new MediaExtractor();
    }

    public void extractor(View view){
        extractorMedia();
    }

    /**
     * 分离视频
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void extractorMedia(){
        verifyStoragePermissions(this);
        FileOutputStream videoOutputStream = null;
        FileOutputStream audioOutputStream = null;
        try{
            File videoFile = new File(SDCARD_PATH ,"output_video.mp4");
            File audioFile = new File(SDCARD_PATH,"output_audio");
            videoOutputStream = new FileOutputStream(videoFile);
            audioOutputStream = new FileOutputStream(audioFile);
            mediaExtractor.setDataSource(SDCARD_PATH + "/input.mp4");
            int trackCount = mediaExtractor.getTrackCount();
            int audioTrackIndex = -1;
            int videoTrackIndex = -1;
            for (int i = 0; i < trackCount; i++){
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String mineType = trackFormat.getString(MediaFormat.KEY_MIME);
                //视频通道
                if (mineType.startsWith("video/")){
                    videoTrackIndex = i;
                }
                //音频通道
                if (mineType.startsWith("audio/")){
                    audioTrackIndex = i;
                }
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(500*1024);
            //切换到视频通道
            mediaExtractor.selectTrack(videoTrackIndex);
            while(true){
                int readSampleCount = mediaExtractor.readSampleData(byteBuffer,0);
                if (readSampleCount < 0){
                    break;
                }
                //保存视频通道信息
                byte[] buffer = new byte[readSampleCount];
                byteBuffer.get(buffer);
                videoOutputStream.write(buffer);
                byteBuffer.clear();
                mediaExtractor.advance();
            }
            //切换到音频通道
            mediaExtractor.selectTrack(audioTrackIndex);
            while (true){
                int readSampleCount = mediaExtractor.readSampleData(byteBuffer,0);
                if (readSampleCount < 0){
                    break;
                }
                //保存音频通道信息
                byte[] buffer = new byte[readSampleCount];
                byteBuffer.get(buffer);
                audioOutputStream.write(buffer);
                byteBuffer.clear();
                mediaExtractor.advance();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mediaExtractor.release();

        }
    }

    /**
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void muxerMedia(View view){
        int videoIndex = -1;
        try {
            mediaExtractor.setDataSource(SDCARD_PATH + "/input.mp4");
            int trackCount = mediaExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++){
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String mimeType = trackFormat.getString(MediaFormat.KEY_MIME);
                //取出视频的信号
                if (mimeType.startsWith("video/")){
                    videoIndex = i;
                }
            }
            //切换到视频信号的通道
            mediaExtractor.selectTrack(videoIndex);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(videoIndex);
            MediaMuxer mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output_video.mp4",
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            //追踪此通道
            int trackIndex = mediaMuxer.addTrack(trackFormat);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024*500);
            MediaCodec.BufferInfo bufferInfo  = new MediaCodec.BufferInfo();
            mediaMuxer.start();
            long videoSampleTime;
            //获取每帧之间的时间
            {
                mediaExtractor.readSampleData(byteBuffer,0);
                if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC){
                    mediaExtractor.advance();
                }
                mediaExtractor.readSampleData(byteBuffer,0);
                long firstVideoPTS = mediaExtractor.getSampleTime();
                mediaExtractor.advance();
                long secondVideoPTS = mediaExtractor.getSampleTime();
                videoSampleTime = Math.abs(secondVideoPTS - firstVideoPTS);
            }
            //重新切换此通道，不然上面跳过了3帧，造成前面的帧数据模糊
            mediaExtractor.unselectTrack(videoIndex);
            mediaExtractor.selectTrack(videoIndex);
            while (true){
                //读取帧之间的数据
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer,0);
                if (readSampleSize < 0){
                    break;
                }
                mediaExtractor.advance();
                bufferInfo.size = readSampleSize;
                bufferInfo.offset = 0;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.presentationTimeUs += videoSampleTime;
                //写入帧数据
                mediaMuxer.writeSampleData(trackIndex,byteBuffer,bufferInfo);
            }
            //release
            mediaMuxer.stop();
            mediaExtractor.release();
            mediaMuxer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
