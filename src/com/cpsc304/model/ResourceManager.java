package com.cpsc304.model;

import java.util.Map;

public class ResourceManager {

    // all data are update-to-date
    private static Map<Integer, Pickup> pickupMap;
    private static Map<Integer, Delivery> deliveryMap;
    private static Map<String, Customer> customerMap;
    private static Map<String, Courier> courierMap;
    private static Map<String, ResourceManager> managerMap;
    private static ResourceManager ourInstance = new ResourceManager();

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    private ResourceManager() {

    }
}
