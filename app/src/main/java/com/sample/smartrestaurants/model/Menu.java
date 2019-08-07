package com.sample.smartrestaurants.model;

public class Menu {

    public String menuName;
    public double price;
    public String restaurantName;
    public String image;
    public double latitude;
    public double longitude;
    public double evaluation;

    public Menu() {

    }

    public Menu(String menuName, double price, String restaurantName, String image, double latitude, double longitude) {
        this.menuName = menuName;
        this.price = price;
        this.restaurantName = restaurantName;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Menu(String menuName, double price, String restaurantName, String image) {
        this.menuName = menuName;
        this.price = price;
        this.restaurantName = restaurantName;
        this.image = image;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public double getPrice() {
        return price;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
