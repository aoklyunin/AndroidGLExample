package com.example.aokly.samsungfragments.defaultClasses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.aokly.samsungfragments.simpleData.Color4;
import com.example.aokly.samsungfragments.simpleData.PointData;
import com.example.aokly.samsungfragments.simpleData.Vector3;


/**
 * Created by aokly on 11.02.2017.
 */
public class GLObject {
    private static int mMVPMatrixHandle;
    private static int mPositionHandle;
    private static int mColorHandle;
    private static float[]  mProjectionMatrix;
    private static float[]  mViewMatrix;

    int mode;

    private final int mBytesPerFloat = 4;
    // сколько памяти нужно одной вершине
    private final int mStrideBytes = 7 * mBytesPerFloat;
    // какой переменной в массиве идёт первая координата
    private final int mPositionOffset = 0;
    // сколько всего координат
    private final int mPositionDataSize = 3;
    // какой переменной в массиве идёт первый цвет
    private final int mColorOffset = 3;
    // сколько компонент цвета
    private final int mColorDataSize = 4;

    private float[] mModelMatrix = new float[16];

    float pointData[];
    private FloatBuffer mPoints;

    void initData(float[] pointData,int mode){
        this.mode = mode;
        this.pointData = pointData.clone();
        mPoints = ByteBuffer.allocateDirect(this.pointData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPoints.put(this.pointData).position(0);
        //Log.e("CONSTRUCTOR",Arrays.toString(pointData));
    }

    public GLObject(float[] pointData,int mode ){
        initData(pointData,mode);
    }

    public GLObject(ArrayList<PointData> arr,int mode){
        float [] points = new float[arr.size()*7];
        for (int i = 0; i < arr.size(); i++) {
            points[i*7]=arr.get(i).getPos().getX();
            points[i*7+1]=arr.get(i).getPos().getY();
            points[i*7+2]=arr.get(i).getPos().getZ();

            points[i*7+3]=arr.get(i).getColor().getR();
            points[i*7+4]=arr.get(i).getColor().getG();
            points[i*7+5]=arr.get(i).getColor().getB();
            points[i*7+6]=arr.get(i).getColor().getA();
        }
        initData(points,mode);
    }

    public GLObject(ArrayList<Vector3> arr, Color4 color, int mode){
        float [] points = new float[arr.size()*7];
        for (int i = 0; i < arr.size(); i++) {
            points[i*7]=arr.get(i).getX();
            points[i*7+1]=arr.get(i).getY();
            points[i*7+2]=arr.get(i).getZ();

            points[i*7+3]=color.getR();
            points[i*7+4]=color.getG();
            points[i*7+5]=color.getB();
            points[i*7+6]=color.getA();
        }
        initData(points,mode);
    }

    private float[] mMVPMatrix = new float[16];

    public void draw(){
        mPoints.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, mPoints);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Передаем значения о цвете.
        mPoints.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, mPoints);
        GLES20.glEnableVertexAttribArray(mColorHandle);

         /* Первая фигура */
        // Перемножаем матрицу ВИДА на матрицу МОДЕЛИ, и сохраняем результат в матрицу MVP
        // (которая теперь содержит модель*вид).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        // Перемножаем матрицу модели*вида на матрицу проекции, сохраняем в MVP матрицу.
        // (которая теперь содержит модель*вид*проекцию).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(mode, 0, 3);
    }

    public void identMatrix(){
        Matrix.setIdentityM(mModelMatrix, 0);
    }
    public void rotate(float angleInDegrees, Vector3 axis){
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, axis.getX(), axis.getY(), axis.getZ());
    }
    public void translate(Vector3 v){
        Matrix.translateM(mModelMatrix, 0, v.getX(), v.getY(), v.getZ());
    }

    static void setDrawMatrices(int mMVPMatrixHandle, int mPositionHandle,int mColorHandle,
                                float [] mProjectionMatrix, float [] mViewMatrix){
        GLObject.mMVPMatrixHandle = mMVPMatrixHandle;
        GLObject.mPositionHandle = mPositionHandle;
        GLObject.mColorHandle = mColorHandle;
        GLObject.mProjectionMatrix = mProjectionMatrix;
        GLObject.mViewMatrix = mViewMatrix;

    }
}
