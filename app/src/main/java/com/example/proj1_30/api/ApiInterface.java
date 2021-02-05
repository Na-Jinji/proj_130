package com.example.proj1_30.api;

import com.example.proj1_30.table.Place;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api/v1/place/{id}")
    Call<Place> getPlace(@Path("id") Long id);
}