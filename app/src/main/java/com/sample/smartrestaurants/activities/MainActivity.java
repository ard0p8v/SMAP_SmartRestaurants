package com.sample.smartrestaurants.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sample.smartrestaurants.R;
import com.sample.smartrestaurants.services.CompassTesting;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle navigation item click here
        if (id == R.id.btnDatabase) {
            // Handle the camera action
        } else if (id == R.id.btnAddRestaurant) {
            Intent intent = new Intent(this, AddRestaurantActivity.class);
            startActivity(intent);

        } else if (id == R.id.btnMap) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);


        } else if (id == R.id.testKompasButton) {
            Intent intent = new Intent(this, CompassTesting.class);
            startActivity(intent);
        } else if (id == R.id.kontakt) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"pavel.ardolf@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.commentAndroidApp);
            try {
                startActivity(Intent.createChooser(i, "Send message to developer"));
            } catch (android.content.ActivityNotFoundException ex) {
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void actionBtnGoToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void actionBasicSearch(View view) {
        Intent intent = new Intent(this, BasicSearchActivity.class);
        startActivity(intent);
    }

    public void actionAdvancedSearch(View view) {
        Intent intent = new Intent(this, AdvancedSearchActivity.class);
        startActivity(intent);
    }

    public void actionSignOut(View view) {
        firebaseAuth.signOut();

        Toast.makeText(MainActivity.this, "Logout was successful", Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
