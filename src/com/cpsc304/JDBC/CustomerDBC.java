package com.cpsc304.JDBC;

import com.cpsc304.UI.MainUI;
import com.cpsc304.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerDBC extends UserDBC {

    private static Connection con = DBConnection.getCon();

    public static void commitOrder(Order order) throws SQLException {
        String insertString;
        PreparedStatement pstmt;
        String time;
        con.setAutoCommit(false);
        insertString = "INSERT INTO orders VALUES (";
        time = order.getTime().toString();
        time = time.substring(0, 5);
        insertString += order.getOrderID() + ", ?, ?, ";
        insertString += order.getAmount() + ", '";
        insertString += order.getStatus() + "', '" + order.getCustomer().getUserID() + "', ";
        insertString += order.getRestOrderedAt().getId() + ")";
        pstmt = con.prepareStatement(insertString);
        pstmt.setDate(1,order.getDate());
        pstmt.setString(2, time);
        pstmt.executeUpdate();
        con.commit();
        pstmt.close();
        if (order instanceof Pickup){
            insertString = "INSERT INTO pick_up VALUES (";
            insertString += order.getOrderID() + ", ?)";
            pstmt = con.prepareStatement(insertString);
            time = ((Pickup)order).getReadyTime().toString();
            time = time.substring(0, 5);
            pstmt.setString(1, time);
            pstmt.executeUpdate();
            con.commit();
            pstmt.close();
        }
        else {
            Delivery delivery = (Delivery)order;
            Address address = delivery.getDest();
            insertString = "INSERT INTO delivery_delivers VALUES (";
            insertString += delivery.getOrderID() + ", ?, ";
            insertString += delivery.getDeliveryFee() + ", '" + delivery.getCourier().getUserID() +"', '";
            insertString += address.getPostalCode() + "', '" + address.getStreet() + "', ";
            insertString += address.getHouseNum() + ")";
            time = delivery.getArrivalTime().toString();
            time = time.substring(0, 5);
            pstmt.setString(1, time);
            pstmt.executeUpdate();
            con.commit();
            pstmt.close();
        }
    }

    public static void updateOrderStatus(int orderID, OrderStatus orderStatus) throws SQLException {
        String sqlString = "UPDATE orders SET order_status = '" + orderStatus.toString() +"'";
        sqlString += "WHERE orderID = " + orderID;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sqlString);
        stmt.close();
    }

    public static List<Restaurant> getRestaurants(List<String> foods) {
        List<Restaurant> restaurants = new ArrayList<>();
        String sqlString = "SELECT ";
        for (String food : foods) {

        }
        return null;
    }

    public static List<Restaurant> getBestRestaurants(String type) {
        return null;
    }

    public static List<Restaurant> getClosestRestaurants(String type) {
        return null;
    }

    public static List<Restaurant> getRecommendedRestaurants() {
        return null;
    }
    public static List<Restaurant> getRestaurantsOfRating(Integer rating) {
        return null;
    }

    public static List<Restaurant> getRankedRestaurants(List<String> food) {
        //note food may be a string containing multiple food names,separated with commas
        return null;
    }
    public static List<Restaurant> getRankedRestaurants(List<String> food,Integer rating) {
        //note food may be a string containing multiple food names,separated with commas
        return null;
    }

    public static double getSpending(Date startDate, Date endDate){
        return 0;
    }

    public static int getChangedPoints(Date startDate, Date endDate) {
        return 0;
    }

    public static List<Order> getOrdersInTimePeriod(Date startDate, Date endDate) {
        return null;
    }

    public static List<Order> getOrders() {
        return null;
    }
    public static Set<Order> getOrders(Date startDate, Date endDate) throws SQLException {
        HashSet<Order> orders = new HashSet<>();
        Set<Pickup> pickups = getPickups(startDate, endDate);
        Set<Delivery> deliveries = getDeliveries(startDate, endDate);
        con.setAutoCommit(false);
        for (Order order : pickups) {
            orders.add(order);
        }
        for (Order order : deliveries) {
            orders.add(order);
        }
        return orders;
    }

    private static Set<Pickup> getPickups(Date startDate, Date endDate) {
        String sqlString;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Set<Pickup> pickups = new HashSet<>();
        sqlString = "SELECT orderID, order_date, order_time, order_amount, order_status, food_name, price, quantity, res_name, res_rating";
        return pickups;
    }

    private static Set<Delivery> getDeliveries(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        Delivery delivery;
        Set<Delivery> deliveries = new HashSet<>();
        if (MainUI.currentUser == null) return null;
        sqlString = "SELECT o.*, delivery_fee, userName";
        sqlString += "FROM order o, customer, delivery_delivers d, courier, user";
        sqlString += "WHERE o.orderID = d.oderID AND order_date >=  ?";
        sqlString += "AND order_date <= ? AND order_customerID = ?";
        sqlString += "AND cor_userID = courierID AND cor_userID = userID";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()) {

        }
        return deliveries;
    }
}