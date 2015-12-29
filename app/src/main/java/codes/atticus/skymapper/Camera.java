package codes.atticus.skymapper;

import android.opengl.Matrix;

/**
 * Created by Liam on 11/29/2015.
 */
public class Camera extends SceneObject {
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] defaultViewMatrix = new float[16];

    public float[] getViewMatrix() { return viewMatrix; }

    public void updateViewMatrix() {
        Matrix.multiplyMM(viewMatrix, 0, getTransform(), 0, defaultViewMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f);
    }

    public float[] getProjectionMatrix() { return projectionMatrix; }

    public void updateProjectionMatrix(float fovY, float aspect, float near, float far) {
        Matrix.perspectiveM(projectionMatrix, 0, fovY, aspect, near, far);
    }

    public Camera() {
        Matrix.setLookAtM(defaultViewMatrix, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);
        viewMatrix = defaultViewMatrix.clone();
    }
}
