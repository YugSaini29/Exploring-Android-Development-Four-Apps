package com.example.sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView accelText;
    TextView proxText;
    TextView lightText;

    SensorManager sensorManager;
    Sensor accelSensor;
    Sensor lightSensor;
    Sensor proxSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        accelText = findViewById(R.id.accelText);
        proxText = findViewById(R.id.proxText);
        lightText = findViewById(R.id.lightText);

        // initializing the sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // getting sensors using Sensor.(SensorType)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // creating listeners so we can override them together later on(since we have used implements SensorEventListener in our class).
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelText.setText("Accelerometer: X=" + x + " Y=" + y + " Z=" + z);
        }
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            float light = event.values[0];
            lightText.setText("Light: " + light + " lx");
        }
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            float prox = event.values[0];
            proxText.setText("Proximity: " + prox + " cm");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}