package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sample.smartrestaurants.db.Restaurant;

public class AddRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private Button btnSave;
    private Button btnDisplayOnMap;
    private EditText txtName;
    private EditText txtLatitude;
    private EditText txtLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        txtName = findViewById(R.id.name);
        txtLatitude = findViewById(R.id.latitude);
        txtLongitude = findViewById(R.id.longitude);
        btnSave = findViewById(R.id.btnSave);
        btnDisplayOnMap = findViewById(R.id.btnDisplayOnMap);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        btnSave.setOnClickListener(this);
        btnDisplayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddRestaurantActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }

    public void actionSave(View view) {

    }

    public void actionDisplayOnMap(View view) {

    }

    private void saveRestaurantInformation() {
        String name = txtName.getText().toString().trim();
        double latitude = Double.parseDouble(txtLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(txtLongitude.getText().toString().trim());

        Restaurant restaurant = new Restaurant(name, latitude, longitude);
        mDatabase.child(txtName.getText().toString()).setValue(restaurant);
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == btnDisplayOnMap) {
            finish();
        }
        if(view == btnSave) {
            saveRestaurantInformation();
            txtName.getText().clear();
            txtLatitude.getText().clear();
            txtLongitude.getText().clear();
        }
    }
}
