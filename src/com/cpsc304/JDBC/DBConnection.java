package com.cpsc304.JDBC;

public class DBConnection {
    private static DBConnection ourInstance = new DBConnection();

    public static DBConnection getInstance() {
        return ourInstance;
    }

    private DBConnection() {
    }
}
