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
import com.sample.smartrestaurants.model.Menu;

public class BasicFindRestaurantActivity extends AppCompatActivity {

    Button btnDisplayOnMap;
    String kindOfFood;

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacis_find_restaurant);

        btnDisplayOnMap = findViewById(R.id.btnDisplayOnMap);

        kindOfFood = getIntent().getStringExtra("kindOfFood");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Menu");

        query = FirebaseDatabase.getInstance().getReference("Menu")
                .orderByChild("menuName")
                .startAt(kindOfFood)
                .endAt(kindOfFood+"\uf8ff")
                .limitToFirst(10);

        btnDisplayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BasicFindRestaurantActivity.this, MapsActivity.class);
                i.putExtra("kindOfFood", kindOfFood);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Menu, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Menu, ViewHolder>(
                        Menu.class,
                        R.layout.row,
                        ViewHolder.class,
                        query
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Menu model, int position) {

                        viewHolder.setDetails(getApplicationContext(), model.getRestaurantName(), model.getMenuName(), model.getPrice(), model.getImage());

                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
