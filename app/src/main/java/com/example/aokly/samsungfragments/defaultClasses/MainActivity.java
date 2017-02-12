package com.example.aokly.samsungfragments.defaultClasses;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aokly.samsungfragments.MyRenderer;
import com.example.aokly.samsungfragments.R;


public class MainActivity extends Activity implements SensorEventListener {

    private GLSurfaceView mGLSurfaceView;
    private Renderer renderer;

    float[] rotationMatrix = new float[16];
    float[] accelData = new float[3];
    float[] magnetData = new float[3];
    float[] OrientationData = new float[3];
    SensorManager msensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mGLSurfaceView = new GLSurfaceView(this);

        // Проверяем поддереживается ли OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Запрос OpenGL ES 2.0 для установки контекста.
            mGLSurfaceView.setEGLContextClientVersion(2);
            renderer = new MyRenderer();
            // Устанавливаем рендеринг, создаем экземпляр класса, он будет описан ниже.
            mGLSurfaceView.setRenderer(renderer);
        } else {
            // Устройство поддерживает только OpenGL ES 1.x
            // опишите реализацию рендеринга здесь, для поддержку двух систем ES 1 and ES 2.
            return;
        }

        ((LinearLayout) findViewById(R.id.glView)).addView(mGLSurfaceView);

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout);
        ViewGroup.LayoutParams params = ll.getLayoutParams();
        params.width = 500;
        ll.setLayoutParams(params);
        //Log.d( "WIDTH_CREATE",""+findViewById(R.id.linearLayout).getMeasuredWidth());

    }

    @Override
    protected void onResume() {
        // Вызывается GL surface view's onResume() когда активити переходит onResume().
        super.onResume();
        mGLSurfaceView.onResume();
        msensorManager.registerListener(this, msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        msensorManager.registerListener(this, msensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    private void loadNewSensorData(SensorEvent event) {
        final int type = event.sensor.getType(); //Определяем тип датчика
        if (type == Sensor.TYPE_ACCELEROMETER) { //Если акселерометр
            accelData = event.values.clone();
        }

        if (type == Sensor.TYPE_MAGNETIC_FIELD) { //Если геомагнитный датчик
            magnetData = event.values.clone();
        }
    }


    @Override
    protected void onPause() {
        // Вызывается GL surface view's onPause() когда наше активити onPause().
        super.onPause();
        mGLSurfaceView.onPause();
        msensorManager.unregisterListener(this);
    }


    // обработчик сенсоров
    @Override
    public void onSensorChanged(SensorEvent event) {
        loadNewSensorData(event); // Получаем данные с датчика
        SensorManager.getRotationMatrix(rotationMatrix, null, accelData, magnetData); //Получаем матрицу поворота
        SensorManager.getOrientation(rotationMatrix, OrientationData); //Получаем данные ориентации устройства в пространстве

        renderer.setOrientationX(OrientationData[2]);
        renderer.setOrientationY(OrientationData[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
