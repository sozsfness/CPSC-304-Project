package com.cpsc304.JDBC;

import com.cpsc304.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

public class CustomerDBC extends UserDBC {

    private static Connection con = DBConnection.getCon();

    public static void commitOrder(Order order) throws SQLException {
        Statement stmt = con.createStatement();
        String insertString = "INSERT INTO orders VALUES (";
        insertString += order.getOrderID() + ", to_date('" + order.getDate() + "','yyyy-mm-dd'), '";
        insertString += order.getTime() + "'," + order.getAmount() + ", '";
        insertString += order.getStatus() + "', '" + order.getCustomer().getUserID() + "', ";
        insertString += order.getRestOrderedAt().getId() + ")";
        stmt.executeUpdate(insertString);
        /* if (order instanceof Pickup) {
            insertString += "Pickup"
        }*/
    }

    public static void updateOrderStatus(int OrderID, OrderStatus orderStatus) {

    }

    public static List<Restaurant> getRestaurants(List<Food> foods) {
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

    protected static Set<Order> getOrdersInProgress(Date startDate, Date endDate) {
        return null;
    }

    protected static Set<Order> getOrders() {
        return null;
    }
}
