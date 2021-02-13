package com.example.proj1_30;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ImageView search_icon, imgPlaces;
    private EditText editSearch;
    private TextView txtSubTitle;

    private RelativeLayout layout;
    private Retrofit flask;
    private RetrofitAPI flaskAPI;
    ViewPager2 viewPager2;
    List<String> recommend_list;

    private TextView[] txtPlaces = new TextView[MAX];
    private List<String> titleList;

    public static final int MAX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, ((GlobalApplication)MainActivity.this.getApplication()).getEmail(), Toast.LENGTH_LONG).show();

        // Springboot RestAPI
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.36.136.219:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        retrofitAPI.getTitles().enqueue(new Callback<List<String>>(){
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response){
                if(response.isSuccessful()){
                    titleList = response.body();
                    Log.d("TEST2", "성공");
                    Log.d("TEST2", titleList.get(0));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t){
                t.printStackTrace();
            }
        });

        // Flask Server
        flask = new Retrofit.Builder()
                .baseUrl("http://3.36.136.219:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        flaskAPI = flask.create(RetrofitAPI.class);

        // View id
        layout = (RelativeLayout)findViewById(R.id.main_layout);
        search_icon = (ImageView)findViewById(R.id.search_icon);
        editSearch = (EditText)findViewById(R.id.editSearch);
        txtSubTitle = (TextView)findViewById(R.id.txtPlaces);
        imgPlaces = (ImageView)findViewById(R.id.imgPlaces);

        // 텍스트뷰 추가
        txtPlaces[0] = (TextView)findViewById(R.id.place0);
        txtPlaces[1] = (TextView)findViewById(R.id.place1);
        txtPlaces[2] = (TextView)findViewById(R.id.place2);
        txtPlaces[3] = (TextView)findViewById(R.id.place3);
        txtPlaces[4] = (TextView)findViewById(R.id.place4);
        viewPager2 = findViewById(R.id.view_pager2);

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

        // 검색 아이콘
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPlace();
            }
        });

        // 검색창 EditText
        editSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_UP) { // 엔터키
                    checkPlace();
                    return true;
                }

                return false;
            }
        });
    }

    // 검색어 확인
    public void checkPlace() {
        viewPager2.setVisibility(View.INVISIBLE);

        String txt = editSearch.getText().toString();
        if (txt.length() <= 0 || txt.equals("")) {
            setPlacesVisible(false, null, "");
            return;
        }
        txt = txt.trim();

        // 비슷한 이름 장소 가져오기
        ArrayList<String> similarPlaces = getSimilarPlaces(txt);
        if(similarPlaces == null) {
            setPlacesVisible(false, null, "Places");
            return;
        }

        // 장소 보여주기
        setPlacesVisible(true, similarPlaces, "Places");
    }

    // 비슷한 관광지 받아오기
    public ArrayList<String> getSimilarPlaces(String place) {
        ArrayList<String> value = new ArrayList<>();

        for(String name : titleList) {
            if(name.contains(place))
                value.add(name);
        }
        return value;
    }

    // Places Visible or not
    public void setPlacesVisible(Boolean flag, ArrayList<String> places, String subTitle) {
        int visibility = View.GONE;
        if(flag) {
            visibility = View.VISIBLE;
        }

        txtSubTitle.setText(subTitle);

        imgPlaces.setVisibility(visibility);

        // Log.d("AAAA","setPlaces");
        for(int i = 0; i < txtPlaces.length; i++) {
            txtPlaces[i].setVisibility(visibility);

            txtPlaces[i].setText("");
            if(places != null && i < places.size())
                txtPlaces[i].setText(places.get(i));
        }
    }

    // 비슷한 관광지 Click시 이벤트 처리
    public void onClick(View view) {
        TextView tmp = (TextView)view;
        String value = tmp.getText().toString();
        if(value.equals("") || value.length() <= 0)
            return;

        Log.d("AAAA", "value : " + value);
        setPlacesVisible(false, null, "Recommendations");

        editSearch.setText(value);

        Log.d("AAAA", "onClick...");
        viewPager2.setVisibility(View.VISIBLE);
        setRecommend_list(value);
    }

    // Flask API에서 추천 관광지 받아오기
    public void setRecommend_list(String item) {
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

                    ArrayList<Data> list = new ArrayList<>();
                    list.add(new Data(android.R.color.black,recommend_list.get(0)));
                    list.add(new Data(android.R.color.holo_red_light, recommend_list.get(1)));
                    list.add(new Data(android.R.color.holo_green_dark, recommend_list.get(2)));
                    list.add(new Data(android.R.color.holo_orange_dark, recommend_list.get(3)));
                    list.add(new Data(android.R.color.holo_blue_light, recommend_list.get(4)));
                    viewPager2.setAdapter(new ViewPagerAdapter(list));
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


}