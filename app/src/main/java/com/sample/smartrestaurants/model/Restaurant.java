package com.sample.smartrestaurants.model;

public class Restaurant {

    public String name;
    public double latitude;
    public double longitude;
    public String type;
    public String kitchen;
    public String priceLevel;
    public double evaluation;
    public int numberEvaluation;
    public String garden;
    public String childrensCorner;
    public String parkingFree;


    public Restaurant() {

    }

    public Restaurant(String name, double latitude, double longitude, String type, String kitchen, String priceLevel, double evaluation, int numberEvaluation, String garden, String childrensCorner, String parkingFree) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.kitchen = kitchen;
        this.priceLevel = priceLevel;
        this.evaluation = evaluation;
        this.numberEvaluation = numberEvaluation;
        this.garden = garden;
        this.childrensCorner = childrensCorner;
        this.parkingFree = parkingFree;
    }
}
