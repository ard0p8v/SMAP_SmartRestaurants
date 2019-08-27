package com.sample.smartrestaurants.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sample.smartrestaurants.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowTopologyActivity extends AppCompatActivity implements SensorEventListener {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Query query;
    PhotoViewAttacher photoViewAttacher;

    Sensor magnetometer;
    Sensor accelerometer;
    Sensor stepCounter;
    SensorManager sensorManager;

    int width;
    int height;

    float azimuth;
    float zenith;
    float[] r = new float[9];
    float[] i = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] orientation = new float[3];

    boolean isRotationMatrix;
    boolean isPlaceSelected = false;

    TextView nameRestaurant;
    ImageView image;

    String name;
    String fName;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_topology);

        nameRestaurant = findViewById(R.id.nameRestaurant);
        image = findViewById(R.id.imageViewTopology);

        fName = getIntent().getStringExtra("fName");
        name = getIntent().getStringExtra("name");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Restaurant");

        if(fName == null) {
            query = FirebaseDatabase.getInstance().getReference("Restaurant")
                    .orderByChild("name")
                    .equalTo(name);
        }

        if(name == null) {
            query = FirebaseDatabase.getInstance().getReference("Restaurant")
                    .orderByChild("name")
                    .equalTo(fName);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (fName == null) {
                        String name = ds.child("name").getValue(String.class);
                        nameRestaurant.setText(name);
                    } else if (name == null) {
                        String name = ds.child("name").getValue(String.class);
                        nameRestaurant.setText(name);
                    }

                    /*
                    String img = ds.child("topology").getValue(String.class);
                    Picasso.get().load(img).into(image);


                    if(img == null) {
                    */
                    boolean isEquals = name.equals("McDonald's");
                    if(!isEquals) {
                        AlertDialog.Builder alertTopology = new AlertDialog.Builder(ShowTopologyActivity.this);
                        alertTopology.setMessage("Topology is not created for this restaurant!");
                        alertTopology.show();
                        alertTopology.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                            onBackPressed();
                                    }
                        }).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Context context = this;
        photoViewAttacher = new PhotoViewAttacher(image);
        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if(isPlaceSelected == false) {
                    Paint paint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mcdonald);
                    Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                    Canvas tempCanvas = new Canvas(tempBitmap);
                    height = tempBitmap.getHeight();
                    width = tempBitmap.getWidth();
                    tempCanvas.drawBitmap(bitmap, 0, 0, null);

                    x = x * width;
                    y = y * height;
                    paint.setColor(Color.rgb(3, 155, 229));
                    tempCanvas.drawCircle(x, y, 50, paint);

                    Drawable d = new BitmapDrawable(context.getResources(), tempBitmap);
                    image.setImageDrawable(d);
                    isPlaceSelected = true;

                    AlertDialog.Builder alertPlace = new AlertDialog.Builder(ShowTopologyActivity.this);
                    alertPlace.setTitle("Seating place was selected!");
                    alertPlace.setMessage("The positions of the other locations will now be loaded..");
                    alertPlace.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounter == null) {
            AlertDialog.Builder alertTopology = new AlertDialog.Builder(ShowTopologyActivity.this);
            alertTopology.setMessage("Location information is not allowed!");
            alertTopology.show();
            alertTopology.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            onBackPressed();
                        }
                    }).show();
        } else {
            sensorManager.registerListener((SensorEventListener) this, stepCounter, SensorManager.SENSOR_DELAY_UI);

            AlertDialog.Builder alertStart = new AlertDialog.Builder(ShowTopologyActivity.this);
            alertStart.setMessage("First of all, please select a seating place.");
            alertStart.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        }
        if(geomagnetic != null && gravity != null) {

        }

        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            isRotationMatrix = SensorManager.getRotationMatrix(r, i, gravity, geomagnetic);
            if(isRotationMatrix) {
                SensorManager.getOrientation(r, orientation);
                azimuth = orientation[0];
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
