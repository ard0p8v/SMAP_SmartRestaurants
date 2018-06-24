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
import com.sample.smartrestaurants.model.Menu;

public class AddMenuRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference menuDatabase;
    private Button btnSaveMenu;
    private Button btnDisplayOnMap;
    private EditText txtMenuName;
    private EditText txtPrice;

    public String rName;
    public String rLatitude;
    public String rLongitude;
    public String rImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_restaurant);

        txtMenuName = findViewById(R.id.menuName);
        txtPrice = findViewById(R.id.price);
        btnSaveMenu = findViewById(R.id.btnDisplayMenu);
        btnDisplayOnMap = findViewById(R.id.btnDisplayOnMap);
        menuDatabase = FirebaseDatabase.getInstance().getReference().child("Menu");

        rName = getIntent().getStringExtra("nameRes");
        rLatitude = getIntent().getStringExtra("latitude");
        rLongitude = getIntent().getStringExtra("longitude");
        rImage = getIntent().getStringExtra("image");

        btnSaveMenu.setOnClickListener(this);
        btnDisplayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMenuRestaurantActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
    }

    private void saveMenuRestaurantInfo() {
        String menuName = txtMenuName.getText().toString().trim();
        double price = Double.parseDouble(txtPrice.getText().toString().trim());
        String restaurantName = rName.toString();
        Double latitude = Double.parseDouble(rLatitude.toString().trim());
        Double longitude = Double.parseDouble(rLongitude.toString().trim());
        String image = rImage.toString();


        Menu menu = new Menu(menuName, price, restaurantName, image, latitude, longitude);
        menuDatabase.push().setValue(menu);
        Toast.makeText(this, "Menu of restaurant was saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == btnDisplayOnMap) {
            finish();
        }
        if(view == btnSaveMenu) {
            saveMenuRestaurantInfo();
            txtMenuName.getText().clear();
            txtPrice.getText().clear();
        }
    }
}
