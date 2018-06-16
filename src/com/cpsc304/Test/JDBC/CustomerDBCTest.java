package com.cpsc304.Test.JDBC;

import com.cpsc304.JDBC.CustomerDBC;
import com.cpsc304.JDBC.DBConnection;
import com.cpsc304.JDBC.LoginDBC;
import com.cpsc304.model.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class CustomerDBCTest {

    private static Date date;
    private static Time time;
    private static Time readyTime;
    private static Customer customer;
    private static Restaurant restaurant;
    private static Order order;
    private static Pickup pickup;

    static void runBefore() {
        date = new Date(100000);
        time = new Time(1000);
        readyTime = new Time(2000);
        restaurant = new Restaurant(null, 5, null,null, null, 1, true, null, null, null);
        customer = new Customer("854", "Harvey", "11111","77879991234", 10000, 5, 100, null, null);
        order = new Pickup(customer, 10000, date, time, 1000, OrderStatus.SUBMITTED, restaurant, null, readyTime);

    }

    static void testCommitOrder() {
        DBConnection.connect();
        try {
            CustomerDBC.commitOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("sql Exception");
        }
    }

    public static void main (String[] args) {
        Food f1 = new Food("PICK", restaurant, 10);
        Food f2 = new Food("PICK", restaurant, 10);
        System.out.println(f1 == f2);
        runBefore();
        //testCommitOrder();
        DBConnection.connect();
        try {
            System.out.println(LoginDBC.verify("customer", "854", "aqb39q"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
        DBConnection.close();
    }
}
