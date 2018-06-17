package com.cpsc304.JDBC;

import com.cpsc304.UI.MainUI;
import com.cpsc304.model.Order;
import com.cpsc304.model.Restaurant;
import com.cpsc304.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Set;

public abstract class UserDBC {

    private static Connection con = DBConnection.getCon();

    public static void updateUserInfo(User user) throws SQLException {
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        con.setAutoCommit(false);
        sqlString = "UPDATE users SET userName = ? ";
        sqlString += ", userPass = ? , phone# = ? WHERE userID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getPassword());
        pstmt.setLong(3, Long.parseLong(user.getPhoneNum()));
        pstmt.setString(4,user.getUserID());
        rs = pstmt.executeQuery();
        con.commit();
    }

    public static User getUser(String userID) throws SQLException {
        User user = null;
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        sqlString = "SELECT userPass, phone# AS BIGINT, userName ";
        sqlString += "FROM users WHERE userID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setString(1, userID);
        rs = pstmt.executeQuery();

//        Statement stmt = con.createStatement();
//        sqlString = "SELECT userPass, phone# AS BIGINT, userName FROM users where userID = '"+userID+"'";
//        rs = stmt.executeQuery(sqlString);
        con.commit();
        System.out.println(sqlString);
        if (rs.next()) {

            String password = rs.getString(1);
            String phoneNum = ((BigDecimal) (rs.getBigDecimal(2))).toString();
            //System.out.println(phoneNum);//successful
            String userName = rs.getString(3);
            user = new User(userID, userName, password, phoneNum) {
            };
        }
        return user;
    }
}
