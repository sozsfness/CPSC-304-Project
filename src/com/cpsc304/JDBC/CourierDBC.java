package com.cpsc304.JDBC;

import com.cpsc304.UI.MainUI;
import com.cpsc304.model.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.util.Pair;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CourierDBC extends UserDBC {

    private static Connection con = DBConnection.getCon();

    public static Courier getCurier(String courierID) throws SQLException {
        User user = getUser(courierID);
        Courier courier = new Courier(user.getUserID(), user.getName(), user.getPassword(), user.getPhoneNum(), null);
        return courier;
    }

    public static Time getScheduledTime(int orderID) throws SQLException {
        Time arivalTime;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
        sqlString = "SELECT estimated_arrival_time";
        sqlString += " FROM delivery_delivers ";
        sqlString += " WHERE orderID= ?";
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
        sqlString +=  " WHERE orderID = " + order.getOrderID();
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
        sqlString = "SELECT o.*, order_restaurantID, delivery_fee, d.house#, d.street, province, city, d.postal_code ";
        sqlString += "FROM orders o, delivery_delivers d , address_detail a ";
        sqlString += "WHERE d.orderID = o.orderID AND o.order_date >= ? ";
        sqlString += "AND order_date <= ? AND d.courierID = ? ";
        sqlString += "AND d.postal_code = a.postal_code";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
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
            System.out.println(deliverFee);
            int houseNum = rs.getInt(9);
            String street = rs.getString(10);
            String province = rs.getString(11);
            String city = rs.getString(12);
            String postal = rs.getString(13);
            address = new Address(houseNum, street, province, city, postal);
            delivery = new Delivery(null, orderID, date, time, amount, orderStatus, restaurant,
                    null, deliverFee, null, (Courier) MainUI.currentUser, address);
            deliveries.add(delivery);
        }
        return deliveries;
    }

    public static Map<Integer, Double> getMonthlyIncomes(Date startDate, Date endDate) {
        return null;
    }

    public static List<Pair<Integer, Double>> getSums(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        List<Pair<Integer, Double>> sums = new ArrayList<>();
        Pair<Integer, Double> pair;
        sqlString = "SELECT to_char(order_date, 'Month') AS \"Month\", Sum(delivery_fee) AS \"Sum Earning\" ";
        sqlString += "FROM orders NATURAL INNER JOIN delivery_delivers ";
        sqlString += "WHERE order_status = 'COMPLETE' AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND courierID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getInt(1), rs.getDouble(2));
            sums.add(pair);
        }
        return sums;
    }

    public static List<Pair<Integer, Double>> getAvgs(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        List<Pair<Integer, Double>> avgs = new ArrayList<>();
        Pair<Integer, Double> pair;
        sqlString = "SELECT to_char(order_date, 'Month') AS \"Month\", Avg(delivery_fee) AS \"Avg Earning\" ";
        sqlString += "FROM orders NATURAL INNER JOIN delivery_delivers ";
        sqlString += "WHERE order_status = 'COMPLETE' AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND courierID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getInt(1), rs.getDouble(2));
            avgs.add(pair);
        }
        return avgs;
    }

    public static List<Pair<Integer, Double>> getMins(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        List<Pair<Integer, Double>> mins = new ArrayList<>();
        Pair<Integer, Double> pair;
        sqlString = "SELECT to_char(order_date, 'Month') AS \"Month\", Sum(delivery_fee) AS \"Sum Earning\" ";
        sqlString += "FROM orders NATURAL INNER JOIN delivery_delivers ";
        sqlString += "WHERE order_status = 'COMPLETE' AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND courierID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getInt(1), rs.getDouble(2));
            mins.add(pair);
        }
        return mins;
    }

    public static  List<Pair<Integer, Double>> getMaxs(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        List<Pair<Integer, Double>> maxs = new ArrayList<>();
        Pair<Integer, Double> pair;
        sqlString = "SELECT to_char(order_date, 'Month') AS \"Month\", Max(delivery_fee) AS \"Max Earning\" ";
        sqlString += "FROM orders NATURAL INNER JOIN delivery_delivers ";
        sqlString += "WHERE order_status = 'COMPLETE' AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND courierID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getInt(1), rs.getDouble(2));
            maxs.add(pair);
        }
        return maxs;
    }

    public static List<Pair<Integer, Double>> getCounts(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        List<Pair<Integer, Double>> counts = new ArrayList<>();
        Pair<Integer, Double> pair;
        sqlString = "SELECT to_char(order_date, 'Month') AS \"Month\", Sum(delivery_fee) AS \"Sum Earning\" ";
        sqlString += "FROM orders NATURAL INNER JOIN delivery_delivers ";
        sqlString += "WHERE order_status = 'COMPLETE' AND (order_date BETWEEN ? AND ?) ";
        sqlString += "AND courierID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getInt(1), rs.getDouble(2));
            counts.add(pair);
        }
        return counts;
    }

    // view for check nested aggregation
    // type should be "Min""Max""Sum""Avg"
    // return list of resName and earning
    public static List<Pair<String, Double>> getEarning(String type, String courerID) throws SQLException {
        List<Pair<String, Double>> earnings = new ArrayList<>();
        Pair<String, Double> pair;
        creatView(type, courerID);
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT res_name, Earning FROM " + type + "Earning";
        pstmt = con.prepareStatement(sqlString);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getString(1), rs.getDouble(2));
            earnings.add(pair);
        }
        return earnings;
    }

    // when type is "Count"
    public static List<Pair<String, Integer>> getCount(String courerID)  throws SQLException{
        List<Pair<String, Integer>> counts = new ArrayList<>();
        Pair<String, Integer> pair;
        creatView("Count", courerID);
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT res_name, Earning FROM CountEarning";
        pstmt = con.prepareStatement(sqlString);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            pair = new Pair<>(rs.getString(1), rs.getInt(2));
            counts.add(pair);
        }
        return counts;
    }

    private static void creatView(String type, String courerID) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        sqlString = "CREATE VIEW " + type + "Earning(resID, res_name, Earning) AS ";
        sqlString += "SELECT resID, res_name, " + type +"(delivery_fee) ";
        sqlString += "FROM orders NATURAL INNER JOIN delivery_delivers, restaurant ";
        sqlString += "WHERE order_restaurantID = resID AND order_status = 'COMPLETE' ";
        sqlString += "AND courierID = ?" ;
        sqlString += " GROUP BY resID, res_name";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, courerID);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // For nested aggregation
    // type should be "Min""Max""Sum""Avg" rather than "Count"
    public static double getEarning(String firstType, String secondType, String courerID) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        creatView(firstType, courerID);
        sqlString = "SELECT " + secondType +"(earning) from " + firstType + "Earning";
        pstmt = con.prepareStatement(sqlString);
        rs = pstmt.executeQuery();
        if (rs.next())
            return rs.getDouble(1);
        return 0;
    }

    // when the first type is any type rather than count, but the second type is "Count"
    public static int getSecondCount(String firstType, String courerID) throws SQLException{
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        creatView(firstType, courerID);
        sqlString = "SELECT Count(earning) from " + firstType + "Earning";
        pstmt = con.prepareStatement(sqlString);
        rs = pstmt.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }

    // when the first type is "Count", but the second type is any type
    public static int getFirstCount(String secondType, String courerID) throws SQLException{
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        creatView("Count", courerID);
        sqlString = "SELECT " + secondType + "(earning) from CountEarning";
        pstmt = con.prepareStatement(sqlString);
        rs = pstmt.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }

    public static double getIncome(Date startDate, Date endDate) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT SUM(delivery_fee) ";
        sqlString += "FROM orders o, delivery_delivers d ";
        sqlString += "WHERE o.orderID = d.orderID AND order_status = 'COMPLETE' ";
        sqlString += "AND (order_date BETWEEN ? AND ?) AND d.courierID = ?";

        pstmt = con.prepareStatement(sqlString);
        pstmt.setDate(1, startDate);
        pstmt.setDate(2, endDate);
        pstmt.setString(3, MainUI.currentUser.getUserID());
        rs = pstmt.executeQuery();
        if (rs.next())
            return rs.getDouble(1);
        else
            return 0;
    }
}
