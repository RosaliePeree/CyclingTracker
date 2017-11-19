package com.example.rosalie.cyclingtracker;

import java.util.List;

/**
 * Created by infer on 16/11/2017.
 */

public class User {
    private String mail;
    private String name;
    private List<String> ride;

    public User() {
    }

    public User(String mail, String name, List<String> ride) {
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

    public List<String> getRide() {
        return ride;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRide(List<String> ride) {
        this.ride = ride;
    }
}
