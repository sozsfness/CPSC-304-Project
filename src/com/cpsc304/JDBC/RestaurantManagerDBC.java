package com.cpsc304.JDBC;

import com.cpsc304.model.*;

import java.sql.*;
import java.util.List;

public class RestaurantManagerDBC extends UserDBC{

    private static Connection con = DBConnection.getCon();

    public static RestaurantManager getManager(String managerID) throws SQLException {
        return (RestaurantManager)getUser(managerID);
    }

    //TODO:show results of cascade
    public static void deleteRestaurant (int restID) throws SQLException {
        String sqlString;
        con.setAutoCommit(false);
        PreparedStatement pstmt;
        sqlString = "DELETE FROM Restaurant ";
        sqlString += "WHERE restaurantID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, restID);
        pstmt.executeUpdate();
        con.commit();
    }

    //return the name of the most popular dish
    public static String getPopularDish(int restID, Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
        sqlString = "SELECT food_name ";
        sqlString += "FROM (SELECT food_name, COUNT(food_name) AS \"Quantity Sold\" ";
        sqlString += "FROM added_in NATURAL INNER JOIN orders ";
        sqlString += "WHERE order_restaurantID = ? AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND EXISTS (SELECT food_name FROM offers  WHERE restaurantID = order_restaurantID) ";
        sqlString += "GROUP BY food_name ";
        sqlString += "ORDER BY COUNT(food_name) DESC) ";
        sqlString += "WHERE ROWNUM=1";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, restID);
        pstmt.setDate(2, startDate);
        pstmt.setDate(3, endDate);
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next())
            return rs.getString(1);
        return null;
    }

    public static void addToMenu(Food food) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        con.setAutoCommit(false);
        sqlString = "INSERT INTO offers ";
        sqlString += "VALUES (?, ?, ?)";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, food.getName());
        pstmt.setLong(2, food.getRestaurant().getId());
        pstmt.setDouble(3, food.getPrice());
        pstmt.executeUpdate();
        con.commit();
        food.getRestaurant().addFood(food);
    }

    public static void deleteInMenu(Restaurant restaurant, Food food) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        sqlString = "DELETE FROM offers ";
        con.setAutoCommit(false);
        sqlString += "WHERE restaurantID = ? AND LOWER(food_name) = LOWER(?)";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setLong(1, food.getRestaurant().getId());
        pstmt.setString(2, food.getName());
        pstmt.executeUpdate();
        con.commit();
        food.getRestaurant().deleteFood(food);
    }

    //if the order status is not SUBMITTED currently, do nothing and return false
    public static boolean updateOrder(Order order) throws SQLException {
        if (order.getStatus() != OrderStatus.SUBMITTED)
            return false;
        String sqlString;
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        sqlString = "UPDATE orders SET order_status = READY";
        stmt.executeUpdate(sqlString);
        con.commit();
        return true;
    }

    public static List<Order> getOrders(Restaurant restaurant, Date startDate, Date endDate) throws SQLException {
        return CustomerDBC.getOrders(restaurant.getId(), startDate, endDate);
    }

    public static double getRevenue(Restaurant restaurant, Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
        sqlString = "SELECT Sum(order_amount) ";
        sqlString += "FROM orders ";
        sqlString += "WHERE order_status = 'Complete' AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND order_restaurantID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setInt(3, restaurant.getId());
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next())
            return rs.getInt(1);
        else
            return 0;
    }
}
