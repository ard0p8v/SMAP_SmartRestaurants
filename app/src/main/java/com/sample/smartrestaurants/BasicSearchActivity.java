package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BasicSearchActivity extends AppCompatActivity {

    private EditText txtKindOfFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_search);

        txtKindOfFood = findViewById(R.id.kindOfFood);
    }

    public void actionFindRestaurant(View view) {

        String kindOfFood = txtKindOfFood.getText().toString().trim();

        Intent intent = new Intent(BasicSearchActivity.this, BasicFindRestaurantActivity.class);
        intent.putExtra("kindOfFood", kindOfFood);
        startActivity(intent);
    }
}
