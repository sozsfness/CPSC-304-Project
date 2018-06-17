package com.cpsc304.Test.JDBC;

import com.cpsc304.JDBC.*;
import com.cpsc304.UI.MainUI;
import com.cpsc304.model.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.jupiter.api.*;
import sun.applet.Main;
import sun.rmi.runtime.Log;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class CustomerDBCTest {

    private static Date date;
    private static Time time;
    private static Time readyTime;
    private static Customer customer;
    private static Restaurant restaurant;
    private static Order order;
    private static Courier courier;
    private static Pickup pickup;

    static void runBefore() {
        date = new Date(100000);
        time = new Time(1000);
        readyTime = new Time(2000);
        restaurant = new Restaurant(null, 5, null,null, null, 1, true, null, null, null);
        customer = new Customer("b9q3u", "Samson Mason", "Wyw3026","4964411825", 10000, 5, 100);
        order = new Pickup(customer, new Long(10000), date, time, 1000, OrderStatus.SUBMITTED, restaurant, null, readyTime);
        courier = new Courier("j2g5z", "ppp","Mtb0525", "1113", null);
        DBConnection.connect();
    }

    static void testCommitOrder() {
        try {
            CustomerDBC.commitOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("sql Exception");
        }
    }

    static void testVerify() {
        try {
            System.out.println(LoginDBC.verify("courier", "j2g5z", "Mtb0525"));
            UserDBC.getUser("j2g5z");
            CourierDBC.getCurier("j2g5z");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void testLogin(){
        try {
            LoginDBC.testCount();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("sql Exception");
        }
    }

    static void testGetOrder(){
        MainUI.currentUser = (User)courier;
        try {
            List<Order> orders = CourierDBC.getOrders(Date.valueOf("2016-01-01"),Date.valueOf("2018-12-12"));
            for (Order order: orders) {
                System.out.println(order.getOrderID() + " " + order.getStatus() + " " + order.getAmount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void testGetPickup(){
        MainUI.currentUser = customer;
        try {
            List<Order> orders = CustomerDBC.getDeliveries(744,Date.valueOf("2016-01-01"),Date.valueOf("2018-12-12"));
            for (Order order: orders) {
                System.out.println(order.getOrderID() + " " + order.getStatus() + " " + order.getAmount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main (String[] args) {
        Food f1 = new Food("PICK", restaurant, 10);
        Food f2 = new Food("PICK", restaurant, 10);
        System.out.println(f1 == f2);
        runBefore();
        ResourceManager.getInstance();
        //testCommitOrder();
        //testVerify();
        //testLogin();
//        testGetOrder();
        testGetPickup();
        System.out.println("Done!");
        DBConnection.close();
    }
}
