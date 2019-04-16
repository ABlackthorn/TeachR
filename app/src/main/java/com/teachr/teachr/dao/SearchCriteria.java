package com.teachr.teachr.dao;

public class SearchCriteria {

    private String key;
    private String value;
    private int comparator;

    public SearchCriteria(){}

    public SearchCriteria(String key, String value, int comparator){
        this.key = key;
        this.value = value;
        this.comparator = comparator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getComparator() {
        return comparator;
    }

    public void setComparator(int comparator) {
        this.comparator = comparator;
    }
}
