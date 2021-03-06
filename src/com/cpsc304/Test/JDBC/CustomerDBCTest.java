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
import java.util.ArrayList;
import java.util.List;

public class CustomerDBCTest {

    private static Date date;
    private static Time time;
    private static Time READYTime;
    private static Customer customer;
    private static Restaurant restaurant;
    private static Order order;
    private static Courier courier;
    private static Pickup pickup;
    private static RestaurantManager restaurantManager;

    static void runBefore() {
        date = new Date(100000);
        time = new Time(1000);
        READYTime = new Time(2000);
        try {
            restaurant = new Restaurant(null, 5, null,null, null, 1, true, null, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customer = new Customer("o5o4z", "Samson Mason", "Kgk0257","4964411825", 10000, 5, 100);
        order = new Pickup(customer, new Long(10000), date, time, 1000, OrderStatus.SUBMITTED, restaurant, null, READYTime);
        courier = new Courier("a0a0a", "ppp","123456", "1113", null);
        restaurantManager = new RestaurantManager("h5c2k",".","Uft5964","1111",null);
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

//    static void testLogin(){
//        try {
//            LoginDBC.testCount();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("sql Exception");
//        }
//    }

    static void testManager(){
        MainUI.currentUser = restaurantManager;
    }

    static void testGetOrder(){
        MainUI.currentUser = (User)courier;
        try {
            List<Order> orders = CourierDBC.getOrders(Date.valueOf("2016-01-01"),Date.valueOf("2018-12-12"));
            for (Order order: orders) {
                System.out.println(order.getOrderID() + " " + order.getStatus() + " " + order.getAmount() + ((Delivery)order).getDeliveryFee());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void testIncome(){
        MainUI.currentUser = courier;
        try{
            Double in = CourierDBC.getIncome(Date.valueOf("2001-01-01"),Date.valueOf("2020-12-12"));
            System.out.println(in);
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
    static void testGetSpd(){
        MainUI.currentUser = customer;
        try {
            System.out.println(CustomerDBC.getSpending(Date.valueOf("2016-01-01"),Date.valueOf("2018-12-12")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void testRecommend(){
        MainUI.currentUser = customer;
        try {
            List<Restaurant> list = CustomerDBC.getRecommendedRestaurants();
            for (Restaurant rest : list)
                System.out.println(rest.getId() + " " + rest.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void testRest(){
        MainUI.currentUser = courier;
        try {
            RestaurantManagerDBC.deleteRestaurant(744);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MainUI.currentUser = courier;
        try{
            CourierDBC.getMaxs(Date.valueOf("2016-01-01"),Date.valueOf("2018-12-12"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<Order> orders = CustomerDBC.getOrders(336,Date.valueOf("2016-01-01"),Date.valueOf("2018-12-12"));
            System.out.println(orders.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void testGetRankedRestaurants(){
        MainUI.currentUser = customer;
        List<String> names = new ArrayList<>();
        names.add("Sushi");
        try {
            List<Restaurant> list = CustomerDBC.getRankedRestaurants(names, false, false, false ,false, false);
            for (Restaurant rest : list) {
                System.out.println(rest.getId() + " " + rest.getName());
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
        testGetOrder();
//        testGetPickup();
//        testIncome();
        //testGetSpd();
        //testRecommend();
        //testGetRankedRestaurants();

        System.out.println("Done!");
        DBConnection.close();
    }
}
