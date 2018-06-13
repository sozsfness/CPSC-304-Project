package com.cpsc304.model;

import java.util.Set;

public class Food {

    private String name;
    private Restaurant restaurant;
    private double price;

    public Food(String name, Restaurant restaurant, double price) {
        this.name = name;
        this.restaurant = restaurant;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public double getPrice() {
        return price;
    }
}
