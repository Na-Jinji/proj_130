package com.example.proj1_30.api;

import com.example.proj1_30.table.Place;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;

// Details 서버 api interface
public interface ApiInterface {

    // 기능: 모든 장소를 다 불러옴
    @GET("api/v1/place")
    Call<JSONArray> getAllPlace();

    /*
    * {
    *   "name": "불국사"
    * }
    * 의 JsonObject로 POST 요청을 보냄
    * */
    @FormUrlEncoded
    @POST("api/v1/place")
    Call<Place> getPlaceByName(@FieldMap HashMap<String, Object> object);
}