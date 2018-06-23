package com.sample.smartrestaurants.model;

public class Restaurant {

    public String name;
    public double latitude;
    public double longitude;
    public String type;
    public String kitchen;

    public Restaurant() {

    }

    public Restaurant(String name, double latitude, double longitude, String type, String kitchen) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.kitchen = kitchen;
    }
}
