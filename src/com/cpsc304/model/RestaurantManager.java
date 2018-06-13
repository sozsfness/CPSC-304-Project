package com.cpsc304.model;

import com.sun.org.apache.regexp.internal.RE;

import java.util.Set;

public class RestaurantManager extends User{

    private Set<Restaurant> restaurants;

    public RestaurantManager(int userID, String name, String password, String phoneNum, Set<Restaurant> restaurants) {
        super(userID, name, password, phoneNum);
        this.restaurants = restaurants;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
