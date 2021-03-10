package com.example.proj1_30;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Bookmark {
    private int bookmark_id;
    private String title;  // 장소 이름
    private String email;

    // 지우가 더 필요한 정보! 주소 & 날짜
    private Calendar time;   // 날짜

    public Bookmark(String title, String email) {
        this.title = title;
        this.email = email;

        // 날짜 생성
        this.time = Calendar.getInstance();
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

    public String getTime() {
        // 현재 시간을 "XXXX년 XX월 XX일 XX시 XX분" 문자열로 리턴
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분");
        String format1_time = format1.format(time.getTime());
        return format1_time;
    }
}
