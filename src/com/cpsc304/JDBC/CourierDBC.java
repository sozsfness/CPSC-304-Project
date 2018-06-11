package com.cpsc304.JDBC;

import com.cpsc304.model.Address;
import com.cpsc304.model.Order;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

public class CourierDBC extends UserDBC {

    public static Time getScheduledTime(int OrderID) {
        return null;
    }

    public static Address getLocation(int OrderID) {
        return null;
    }

    //if the status of the order is not READY or DELIVERING currently, return false
    public static boolean updateOrder(){
        return false;
    }

    public static Set<Order> getOrders(Date startDate, Date endDate) {
        return null;
    }

    public static double getIncome(Date startDate, Date endDate){
        return 0;
    }
}
