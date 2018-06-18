package com.cpsc304.model;

import com.cpsc304.JDBC.RestaurantDBC;

import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class Restaurant {

    private RestaurantManager manager;
    private double rating;
    private Time openTime;
    private Time closeTime;
    private String name;
    private int id;
    private boolean deliveryOption;
    private String type;
    private Address address;
    private List<Food> offers ;
    private Set<Courier> resCouriers;

    public Restaurant(RestaurantManager manager, double rating, Time openTime, Time closeTime, String name, int id, boolean deliveryOption, String type, Address address,List<Food> offers) throws SQLException {
        this.manager = manager;
        this.rating = rating;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.name = name;
        this.id = id;
        this.deliveryOption = deliveryOption;
        this.type = type;
        this.address = address;
        this.offers = offers;
        resCouriers = new HashSet<>();
    }

    public RestaurantManager getManager() {
        return manager;
    }

    public void setManager(RestaurantManager manager) {
        this.manager = manager;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(boolean deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void addFood(Food food) {
        if (offers==null){
            offers = new ArrayList<>();
        }
        offers.add(food);
    }

    public void deleteFood(Food food) {
        if (offers==null){
            offers = new ArrayList<>();
        }
        offers.remove(food);
    }

    public List<Food> getOffers() {
        return offers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;

        Restaurant that = (Restaurant) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
