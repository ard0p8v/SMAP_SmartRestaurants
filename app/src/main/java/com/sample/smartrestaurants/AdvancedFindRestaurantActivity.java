package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sample.smartrestaurants.holder.ViewHolder;
import com.sample.smartrestaurants.model.Restaurant;

public class AdvancedFindRestaurantActivity extends AppCompatActivity {

    Button btnDisplayOnMap;
    String typeRes;
    String kitchenType;
    String priceLevel;
    String garden;
    String childrensCorner;
    String parkingFree;

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_find_restaurant);

        btnDisplayOnMap = findViewById(R.id.btnDisplayOnMap);

        typeRes = getIntent().getStringExtra("typeRes");
        kitchenType = getIntent().getStringExtra("kitchenType");
        priceLevel = getIntent().getStringExtra("priceLevel");
        garden = getIntent().getStringExtra("garden");
        childrensCorner = getIntent().getStringExtra("childrensCorner");
        parkingFree = getIntent().getStringExtra("parkingFree");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Restaurant");

        query = FirebaseDatabase.getInstance().getReference("Restaurant")
                .orderByChild("type")
                .equalTo(typeRes)
                .orderByChild("kitchen")
                .equalTo(kitchenType)
                .orderByChild("priceLevel")
                .equalTo(priceLevel)
                .orderByChild("garden")
                .equalTo(garden)
                .orderByChild("childrensCorner")
                .equalTo(childrensCorner)
                .orderByChild("parkingFree")
                .equalTo(parkingFree);

        btnDisplayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdvancedFindRestaurantActivity.this, MapsActivity.class);
                i.putExtra("typeRes", typeRes);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Restaurant, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Restaurant, ViewHolder>(
                        Restaurant.class,
                        R.layout.row,
                        ViewHolder.class,
                        query
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Restaurant model, int position) {

                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getEvaluation(), model.getImage());

            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
