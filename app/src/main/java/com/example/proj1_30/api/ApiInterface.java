package com.example.proj1_30.api;

import com.example.proj1_30.table.Place;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api/v1/place")
    Call<JSONArray> getAllPlace();

    @POST("api/v1/place")
    Call<Place> getPlaceByName(@Body PlaceRequestDto dto);
}