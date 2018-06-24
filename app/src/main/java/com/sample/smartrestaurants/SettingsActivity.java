package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private Button btnFindRestaurants;
    private EditText txtTypeRes;
    private EditText txtKitchenType;
    private EditText txtKindOfFood;
    private EditText txtPriceLevel;
    private EditText txtGarden;
    private EditText txtChildrensCorner;
    private EditText txtParkingFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtTypeRes = findViewById(R.id.typeRes);
        txtKitchenType = findViewById(R.id.kitchenType);
        txtKindOfFood = findViewById(R.id.kindOfFood);
        txtPriceLevel = findViewById(R.id.priceLevel);
        txtGarden = findViewById(R.id.garden);
        txtChildrensCorner = findViewById(R.id.childrensCorner);
        txtParkingFree = findViewById(R.id.parkingFree);
    }

    public void actionFindRestaurant(View view) {
        String typeRes = txtTypeRes.getText().toString().trim();
        String kitchenType = txtKitchenType.getText().toString().trim();
        String kindOfFood = txtKindOfFood.getText().toString().trim();
        String priceLevel = txtPriceLevel.getText().toString().trim();
        String garden = txtGarden.getText().toString().trim();
        String childrensCorner = txtChildrensCorner.getText().toString().trim();
        String parkingFree = txtParkingFree.getText().toString().trim();

        Intent intent = new Intent(SettingsActivity.this, FindRestaurantActivity.class);
        intent.putExtra("typeRes", typeRes);
        intent.putExtra("kitchenType", kitchenType);
        intent.putExtra("kindOfFood", kindOfFood);
        intent.putExtra("priceLevel", priceLevel);
        intent.putExtra("garden", garden);
        intent.putExtra("childrensCorner", childrensCorner);
        intent.putExtra("parkingFree", parkingFree);
        startActivity(intent);
    }
}
