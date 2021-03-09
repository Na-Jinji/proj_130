package com.example.proj1_30;

public class UserInfo {
    private int id;
    private String usr_name;
    private String email;
    private String sex;
    private int age;
    private String residence;

    public UserInfo(String usr_name, String email) {
        this.usr_name = usr_name;
        this.email = email;
    }

    public UserInfo(String sex, int age, String residence){
        this.sex = sex;
        this.age = age;
        this.residence = residence;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }
}
