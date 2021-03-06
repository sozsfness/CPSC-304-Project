package com.cpsc304.model;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

public class Delivery extends Order {

    private double deliveryFee;
    private Time arrivalTime;
    private Courier courier;
    private Address dest;

    public Delivery(Customer customer, Long orderID, Date date, Time time, double amount, OrderStatus status, Restaurant restOrderedAt, Map<Food, Integer> quantity, double deliveryFee, Time arrivalTime, Courier courier, Address dest) {
        super(customer, orderID, date, time, amount, status, restOrderedAt, quantity);
        this.deliveryFee = deliveryFee;
        this.arrivalTime = arrivalTime;
        this.courier = courier;
        this.dest = dest;

    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Address getDest() {
        return dest;
    }
}
