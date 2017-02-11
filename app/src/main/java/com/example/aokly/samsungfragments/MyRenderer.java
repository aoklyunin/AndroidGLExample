package com.example.aokly.samsungfragments;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.example.aokly.samsungfragments.defaultClasses.GLObject;
import com.example.aokly.samsungfragments.defaultClasses.Renderer;
import com.example.aokly.samsungfragments.simpleData.Color4;
import com.example.aokly.samsungfragments.simpleData.PointData;
import com.example.aokly.samsungfragments.simpleData.Vector3;

import java.util.ArrayList;

/**
 * Created by aokly on 11.02.2017.
 */
public class MyRenderer extends Renderer {
    GLObject triangle, triangle2, triangle3, triangle4;

    public void init() {
        ArrayList<Vector3> triangleLst = new ArrayList<>();
        triangleLst.add(new Vector3(-0.5f, -0.25f, 0.0f));
        triangleLst.add(new Vector3(0.5f, -0.25f, 0.0f));
        triangleLst.add(new Vector3(0.0f, 0.559016994f, 0.0f));

        triangle = new GLObject(triangleLst, new Color4(1.0f, 0.0f, 0.0f, 1.0f), GLES20.GL_TRIANGLES);

        ArrayList<PointData> triangleLst2 = new ArrayList<>();
        triangleLst2.add(new PointData(new Vector3(-0.5f, -0.8f, 0.0f), new Color4(1.0f, 0.0f, 0.0f, 1.0f)));
        triangleLst2.add(new PointData(new Vector3(0.5f, -0.8f, 0.0f), new Color4(0.0f, 0.0f, 1.0f, 1.0f)));
        triangleLst2.add(new PointData(new Vector3(0.0f, 0.6f, 0.0f), new Color4(0.0f, 1.0f, 0.0f, 1.0f)));

        triangle2 = new GLObject(triangleLst2, GLES20.GL_TRIANGLES);

        triangle3 = new GLObject(new float[]{
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f}
                , GLES20.GL_TRIANGLES);

        triangle4 = new GLObject(new float[]{
                -0.5f, -0.8f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.8f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.6f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f
        }, GLES20.GL_TRIANGLES);
    }

    public void process() {
        /* первая фигура */
        // по полученному времени
        long time = SystemClock.uptimeMillis() % 10000L;
        // генерируем угол в градусах
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        // Draw the triangle facing straight on.
        triangle.identMatrix();
        triangle.translate(new Vector3(-1, -1, 0));
        triangle.rotate(angleInDegrees * 2, new Vector3(0, 0, 1));
        triangle2.identMatrix();
        triangle2.translate(new Vector3(1, 1, 0));
        triangle2.rotate(angleInDegrees, new Vector3(0, 0, 1));
    }

    public void draw() {
        triangle.draw();
        triangle2.draw();
    }
}
