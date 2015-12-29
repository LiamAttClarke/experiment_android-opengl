package codes.atticus.skymapper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Liam on 11/25/2015.
 */

public class CaptureActivity extends Activity implements SensorEventListener {
    private GLSurfaceView surfaceView;
    private OpenGLRenderer renderer;
    private SensorManager sensorManager;

    private Sensor rotationVectorSensor;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;

    private float[] gravityVector = new float[9];
    private float[] compassVector = new float[9];
    private float[] deviceRotationMatrix = new float[16];

    private boolean hasGyroscope = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize sensor manager / sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(rotationVectorSensor != null) {
            hasGyroscope = true;
        } else {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        // initialize OpenGL view
        surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        renderer = new OpenGLRenderer();
        surfaceView.setRenderer(renderer);
        setContentView(surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
        // register device rotation listener
        if(hasGyroscope) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
        // deregister device rotation listener
        if(hasGyroscope) {
            sensorManager.unregisterListener(this, rotationVectorSensor);
        } else {
            sensorManager.unregisterListener(this, accelerometerSensor);
            sensorManager.unregisterListener(this, magnetometerSensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ROTATION_VECTOR:
                SensorManager.getRotationMatrixFromVector(deviceRotationMatrix, event.values);
            case Sensor.TYPE_ACCELEROMETER:
                gravityVector = event.values;
                SensorManager.getRotationMatrix(deviceRotationMatrix, null, gravityVector, compassVector);
            case Sensor.TYPE_MAGNETIC_FIELD:
                compassVector = event.values;
                SensorManager.getRotationMatrix(deviceRotationMatrix, null, gravityVector, compassVector);
        }
        renderer.setCameraTransform(deviceRotationMatrix); // replace with scene graph !!!
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}