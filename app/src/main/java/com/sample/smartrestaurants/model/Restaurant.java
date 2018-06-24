package com.sample.smartrestaurants.model;

public class Restaurant {

    public String name;
    public double latitude;
    public double longitude;
    public String type;
    public String kitchen;
    public String image;
    public String priceLevel;
    public double evaluation;
    public int numberEvaluation;
    public String garden;
    public String childrensCorner;
    public String parkingFree;


    public Restaurant() {

    }

    public Restaurant(String name, double latitude, double longitude, String type, String kitchen, String priceLevel, String image, double evaluation, int numberEvaluation, String garden, String childrensCorner, String parkingFree) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.kitchen = kitchen;
        this.image = image;
        this.priceLevel = priceLevel;
        this.evaluation = evaluation;
        this.numberEvaluation = numberEvaluation;
        this.garden = garden;
        this.childrensCorner = childrensCorner;
        this.parkingFree = parkingFree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public int getNumberEvaluation() {
        return numberEvaluation;
    }

    public void setNumberEvaluation(int numberEvaluation) {
        this.numberEvaluation = numberEvaluation;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getChildrensCorner() {
        return childrensCorner;
    }

    public void setChildrensCorner(String childrensCorner) {
        this.childrensCorner = childrensCorner;
    }

    public String getParkingFree() {
        return parkingFree;
    }

    public void setParkingFree(String parkingFree) {
        this.parkingFree = parkingFree;
    }
}
