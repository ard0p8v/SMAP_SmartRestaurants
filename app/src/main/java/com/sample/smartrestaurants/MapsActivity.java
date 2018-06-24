package com.sample.smartrestaurants;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mRestaurants;
    private Query query;
    Marker marker;

    String kindOfFood;
    String typeRes;
    String nameRes;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    public static final float BLUE = 200.0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        kindOfFood = getIntent().getStringExtra("kindOfFood");
        typeRes = getIntent().getStringExtra("typeRes");
        nameRes = getIntent().getStringExtra("nameRes");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;

        if (nameRes != null) {
            query = FirebaseDatabase.getInstance().getReference("Restaurant")
                    .orderByChild("name")
                    .equalTo(nameRes);
        } else if (kindOfFood != null) {
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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

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

        if (nameRes != null) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Restaurant restaurant = s.getValue(Restaurant.class);
                        LatLng location = new LatLng(restaurant.latitude, restaurant.longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(restaurant.name))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.19776, 15.83386)));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (kindOfFood != null) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Menu menu = s.getValue(Menu.class);
                        LatLng location = new LatLng(menu.latitude, menu.longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(menu.restaurantName))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.19776, 15.83386)));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
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
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(restaurant.name))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.19776, 15.83386)));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
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
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(restaurant.name))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BLUE));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.19776, 15.83386)));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
                    }
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            String title = marker.getTitle();
                            Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                            intent.putExtra("markertitle", title);
                            startActivity(intent);
                            return true;
                        }
                    });
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) MapsActivity.this);
        }

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
