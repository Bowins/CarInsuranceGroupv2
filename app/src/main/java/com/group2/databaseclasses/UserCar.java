package com.group2.databaseclasses;

public class UserCar {

    String registration;
    String make;
    String model;

    public UserCar(){

    }

    public UserCar(String registration, String make, String model) {
        this.registration = registration;
        this.make = make;
        this.model = model;
    }

    public String getRegistration() {
        return registration;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
