package chen.com.audiovideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.chen.drawimage.doub.DoubleView;
import com.chen.drawimage.single.SingleImage;
import com.chen.drawimage.single.SingleView;
import com.chen.drawimage.surface.SinView;

/**
 * 版权:中国东方航空-信息部-移动互联部
 * 作者:JackyChen
 * 日期:2018-01-31 20:05
 * 描述:
 */

public class DrawActivity extends AppCompatActivity {


    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = getIntent().getStringExtra("path");

        addView();

    }

    private void addClass(){
        try {
            View view = (View) Class.forName(path).newInstance();
            setContentView(view);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addView(){
        View view = null;
        if (path.contains("SingleImage")){
            view = new SingleImage(this);
        }else if (path.contains("SingleView")){
            view = new SingleView(this);
        }else if (path.contains("DoubleView")){
            view = new DoubleView(this);
        }else if (path.contains("SinView")){
            view = new SinView(this);
        }
        setContentView(view);
    }

}
