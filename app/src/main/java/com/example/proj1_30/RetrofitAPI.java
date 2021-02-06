package com.example.proj1_30;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @GET("/test/titles")
    Call<List<String>> getTitles();

    @GET("/test/select")
    Call<List<Data>> getList();

    @POST("/recommender")
    Call<JsonObject> postRecommend(@Body JsonObject object);
}


