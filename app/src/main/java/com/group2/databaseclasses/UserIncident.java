package com.group2.databaseclasses;

public class UserIncident {

    String date;
    String time;
    String description;
    String car;
    long latitude;
    long longitude;

    public UserIncident(){

    }

    public UserIncident(String date, String time, String description, String car, long latitude, long longitude) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.car = car;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getCar() {
        return car;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }


}
