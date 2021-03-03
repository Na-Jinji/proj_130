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

public interface ApiInterface {
    @GET("api/v1/place")
    Call<JSONArray> getAllPlace();

    @POST("api/v1/place")
    Call<Place> getPlaceByName(@Body DetailsObject object);
}