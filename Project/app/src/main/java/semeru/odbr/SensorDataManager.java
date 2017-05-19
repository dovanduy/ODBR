package semeru.odbr;

import java.util.Arrays;
import java.util.HashMap;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;
import android.content.Context;
import android.hardware.Sensor;

/**
 * Created by Richard Bonett on 2/11/16.
 * Manages the sensors that we are going to be listening for. It allows for the start / stop of the listening process
 * where the listening registers changes to the values. If a change is detected then the value is registered.
 */
public class SensorDataManager implements SensorEventListener {

    HashMap<Sensor, float[]> lastLoggedData = new HashMap<Sensor, float[]>();
    OrientationLogger orientationLogger;

    public SensorDataManager(Context c) {
        orientationLogger = new OrientationLogger(c);
    }

    /**
     * Method to start recording of all designated sensors
     */
    public void startRecording() {
        for (Sensor s : Globals.sensors) {
            Globals.sMgr.registerListener(this, s, (SensorManager.SENSOR_DELAY_NORMAL)*10);
            lastLoggedData.put(s, new float[] {});
        }
        orientationLogger.enable();
    }

    /**
     * Method to stop recording all designated sensors
     */
    public void stopRecording() {
        Globals.sMgr.unregisterListener(this);
        orientationLogger.disable();
    }


    /**
     * Adds sensor data to the BugReport
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!Arrays.equals(lastLoggedData.get(event.sensor), event.values)) {
            BugReport.getInstance().addSensorData(event.sensor, event);
            lastLoggedData.put(event.sensor, event.values.clone());
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {/* Nothing to do */}



}

/**
 * Listener class to register the orientation of the device, associating the orientation with times.
 */
class OrientationLogger extends OrientationEventListener {

    public OrientationLogger(Context c) {
        super(c);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        Log.d("Orientation", "Changed to: " + orientation);
        BugReport.getInstance().addOrientationChange(System.currentTimeMillis(), orientation);
    }
}