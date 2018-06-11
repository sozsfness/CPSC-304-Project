package com.cpsc304.model;

public abstract class User {

    protected int userID;
    protected String name;
    protected String password;
    protected String phoneNum;

    public User(int userID, String name, String password, String phoneNum) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
