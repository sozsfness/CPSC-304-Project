package com.cpsc304.model;

import com.cpsc304.JDBC.LoginDBC;
import com.cpsc304.UI.Login;

import java.sql.SQLException;
import java.util.Map;

public class ResourceManager {

    // all data are update-to-date
    private static Map<String, Courier> courierMap;
    private static Map<String, RestaurantManager> managerMap;
    private static Map<Integer, Restaurant> restaurantMap;
    private static ResourceManager ourInstance = new ResourceManager();

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    //TODO:
    private ResourceManager() {
        try {
            courierMap = LoginDBC.getCouriers();
            managerMap = LoginDBC.getManagers();
            restaurantMap = LoginDBC.getRestaurants();
        } catch (SQLException e) {
            System.out.println("Initialization fails");
            e.printStackTrace();
        }
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
