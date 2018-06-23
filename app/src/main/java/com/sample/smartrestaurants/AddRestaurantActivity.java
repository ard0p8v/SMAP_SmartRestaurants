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
import com.sample.smartrestaurants.model.Restaurant;

public class AddRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference resDatabase;
    private Button btnSaveRestaurant;
    private Button btnDisplayOnMap;
    private EditText txtName;
    private EditText txtLatitude;
    private EditText txtLongitude;
    private EditText txtType;
    private EditText txtKitchen;
    private EditText txtPriceLevel;
    private EditText txtGarden;
    private EditText txtChildrensCorner;
    private EditText txtParkingFree;

    private DatabaseReference menuDatabase;
    private Button btnSaveMenu;
    private EditText txtMenuName;
    private EditText txtPrice;

    String nameRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        txtName = findViewById(R.id.name);
        txtLatitude = findViewById(R.id.latitude);
        txtLongitude = findViewById(R.id.longitude);
        txtType = findViewById(R.id.typeRes);
        txtKitchen = findViewById(R.id.parkingFree);
        txtPriceLevel = findViewById(R.id.priceLevel);
        txtGarden = findViewById(R.id.garden);
        txtChildrensCorner = findViewById(R.id.childrensCorner);
        txtParkingFree = findViewById(R.id.parkingFree);
        btnSaveRestaurant = findViewById(R.id.btnSaveRestaurant);
        btnDisplayOnMap = findViewById(R.id.btnDisplayOnMap);
        resDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurant");

        btnSaveRestaurant.setOnClickListener(this);
        btnDisplayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddRestaurantActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }

    private void saveRestaurantInfo() {
        DatabaseReference pResDatabase = resDatabase.push();
        String name = txtName.getText().toString().trim();
        double latitude = Double.parseDouble(txtLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(txtLongitude.getText().toString().trim());
        String type = txtType.getText().toString().trim();
        String kitchen = txtKitchen.getText().toString().trim();
        String priceLevel = txtPriceLevel.getText().toString().trim();
        String garden = txtGarden.getText().toString().trim();
        String childrensCorner = txtChildrensCorner.getText().toString().trim();
        String parkingFree = txtParkingFree.getText().toString().trim();
        Double evaluation = 0.0;
        Integer numberEvaluation = 0;

        Restaurant restaurant = new Restaurant(name, latitude, longitude, type, kitchen, priceLevel, evaluation, numberEvaluation, garden, childrensCorner, parkingFree);
        pResDatabase.setValue(restaurant);
        Toast.makeText(this, "Restaurant was saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == btnDisplayOnMap) {
            finish();
        }
        if(view == btnSaveRestaurant) {
            saveRestaurantInfo();

            nameRes = txtName.getText().toString();

            txtName.getText().clear();
            txtLatitude.getText().clear();
            txtLongitude.getText().clear();
            txtType.getText().clear();
            txtKitchen.getText().clear();
            txtPriceLevel.getText().clear();
            txtGarden.getText().clear();
            txtChildrensCorner.getText().clear();
            txtParkingFree.getText().clear();
        }
    }
    public void actionAddMenu(View view){
        Intent intent = new Intent(AddRestaurantActivity.this, AddMenuRestaurantActivity.class);
        intent.putExtra("nameRes", nameRes);
        startActivity(intent);
    }
}
