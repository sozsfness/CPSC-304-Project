package com.cpsc304.Test.JDBC;

import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.DBConnection;
import com.cpsc304.model.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class CustomerDBCTest {

    private static Date date;
    private static Time time;
    private static Customer customer;
    private static Restaurant restaurant;
    private static Order order;

    static void runBefore() {
        date = new Date(100000);
        time = new Time(1000);
        restaurant = new Restaurant(null, 5, null,null, null, 2000, true, null, null, null , null);
        customer = new Customer(1111, "Harvey", "11111","77879991234", 10000, 5, 100, null, null);
        order = new Pickup(customer, 10000, date, time, 1000, OrderStatus.SUBMITTED, restaurant, null, null);

    }

    static void testCommitOrder() {
        DBConnection.connect();
        try {
            CustomerDBC.commitOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        runBefore();
        testCommitOrder();
    }
}
