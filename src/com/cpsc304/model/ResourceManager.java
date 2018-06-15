package com.cpsc304.model;

import java.util.Map;

public class ResourceManager {

    // all data are update-to-date
    private static Map<Integer, Pickup> pickupMap;
    private static Map<Integer, Delivery> deliveryMap;
    private static Map<String, Customer> customerMap;
    private static Map<String, Courier> courierMap;
    private static Map<String, RestaurantManager> managerMap;
    private static Map<String, Restaurant> restaurantMap;
    private static ResourceManager ourInstance = new ResourceManager();

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    //TODO:
    private ResourceManager() {

    }

    public Pickup getPickup(int ID) {
        return pickupMap.get(ID);
    }

    public Delivery delivery(int ID) {
        return deliveryMap.get(ID);
    }

    public Customer getCustomer(String ID) {
        return customerMap.get(ID);
    }

    public Courier getCourier(String ID) {
        return courierMap.get(ID);
    }

    public RestaurantManager getManager(String ID) {
        return managerMap.get(ID);
    }

    public Restaurant getRestaurant(int ID) {
        return restaurantMap.get(ID);
    }
}
