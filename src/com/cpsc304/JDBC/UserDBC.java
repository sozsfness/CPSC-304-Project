package com.cpsc304.JDBC;

import com.cpsc304.model.Order;
import com.cpsc304.model.Restaurant;
import com.cpsc304.model.User;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public abstract class UserDBC {
    public static void updateUserInfo(User user) {

    }

    protected static Set<Order> getOrders(Restaurant restaurant) {
        return null;
    }

    protected static Set<Order> getOrders(Date startDate, Date endDate) {
        return null;
    }

    public static List<Order> getOrders(String restaurantName, Date startDate, Date endDate) {
        return null;
        //use MAINUI currentUser to get orders for a specific customer
    }

    public static Order getOrder(String orderID){
        return null;
    }
}
