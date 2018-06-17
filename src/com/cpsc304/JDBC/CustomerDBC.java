package com.cpsc304.JDBC;

import com.cpsc304.UI.MainUI;
import com.cpsc304.model.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CustomerDBC extends UserDBC {

    private static Connection con = DBConnection.getCon();

    public static Customer getCustomer(String custID) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        User user = getUser(custID);
        System.out.println("user constructed");
        Customer customer = null;
        con.setAutoCommit(false);
        //Statement stmt = con.createStatement();
        ResultSet rs;
        sqlString = "SELECT cus_spending, points, vip_level ";
        sqlString += "FROM customer, points, vip_level ";
        sqlString += "WHERE cus_userID = '"+custID+"' AND cus_spending = spending";
        pstmt = con.prepareStatement(sqlString);

        rs = pstmt.executeQuery();
        con.commit();
        //rs = stmt.executeQuery(sqlString);
        System.out.println(rs.getRow());
        rs.next();
            double spending = rs.getDouble(1);
            int points = rs.getInt(2);
            int vipLevel = rs.getInt(3);
            customer = new Customer(custID, user.getName(), user.getPassword(), user.getPhoneNum(), spending, vipLevel, points);
            System.out.println(customer.getUserID());

        return customer;
    }

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

    public static List<Restaurant> getBestRestaurants(String type,boolean brating, boolean bhours, boolean bdeliveryOption, boolean btype, boolean baddress) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        Restaurant rest;
        List<Restaurant> restaurants = new ArrayList<>();
        ResourceManager rm = ResourceManager.getInstance();
        con.setAutoCommit(false);
        sqlString = "SELECT * ";
        sqlString += "FROM restaurant ";
        sqlString += "WHERE LOWER(res_type) = LOWER(?) ";
        sqlString += "ORDER BY res_rating DESC";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, type);
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()){
            int resID = rs.getInt(1);
            String resName = rs.getString(2);
            Time openTime = Time.valueOf(rs.getString(3) + ":00");
            Time closeTime = Time.valueOf(rs.getString(4) +":00");
            Double rating = rs.getDouble(5);
            String t = rs.getString(6);
            boolean deliveryOption;
            if (rs.getInt(7) != 0)
                deliveryOption = true;
            else
                deliveryOption = false;
            String managerID = rs.getString(8);
            String postal = rs.getString(9);
            String street = rs.getString(10);
            int houseNum = rs.getInt(11);
            Address address = new Address(houseNum, street, null, null, postal);
            rest = new Restaurant(rm.getManager(managerID), rating, openTime, closeTime, resName, resID, deliveryOption,
                    type, address, null);
            restaurants.add(rest);
        }
        pstmt.close();
        return restaurants;
    }

    public static List<Restaurant> getRecommendedRestaurants() {
        return null;
    }

    public static List<Restaurant> getRestaurantsOfRating(Integer rating,boolean brating, boolean bhours, boolean bdeliveryOption, boolean btype, boolean baddress) throws SQLException {
        return getRankedRestaurants(null, rating,brating, bhours, bdeliveryOption, btype, baddress);
    }

    public static List<Restaurant> getRankedRestaurants(List<String> foods, boolean brating, boolean bhours, boolean bdeliveryOption, boolean btype, boolean baddress) throws SQLException {
        //note food may be a string containing multiple food names,separated with commas
        return getRankedRestaurants(foods,0, brating, bhours, bdeliveryOption, btype, baddress);
    }
    public static List<Restaurant> getRankedRestaurants(List<String> foods,Integer minRating, boolean brating, boolean bhours, boolean bdeliveryOption, boolean btype, boolean baddress) throws SQLException {
        //note food may be a string containing multiple food names,separated with commas
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        Restaurant rest;
        List<Restaurant> restaurants = new ArrayList<>();
        con.setAutoCommit(false);
        sqlString = "SELECT resID, res_name";
        if (brating)
            sqlString += ", res_rating";
        if (bhours)
            sqlString += ", res_open_time, res_close_time";
        if (btype)
            sqlString += ", res_type";
        if (bdeliveryOption)
            sqlString += ", res_delivery_option";
        if (baddress)
            sqlString += ", res_postal_code, res_street, res_house#";
        sqlString += " FROM restaurant r, offers o";
        sqlString += "WHERE o.restaurantID = r.resI";
        sqlString += " AND res_rating >= ?";
        for (String food: foods) {
            sqlString += " AND LOWER(o.food_name) = LOWER(" + food + ")";
        }
        sqlString += "ORDER BY res_rating DESC";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDouble(1, minRating);
        rs = pstmt.executeQuery();
        con.commit();
        while (rs.next()) {
            int resID = rs.getInt(1);
            String resName = rs.getString(2);
            Double rating = null;
            Time openTime = null;
            Time closeTime = null;
            String type =null;
            boolean deliveryOption = false;
            if (brating)
                rating= rs.getDouble("res_rating");
            if (bhours) {
                openTime = Time.valueOf(rs.getString("res_open_time") + ":00");
                closeTime = Time.valueOf(rs.getString("res_close_time") +":00");
            }
            if (btype)
                type = rs.getString(6);
            if (bdeliveryOption)
                if (rs.getInt(7) != 0)
                    deliveryOption = true;
                else
                    deliveryOption = false;
            String postal = null;
            String street = null;
            int houseNum = -1;
            Address address = null;
            if (baddress) {
                postal = rs.getString("res_postal_code");
                street = rs.getString("res_street");
                houseNum = rs.getInt("res_house#");
                address = getAddress(postal, street, houseNum);
            }
            rest = new Restaurant(null, rating, openTime, closeTime, resName, resID, deliveryOption,
                    type, address, null);
            restaurants.add(rest);
        }
        return null;
    }

    public static List<Order> getOrders(Date startDate, Date endDate) throws SQLException {
        List<Order> orders = new ArrayList<>();
        List<Order> pickups = getPickups(0, startDate, endDate);
        List<Order> deliveries = getDeliveries(0, startDate, endDate);
        con.setAutoCommit(false);
        for (Order order : pickups) {
            orders.add(order);
        }
        for (Order order : deliveries) {
            orders.add(order);
        }
        return orders;
    }

    public static List<Order> getOrders(int resID, Date startDate, Date endDate) throws SQLException {
            List<Order> orders = new ArrayList<>();
            List<Order> pickups = getPickups(resID, startDate, endDate);
            List<Order> deliveries = getDeliveries(resID, startDate, endDate);
            con.setAutoCommit(false);
            for (Order order : pickups) {
                orders.add(order);
            }
            for (Order order : deliveries) {
                orders.add(order);
            }
            return orders;
        }

        //resID == 0 means no rest restrictions
        public static List<Order> getPickups(int resID, Date startDate, Date endDate) throws SQLException {
            String sqlString;
            PreparedStatement pstmt;
            ResultSet rs;
            Pickup pickup;
            ResourceManager rm = ResourceManager.getInstance();
            List<Order> pickups = new ArrayList<>();
            if (MainUI.currentUser == null) return null;
            sqlString = "SELECT o.*, estimated_ready_time";
            sqlString += "FROM order o, pick_up p";
            sqlString += "WHERE p.orderID = o.orderID AND order_date >= ? ";
            sqlString += "AND order_date <= ? AND order_customerID = ?";
            if (resID != 0)
                sqlString += " AND order_restaurantID = " + resID;
            pstmt = con.prepareStatement(sqlString);
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            pstmt.setString(3, MainUI.currentUser.getUserID());
            rs = pstmt.executeQuery();
            con.commit();
            while (rs.next()) {
                int orderID = rs.getInt(1);
                Date date = rs.getDate(2);
                Time time = Time.valueOf(rs.getString(3) + ":00");
                Double amount = rs.getDouble(4);
                OrderStatus orderStatus = OrderStatus.valueOf(rs.getString(5));
                String custID = rs.getString(6);
                Restaurant restaurant = rm.getRestaurant(rs.getInt(7));
                Time readyTime = Time.valueOf(rs.getString(8) + ":00");
                pickup = new Pickup((Customer) MainUI.currentUser, orderID, date, time, amount, orderStatus, restaurant,
                        getFoods(orderID), readyTime);
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
            sqlString = "SELECT o.*, delivery_fee, courierID, postal_code, street, house#";
            sqlString += "FROM order o, customer, delivery_delivers d, courier, user";
            sqlString += "WHERE o.orderID = d.oderID AND order_date >= ? ";
            sqlString += "AND order_date <= ? AND order_customerID = ? ";
            sqlString += "AND cor_userID = courierID AND cor_userID = userID";
            if (resID != 0)
                sqlString += " AND order_restaurantID = " + resID;
            pstmt = con.prepareStatement(sqlString);
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            pstmt.setString(3, MainUI.currentUser.getUserID());
            rs = pstmt.executeQuery();
            con.commit();
            while (rs.next()) {
                int orderID = rs.getInt(1);
                Date date = rs.getDate(2);
                Time time = Time.valueOf(rs.getString(3) + ":00");
                Double amount = rs.getDouble(4);
                OrderStatus orderStatus = OrderStatus.valueOf(rs.getString(5));
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

    private static Time getArrivalTime(int orderID) throws SQLException {
        Time arivalTime;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT estimated_arrival_time";
        sqlString += "FROM delivery_delivers ";
        sqlString += "WHERE orderID= ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, orderID);
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next())
            arivalTime = Time.valueOf(rs.getString(1) + ":00");
        else
            return null;
        return arivalTime;
    }

    private static Map<Food, Integer> getFoods(int orderID) throws SQLException {
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
        pstmt.setInt(1, orderID);
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
        sqlString = "SELECT province, city";
        sqlString += "FROM addresses, address_detail ";
        sqlString += "WHERE house# = ? AND street = ? AND postal_code = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, houseNum);
        pstmt.setString(2, street);
        pstmt.setString(3, postal);
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next()) {
            address = new Address(houseNum, street, rs.getString(1), rs.getString(2), postal);
        }
        else
            return null;
        return address;
    }

    public static double getSpending(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT Sum(order_amount) ";
        sqlString += "FROM orders o";
        sqlString += "WHERE order_status = 'Complete' ";
        sqlString += "AND (order_date BETWEEN ? AND ?) AND order_customerID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else
            return 0;
    }
}
