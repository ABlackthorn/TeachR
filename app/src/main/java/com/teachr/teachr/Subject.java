package com.teachr.teachr;

public class Subject {

    private String id;
    private String name;

    Subject() {

    }

    Subject(String name) {
        this.name = name;
    }

    Subject(String id, String name) {
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
}
