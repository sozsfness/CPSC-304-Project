package com.cpsc304.JDBC;

import java.sql.*;


public class DBConnection {
    private static Connection con;
    private static String username = "ora_h6f1b";
    private static String password = "a41428160";

    public static Connection getCon() {
        return con;
    }

    public static boolean connect() {
        String connectURL = "jdbc:oracle:thin:@localhost:1522:ug";

        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(connectURL,username,password);
            con.commit();
            System.out.println("\nConnected to Oracle!");
            return true;
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            return false;
        }
    }

    public static boolean close() {
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Close Error");
            return false;
        }
    }
}
