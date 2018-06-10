package com.cpsc304.model;

import java.util.Set;

public class Customer extends User{

    private double spending;
    private int vipLevel;
    private int points;
    private Set<Address> addresses;
    private Set<Order> orders;
}
