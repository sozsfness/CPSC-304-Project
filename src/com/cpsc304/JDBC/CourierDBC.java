package com.cpsc304.JDBC;

import com.cpsc304.model.Address;
import com.cpsc304.model.Order;
import com.cpsc304.model.OrderStatus;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.*;
import java.util.Set;

public class CourierDBC extends UserDBC {

    private static Connection con = DBConnection.getCon();

    public static Time getScheduledTime(int OrderID) {
        return null;
    }

    public static Address getLocation(int OrderID) {
        String sqlString;

        return null;
    }

    //if the status of the order is not READY or DELIVERING currently, return false
    public static boolean updateOrder(Order order) throws SQLException {
        if (order.getStatus() != OrderStatus.READY && order.getStatus() != OrderStatus.DELIVERING)
            return false;
        String sqlString;
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        if (order.getStatus() == OrderStatus.READY)
            sqlString = "UPDATE orders SET order_status = DELIVERING";
        else
            sqlString = "UPDATE orders SET order_status = DELIVERED";
        stmt.executeUpdate(sqlString);
        con.commit();
        return true;
    }

    public static Set<Order> getOrders(Date startDate, Date endDate) {
        return null;
    }

    public static double getIncome(Date startDate, Date endDate){
        return 0;
    }
}
