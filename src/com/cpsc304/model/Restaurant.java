package com.cpsc304.model;

import java.sql.Time;
import java.util.Map;
import java.util.Set;

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
    private Map<Food, Double> offers;
    private Set<Courier> resCouriers;
}
