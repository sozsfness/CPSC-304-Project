package com.cpsc304.model;

import com.cpsc304.JDBC.CourierDBC;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Map;

public class Order {

    protected Customer customer;
    protected Long orderID;
    protected Date date;
    protected Time time;
    protected double amount;
    protected OrderStatus status;
    protected Restaurant restOrderedAt;
    protected Map<Food, Integer> quantity;

    protected Order(Customer customer, Long orderID, Date date, Time time, double amount, OrderStatus status, Restaurant restOrderedAt, Map<Food, Integer> quantity) {
        this.customer = customer;
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.status = status;
        this.restOrderedAt = restOrderedAt;
        this.quantity = quantity;
    }
    public Order(Customer customer,double amount, Restaurant restOrderedAt, Map<Food, Integer> quantity) {
        //generate new order
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

     public void setStatus(OrderStatus status) {
        this.status = status;
         try {
             CourierDBC.updateOrder(this);
         } catch (SQLException e) {
             System.out.println("Status setting fails");
             e.printStackTrace();
         }
     }

    public Restaurant getRestOrderedAt() {
        return restOrderedAt;
    }

    public void setRestOrderedAt(Restaurant restOrderedAt) {
        this.restOrderedAt = restOrderedAt;
    }

    public Map<Food, Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(Map<Food, Integer> quantity) {
        this.quantity = quantity;
    }

    private int generateOrderID(){
        return 0;
    }
}
