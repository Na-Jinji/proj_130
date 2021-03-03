package com.example.proj1_30.api;

public class DetailsObject {
    private String name;
    public DetailsObject(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "DetailsObject [name = " + name + "]";
    }
}
