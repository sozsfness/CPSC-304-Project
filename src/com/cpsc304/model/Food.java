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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;

        Food food = (Food) o;

        if (name != null ? !name.equals(food.name) : food.name != null) return false;
        return restaurant != null ? restaurant.equals(food.restaurant) : food.restaurant == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (restaurant != null ? restaurant.hashCode() : 0);
        return result;
    }
}
