package com.example.proj1_30;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecommendActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    List<String> recommend_list;
    ArrayList<Data> list;
    Retrofit flask;
    RetrofitAPI flaskAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        viewPager2 = findViewById(R.id.view_pager);

        Intent main = getIntent();
        String item = main.getExtras().getString("value");

        Log.d("INTENT TEST", item);

        // Flask Server
        flask = new Retrofit.Builder()
                .baseUrl("http://3.36.136.219:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        flaskAPI = flask.create(RetrofitAPI.class);

        // 추천지 5군데 페이지 설정
        int dpValue = 54;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager2.setPadding(margin, 0, margin, 0);

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
/*
        list = new ArrayList<>();
        list.add(new Data(android.R.color.black,"1 Page"));
        list.add(new Data(android.R.color.holo_red_light, "2 Page"));
        list.add(new Data(android.R.color.holo_green_dark, "3 Page"));
        list.add(new Data(android.R.color.holo_orange_dark, "4 Page"));
        list.add(new Data(android.R.color.holo_blue_light, "5 Page"));
        viewPager2.setAdapter(new ViewPagerAdapter(list));
*/
        //viewPager2.setVisibility(View.INVISIBLE);

        JsonObject input = new JsonObject();
        input.addProperty("name", item);

        flaskAPI.postRecommend(input).enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject object = response.body();
                    JsonArray jsonlist= object.getAsJsonArray("recommended_landmarks");
                    Log.d("POST TEST", jsonlist.toString());
                    recommend_list = new ArrayList<>();
                    for(int i = 0; i < 5; i++)
                        recommend_list.add(jsonlist.get(i).toString());
                    Log.d("POST TEST", recommend_list.get(0));
                    Log.d("POST TEST", "성공");

                    list = new ArrayList<>();
                    list.add(new Data(android.R.color.black,recommend_list.get(0)));
                    list.add(new Data(android.R.color.holo_red_light, recommend_list.get(1)));
                    list.add(new Data(android.R.color.holo_green_dark, recommend_list.get(2)));
                    list.add(new Data(android.R.color.holo_orange_dark, recommend_list.get(3)));
                    list.add(new Data(android.R.color.holo_blue_light, recommend_list.get(4)));
                    viewPager2.setAdapter(new ViewPagerAdapter(list));
                    //viewPager2.setVisibility(View.VISIBLE);
                }
                else{
                    Log.d("POST TEST", "response 실패");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("POST TEST", "실패");
                t.printStackTrace();
            }
        });

        /*
        edit.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                Log.d("TEXT INPUT", item);

                JsonObject input = new JsonObject();
                input.addProperty("name", item);

                flaskAPI.postRecommend(input).enqueue(new Callback<JsonObject>(){
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject object = response.body();
                            JsonArray jsonlist= object.getAsJsonArray("recommended_landmarks");
                            Log.d("POST TEST", list.toString());
                            recommend_list = new ArrayList<>();
                            for(int i = 0; i < 5; i++)
                                recommend_list.add(jsonlist.get(i).toString());
                            Log.d("POST TEST", recommend_list.get(0));
                            Log.d("POST TEST", "성공");

                            list = new ArrayList<>();
                            list.add(new Data(android.R.color.black,recommend_list.get(0)));
                            list.add(new Data(android.R.color.holo_red_light, recommend_list.get(1)));
                            list.add(new Data(android.R.color.holo_green_dark, recommend_list.get(2)));
                            list.add(new Data(android.R.color.holo_orange_dark, recommend_list.get(3)));
                            list.add(new Data(android.R.color.holo_blue_light, recommend_list.get(4)));
                            viewPager2.setAdapter(new ViewPagerAdapter(list));
                            viewPager2.setVisibility(View.VISIBLE);
                        }
                        else{
                            Log.d("POST TEST", "response 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("POST TEST", "실패");
                        t.printStackTrace();
                    }
                });
            }
        });
        */
    }
}