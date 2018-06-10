package com.cpsc304.model;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

public abstract class Order {

    protected Customer customer;
    protected int orderID;
    protected Date date;
    protected Time time;
    protected double amount;
    protected String status;
    protected Restaurant restOrderedAt;
    protected Map<Food, Integer> quantity;
}
