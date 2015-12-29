package codes.atticus.skymapper;

import android.opengl.Matrix;

/**
 * Created by Liam on 11/29/2015.
 */
public class SceneObject {
    private float[] transform = {
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    };

    public float[] getTransform() { return transform; }
    public void setTransform(float[] newTransform) { transform = newTransform; }

    public void translate(float x, float y, float z) {
        Matrix.translateM(transform, 0, x, y, z);
    }

    public void rotate(float x, float y, float z, float angle) {
        Matrix.rotateM(transform, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(transform, 0, x, y, z);
    }
}
