package com.example.aokly.samsungfragments;

import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;

import com.example.aokly.samsungfragments.defaultClasses.GLObject;
import com.example.aokly.samsungfragments.defaultClasses.Renderer;
import com.example.aokly.samsungfragments.simpleData.Color4;
import com.example.aokly.samsungfragments.simpleData.PointData;
import com.example.aokly.samsungfragments.simpleData.Vector3;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/*
    Класс для рисования OpenGL
 */

public class MyRenderer extends Renderer {
    // положение треугольника
    private Vector3 trianglePos = new Vector3();
    // угол поворота треугольника
    private float angleInDegrees;
    // время на предыдущем кадре
    private long prevTime;

    // коэффициент скорости
    private static float ACC_CONST = 2.0f;
    // объект треугольника
    private GLObject triangle;

    // инициализация
    public void init() {
        // треугольник
        ArrayList<Vector3> triangleLst = new ArrayList<>();
        triangleLst.add(new Vector3(-0.5f, -0.8f, 0.0f));
        triangleLst.add(new Vector3(0.5f, -0.8f, 0.0f));
        triangleLst.add(new Vector3(0.0f, 0.4f, 0.0f));
        triangle = new GLObject(triangleLst,new Color4(1.0f, 0.0f, 0.0f, 1.0f), GLES20.GL_TRIANGLES);
    }
    // обработка вызова
    public void process() {
        // получаем текущее время
        long time = SystemClock.uptimeMillis() % 10000L;
        // получаем разницу во времени между кадрами
        float deltaT = (float) (prevTime - time) / 1000;
        // запоминаем новое время
        prevTime = time;
        // считаем угол поворота в градусаъ
        angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // находим ускорение
        Vector3 triangleSpeed = new Vector3();
        // если наклон больше 0.1
        if (Math.abs(orientationX) > 0.1)
            triangleSpeed.setX(orientationX * ACC_CONST);
        // если наклон больше 0.1
        if (Math.abs(orientationY) > 0.1)
            triangleSpeed.setY(orientationY * ACC_CONST);

        // считаем положение треугольника
        trianglePos = trianglePos.sum(triangleSpeed.mul(deltaT));
        //Log.e("TRIANGLE_POS",trianglePos+"");
    }
    // рисование сцены
    public void draw() {
        // обнуляем матрицу треугольника
        triangle.identMatrix();
        // смещаем треугольник
        triangle.translate(trianglePos);
        // поворачиваем треугольник
        //triangle.rotate(angleInDegrees, new Vector3(0, 0, 1));
        // рисуем треугольник
        triangle.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.onSurfaceChanged(glUnused, width, height);
        trianglePos = new Vector3();
    }
}
