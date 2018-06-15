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
                sqlString += "customer";
                break;
            case "restaurant manager":
                sqlString += "restaurant_manager";
                break;
            case "courier":
                sqlString += "courier";
                break;
            default:
                System.out.println("type error");
                return false;
        }
        sqlString += "WHERE userID = " + userID;
        rs = stmt.executeQuery(sqlString);
        stmt.close();
        if (rs.getInt(1) == 0) {
            System.out.println("User in current type doesn't exist.");
            return false;
        }
        sqlString = "SELECT COUNT(*) FROM users WHERE userID = " + userID;
        sqlString = "AND userPass = " + password;
        stmt = con.createStatement();
        rs = stmt.executeQuery(sqlString);
        //con.commit();
        if (rs.getInt(1) == 0) {
            System.out.println("Password Error");
            return false;
        }
        return true;
    }


}
