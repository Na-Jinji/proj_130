package com.example.proj1_30;

import com.example.proj1_30.api.ApiInterface;
import com.example.proj1_30.table.Picture;
import com.example.proj1_30.table.Place;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    String place_name;
    ViewPager2 viewPager2;

    TextView site_name_text;
    TextView card_view_address_text;
    TextView card_view_tag_text;
    TextView card_view_phone_text;
    TextView card_view_url_text;
    TextView card_view_sum_text;
    TextView card_view_details_text;

    Double latitude = 0.0, longitude = 0.0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setPadding(10, 0, 10, 0);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        // ViewPagerAdapter로부터 넘어오는 intent 처리
        Intent intent = getIntent();
        place_name = intent.getExtras().getString("place_name");

        site_name_text = (TextView) findViewById(R.id.site_name);
        card_view_address_text = (TextView) findViewById(R.id.card_view_address_text);
        card_view_tag_text = (TextView) findViewById(R.id.card_view_tag_text);
        card_view_phone_text = (TextView) findViewById(R.id.card_view_phone_text);
        card_view_url_text = (TextView) findViewById(R.id.card_view_url_text);
        card_view_sum_text = (TextView) findViewById(R.id.card_view_sum_text);
        card_view_details_text = (TextView) findViewById(R.id.card_view_details_text);

        // NaverMapCallback OnMapReadyCallback
        OnMapReadyCallback callback = this;

        // Retrofit 객체 생성
        // (아직 서버 문제 해결을 못해서 baseUrl에 올바른 url이 들어가있지 않음)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.36.6.110:80/") // 서버 에러 고치면 url 넣는 곳.
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // retrofit 객체 생성 후 call 객체로 동기 통신
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        // Post 요청을 위한 Body 생성
        JsonObject input = new JsonObject();
        input.addProperty("name", place_name);

        // /api/v1/place로 응답 요청 (비동기)
        apiInterface.getPlaceByName(input).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (response.isSuccessful()) {
                    Place place = response.body();

                    ArrayList<Picture> list = new ArrayList<>();
                    list.add(place.getPicture().get(0));
                    list.add(place.getPicture().get(1));
                    list.add(place.getPicture().get(2));
                    list.add(place.getPicture().get(3));
                    list.add(place.getPicture().get(4));

                    viewPager2.setAdapter(new DetailsViewPagerAdapter(list));

                    site_name_text.setText(place.getName());
                    card_view_address_text.setText(place.getAddress());
                    card_view_phone_text.setText(place.getPhone());
                    card_view_tag_text.setText(tagStr(place.getTag()));
                    card_view_url_text.setText(place.getUrl());
                    card_view_sum_text.setText(place.getSum());
                    card_view_details_text.setText(place.getDetails());

                    latitude = place.getLatitude();
                    longitude = place.getLongitude();

                    // NaverMap Fragment 객체 생성 후 getMapAsync 함수로 OnMapReady 함수를 호출
                    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                    if (mapFragment == null) {
                        mapFragment = MapFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
                    }
                    mapFragment.getMapAsync(callback);

                    Log.d("Response Success DetailsActivity", place.toString());
                }
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.d("DetailsActivity", t.toString());
            }
        });
    }

    // 마커 생성 부분
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Marker marker = new Marker();

        // marker 찍는 부분
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(naverMap);

        // 지도 위치를 marker 위치로 조정하는 부분
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(latitude, longitude),
                10,
                0,
                0
        );
        naverMap.setCameraPosition(cameraPosition);

        // marker 클릭 시 네이버 지도로 넘어가는 부분은 이후에 구현할 예정
        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Log.d("maker", "click");
                return false;
            }
        });
    }

    // tag 화면 표시 부분
    public String tagStr(String tag) {
        tag = "#" + tag.replace(" ", " #");
        return tag;
    }
}
