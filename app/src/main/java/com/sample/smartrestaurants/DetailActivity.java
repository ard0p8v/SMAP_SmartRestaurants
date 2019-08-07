package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Query query;

    ImageView image;
    TextView evaluationRes;
    TextView nameRestaurant;
    TextView typeRes;
    TextView kitchenRes;
    TextView priceLevelRes;
    TextView gardenRes;
    TextView cornerRes;
    TextView parkingRes;

    String name;
    String fName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        evaluationRes = findViewById(R.id.evaluationRes);
        nameRestaurant = findViewById(R.id.nameRestaurant);
        typeRes = findViewById(R.id.typeRestaurant);
        kitchenRes = findViewById(R.id.kitchenRestaurant);
        priceLevelRes = findViewById(R.id.priceLevelRestaurant);
        gardenRes = findViewById(R.id.gardenRestaurant);
        cornerRes = findViewById(R.id.cornerRestaurant);
        parkingRes = findViewById(R.id.parkingFreeRestaurant);
        image = findViewById(R.id.imageView);


        fName = getIntent().getStringExtra("rNameTv");
        name = getIntent().getStringExtra("markertitle");

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
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(fName == null) {
                        String name = ds.child("name").getValue(String.class);
                        nameRestaurant.setText(name);
                    }

                    if(name == null){
                        String name = ds.child("name").getValue(String.class);
                        nameRestaurant.setText(name);
                    }

                    Double evaluation = ds.child("evaluation").getValue(Double.class);
                    evaluationRes.setText(evaluation.toString());

                    String type = ds.child("type").getValue(String.class);
                    typeRes.setText(type);

                    String kitchen = ds.child("kitchen").getValue(String.class);
                    kitchenRes.setText(kitchen);

                    String priceLevel = ds.child("priceLevel").getValue(String.class);
                    priceLevelRes.setText(priceLevel);

                    String garden = ds.child("garden").getValue(String.class);
                    gardenRes.setText(garden);

                    String corner = ds.child("childrensCorner").getValue(String.class);
                    cornerRes.setText(corner);

                    String parking = ds.child("parkingFree").getValue(String.class);
                    parkingRes.setText(parking);

                    String img = ds.child("image").getValue(String.class);
                    Picasso.get().load(img).into(image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void actionDisplayMenu(View view) {
        Intent intent = new Intent(DetailActivity.this, DetailMenuActivity.class);
        if(name == null){
            intent.putExtra("name", fName);
        } else {
            intent.putExtra("name", name);
        }
        startActivity(intent);
    }

    public void actionEvaluate(View view) {
        Intent intent = new Intent(DetailActivity.this, EvaluateActivity.class);
        if(name == null){
            intent.putExtra("name", fName);
        } else {
            intent.putExtra("name", name);
        }
        startActivity(intent);
    }
}
