package com.sample.smartrestaurants.model;

public class Menu {

    public String menuName;
    public double price;
    public String restaurantName;

    public Menu() {

    }

    public Menu(String menuName, double price, String restaurantName) {
        this.menuName = menuName;
        this.price = price;
        this.restaurantName = restaurantName;
    }
}
