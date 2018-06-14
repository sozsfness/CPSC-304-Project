package com.cpsc304.model;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

public class Pickup extends Order {

    private Time readyTime;

    public Pickup(Customer customer, int orderID, Date date, Time time, double amount, OrderStatus status, Restaurant restOrderedAt, Map<Food, Integer> quantity, Time readyTime) {
        super(customer, orderID, date, time, amount, status, restOrderedAt, quantity);
        this.readyTime = readyTime;
    }
    public String getReadyTime(){
        return readyTime;
    }

    public Time getReadyTime() {
        return readyTime;
    }
}
