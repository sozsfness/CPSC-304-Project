package com.cpsc304.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Courier extends User {
    private Set<Restaurant> workRes;
    private List<Delivery> deliveries;

    public Courier(String userID, String name, String password, String phoneNum, Set<Restaurant> workRes) {
        super(userID, name, password, phoneNum);
        this.workRes = workRes;
        this.deliveries = new ArrayList<>();
    }

    public Set<Restaurant> getWorkRes() {
        return workRes;
    }

    public void setWorkRes(Set<Restaurant> workRes) {
        this.workRes = workRes;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
