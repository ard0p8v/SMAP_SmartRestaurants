package com.sample.smartrestaurants.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import com.sample.smartrestaurants.R;

public class AdvancedSearchActivity extends AppCompatActivity {

    private EditText txtTypeRes;
    private EditText txtKitchenType;
    private EditText txtPriceLevel;
    private EditText txtGarden;
    private EditText txtChildrensCorner;
    private EditText txtParkingFree;

    RadioGroup radioGroupLevel;
    RadioButton level_300;
    RadioButton level_300600;
    RadioButton level_600;
    RadioButton level_nevim;

    RadioGroup radioGroupParking;
    RadioButton parkingFree_ano;
    RadioButton parkingFree_ne;
    RadioButton parkingFree_nevim;

    RadioGroup radioGroupCorner;
    RadioButton corner_ano;
    RadioButton corner_ne;
    RadioButton corner_nevim;

    RadioGroup radioGroupGarden;
    RadioButton garden_ano;
    RadioButton garden_ne;
    RadioButton garden_nevim;

    RadioGroup radioGroupType;
    RadioButton type_restaurant;
    RadioButton type_pizzeria;
    RadioButton type_fastfood;
    RadioButton type_cafeterie;

    RadioGroup radioGroupKitchen;
    RadioButton kitchen_czech;
    RadioButton kitchen_italian;
    RadioButton kitchen_mexician;
    RadioButton kitchen_chinese;
    RadioButton kitchen_nevim;

    RatingBar ratingBar;

    Button btnFindRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        radioGroupLevel = findViewById(R.id.radioGroupLevel);
        level_300 = findViewById(R.id.level_300);
        level_300600 = findViewById(R.id.level_300600);
        level_600 = findViewById(R.id.level_600);
        level_nevim = findViewById(R.id.level_nevim);

        radioGroupParking = findViewById(R.id.radioGroupParking);
        parkingFree_ano = findViewById(R.id.parkingFree_ano);
        parkingFree_ne = findViewById(R.id.parkingFree_ne);
        parkingFree_nevim = findViewById(R.id.parkingFree_nevim);

        radioGroupCorner = findViewById(R.id.radioGroupCorner);
        corner_ano = findViewById(R.id.corner_ano);
        corner_ne = findViewById(R.id.corner_ne);
        corner_nevim = findViewById(R.id.parkingFree_nevim);

        radioGroupGarden = findViewById(R.id.radioGroupGarden);
        garden_ano = findViewById(R.id.garden_ano);
        garden_ne = findViewById(R.id.garden_ne);
        garden_nevim = findViewById(R.id.garden_nevim);

        radioGroupType = findViewById(R.id.radioGroupType);
        type_restaurant = findViewById(R.id.type_restaurant);
        type_pizzeria = findViewById(R.id.type_pizzeria);
        type_fastfood = findViewById(R.id.type_fastfood);
        type_cafeterie = findViewById(R.id.type_cafeterie);

        radioGroupKitchen = findViewById(R.id.radioGroupKitchen);
        kitchen_czech = findViewById(R.id.kitchen_czech);
        kitchen_italian = findViewById(R.id.kitchen_italian);
        kitchen_mexician = findViewById(R.id.kitchen_mexician);
        kitchen_chinese = findViewById(R.id.kitchen_chinese);
        kitchen_nevim = findViewById(R.id.kitchen_nevim);

        ratingBar = findViewById(R.id.ratingbar);

        btnFindRestaurant = findViewById(R.id.btnFindRestaurant);

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
        int typeId = radioGroupType.getCheckedRadioButtonId();
        RadioButton buttonTypeId = (RadioButton) findViewById(typeId);
        String type = buttonTypeId.getText().toString();

        int kitchenId = radioGroupKitchen.getCheckedRadioButtonId();
        RadioButton buttonKitchenId = (RadioButton) findViewById(kitchenId);
        String kitchen = buttonKitchenId.getText().toString();

        int priceId = radioGroupLevel.getCheckedRadioButtonId();
        RadioButton buttonPriceId = (RadioButton) findViewById(priceId);
        String price = buttonPriceId.getText().toString();

        int gardenId = radioGroupGarden.getCheckedRadioButtonId();
        RadioButton buttonGarderId = (RadioButton) findViewById(gardenId);
        String garden = buttonGarderId.getText().toString();

        int cornerId = radioGroupCorner.getCheckedRadioButtonId();
        RadioButton buttonCornerId = (RadioButton) findViewById(cornerId);
        String corner = buttonCornerId.getText().toString();

        int parkingId = radioGroupParking.getCheckedRadioButtonId();
        RadioButton buttonParkingId = (RadioButton) findViewById(parkingId);
        String parking = buttonParkingId.getText().toString();

        float rating = ratingBar.getRating();

        Intent intent = new Intent(AdvancedSearchActivity.this, AdvancedFindRestaurantActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("kitchen", kitchen);
        intent.putExtra("price", price);
        intent.putExtra("garden", garden);
        intent.putExtra("corner", corner);
        intent.putExtra("parking", parking);
        intent.putExtra("rating", rating);
        startActivity(intent);
    }

    public void onRadioButtonClickedTypeRestaurant(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.type_restaurant:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.type_pizzeria:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.type_fastfood:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.type_cafeterie:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    public void onRadioButtonClickedKitchen(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.kitchen_czech:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.kitchen_italian:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.kitchen_mexician:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.kitchen_chinese:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.kitchen_nevim:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    public void onRadioButtonClickedPrice(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.level_300:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.level_300600:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.level_600:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.level_nevim:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    public void onRadioButtonClickedGarden(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.garden_ano:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.garden_ne:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.garden_nevim:
                if (checked)
                    // Pirates are the best
                    break;
        }
    }

    public void onRadioButtonClickedCorner(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.corner_ano:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.corner_ne:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.corner_nevim:
                if (checked)
                    // Pirates are the best
                    break;
        }
    }

    public void onRadioButtonClickedParking(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.parkingFree_ano:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.parkingFree_ne:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.parkingFree_nevim:
                if (checked)
                    // Pirates are the best
                    break;
        }
    }
}
