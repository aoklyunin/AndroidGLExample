package com.example.aokly.samsungfragments.defaultClasses;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.aokly.samsungfragments.simpleData.Vector3;


// TODO:
// надо обрабатывать корректно поворот планшета
abstract public class Renderer implements GLSurfaceView.Renderer {
    // ориентация планшета X
    protected float orientationX;
    // ориентация планшета Y
    protected float orientationY;

    // матрица вида
    private float[] mViewMatrix = new float[16];
    // матрица проекций
    private float[] mProjectionMatrix = new float[16];


    protected int mMVPMatrixHandle ;
    protected int mPositionHandle ;
    protected int mColorHandle ;
    protected int programHandle ;

    // положение камеры
    protected Vector3 pos;
    // вектор направления
    protected Vector3 dir;
    // вектор вверх
    protected Vector3 up;

    // задаём ориентацию X
    public void setOrientationY(float orientationY) {
        this.orientationY = orientationY;
    }
    // задаём ориентацию Y
    public void setOrientationX(float orientationX) {
        this.orientationX = orientationX;
    }

    // инициализация
    public abstract void init();

    // конструктор
    public Renderer() {

        init();
    }

    // рисование
    abstract public void draw();
    // обработка математики
    abstract public void process();

    // рисование
    @Override
    public void onDrawFrame(GL10 glUnused) {
        // очищаем экран
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, pos.getX(), pos.getY(), pos.getZ(), dir.getX(), dir.getY(), dir.getZ(), up.getX(), up.getY(), up.getZ());
        // меняем положения и углы
        process();
        // рисуем
        draw();
    }

    // когда создаётся поверхность рисования
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Устанавливаем цвет фона светло серый.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);


    }

    // если поверхность была изменена
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Устанавливаем OpenGL окно просмотра того же размера что и поверхность экрана.
        GLES20.glViewport(0, 0, width, height);
        // Создаем новую матрицу проекции. Высота остается та же,
        // а ширина будет изменяться в соответствии с соотношением сторон.
        final float ratio = (float) (width) / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

        pos = new Vector3(0.0f,0.0f,1.5f);
        dir = new Vector3(0.0f,0.0f,-5.0f);
        up = new Vector3(0.0f,1.0f,0.0f);

        // Устанавливаем матрицу ВИДА. Она описывает положение камеры.
        // Примечание: В OpenGL 1, матрица ModelView используется как комбинация матрицы МОДЕЛИ
        // и матрицы ВИДА. В OpenGL 2, мы можем работать с этими матрицами отдельно по выбору.
        Matrix.setLookAtM(mViewMatrix, 0, pos.getX(), pos.getY(), pos.getZ(), dir.getX(), dir.getY(), dir.getZ(), up.getX(), up.getY(), up.getZ());
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"     // Константа отвечающая за комбинацию матриц МОДЕЛЬ/ВИД/ПРОЕКЦИЯ.

                        + "attribute vec4 a_Position;     \n"     // Информация о положении вершин.
                        + "attribute vec4 a_Color;        \n"     // Информация о цвете вершин.

                        + "varying vec4 v_Color;          \n"     // Это будет передано в фрагментный шейдер.

                        + "void main()                    \n"     // Начало программы вершинного шейдера.
                        + "{                              \n"
                        + "   v_Color = a_Color;          \n"     // Передаем цвет для фрагментного шейдера.
                                                                    // Он будет интерполирован для всего треугольника.
                        + "   gl_Position = u_MVPMatrix   \n"     // gl_Position специальные переменные используемые для хранения конечного положения.
                        + "               * a_Position;   \n"     // Умножаем вершины на матрицу для получения конечного положения
                        + "}                              \n";    // в нормированных координатах экрана.
        final String fragmentShader =
                "precision mediump float;       \n"     // Устанавливаем по умолчанию среднюю точность для переменных. Максимальная точность
                        // в фрагментном шейдере не нужна.
                        + "varying vec4 v_Color;          \n"     // Цвет вершинного шейдера преобразованного
                        // для фрагмента треугольников.
                        + "void main()                    \n"     // Точка входа для фрагментного шейдера.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"     // Передаем значения цветов.
                        + "}                              \n";
        // Загрузка вершинного шейдера.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vertexShaderHandle != 0) {
            // Передаем в наш шейдер программу.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);
            // Компиляция шейреда
            GLES20.glCompileShader(vertexShaderHandle);
            // Получаем результат процесса компиляции
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            // Если компиляция не удалась, удаляем шейдер.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }
        if (vertexShaderHandle == 0)
            throw new RuntimeException("Error creating vertex shader.");
        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);
            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }
        // Создаем объект программы вместе со ссылкой на нее.
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            // Подключаем вершинный шейдер к программе.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            // Подключаем фрагментный шейдер к программе.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            // Подключаем атрибуты цвета и положения
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            // Объединяем оба шейдера в программе.
            GLES20.glLinkProgram(programHandle);
            // Получаем ссылку на программу.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // Если ссылку не удалось получить, удаляем программу.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0)
            throw new RuntimeException("Error creating program.");
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
        GLObject.setDrawMatrices(mMVPMatrixHandle,mPositionHandle,mColorHandle,mProjectionMatrix,mViewMatrix);
    }

}