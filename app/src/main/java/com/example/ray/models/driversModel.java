package com.example.ray.models;

public class driversModel {

    //variable declaration
    String fullName, email, phoneNumber, userID;

    public driversModel() {
    }

    public driversModel(String fullName, String email, String phoneNumber, String userID) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
