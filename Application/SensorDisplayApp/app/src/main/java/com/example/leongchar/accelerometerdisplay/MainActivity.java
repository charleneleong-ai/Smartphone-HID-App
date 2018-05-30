package com.example.leongchar.accelerometerdisplay;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //define the log tag
    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroSensor, magnoSensor, lightSensor, pressureSensor, tempSensor, humiditySensor;

    TextView xValue, yValue, zValue;
    TextView xGyroValue, yGyroValue, zGyroValue;
    TextView xMagnoValue, yMagnoValue, zMagnoValue;
    TextView light, pressure, temp, humidity;

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

        Log.d(TAG, "onCreate: Initialising Sensor Services");
        //accesses systems sensor services - permission to use sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //accesses the accelerometer readings
        accelerometer =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
            sensorManager.registerListener(MainActivity.this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
            sensorManager.registerListener(MainActivity.this, magnoSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
            sensorManager.registerListener(MainActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Light listener");
        }else{
            light.setText("Light Not Supported");
        }

        //accesses the pressure readings
        pressureSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressureSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Pressure listener");
        }else{
            pressure.setText("Pressure Not Supported");
        }

        //accesses the temp readings
        tempSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(tempSensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Temperature listener");
        }else{
            temp.setText("Temperature Not Supported");
        }

        //accesses the humidity readings
        humiditySensor =  sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(humiditySensor != null){
            //register the listener with no delay with the sensor manager
            sensorManager.registerListener(MainActivity.this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Temperature listener");
        }else{
            humidity.setText("Humidity Not Supported");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.d(TAG, "onSensorChanged: X: "+ event.values[0] +" Y: "+ event.values[1] +" Z: "+ event.values[2]);

            xValue.setText("xValue: " + event.values[0]);
            yValue.setText("yValue: " + event.values[1]);
            zValue.setText("zvalue: " + event.values[2]);


        }else if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
            xGyroValue.setText("xGyro Value: "+event.values[0]);
            yGyroValue.setText("yGyroValue: " + event.values[1]);
            zGyroValue.setText("zGyroValue: " + event.values[2]);

        }else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            xMagnoValue.setText("xMagnoValue Value: "+event.values[0]);
            yMagnoValue.setText("yMagnoValue: " + event.values[1]);
            zMagnoValue.setText("zMagnoValue: " + event.values[2]);

        }else if(sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText("Light: "+ event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_PRESSURE){
            pressure.setText("Pressure: "+ event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temp.setText("Ambient Temperature :"+ event.values[0]);

        }else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            humidity.setText("Relative Temperature :"+ event.values[0]);
        }

    }
}
