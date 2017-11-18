package com.example.rosalie.cyclingtracker.Database;

/**
 * Created by infer on 16/11/2017.
 */

public class Ride {
    private double average_speed;
    private String date;
    private double distance;
    private int id;
    private double time;

    public Ride() {
    }

    public Ride(double average_speed, String date, double distance, int id, double time) {

        this.average_speed = average_speed;
        this.date = date;
        this.distance = distance;
        this.id = id;
        this.time = time;
    }

    public double getAverage_speed() {
        return average_speed;
    }

    public String getDate() {
        return date;
    }

    public double getDistance() {
        return distance;
    }

    public int getId() {
        return id;
    }

    public double getTime() {
        return time;
    }

    public void setAverage_speed(double average_speed) {
        this.average_speed = average_speed;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
