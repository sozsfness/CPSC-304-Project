package com.cpsc304.model;

import java.util.Set;

public class Food {

    private String name;
    private Set<Food> offeringRes;

    public Food(String name, Set<Food> offeringRes) {
        this.name = name;
        this.offeringRes = offeringRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Food> getOfferingRes() {
        return offeringRes;
    }

    public void setOfferingRes(Set<Food> offeringRes) {
        this.offeringRes = offeringRes;
    }
}
