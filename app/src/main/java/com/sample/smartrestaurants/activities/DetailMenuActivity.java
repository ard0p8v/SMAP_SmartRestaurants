package com.sample.smartrestaurants.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sample.smartrestaurants.R;
import com.sample.smartrestaurants.services.ViewHolderMenu;
import com.sample.smartrestaurants.model.Menu;

public class DetailMenuActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Query query;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        name = getIntent().getStringExtra("name");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Menu");

        query = FirebaseDatabase.getInstance().getReference("Menu")
                .orderByChild("restaurantName")
                .equalTo(name);
    }

        @Override
        protected void onStart() {
            super.onStart();

            FirebaseRecyclerAdapter<Menu, ViewHolderMenu> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<Menu, ViewHolderMenu>(
                            Menu.class,
                            R.layout.row_menu,
                            ViewHolderMenu.class,
                            query
                    ) {
                        @Override
                        protected void populateViewHolder(ViewHolderMenu viewHolderMenu, Menu model, int position) {

                            viewHolderMenu.setDetailsMenu(getApplicationContext(), model.getMenuName(), model.getPrice());

                        }
                    };

            mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void actionDisplayOnMap(View view) {
        Intent i = new Intent(DetailMenuActivity.this, MapsActivity.class);
        i.putExtra("nameRes", name);
        startActivity(i);
    }
}
