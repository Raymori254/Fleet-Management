package com.example.ray.models;

public class usersModel {

    //variable declaration

    String FullName, Email, Password, Type, UserID;

    //empty constructor

    public usersModel(){

    }

    //constructor with variables initialization

    public usersModel(String FullName,String Email, String Password,String Type, String UserID){

        this.FullName = FullName;
        this.Email = Email;
        this.Password = Password;
        this.Type = Type;
        this.UserID = UserID;

    }

    //getters and setters to handle the data

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
