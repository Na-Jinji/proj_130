package com.example.proj1_30;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Bookmark {
    private int bookmark_id;
    private String title;  // 장소 이름
    private String email;
    private String creation_date;

    public Bookmark(String title, String email) {
        this.title = title;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreation_date(){return this.creation_date;}
}
