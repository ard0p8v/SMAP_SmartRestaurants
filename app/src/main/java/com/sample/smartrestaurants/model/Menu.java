package com.sample.smartrestaurants.model;

public class Menu {

    public String menuName;
    public double price;
    public String restaurantName;
    public String image;

    public Menu() {

    }

    public Menu(String menuName, double price, String restaurantName) {
        this.menuName = menuName;
        this.price = price;
        this.restaurantName = restaurantName;
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
