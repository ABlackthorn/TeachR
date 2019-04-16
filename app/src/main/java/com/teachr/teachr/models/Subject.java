package com.teachr.teachr.models;

public class Subject implements Cloneable{

    private String id;
    private String name;

    public Subject() {

    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Object clone() {
        try {
            return super.clone();
        }catch(Exception e){return null;}
    }
}
