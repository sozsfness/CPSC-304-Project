package com.cpsc304.JDBC;

import com.cpsc304.UI.MainUI;
import com.cpsc304.model.*;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class RestaurantManagerDBC extends UserDBC{

    private static Connection con = DBConnection.getCon();

    public static RestaurantManager getManager(String managerID) throws SQLException {
        User user = getUser(managerID);
        return new RestaurantManager(user.getUserID(),user.getName(),user.getPassword(),user.getPhoneNum(),getResS(managerID));
    }

    public static Set<Restaurant> getResS(String managerID) throws SQLException {
        Set<Restaurant> toRet = new HashSet<>();
        String sqlString;
        ResultSet rs;
        con.setAutoCommit(false);
        PreparedStatement pstmt;
        sqlString = "SELECT * FROM restaurant r, restaurant_managers rm WHERE res_managerID =  res_userID AND res_userID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, managerID);
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()){
            int resID = rs.getInt(1);
            String resN = rs.getString(2);
            System.out.println(resN);
            Time o = Time.valueOf(rs.getString(3) + ":00");
            Time cl = Time.valueOf(rs.getString(4) + ":00");
            Double r = rs.getDouble(5);
            String ty = rs.getString(6);
            boolean del = rs.getInt(7)==1;
            String pos = rs.getString(9);
            String str = rs.getString(10);
            int hnum = rs.getInt(11);
            toRet.add(new Restaurant((RestaurantManager) MainUI.currentUser,r,o,cl,resN,resID,del,ty,new Address(hnum,str,null,null,pos),null));

        }
        System.out.println(toRet.size());
        return toRet;
    }

    //TODO:show results of cascade
    public static void deleteRestaurant (int restID) throws SQLException {
        String sqlString;
        con.setAutoCommit(false);
        PreparedStatement pstmt;
        sqlString = "DELETE FROM restaurant ";
        sqlString += "WHERE resID = ?";
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
        con.setAutoCommit(false);
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

    public static List<Order> getOrders(Restaurant restaurant, Date startDate, Date endDate) throws SQLException {
        List<Order> orders = new ArrayList<>();
        List<Order> pickups = getPickups(restaurant.getId(), startDate, endDate);
        List<Order> deliveries = getDeliveries(restaurant.getId(), startDate, endDate);
        con.setAutoCommit(false);
        for (Order order : pickups) {
            orders.add(order);
        }
        for (Order order : deliveries) {
            orders.add(order);
        }
        return orders;
    }

    public static List<Order> getPickups(int resID, Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        Pickup pickup;
        ResourceManager rm = ResourceManager.getInstance();
        List<Order> pickups = new ArrayList<>();
        if (MainUI.currentUser == null) return null;
        sqlString = "SELECT o.*, estimated_READY_time ";
        sqlString += "FROM orders o, pick_up p ";
        sqlString += "WHERE p.orderID = o.orderID AND order_date >= ? ";
        sqlString += "AND order_date <= ? AND order_restaurantID = ?";
        System.out.println(sqlString);
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setInt(3, resID);
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()) {
            Long orderID = rs.getLong(1);
            Date date = rs.getDate(2);
            Time time = Time.valueOf(rs.getString(3) + ":00");
            Double amount = rs.getDouble(4);
            OrderStatus orderStatus = OrderStatus.valueOf(rs.getString(5).toUpperCase());
            String custID = rs.getString(6);
            Restaurant restaurant = rm.getRestaurant(rs.getInt(7));
            Time READYTime = Time.valueOf(rs.getString(8) + ":00");
            pickup = new Pickup(null, orderID, date, time, amount, orderStatus, restaurant,
                    getFoods(orderID), READYTime);
            pickups.add(pickup);
        }
        pstmt.close();
        return pickups;
    }

    public static List<Order> getDeliveries(int resID, Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        Delivery delivery;
        ResourceManager rm = ResourceManager.getInstance();
        List<Order> deliveries = new ArrayList<>();
        if (MainUI.currentUser == null) return null;
        sqlString = "SELECT o.*, delivery_fee, courierID, postal_code, street, house# ";
        sqlString += "FROM orders o, delivery_delivers d ";
        sqlString += "WHERE o.orderID = d.orderID AND order_date >= ? ";
        sqlString += "AND order_date <= ? AND order_restaurantID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setInt(3, resID);
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()) {
            Long orderID = rs.getLong(1);
            Date date = rs.getDate(2);
            Time time = Time.valueOf(rs.getString(3) + ":00");
            Double amount = rs.getDouble(4);
            OrderStatus orderStatus = OrderStatus.valueOf(rs.getString(5).toUpperCase());
            String custID = rs.getString(6);
            Restaurant restaurant = rm.getRestaurant(rs.getInt(7));
            double deliverFee = rs.getDouble(8);
            Courier courier = rm.getCourier(rs.getString(9));
            String postal = rs.getString(10);
            String street = rs.getString(11);
            int houseNum = rs.getInt(12);
            delivery = new Delivery((Customer) MainUI.currentUser, orderID, date, time, amount, orderStatus, restaurant,
                    getFoods(orderID), deliverFee, getArrivalTime(orderID), courier, getAddress(postal, street, houseNum));
            deliveries.add(delivery);
        }
        pstmt.close();
        return deliveries;
    }

    private static Time getArrivalTime(Long orderID) throws SQLException {
        Time arivalTime;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT estimated_arrival_time ";
        sqlString += "FROM delivery_delivers ";
        sqlString += "WHERE orderID= ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setLong(1, orderID);
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next())
            arivalTime = Time.valueOf(rs.getString(1) + ":00");
        else
            return null;
        return arivalTime;
    }

    private static Map<Food, Integer> getFoods(Long orderID) throws SQLException {
        Map<Food, Integer> foods = new HashMap<>();
        String sqlString;
        PreparedStatement pstmt;
        ResourceManager rm = ResourceManager.getInstance();
        ResultSet rs;
        sqlString = "SELECT a.food_name, r.resID, o.price, a.quantity ";
        sqlString += "FROM orders, added_in a, restaurant r, offers o ";
        sqlString += "WHERE a.orderID = ? AND orders.orderID = a.orderID AND ";
        sqlString += "order_restaurantID = r.resID AND o.restaurantID = order_restaurantID ";
        sqlString += "AND a.food_name = o.food_name";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setLong(1, orderID);
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()) {
            Food food = new Food(rs.getString(1), rm.getRestaurant(rs.getInt(2)), rs.getDouble(3));
            foods.put(food, rs.getInt(4));
        }
        return foods;
    }

    private static Address getAddress(String postal, String street, int houseNum) throws SQLException {
        Address address;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT province, city ";
        sqlString += "FROM address_detail ";
        sqlString += "WHERE postal_code = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, postal);
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next()) {
            address = new Address(houseNum, street, rs.getString(1), rs.getString(2), postal);
        }
        else
            return null;
        return address;
    }

    public static double getRevenue(Restaurant restaurant, Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
        sqlString = "SELECT Sum(order_amount) ";
        sqlString += "FROM orders ";
        sqlString += "WHERE order_status = 'COMPLETE' AND (order_date BETWEEN ? AND ?) ";
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
