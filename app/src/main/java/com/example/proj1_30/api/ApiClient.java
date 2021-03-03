package com.example.proj1_30.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL = "http://54.180.225.210:80/";
    /*
        주소는 반드시 /로 끝나야한다.
        예를 들어, http://com.example.com/hello" 까지 적는다면,
        실제로 http://com.example.com/" 까지만 가져오게 되는 것이다.
        따라서 BASE_URL인 만큼 베이스 주소만 적어두고 나머지 주소는 interface에서 써준다.
     */

    private static Retrofit retrofit;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder() // retrofit 객체 생성
                    .baseUrl(BASE_URL) // 어떤 서버(BASE_URL)로 네트워크 통신을 할 것인지 설정
                    .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후, 어떤 converter로 데이터를 parsing할 것인지 결정
                    .build(); // 통신하여 데이터를 파싱한 retrofit 객체 생성 완료
        }
        return retrofit;
    }
}
