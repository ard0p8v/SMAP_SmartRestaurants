package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdvancedSearchActivity extends AppCompatActivity {

    private EditText txtTypeRes;
    private EditText txtKitchenType;
    private EditText txtPriceLevel;
    private EditText txtGarden;
    private EditText txtChildrensCorner;
    private EditText txtParkingFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        /*
        txtTypeRes = findViewById(R.id.typeRes);
        txtKitchenType = findViewById(R.id.kitchenType);
        txtPriceLevel = findViewById(R.id.priceLevel);
        txtGarden = findViewById(R.id.garden);
        txtChildrensCorner = findViewById(R.id.childrensCorner);
        txtParkingFree = findViewById(R.id.parkingFree);
        */
    }

    public void actionFindRestaurant(View view) {

        /*String typeRes = txtTypeRes.getText().toString().trim();
        String kitchenType = txtKitchenType.getText().toString().trim();
        String priceLevel = txtPriceLevel.getText().toString().trim();
        String garden = txtGarden.getText().toString().trim();
        String childrensCorner = txtChildrensCorner.getText().toString().trim();
        String parkingFree = txtParkingFree.getText().toString().trim();

        Intent intent = new Intent(AdvancedSearchActivity.this, AdvancedFindRestaurantActivity.class);
        intent.putExtra("typeRes", typeRes);
        intent.putExtra("kitchenType", kitchenType);
        intent.putExtra("priceLevel", priceLevel);
        intent.putExtra("garden", garden);
        intent.putExtra("childrensCorner", childrensCorner);
        intent.putExtra("parkingFree", parkingFree);
        startActivity(intent);
        */
    }
}
