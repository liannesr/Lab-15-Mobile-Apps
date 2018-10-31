package com.techexchange.mobileapps.lab15;

import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.hardware.Sensor;
import android.content.Context;
import java.util.List;
import android.support.v7.app.AlertDialog;
import android.hardware.SensorEvent;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView lightSensor, luminescenceValue, proximityText, rotationText, stepCount, magneticText, pressureText, temperatureText,azimuth,roll,pitch;
    private Sensor light, proximity, rotation, stepCounter, magnetic, pressure, temperature;
    private SensorManager sensorManager;
    private String sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSensor = findViewById(R.id.light_sensor);
        proximityText = findViewById(R.id.proximity);
        //rotationText = findViewById(R.id.rotation);
        stepCount = findViewById(R.id.step_count);
        magneticText = findViewById(R.id.magnetic);
        pressureText = findViewById(R.id.pressure);
        temperatureText = findViewById(R.id.temperature);
        azimuth = findViewById(R.id.azimuth);
        roll = findViewById(R.id.roll);
        pitch = findViewById(R.id.pitch);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        getSensorAvailabilty();
        sensorList = getSensorList();

        Button button = findViewById(R.id.button_id);
        button.setBackgroundColor(Color.CYAN);
        button.setOnClickListener(v -> onButtonPressed());
    }

    private String getSensorList() {

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        String sensorInfo = "";
        for (Sensor s : sensorList) {
            sensorInfo = sensorInfo + s.getName() + "\n";
        }
        return sensorInfo;
    }

    private void getSensorAvailabilty() {
        if ((light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)) == null)
            lightSensor.setText("Luminescence: N/A");
        if ((proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)) == null)
            proximityText.setText("Proximity: N/A");
        if ((rotation = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)) == null) {
            rotationText.setText("Accelerometer - N/A");
            pitch.setText("Pitch: N/A");
            roll.setText("Roll: N/A");
            azimuth.setText("Azimuth: N/A");
        }
        if ((stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)) == null)
            stepCount.setText("Steps: N/A");
        if ((magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) == null)
            magneticText.setText("Magnitude: N/A");
        if ((pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)) == null)
            pressureText.setText("Pressure: N/A");
        if ((temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)) == null)
            temperatureText.setText("Ambient Temperature: N/A");

    }


    public void onButtonPressed() {
        new AlertDialog.Builder(this).setMessage(sensorList)
                .setTitle(getString(R.string.sensorList))
                .setPositiveButton(getString(R.string.OK), (arg0, arg1) -> {
                }).show();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                float valueZ = event.values[0];
                lightSensor.setText(String.format("Luminescence: %.2f", valueZ));
                break;

            case Sensor.TYPE_PROXIMITY:
                float proximity = event.values[0];
                proximityText.setText(String.format("Proximity: %.2f", proximity));
                break;

            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float[] rotMatrix = new float[9];
                float[] rotVals = new float[3];

                SensorManager.getRotationMatrixFromVector(rotMatrix, event.values);
                SensorManager.remapCoordinateSystem(rotMatrix,
                        SensorManager.AXIS_X, SensorManager.AXIS_Y, rotMatrix);

                SensorManager.getOrientation(rotMatrix, rotVals);
                float azimuthResult = (float) Math.toDegrees(rotVals[0]);
                float pitchResult = (float) Math.toDegrees(rotVals[1]);
                float rollResult = (float) Math.toDegrees(rotVals[2]);
                pitch.setText(String.format("Pitch: %.2f", pitchResult));
                roll.setText(String.format("Roll: %.2f", rollResult));
                azimuth.setText(String.format("Azimuth: %.2f", azimuthResult));

                break;
            case Sensor.TYPE_STEP_COUNTER:
                float counter = event.values[0];
                stepCount.setText(String.format("Steps: %.2f", counter));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                float xAxis = event.values[0];
                float yAxis = event.values[1];
                float zAxis = event.values[2];
                double magnetic = Math.sqrt((xAxis*xAxis) + (yAxis*yAxis) + (zAxis*zAxis));
                magneticText.setText(String.format("Magnitude: %.2f", magnetic));
                break;
            case Sensor.TYPE_PRESSURE:
                float pressure = event.values[0];
                pressureText.setText(String.format("Pressure: %.2f", pressure));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                float ambient = event.values[0];
                temperatureText.setText(String.format("Ambient Temperature: %.2f", ambient));
                break;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}