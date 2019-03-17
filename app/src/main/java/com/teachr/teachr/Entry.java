package com.teachr.teachr;

public class Entry {

    private String id;
    private String date;
    private long duration;
    private double longitude;
    private double latitude;
    private long price;
    private String subject;
    private String user;
    private long type;

    public Entry(){}

    public Entry(String date, long duration, double longitude, double latitude, long price, String subject, String user, long type ){
        this.date = date;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.subject = subject;
        this.user = user;
        this.type = type;
    }

    public Entry(String id, String date, long duration, double longitude, double latitude, long price, String subject, String user, long type ){
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.subject = subject;
        this.user = user;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
