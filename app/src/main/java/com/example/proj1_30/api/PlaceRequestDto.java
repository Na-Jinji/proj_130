package com.example.proj1_30.api;

public class PlaceRequestDto {
    private String name;
    public PlaceRequestDto(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return "PlaceRequestDto [ name= " + name + "]";
    }
}
