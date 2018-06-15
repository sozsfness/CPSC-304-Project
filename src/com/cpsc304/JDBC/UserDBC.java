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

    public static Set<Order> getOrders(Restaurant restaurant) {
        return null;
    }

    public static Order getOrder(String orderID){
        return null;
    }
}
