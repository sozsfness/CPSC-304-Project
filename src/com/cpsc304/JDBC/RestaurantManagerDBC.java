package com.cpsc304.JDBC;

import com.cpsc304.model.Food;
import com.cpsc304.model.Order;
import com.cpsc304.model.Restaurant;

import java.sql.Date;
import java.util.List;

public class RestaurantManagerDBC extends UserDBC{
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
    public static boolean updateOrder(){
        return false;
    }

    public static double getOrder(Date startDate, Date endDate){
        return 0;
    }

    public static List<Order> getOrders(Restaurant restaurant, Date startDate, Date endDate){
        return null;
    }

    public static double getRevenue(Restaurant restaurant, Date startDate, Date endDate){
        return 0;
    }
}
