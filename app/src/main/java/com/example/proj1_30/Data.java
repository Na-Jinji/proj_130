package com.example.proj1_30;

public class Data {
    private int color;
    private String category;
    private String title;
    private String tag;
    private String address;
    private String phone;
    private String homepage;
    private String sum_info;
    private String detail_info;
    private String img_url;

    public Data(String img_url, String title){
        this.img_url = img_url;
        //this.color = color;
        this.title = title;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public String getAddress(){
        return address;
    }

    public String getPhone(){
        return phone;
    }

    public String getHomepage(){
        return homepage;
    }

    public String getSum_info(){
        return sum_info;
    }

    public String getDetail_info(){
        return detail_info;
    }

    public String getImg_url(){
        return img_url;
    }

}
