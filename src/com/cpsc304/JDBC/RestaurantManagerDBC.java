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
        PreparedStatement pstmt;
        sqlString = "DELETE FROM Restaurant ";
        sqlString += "WHERE restaurantID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, restID);
        pstmt.executeUpdate();
        con.commit();
    }

    public static List<Food> getPopularDish(Restaurant restaurant) throws SQLException {
        return null;
    }

    public static void addToMenu(Food food) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        sqlString = "INSERT INTO offers ";
        sqlString += "VALUES (?, ?, ?)";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, food.getName());
        pstmt.setInt(2, food.getRestaurant().getId());
        pstmt.setDouble(3, food.getPrice());
        pstmt.executeUpdate();
        con.commit();
        food.getRestaurant().addFood(food);
    }

    public static void deleteInMenu(Restaurant restaurant, Food food) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        sqlString = "DELETE FROM offers ";
        sqlString += "WHERE restaurantID = ? AND LOWER(food_name) = LOWER(?)";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, food.getRestaurant().getId());
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

    public static int getOrderNum(Date startDate, Date endDate){
        return 0;
    }

    public static List<Order> getOrders(Restaurant restaurant, Date startDate, Date endDate){
        return null;
    }

    public static double getRevenue(Restaurant restaurant, Date startDate, Date endDate){
        return 0;
    }
}
