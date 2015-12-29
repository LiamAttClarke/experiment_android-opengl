package codes.atticus.skymapper;

import android.opengl.GLES20;
import android.transition.Scene;

/**
 * Created by Liam on 11/28/2015.
 */
public abstract class Geometry extends SceneObject {

    public abstract void draw(float[] mvpMatrix);

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader( type );
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader( shader );
        return shader;
    }
}
