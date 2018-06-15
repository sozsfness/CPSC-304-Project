package com.cpsc304.JDBC;

import com.cpsc304.UI.MainUI;
import com.cpsc304.model.*;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CourierDBC extends UserDBC {

    private static Connection con = DBConnection.getCon();

    public static Courier getCurier(String courierID) {
        return null;
    }

    public static Time getScheduledTime(int orderID) throws SQLException {
        Time arivalTime;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
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

    public static Address getLocation(int orderID) throws SQLException {
        Address address;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
        if (MainUI.currentUser == null) return null;
        sqlString = "SELECT house#, street, province, city, postal_code ";
        sqlString += "FROM delivery_delivers d, addresses a, address_detail ad ";
        sqlString += "WHERE d.orderID = ? AND d.courierID = ? ";
        sqlString += "AND d.postal_code = a.postal_code AND a.postal_code = ad.postal_code ";
        sqlString += "AND d.street = a.street AND d.house# = a.house#";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, orderID);
        pstmt.setString(2, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        con.commit();
        if (rs.next()) {
            int houseNum = rs.getInt(1);
            String street = rs.getString(2);
            String province = rs.getString(3);
            String city = rs.getString(4);
            String postal = rs.getString(5);
            address = new Address(houseNum, street, province, city, postal);
        }
        else
            return null;
        return address;
    }

    //TODO:compare with docs sql code
    //if the status of the order is not READY or DELIVERING currently, return false
    public static boolean updateOrder(Order order) throws SQLException {
        if (order.getStatus() != OrderStatus.READY && order.getStatus() != OrderStatus.DELIVERING)
            return false;
        String sqlString;
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        if (order.getStatus() == OrderStatus.READY)
            sqlString = "UPDATE orders SET order_status = DELIVERING";
        else
            sqlString = "UPDATE orders SET order_status = DELIVERED";
        stmt.executeUpdate(sqlString);
        con.commit();
        return true;
    }

    public static List<Order> getOrders(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        Delivery delivery;
        Address address;
        ResourceManager rm = ResourceManager.getInstance();
        List<Order> deliveries = new ArrayList<>();
        con.setAutoCommit(false);
        if (MainUI.currentUser == null) return null;
        sqlString = "SELECT o.*, resID, delivery_fee, d.house#, d.street, province, city, d.postal_code ";
        sqlString += "FROM orders o, restaurant r, courier c, delivery_delivers d ";
        sqlString += "WHERE d.orderID = o.orderID AND o.order_date >= ? ";
        sqlString += "AND order_date <= ? AND d.courierID =c.cor_userID ";
        sqlString += " AND d.courierID = ? AND o.order_restaurantID = r.resID";
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
            int houseNum = rs.getInt(9);
            String street = rs.getString(10);
            String province = rs.getString(11);
            String city = rs.getString(12);
            String postal = rs.getString(13);
            address = new Address(houseNum, street, province, city, postal);
            delivery = new Delivery((Customer) MainUI.currentUser, orderID, date, time, amount, orderStatus, restaurant,
                    null, deliverFee, null, (Courier) MainUI.currentUser, address);
            deliveries.add(delivery);
        }
        return deliveries;
    }

    public static Map<Integer, Double> getMonthlyIncomes(Date startDate, Date endDate) {
        return null;
    }

    public static int getMaxMonth(Date startDate, Date endDate) {
        return 0;
    }

    public static int getMinMonth(Date startDate, Date endDate) {
        return 0;
    }

    public static double getIncome(Date startDate, Date endDate){
        return 0;
    }
}
