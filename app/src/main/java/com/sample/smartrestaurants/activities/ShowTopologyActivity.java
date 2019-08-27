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

    String xPosToilets;
    String yPosToilets;
    String xPosCashbox;
    String yPosCashbox;
    float[] selectedPlacePosition;
    float[] otherPlacePosition;

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

                    xPosToilets = ds.child("xPosToilets").getValue(String.class);
                    yPosToilets = ds.child("yPosToilets").getValue(String.class);
                    xPosCashbox = ds.child("xPosCashbox").getValue(String.class);
                    yPosCashbox = ds.child("yPosCashbox").getValue(String.class);

                    otherPlacePosition = utilsOtherPlace(xPosToilets, yPosToilets, xPosCashbox, yPosCashbox);

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
                    selectedPlacePosition = selectPlace(x, y);

                    AlertDialog.Builder alertPlace = new AlertDialog.Builder(ShowTopologyActivity.this);
                    alertPlace.setTitle("Seating place was selected!");
                    alertPlace.setMessage("The positions of the other locations will be loaded now..");
                    alertPlace.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    isPlaceSelected = true;
                                    updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition, isPlaceSelected);
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
        Context context = this;
        if(isPlaceSelected){
            //updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition);
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        }
        if(geomagnetic != null && gravity != null) {

        }

        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(isPlaceSelected){
                updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition, isPlaceSelected);
            }
            isRotationMatrix = SensorManager.getRotationMatrix(r, i, gravity, geomagnetic);
            if(isRotationMatrix) {
                SensorManager.getOrientation(r, orientation);
                //updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition);
                azimuth = orientation[0];
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void updateTopology(Context context, ImageView image, int height, int width, float[] selectedPlacePosition, float[] otherPlacePosition, boolean isPlaceSelected) {
        Paint paint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mcdonald);
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        height = tempBitmap.getHeight();
        width = tempBitmap.getWidth();
        canvas.drawBitmap(bitmap, 0, 0, null);

        //Canvas tempCanvas;
        drawSelectedPlace(paint, canvas, height, width, selectedPlacePosition, isPlaceSelected);
        drawOtherPositions(paint, canvas, height, width, otherPlacePosition);
        drawNavigationLines(paint, canvas, height, width);
        drawMyPosition(context, paint, canvas, height, width);

        Drawable d = new BitmapDrawable(context.getResources(), tempBitmap);
        image.setImageDrawable(d);
    }

    public static float[] selectPlace(float x, float y) {
        float[] positionPlace = new float[2];
        positionPlace[0] = x;
        positionPlace[1] = y;

        return positionPlace;
    }

    public static void drawSelectedPlace(Paint paint, Canvas canvas, float height, float width, float[] positions, boolean isPlaceSelected){
        if(isPlaceSelected) {
            float x = positions[0];
            float y = positions[1];
            x *= width;
            y *= height;

            paint.setColor(Color.rgb(3, 155, 229));
            canvas.drawCircle(x, y, 50, paint);
        }
    }

    public static float[] utilsOtherPlace(String xPosToilets, String yPosToilets, String xPosCashbox, String yPosCashbox) {
        float[] positionOtherPlace = new float[4];
        Float xPosFirstParam = Float.parseFloat(xPosToilets);
        Float yPosFirstParam = Float.parseFloat(yPosToilets);
        Float xPosSecondParam = Float.parseFloat(xPosCashbox);
        Float yPosSecondParam = Float.parseFloat(yPosCashbox);
        positionOtherPlace[0] = xPosFirstParam;
        positionOtherPlace[1] = yPosFirstParam;
        positionOtherPlace[2] = xPosSecondParam;
        positionOtherPlace[3] = yPosSecondParam;


        return positionOtherPlace;
    }

    public static void drawOtherPositions(Paint paint, Canvas canvas, int height, int width, float[] otherPlacePosition) {
        float xPosFirstParam = otherPlacePosition[0];
        float yPosFirstParam = otherPlacePosition[1];
        float xPosSecondParam = otherPlacePosition[2];
        float yPosSecondParam = otherPlacePosition[3];

        xPosFirstParam = xPosFirstParam * width;
        yPosFirstParam = yPosFirstParam * height;
        xPosSecondParam = xPosSecondParam * width;
        yPosSecondParam = yPosSecondParam * height;
        paint.setColor(Color.rgb(3, 155, 229));
        canvas.drawCircle(xPosFirstParam, yPosFirstParam, 50, paint);
        canvas.drawCircle(xPosSecondParam, yPosSecondParam, 50, paint);
    }

    public static int[] getCorridorPosition(int posX, int posY){
        int[] sector = new int[2];
        int xStart = 49;
        int yStart = 41;

        for (int height = 0; height < 3;height++) {
            for (int width = 0; width < 5; width++) {
                if (posX < width*250+49){
                    sector[0] = width - 1;
                    break;
                }
            }
            if (posY < height*817+41){
                sector[1] = height;
                sector[1]-= 1;
                break;
            }
        }
        return sector;
    }

    public static void drawNavigationLines(Paint paint, Canvas canvas, int height, int width){
        float x = 0.70500000f;
        float y = 0.001000000f;
        x *= width;
        y *= height;

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2.5f);
        canvas.drawLine(x, y + 50, x, 1700, paint);
    }

    public static void drawMyPosition(Context context, Paint paint, Canvas canvas, int height, int width) {
        float startX = 0.682000000f;
        float startY = 0.001000000f;
        startX *= width;
        startY *= height;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_location);
        canvas.drawBitmap(bitmap, startX, startY, paint);
        //paint.setColor(Color.rgb(0, 255, 0));
    }
}
