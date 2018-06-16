package com.cpsc304.JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                sqlString += "courier WHERE ";
                break;
            default:
                System.out.println("type error");
                return false;
        }
        sqlString += "userID = '" + userID + "'";
        System.out.println(sqlString);
        rs = stmt.executeQuery(sqlString);
        rs.next();
        System.out.println(rs.getRow());
        if (rs.getRow() != 1) {
            System.out.println("User in current type doesn't exist.");
            return false;
        }
        sqlString = "SELECT COUNT(*) FROM users WHERE userID = '" + userID + "' ";
        sqlString += "AND userPass = '" + password + "'";
        stmt.close();
        System.out.println(sqlString);
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

    public static void getUserInfo(String userID, String userType) {

    }

}
