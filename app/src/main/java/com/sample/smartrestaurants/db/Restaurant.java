package com.sample.smartrestaurants.db;

public class Restaurant {

    public String name;
    public double latitude;
    public double longitude;

    public Restaurant() {

    }

    public Restaurant(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
