package com.example.ray.models;

public class vehiclesModel {

    //variable declaration

    String model,plate,vehicleId,vehicleImage,driver,driverID;

    //empty constructor

    public vehiclesModel(){

    }


    //constructor with variable initialization to handle the data model

    public vehiclesModel(String modelDescription, String plateDescription, String vehicleIdentification, String vehicleImag,String driverAssign, String driverIdentification) {
        this.model = modelDescription;
        this.plate = plateDescription;
        this.vehicleId = vehicleIdentification;
        this.vehicleImage = vehicleImag;
        this.driver = driverAssign;
        this.driverID= driverIdentification;
    }



    //getters and setters to handle the data


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getDriver() {
        return driver;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
