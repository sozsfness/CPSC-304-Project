package com.cpsc304.JDBC;

import com.cpsc304.model.Food;
import com.cpsc304.model.Order;
import com.cpsc304.model.OrderStatus;
import com.cpsc304.model.Restaurant;

import java.sql.Date;
import java.util.List;

public class CustomerDBC extends UserDBC {
    public static void commitOrder(Order order) {

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
}
