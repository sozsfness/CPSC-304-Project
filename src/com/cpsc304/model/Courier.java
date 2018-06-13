package com.cpsc304.model;

import java.util.Set;

public class Courier extends User {
    private Set<Restaurant> workRes;
    private Set<Delivery> deliveries;

    public Courier(int userID, String name, String phoneNum, Set<Restaurant> workRes, Set<Delivery> deliveries) {
        super(userID, name, phoneNum);
        this.workRes = workRes;
        this.deliveries = deliveries;
    }

    public Set<Restaurant> getWorkRes() {
        return workRes;
    }

    public void setWorkRes(Set<Restaurant> workRes) {
        this.workRes = workRes;
    }

    public Set<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
