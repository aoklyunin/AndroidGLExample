package com.example.aokly.samsungfragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

    /** Создаем экземпляр нашего GLSurfaceView */
    private GLSurfaceView mGLSurfaceView;
    private Renderer renderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLSurfaceView = new GLSurfaceView(this);

        // Проверяем поддереживается ли OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Запрос OpenGL ES 2.0 для установки контекста.
            mGLSurfaceView.setEGLContextClientVersion(2);
            renderer = new Renderer();
            // Устанавливаем рендеринг, создаем экземпляр класса, он будет описан ниже.
            mGLSurfaceView.setRenderer(renderer);
        }
        else
        {
            // Устройство поддерживает только OpenGL ES 1.x
            // опишите реализацию рендеринга здесь, для поддержку двух систем ES 1 and ES 2.
            return;
        }

        ((LinearLayout)findViewById(R.id.glView)).addView(mGLSurfaceView);

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout);
        ViewGroup.LayoutParams params = ll.getLayoutParams();
        params.width = 500;
        ll.setLayoutParams(params);
        //Log.d( "WIDTH_CREATE",""+findViewById(R.id.linearLayout).getMeasuredWidth());

    }
    @Override
    protected void onResume()
    {
        // Вызывается GL surface view's onResume() когда активити переходит onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // Вызывается GL surface view's onPause() когда наше активити onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }


}
