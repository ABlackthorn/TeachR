package com.teachr.teachr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry implements Parcelable, Cloneable {

    private String id;
    private String date;
    private long duration;
    private double longitude = 0;
    private double latitude;
    private long price;
    private String subject;
    private String user;
    private long type;
    private String username;
    private String address;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public Entry(){

    }

    public Entry(String date, long duration, double longitude, double latitude, long price, String subject, String user, long type, String address ){
        this.date = date;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.subject = subject;
        this.user = user;
        this.type = type;
        this.address = address;
    }

    public Entry(String id, String date, long duration, double longitude, double latitude, long price, String subject, String user, long type, String address ){
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.subject = subject;
        this.user = user;
        this.type = type;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username){this.username = username;}

    public String getUsername(){return this.username;}

    /**
     * Parcelable implementation
     */

    public Entry(Parcel in){
        this.id = in.readString();
        this.date = in.readString();
        this.duration =  in.readLong();
        this.longitude =  in.readDouble();
        this.latitude =  in.readDouble();
        this.price =  in.readLong();
        this.subject =  in.readString();
        this.user =  in.readString();
        this.type = in.readLong();
        this.address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeLong(this.duration);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeLong(this.price);
        dest.writeString(this.subject);
        dest.writeString(this.user);
        dest.writeLong(this.type);
        dest.writeString(this.address);
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", price='" + price + '\'' +
                ", subject='" + subject + '\'' +
                ", user='" + user + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Object clone() {
        try {
            return super.clone();
        }catch(Exception e){return null;}
    }

}
