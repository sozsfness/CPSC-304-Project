package com.cpsc304.JDBC;

import com.cpsc304.model.Order;
import com.cpsc304.model.Restaurant;
import com.cpsc304.model.User;

import java.sql.*;
import java.util.List;
import java.util.Set;

public abstract class UserDBC {

    private static Connection con = DBConnection.getCon();

    public static void updateUserInfo(User user) {

    }

    public static Set<Order> getOrders(Restaurant restaurant) {
        return null;
    }

    public static Order getOrder(String orderID){
        return null;
    }

    public static User getUser(String userID) throws SQLException {
        User user = null;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT userPass, phone#, userName";
        sqlString += "FROM users WHERE userID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, userID);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            String password = rs.getString(1);
            String phoneNum = String.valueOf(rs.getInt(2));
            String userName = rs.getString(3);
            user = new User(userID, userName, password, phoneNum) {
            };
        }
        return user;
    }
}
