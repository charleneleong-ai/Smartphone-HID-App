package com.example.leongchar.accelerometerdisplay;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //define the log tag
    private static final String TAG = "MainActivity";
    private static final float ALPHA = 0.5f;

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroSensor, magnoSensor, lightSensor, pressureSensor, tempSensor, humiditySensor, rotationSensor;


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://130.195.9.123:3000");
        } catch (URISyntaxException e) {}
    }


    TextView xValue, yValue, zValue;
    TextView xGyroValue, yGyroValue, zGyroValue;
    TextView xMagnoValue, yMagnoValue, zMagnoValue;
    TextView light, pressure, temp, humidity, rotation;

    String accelerometerReading, gyroReading, magnoReading, lightReading, pressureReading, tempReading, humidityReading, rotationReading;
    JSONObject data = new JSONObject();

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sensor Display");

        //initialising textviews for x y and z value
        xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);
        //init gyro
        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);
        //init magno
        xMagnoValue = (TextView) findViewById(R.id.xMagnoValue);
        yMagnoValue = (TextView) findViewById(R.id.yMagnoValue);
        zMagnoValue = (TextView) findViewById(R.id.zMagnoValue);

        light = (TextView) findViewById(R.id.light);
        pressure = (TextView) findViewById(R.id.pressure);
        temp = (TextView) findViewById(R.id.temp);
        humidity = (TextView) findViewById(R.id.humidity);
        rotation = (TextView) findViewById(R.id.rotation);

        Log.d(TAG, "onCreate: Initialising Sensor Services");
        //accesses systems sensor services - permission to use sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSocket.connect();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        //accesses the accelerometer readings
        accelerometer =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Accelerometer listener");
        }else{
            xValue.setText("Accelerometer Not Supported");
            yValue.setText("Accelerometer Not Supported");
            zValue.setText("Accelerometer Not Supported");
        }

        //accesses the gyroscope readings
        gyroSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyroSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Gyroscope listener");
        }else{
            xGyroValue.setText("Gyroscope Not Supported");
            yGyroValue.setText("Gyroscope Not Supported");
            zGyroValue.setText("Gyroscope Not Supported");
        }

        //accesses the magnometer readings
        magnoSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnoSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, magnoSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Magnometer listener");
        }else{
            xMagnoValue.setText("Magnometer Not Supported");
            yMagnoValue.setText("Magnometer Not Supported");
            zMagnoValue.setText("Magnometer Not Supported");
        }

        //accesses the light readings
        lightSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Light listener");
        }else{
            light.setText("Light Not Supported");
        }

        //accesses the pressure readings
        pressureSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressureSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Pressure listener");
        }else{
            pressure.setText("Pressure Not Supported");
        }

        //accesses the temp readings
        tempSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(tempSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Temperature listener");
        }else{
            temp.setText("Temperature Not Supported");
        }

        //accesses the humidity readings
        humiditySensor =  sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(humiditySensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Temperature listener");
        }else{
            humidity.setText("Humidity Not Supported");
        }
        //rotation
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(rotationSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, rotationSensor, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "onCreate: Registered Rotation listener");
        }else{
            rotation.setText("Rotation Not Supported");
        }
    }

    protected void onPause() {
        super.onPause();
        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            Log.d(TAG, "onSensorChanged: X: "+ event.values[0] +" Y: "+ event.values[1] +" Z: "+ event.values[2]);

            xValue.setText("xValue: " + event.values[0]);
            yValue.setText("yValue: " + event.values[1]);
            zValue.setText("zValue: " + event.values[2]);

            System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.length);

//            accelerometerReading = xValue.getText().toString() +" "+ yValue.getText().toString()+" "+zValue.getText().toString();
//            mSocket.emit("accelerometer", accelerometerReading);

        }else if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
            xGyroValue.setText("xGyro Value: "+event.values[0]);
            yGyroValue.setText("yGyroValue: " + event.values[1]);
            zGyroValue.setText("zGyroValue: " + event.values[2]);


        }else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            xMagnoValue.setText("xMagnoValue Value: "+event.values[0]);
            yMagnoValue.setText("yMagnoValue: " + event.values[1]);
            zMagnoValue.setText("zMagnoValue: " + event.values[2]);

            System.arraycopy(event.values, 0, mMagnetometerReading,0, mMagnetometerReading.length);

        }else if(sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText("Light: "+ event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_PRESSURE){
            pressure.setText("Pressure: "+ event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temp.setText("Ambient Temperature :"+ event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity.setText("Relative Temperature :" + event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){

            //https://strsackoverflow.com/questions/30780474/android-get-quaternion-data
            float[] quaternion = new float[4];
//            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            SensorManager.getQuaternionFromVector(quaternion, event.values);
//
            float quarternion_w = quaternion[0];
            float quarternion_x = quaternion[1];
            float quarternion_y = quaternion[2];
            float quarternion_z = quaternion[3];

            try {
                data.put("x", quarternion_x);
                data.put("y", quarternion_y);
                data.put("z", quarternion_z);
                data.put("w", quarternion_w);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onSensorChanged: W: "+ event.values[0] +" X: "+ event.values[1] +" Y: "+ event.values[2] + "Z: "+event.values[3]);

//            try {
//                data.put("x", event.values[1]);
//                data.put("y", event.values[2]);
//                data.put("z", event.values[3]);
//                data.put("w", event.values[0]);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            //System.out.println(quarternion_w +" "+quarternion_x+" "+quarternion_y +" "+quarternion_z);

//            updateOrientationAngles();

//            try {
//                data.put("x", mOrientationAngles[1]);
//                data.put("y", mOrientationAngles[2]);
//                data.put("z", mOrientationAngles[0]);
//                data.put("w", 0);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            mSocket.emit("orientation", data);

        }

    }
    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        sensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        // "mRotationMatrix" now has up-to-date information.

        sensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        // "mOrientationAngles" now has up-to-date information.
    }

    private float[] applyLowPassFilter(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
