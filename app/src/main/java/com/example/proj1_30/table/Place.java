package com.example.proj1_30.table;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Place {
    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("tag")
    private String tag;
    @SerializedName("url")
    private String url;
    @SerializedName("sum")
    private String sum;
    @SerializedName("details")
    private String details;
    @SerializedName("picture")
    private List<Picture> picture;

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public String getSum() {
        return sum;
    }

    public String getDetails() {
        return details;
    }

    public List<Picture> getPicture() {
        return picture;
    }

    public String toString() {
        return "id= " + id +
                " name= " + name +
                " address= " + address +
                " phone= " + phone +
                " tag= " + tag +
                " url= " + url +
                " sum= " + sum +
                " details= " + details +
                " pictures= " + picture.toString();
    }
}
