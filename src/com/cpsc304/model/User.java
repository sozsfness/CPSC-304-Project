package com.cpsc304.model;

public abstract class User {

    protected int userID;
    protected String name;
    protected String password;
    protected String phoneNum;

    public User(int userID, String name, String password, String phoneNum) {
        this.userID = userID;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
