package com.cpsc304.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Customer extends User{

    private double spending;
    private int vipLevel;
    private int points;
    private Set<Address> addresses;
    private List<Order> orders;

    public Customer(String userID, String name, String password, String phoneNum, double spending, int vipLevel, int points) {
        super(userID, name, password, phoneNum);
        this.spending = spending;
        this.vipLevel = vipLevel;
        this.points = points;
        this.addresses = new HashSet<>();
        orders = new ArrayList<>();
    }

    public void setSpending(double spending) {
        this.spending = spending;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public double getSpending() {
        return spending;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public int getPoints() {
        return points;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
