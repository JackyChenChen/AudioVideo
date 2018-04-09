package chen.com.audiovideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chen.drawimage.camera.Camera2BySurfaceActivity;
import com.chen.drawimage.camera.CameraBySurfaceActivity;
import com.chen.opengl.camera.FirstOpenGLActivity;
import com.chen.video.MediaExtractorActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    /**
     * 绘制显示缓冲区
     * @param view
     */
    public void singleImage(View view){
        jump2NextActivity("com.chen.drawimage.single.SingleImage");
    }

    /**
     * 绘制显示缓冲区
     * @param view
     */
    public void singleView(View view){
        jump2NextActivity("com.chen.drawimage.single.SingleView");
    }

    /**
     * SurfaceView画正弦
     * @param view
     */
    public void sinView(View view){
        jump2NextActivity("com.chen.drawimage.surface.SinView");
    }

    /**
     * 双缓冲区
     * @param view
     */
    public void doubleView(View view){
        jump2NextActivity("com.chen.drawimage.doub.DoubleView");
    }




    /**
     * 跳转到下一个UI
     * @param path
     */
    private void jump2NextActivity(String path){
        Intent intent = new Intent(this,DrawActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);
    }


    public void Camera(View view){
        Intent intent = new Intent(this,CameraBySurfaceActivity.class);
        startActivity(intent);
    }

    public void Camera2(View view){
        Intent intent = new Intent(this,Camera2BySurfaceActivity.class);
        startActivity(intent);
    }

    public void Media(View view){
        Intent intent = new Intent(this,MediaExtractorActivity.class);
        startActivity(intent);
    }

    public void firstOpenGL(View view){
        Intent intent = new Intent(this, FirstOpenGLActivity.class);
        startActivity(intent);
    }


}
