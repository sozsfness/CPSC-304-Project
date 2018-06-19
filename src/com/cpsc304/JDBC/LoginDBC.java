package com.cpsc304.JDBC;

import com.cpsc304.model.Address;
import com.cpsc304.model.Courier;
import com.cpsc304.model.Restaurant;
import com.cpsc304.model.RestaurantManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LoginDBC {

    private static Connection con = DBConnection.getCon();

    //verify user's password
    public static boolean verify(String type, String userID, String password) throws SQLException {
        String sqlString;
        Statement stmt = con.createStatement();
        ResultSet rs;
        con.setAutoCommit(true);
        sqlString = "SELECT COUNT(*) FROM ";
        switch (type) {
            case "customer":
                sqlString += "customer WHERE cus_";
                break;
            case "restaurant manager":
                sqlString += "restaurant_managers WHERE res_";
                break;
            case "courier":
                sqlString += "courier WHERE cor_";
                break;
            default:
                System.out.println("type error");
                return false;
        }
        sqlString += "userID = '" + userID + "'";
        rs = stmt.executeQuery(sqlString);

        rs.next();
        //System.out.println(rs.getRow());
        if (rs.getInt(1) != 1) {
            System.out.println("User in current type doesn't exist.");
            return false;
        }
        sqlString = "SELECT * FROM users WHERE userID = '" + userID + "' ";
        sqlString += "AND userPass = '" + password + "'";
        stmt.close();

        stmt = con.createStatement();
        rs = stmt.executeQuery(sqlString);
        //con.commit();
        rs.next();
        if (rs.getRow() == 0) {
            System.out.println("Password Error");
            return false;
        }

        return true;
    }

    public static Map<String, Courier> getCouriers() throws SQLException {
        Map<String, Courier> courierMap = new HashMap<>();
        String sqlString;
        Statement stmt = con.createStatement();
        ResultSet rs;
        Courier courier;
        sqlString = "SELECT users.* FROM users, courier ";
        sqlString += "WHERE users.userID = courier.cor_userID";
        rs = stmt.executeQuery(sqlString);
        con.commit();
        while (rs.next()) {
            String userID = rs.getString(1);
            String password = rs.getString(2);
            String phoneNum = ((BigDecimal)(rs.getBigDecimal(3))).toString();
            //System.out.println(phoneNum);//successful
            String userName = rs.getString(4);
            courier = new Courier(userID, userName, password, phoneNum, null);
            courierMap.put(userID, courier);
        }
        return courierMap;
    }

    public static Map<Integer, Restaurant> getRestaurants() throws SQLException {
        Map<Integer, Restaurant> restMap = new HashMap<>();
        String sqlString;
        Statement stmt = con.createStatement();
        ResultSet rs;
        Restaurant restaurant;
        sqlString = "SELECT * ";
        sqlString += "FROM restaurant ";
        rs = stmt.executeQuery(sqlString);
        con.commit();
        while (rs.next()){
            int resID = rs.getInt(1);
            String resName = rs.getString(2);
            Time openTime = Time.valueOf(rs.getString(3) + ":00");
            Time closeTime = Time.valueOf(rs.getString(4) +":00");
            Double rating = rs.getDouble(5);
            String type = rs.getString(6);
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
            restaurant = new Restaurant(null, rating, openTime, closeTime, resName, resID, deliveryOption,
                    type, address, null);
            restMap.put(resID, restaurant);
        }
        return restMap;
    }

    public static Map<String, RestaurantManager> getManagers() throws SQLException {
        Map<String, RestaurantManager> managerMap = new HashMap<>();
        String sqlString;
        Statement stmt = con.createStatement();
        ResultSet rs;
        RestaurantManager manager;
        sqlString = "SELECT u.* FROM users u, restaurant_managers r ";
        sqlString += "WHERE u.userID = r.res_userID";
        rs = stmt.executeQuery(sqlString);
        con.commit();
        while (rs.next()) {
            String userID = rs.getString(1);
            String password = rs.getString(2);
            String phoneNum = ((BigDecimal)(rs.getBigDecimal(3))).toString();
            //System.out.println(phoneNum);//successful
            String userName = rs.getString(4);
            manager = new RestaurantManager(userID, userName, password, phoneNum, null);
            managerMap.put(userID, manager);
        }
        return managerMap;
    }
//
//    public static void testCount() throws SQLException {
//        String sqlString;
//        ResultSet rs;
//        con.setAutoCommit(true);
//        con.commit();
//        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        sqlString = "SELECT * FROM users";
//        rs = stmt.executeQuery(sqlString);
//        con.commit();
////        rs.next();
////        System.out.println("Count: " + rs.getInt(1));
////        rs.last();
////        System.out.println("ROW: " + rs.getRow());
////        rs.beforeFirst();
//        int count = 0;
//        while (rs.next())
//            ++ count;
//
//    }
}
