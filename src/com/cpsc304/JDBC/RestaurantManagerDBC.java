package com.cpsc304.JDBC;

import com.cpsc304.model.Food;
import com.cpsc304.model.Order;
import com.cpsc304.model.OrderStatus;
import com.cpsc304.model.Restaurant;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RestaurantManagerDBC extends UserDBC{

    private static Connection con = DBConnection.getCon();

    public static void addRestaurant(Restaurant restaurant) {

    }

    public static void deleteRestaurant (Restaurant restaurant) {

    }

    public static List<Food> getPopularDish(Restaurant restaurant) {
        return null;
    }

    public static void AddToMenu(Restaurant restaurant, Food food) {

    }

    public static void deleteInMenu(Restaurant restaurant, Food food) {

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
