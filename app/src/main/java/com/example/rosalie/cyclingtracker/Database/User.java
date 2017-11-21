package com.example.rosalie.cyclingtracker.Database;

import java.util.List;

/**
 * Created by infer on 16/11/2017.
 */

public class User {
    private String mail;
    private String name;
    private List<String> rides;

    public User() {
    }

    public User(String mail, String name, List<String> ride) {
        this.mail = mail;
        this.name = name;
        this.rides = ride;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public List<String> getRides() {
        return rides;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRides(List<String> rides) {
        this.rides = rides;
    }
}
