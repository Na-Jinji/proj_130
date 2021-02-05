package com.example.proj1_30.table;

import com.google.gson.annotations.SerializedName;

public class Picture {

    @SerializedName("id")
    private Long id;
    @SerializedName("url")
    private String url;
    @SerializedName("place_id")
    private Long place_id;

    public String getUrl(){
        return url;
    }

    public String toString() {
        return "id= " + id +
                " url= " + url +
                " place= {" + place_id.toString() +"}";
    }
}