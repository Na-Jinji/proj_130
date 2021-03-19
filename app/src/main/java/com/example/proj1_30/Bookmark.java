package com.example.proj1_30;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    public String getDateFormat(){
        String string = this.getCreation_date();
        LocalDateTime date = LocalDateTime.parse(string);
        // 메타문자(.) 처리 -> \\
        /*String[] str = string.split("\\.");
        Log.d("TIME", str[0]);
        LocalDate date = LocalDate.parse(str[0], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
*/
        return date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh시 mm분"));
    }
}
