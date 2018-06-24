package com.sample.smartrestaurants;

import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sample.smartrestaurants.model.Menu;
import com.sample.smartrestaurants.model.Restaurant;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mRestaurants;
    private Query query;
    Marker marker;
    String kindOfFood;
    String typeRes;
    public static final float BLUE = 200.0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        kindOfFood = getIntent().getStringExtra("kindOfFood");
        typeRes = getIntent().getStringExtra("typeRes");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;

        if (kindOfFood != null) {
            query = FirebaseDatabase.getInstance().getReference("Menu")
                    .orderByChild("menuName")
                    .startAt(kindOfFood)
                    .endAt(kindOfFood+"\uf8ff")
                    .limitToFirst(10);
        } else if (typeRes != null) {
            query = FirebaseDatabase.getInstance().getReference("Restaurant")
                    .orderByChild("type")
                    .equalTo(typeRes);
        } else {
            mRestaurants = FirebaseDatabase.getInstance().getReference("Restaurant");
            mRestaurants.push().setValue(marker);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyledark));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }

        googleMap.setOnMarkerClickListener(this);

        if (kindOfFood != null) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Menu menu = s.getValue(Menu.class);
                        LatLng location = new LatLng(menu.latitude, menu.longitude);
                        mMap.addMarker(new MarkerOptions().position(location).title(menu.restaurantName)).setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (typeRes != null) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Restaurant restaurant = s.getValue(Restaurant.class);
                        LatLng location = new LatLng(restaurant.latitude, restaurant.longitude);
                        mMap.addMarker(new MarkerOptions().position(location).title(restaurant.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            mRestaurants.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s : dataSnapshot.getChildren()) {
                        Restaurant restaurant = s.getValue(Restaurant.class);
                        LatLng location = new LatLng(restaurant.latitude, restaurant.longitude);
                        mMap.addMarker(new MarkerOptions().position(location).title(restaurant.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        /*
        // Add a marker in Sydney and move the camera
        LatLng hk = new LatLng(50.2042764, 15.829358700000057);
        mMap.addMarker(new MarkerOptions().position(hk).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hk, 15.2f));
        */
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
