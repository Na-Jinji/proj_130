package com.example.proj1_30;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("/test/titles")
    Call<List<String>> getTitles();

    @GET("/test/select")
    Call<List<Data>> getList();

    @POST("/recommender")
    Call<JsonObject> postRecommend(@Body JsonObject object);

    @GET("/test/url/{title}")
    Call<String> getUrl(@Path("title") String title);

    // 사용자 정보 생성하기
    @POST("/test/userinfo")
    Call<UserInfo> createUserInfo(@Body UserInfo userInfo);

    // 전체 사용자 정보 가져오기
    @GET("/test/userinfo")
    Call<List<UserInfo>> getAllUser();

    // 특정 사용자 정보 가져오기
    @POST("/test/userinfo/{email}")
    Call<UserInfo> getUserInfo(@Path("email") String email);

    // 사용자 정보 수정하기
    @PUT("/test/userinfo/{email}")
    Call<UserInfo> updateUserInfo(@Path("email") String email);

    // 북마크 생성하기
    @POST("/test/bookmark")
    Call<Bookmark> createBookmark(@Body Bookmark bookmark);

    // 북마크 목록 가져오기
    @GET("/test/bookmark/{email}")
    Call<List<Bookmark>> getBookmarks(@Path("email")String email);

    // 북마크 삭제하기
    @DELETE("/test/bookmark/{email}/{title}")
    Call<String> deletedBookmark(@Path("email") String email, @Path("title") String title);
}


