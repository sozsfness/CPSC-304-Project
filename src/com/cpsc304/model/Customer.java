package com.cpsc304.model;

import java.util.Set;

public class Customer extends User{

    private double spending;
    private int vipLevel;
    private int points;
    private Set<Address> addresses;
    private Set<Order> orders;

    public Customer(int userID, String name, String password, String phoneNum, double spending, int vipLevel, int points, Set<Address> addresses, Set<Order> orders) {
        super(userID, name, password, phoneNum);
        this.spending = spending;
        this.vipLevel = vipLevel;
        this.points = points;
        this.addresses = addresses;
        this.orders = orders;
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

    public void setOrders(Set<Order> orders) {
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

    public Set<Order> getOrders() {
        return orders;
    }

    // TBC
    public void addSpending(double money) {
        this.spending += money;
        addPoints(money / 100);
    }

    private void addPoints(double points) {
        this.points += points;
        checkVipLevel();
    }

    // TBC
    private void checkVipLevel() {

    }
}
