package codes.atticus.skymapper;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Liam on 11/25/2015.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {
    Camera camera;
    Cube bottomCube, topCube, leftCube, rightCube, frontCube, backCube;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // init camera
        camera = new Camera();
        // create cube
        float cubeDist = 50.0f;
        frontCube = new Cube();
        frontCube.translate(0.0f, 0.0f, -cubeDist);
        backCube = new Cube();
        backCube.translate(0.0f, 0.0f, cubeDist);
        leftCube = new Cube();
        leftCube.translate(-cubeDist, 0.0f, 0.0f);
        rightCube = new Cube();
        rightCube.translate(cubeDist, 0.0f, 0.0f);
        bottomCube = new Cube();
        bottomCube.translate(0.0f, -cubeDist, 0.0f);
        topCube = new Cube();
        topCube.translate(0.0f, cubeDist, 0.0f);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float aspect = (float) width / height;
        camera.updateProjectionMatrix(60.0f, aspect, 0.1f, 100.0f);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // calculate View Projection Matrix
        float[] vpMatrix = new float[16];
        Matrix.multiplyMM(vpMatrix, 0, camera.getProjectionMatrix(), 0, camera.getViewMatrix(), 0);
        // render
        //cube.rotate(0.5f, 1.0f, 1.0f, 0.5f);
        bottomCube.draw(vpMatrix);
        topCube.draw(vpMatrix);
        leftCube.draw(vpMatrix);
        rightCube.draw(vpMatrix);
        frontCube.draw(vpMatrix);
        backCube.draw(vpMatrix);
    }

    public void setCameraTransform(float[] rotationMatrix) {
        if(camera != null) {
            camera.setTransform(rotationMatrix);
            camera.updateViewMatrix();
        }
    }
}
