package codes.atticus.skymapper;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Liam on 11/27/2015.
 */
public class Cube extends Geometry {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private float[] mvpMatrix = new float[16];
    private final float[] vertexData = {
        // front face
        -1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        // right face
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        // back face
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        // left face
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        // top face
        -1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, 1.0f,
        // bottom face
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, -1.0f
    };
    private final String vertexShaderCode =
        "attribute vec4 aPosition;" +
        "uniform mat4 uMVPMatrix;" +
        "void main() {" +
            "gl_Position = uMVPMatrix * aPosition;" +
        "}";
    private final String fragmentShaderCode =
        "precision mediump float;" +
        "void main() {" +
            "gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
        "}";
    private int programHandle;

    public Cube() {
        // allocate buffers
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4 ).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // populate buffers & set position to 0
        vertexBuffer.put(vertexData).position(0);
        // load compiled shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        // create shader program
        programHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(programHandle, vertexShader);
        GLES20.glAttachShader(programHandle, fragmentShader);
        GLES20.glLinkProgram(programHandle);
    }

    public void draw(float[] vpMatrix) {
        // multiply by model matrix
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, getTransform(), 0);
        // add program to OpenGL environment
        GLES20.glUseProgram(programHandle);
        // get position handle
        int positionHandle = GLES20.glGetAttribLocation(programHandle, "aPosition");
        // enable handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        // get MVP Matrix handle
        int mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        // render primitives from array data
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.length / 3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
