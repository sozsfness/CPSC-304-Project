package com.cpsc304.JDBC;

import com.cpsc304.model.Food;
import com.cpsc304.model.ResourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDBC {

    private static Connection con = DBConnection.getCon();

    public static List<Food> getMenu(int restID) throws SQLException {
        List<Food> foods = new ArrayList<>();
        String sqlString;
        PreparedStatement pstmt;
        ResultSet rs;
        ResourceManager rm = ResourceManager.getInstance();
        sqlString = "SELECT food_name, price ";
        sqlString += "FROM offers ";
        sqlString += "WHERE restaurantID = ?";
        pstmt = con.prepareStatement(sqlString);
        pstmt.setInt(1, restID);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Food food = new Food(rs.getString(1), rm.getRestaurant(restID), rs.getDouble(2));
            foods.add(food);
        }
        return foods;
    }
}
