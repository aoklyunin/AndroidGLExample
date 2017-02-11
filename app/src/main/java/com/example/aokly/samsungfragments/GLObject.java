package com.example.aokly.samsungfragments;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;


/**
 * Created by aokly on 11.02.2017.
 */
public class GLObject {
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

    public GLObject(float[] pointData) {
        this.pointData = pointData;
        mPoints = ByteBuffer.allocateDirect(this.pointData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPoints.put(this.pointData).position(0);
    }

    void draw(int mMVPMatrixHandle, int mPositionHandle,int mColorHandle,
              float[] mViewMatrix ,float[] mProjectionMatrix,float[] mMVPMatrix){
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    void identMatrix(){
        Matrix.setIdentityM(mModelMatrix, 0);
    }
    void rotate(float angleInDegrees, Vector3 axis){
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, axis.getX(), axis.getY(), axis.getZ());
    }
    void translate(Vector3 v){
        Matrix.translateM(mModelMatrix, 0, v.getX(),v.getY(), v.getZ());
    }
}
