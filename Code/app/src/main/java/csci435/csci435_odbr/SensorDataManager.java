package csci435.csci435_odbr;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.graphics.BitmapFactory;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.hardware.Sensor;

/**
 * Created by Rich on 2/11/16.
 */
public class SensorDataManager implements SensorEventListener {

    HashMap<Sensor, float[]> lastLoggedData = new HashMap<Sensor, float[]>();

    public void startRecording() {
        for (Sensor s : Globals.sensors) {
            Globals.sMgr.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
            lastLoggedData.put(s, new float[] {});
        }
    }


    public void stopRecording() {
        Globals.sMgr.unregisterListener(this);
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
