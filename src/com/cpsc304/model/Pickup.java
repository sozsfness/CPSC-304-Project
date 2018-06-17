package com.cpsc304.model;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

public class Pickup extends Order {

    private Time READYTime;

    public Pickup(Customer customer, Long orderID, Date date, Time time, double amount, OrderStatus status, Restaurant restOrderedAt, Map<Food, Integer> quantity, Time READYTime) {
        super(customer, orderID, date, time, amount, status, restOrderedAt, quantity);
        this.READYTime = READYTime;
    }
    public Time getREADYTime(){
        return READYTime;
    }


}
