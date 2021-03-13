package com.example.proj1_30;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Bookmark {
    private int bookmark_id;
    private String title;  // 장소 이름
    private String email;

    // 지우가 더 필요한 정보! 날짜
    private Calendar cal;
    private String current_time;

    public Bookmark(String title, String email) {
        this.title = title;
        this.email = email;

        // 날짜 생성
        this.cal = Calendar.getInstance();
        this.current_time = "";
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

    public void setCurrent_time(String time) { this.current_time = time; }

    public String getCurrent_time() { return this.current_time; }

    public String getTimeFromCal() {
        // 현재 시간을 "XXXX년 XX월 XX일 XX시 XX분" 문자열로 리턴
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분");
        String format1_time = format1.format(cal.getTime());
        return format1_time;
    }
}
