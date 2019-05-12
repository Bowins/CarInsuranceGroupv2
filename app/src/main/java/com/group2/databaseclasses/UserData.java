package com.group2.databaseclasses;


import java.util.Date;

public class UserData {

    String email;
    String firstName;
    String lastName;
    Date dob;
    String gender;

    public UserData() {

    }

    public UserData(String email, String firstName, String lastName, Date dob, String gender) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
