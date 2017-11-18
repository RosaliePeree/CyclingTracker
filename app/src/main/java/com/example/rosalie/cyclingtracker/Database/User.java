package com.example.rosalie.cyclingtracker.Database;

/**
 * Created by infer on 16/11/2017.
 */

public class User {
    private String mail;
    private String name;
    private String[] ride;

    public User() {
    }

    public User(String mail, String name, String[] ride) {
        this.mail = mail;
        this.name = name;
        this.ride = ride;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String[] getRide() {
        return ride;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRide(String[] ride) {
        this.ride = ride;
    }
}
