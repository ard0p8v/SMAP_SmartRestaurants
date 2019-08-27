package com.sample.smartrestaurants.activities;

import android.Manifest;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sample.smartrestaurants.R;
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
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLatLng;
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
                    .endAt(kindOfFood + "\uf8ff")
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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                        .position(userLatLng)
                        .title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));

                marker.showInfoWindow();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }


        };

        askLocationPermission();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
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
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BLUE))
                                .position(location)
                                .title(restaurant.name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.19776, 15.83386)));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));    //13

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
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BLUE))
                                .position(location)
                                .title(menu.restaurantName));
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
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BLUE))
                                .position(location)
                                .title(restaurant.name));
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
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Restaurant restaurant = s.getValue(Restaurant.class);
                        LatLng location = new LatLng(restaurant.latitude, restaurant.longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BLUE))
                                .position(location)
                                .title(restaurant.name));
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

    private void askLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)).position(userLatLng).title("Current Location"));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
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



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
