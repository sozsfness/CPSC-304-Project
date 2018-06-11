package com.cpsc304.model;

public class Address {

    private int houseNum;
    private String street;
    private String province;
    private String city;
    private String postalCode;

    public Address(int houseNum, String street, String province, String city, String postalCode) {
        this.houseNum = houseNum;
        this.street = street;
        this.province = province;
        this.city = city;
        this.postalCode = postalCode;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
