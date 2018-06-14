package com.cpsc304.JDBC;

import com.cpsc304.model.*;

import java.sql.*;
import java.util.ArrayList;
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

    public static List<Restaurant> getRestaurants(List<Food> foods) {
        List<Restaurant> restaurants = new ArrayList<>();
        String sqlString = "SELECT ";
        for (Food food : foods) {

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

    public static List<Restaurant> getRankedRestaurants(Food food) {
        return null;
    }

    public static double getSpending(Date startDate, Date endDate){
        return 0;
    }

    public static int getChangedPoints(Date startDate, Date endDate) {
        return 0;
    }

    protected static List<Order> getOrdersInProgress(Date startDate, Date endDate) {
        return null;
    }

    public static List<Order> getOrders() {
        return null;
    }
}
