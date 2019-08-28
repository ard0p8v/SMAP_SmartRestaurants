package com.sample.smartrestaurants.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

public class CompassTesting extends Activity implements SensorEventListener {

    Float azimuth;
    Sensor accelerometer;
    Sensor magnetometer;

    float[] gravity;
    float[] geomagnetic;

    SensorManager sensorManager;
    CustomDrawableView customDrawableView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDrawableView = new CustomDrawableView(this);
        setContentView(customDrawableView);    // Register the sensor listeners
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values;
        if (gravity != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[0]; //  azimut, pitch and roll
                System.out.println(azimuth);
            }
        }
        customDrawableView.invalidate();
    }

    public class CustomDrawableView extends View {
        Paint paint = new Paint();

        public CustomDrawableView(Context context) {
            super(context);
            paint.setColor(0xff00ff00);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setTextSize(20);
            paint.setAntiAlias(true);
        }

        protected void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            int centerx = width / 2;
            int centery = height / 2;

            paint.setStrokeWidth(10f);
            canvas.drawLine(centerx, 0, centerx, height, paint);
            canvas.drawLine(0, centery, width, centery, paint);

            if (azimuth != null)
                canvas.rotate(-azimuth * 360 / (2 * 3.14159f), centerx, centery);

            paint.setColor(Color.rgb(3, 155, 229));

            canvas.drawLine(centerx, -1000, centerx, +1000, paint);
            canvas.drawLine(-1000, centery, 1000, centery, paint);

            canvas.drawText("NORTH", centerx + 5, centery - 10, paint);
            canvas.drawText("SOUTH", centerx - 10, centery + 15, paint);

            if (azimuth != null)
                canvas.drawText(azimuth.toString(), 300, 300, paint);

            paint.setColor(Color.rgb(3, 155, 229));
        }
    }
}
