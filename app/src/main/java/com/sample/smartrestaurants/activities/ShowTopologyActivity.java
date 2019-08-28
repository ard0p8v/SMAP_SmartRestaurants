package com.sample.smartrestaurants.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    boolean moveOn = false;

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
    int[] myPosition = new int[]{1084, 2};

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

        if (fName == null) {
            query = FirebaseDatabase.getInstance().getReference("Restaurant")
                    .orderByChild("name")
                    .equalTo(name);
        }

        if (name == null) {
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

                    boolean isEquals = name.equals("McDonald's");
                    if (!isEquals) {
                        AlertDialog.Builder alertTopology = new AlertDialog.Builder(ShowTopologyActivity.this);
                        alertTopology.setTitle("Warning!");
                        alertTopology.setMessage("Topology is not created for this restaurant!");
                        alertTopology.show();
                        alertTopology.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        onBackPressed();
                                    }
                                }).show();
                    } else {
                        xPosToilets = ds.child("xPosToilets").getValue(String.class);
                        yPosToilets = ds.child("yPosToilets").getValue(String.class);
                        xPosCashbox = ds.child("xPosCashbox").getValue(String.class);
                        yPosCashbox = ds.child("yPosCashbox").getValue(String.class);

                        otherPlacePosition = utilsOtherPlace(xPosToilets, yPosToilets, xPosCashbox, yPosCashbox);
                    }

                    /*
                    String img = ds.child("topology").getValue(String.class);
                    Picasso.get().load(img).into(image);


                    if(img == null) {
                    */
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
                if (isPlaceSelected == false) {
                    selectedPlacePosition = selectPlace(x, y);

                    AlertDialog.Builder alertPlace = new AlertDialog.Builder(ShowTopologyActivity.this);
                    alertPlace.setTitle("Seating place was selected!");
                    alertPlace.setMessage("The positions of the other locations will be loaded now..");
                    alertPlace.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    isPlaceSelected = true;
                                    updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition, myPosition, isPlaceSelected, moveOn, azimuth);
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
        if (stepCounter == null) {
            AlertDialog.Builder alertTopology = new AlertDialog.Builder(ShowTopologyActivity.this);
            alertTopology.setTitle("Warning!");
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
        if (isPlaceSelected) {
            //updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition, isPlaceSelected);
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        }
        if (geomagnetic != null && gravity != null) {

        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (isPlaceSelected) {
                updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition, myPosition, isPlaceSelected, moveOn, azimuth);
            }

            isRotationMatrix = SensorManager.getRotationMatrix(r, i, gravity, geomagnetic);
            if (isRotationMatrix) {
                SensorManager.getOrientation(r, orientation);
                //updateTopology(context, image, height, width, selectedPlacePosition, otherPlacePosition, isPlaceSelected);
                azimuth = orientation[0];
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void updateTopology(Context context, ImageView image, int height, int width, float[] selectedPlacePosition, float[] otherPlacePosition, int[] myPosition, boolean isPlaceSelected, boolean moveOn, float azimuth) {
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
        drawLineToSelectedPlace(context, paint, canvas, height, width, selectedPlacePosition);
        myPosition = drawMyPosition(context, paint, canvas, bitmap, height, width, myPosition, azimuth, moveOn);
        moveOn = true;

        Drawable d = new BitmapDrawable(context.getResources(), tempBitmap);
        image.setImageDrawable(d);
    }

    public static float[] selectPlace(float x, float y) {
        float[] positionPlace = new float[2];
        positionPlace[0] = x;
        positionPlace[1] = y;

        return positionPlace;
    }

    public static void drawSelectedPlace(Paint paint, Canvas canvas, float height, float width, float[] positions, boolean isPlaceSelected) {
        if (isPlaceSelected) {
            float x = positions[0];
            float y = positions[1];
            System.out.println("X coordinates: " + x);
            System.out.println("Y coordinates: " + y);
            x *= width;
            y *= height;
            System.out.println("X coordinates: " + x);
            System.out.println("Y coordinates: " + y);

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

    public static void drawNavigationLines(Paint paint, Canvas canvas, int height, int width) {
        float x = 0.70500000f;
        float y = 0.001000000f;
        x *= width;
        y *= height;

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(11f);
        canvas.drawLine(x, y + 100, x, 1700, paint);
        canvas.drawLine(x, 1700, 695, 1700, paint);
        canvas.drawLine(695, 1700, 690, 2428, paint);
    }

    public static void drawLineToSelectedPlace(Context context, Paint paint, Canvas canvas, int height, int width, float[] positions) {
        float x = positions[0];
        float y = positions[1];
        x *= width;
        y *= height;

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(11f);
        if (y <= 500) { // prvni rada
            if (x > 100 && x <= 280) {
                canvas.drawLine(215, 360, 1120.95f, 360, paint);
            } else if (x > 280 && x <= 600) {
                canvas.drawLine(542, 360, 1120.95f, 360, paint);
            } else if (x > 600 && x <= 940) {
                canvas.drawLine(851, 360, 1120.95f, 360, paint);
            } else if (x > 1250 && x <= 1500) {
                canvas.drawLine(1374, 360, 1120.95f, 360, paint);
            } else {
                alertSelectRightPlace(context);
            }
        } else if (y > 500 && y <= 1400) {
            if (x > 100 && x <= 280) {
                canvas.drawLine(215, 1035, 1120.95f, 1035, paint);
            } else if (x > 280 && x <= 600) {
                canvas.drawLine(542, 1035, 1120.95f, 1035, paint);
            } else if (x > 600 && x <= 940) {
                canvas.drawLine(851, 1035, 1120.95f, 1035, paint);
            } else if (x > 1250 && x <= 1500) {
                canvas.drawLine(1374, 1035, 1120.95f, 1035, paint);
            } else {
                alertSelectRightPlace(context);
            }
        } else if (y > 1300 && y <= 1700) {
            if (x > 100 && x <= 280) {
                canvas.drawLine(215, 1700, 1120.95f, 1700, paint);
            } else if (x > 280 && x <= 560) {
                canvas.drawLine(542, 1700, 1120.95f, 1700, paint);
            } else if (x > 560 && x <= 880) {
                canvas.drawLine(851, 1700, 1120.95f, 1700, paint);
            } else if (x > 1250 && x <= 1500) {
                canvas.drawLine(1374, 1700, 1120.95f, 1700, paint);
            } else {
                alertSelectRightPlace(context);
            }
        }
    }

    public static void alertSelectRightPlace(Context context) {
        AlertDialog.Builder alertSelectRightPlace = new AlertDialog.Builder(context);
        alertSelectRightPlace.setTitle("Warning!");
        alertSelectRightPlace.setMessage("Please select one of these black tables..");
        alertSelectRightPlace.show();
        alertSelectRightPlace.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Intent intent = new Intent(context, ShowTopologyActivity.class);
                        context.startActivity(intent);
                    }
                }).show();
    }

    public static int[] drawMyPosition(Context context, Paint paint, Canvas canvas, Bitmap bitmap, int height, int width, int[] myPosition, float azimuth, boolean moveOn) {
        if (!moveOn) {
            int startX = myPosition[0];
            int startY = myPosition[1];

            Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_location);
            canvas.drawBitmap(bitmapIcon, startX, startY, paint);

            return myPosition;
            //paint.setColor(Color.rgb(0, 255, 0));
        } else {

            int X = myPosition[0];
            int Y = myPosition[1];

            for(int i = 0; i < 50; i++) {
                if (Y + i <= myPosition[1]) {
                    int color = bitmap.getPixel(X + i, Y);
                    int colorGreen = Color.GREEN;
                    if (color == colorGreen) {
                        Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_location);
                        canvas.drawBitmap(bitmapIcon, X + i, Y, paint);
                        myPosition[0] = X + i;
                        myPosition[1] = Y;
                        break;
                    }
                } else if (Y - i >= 0){
                    int color = bitmap.getPixel(X + i, Y);
                    int colorGreen = Color.GREEN;
                    if (color == colorGreen) {
                        Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_location);
                        canvas.drawBitmap(bitmapIcon, X + i, Y, paint);
                        myPosition[0] = X + i;
                        myPosition[1] = Y;
                        break;
                    }
                } else if (X + i <= myPosition[0]){
                    int color = bitmap.getPixel(X + i, Y);
                    int colorGreen = Color.GREEN;
                    if (color == colorGreen) {
                        Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_location);
                        canvas.drawBitmap(bitmapIcon, X + i, Y, paint);
                        myPosition[0] = X + i;
                        myPosition[1] = Y;
                        break;
                    }
                } else if (X + i <= 0){
                    int color = bitmap.getPixel(X + i, Y);
                    int colorGreen = Color.GREEN;
                    if (color == colorGreen) {
                        Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_location);
                        canvas.drawBitmap(bitmapIcon, X + i, Y, paint);
                        myPosition[0] = X + i;
                        myPosition[1] = Y;
                        break;
                    }
                }
            }
            return myPosition;
        }

    }
}
