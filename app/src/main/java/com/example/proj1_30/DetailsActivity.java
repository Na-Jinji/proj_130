package com.example.proj1_30;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proj1_30.api.ApiClient;
import com.example.proj1_30.api.ApiInterface;
import com.example.proj1_30.api.PlaceRequestDto;
import com.example.proj1_30.table.Picture;
import com.example.proj1_30.table.Place;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    Place place;
    String place_name;
    ViewPager2 viewPager2;

    ImageView card_view_arrow_back_icon;
    ImageView card_view_user_icon;
    ImageView card_view_heart_icon;
    ImageView card_view_share_icon;

    TextView site_name_text;
    TextView card_view_address_text;
    TextView card_view_tag_text;
    TextView card_view_phone_text;
    TextView card_view_url_text;
    TextView card_view_sum_text;
    TextView card_view_details_text;

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();
    private GlobalApplication global = GlobalApplication.getGlobalApplicationContext();
    private boolean flag = false;

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

        card_view_arrow_back_icon = (ImageView) findViewById(R.id.card_view_back_icon);
        card_view_user_icon = (ImageView) findViewById(R.id.card_view_user_icon);
        card_view_heart_icon = (ImageView) findViewById(R.id.card_view_heart_icon);
        card_view_share_icon = (ImageView) findViewById(R.id.card_view_share_icon);

        site_name_text = (TextView) findViewById(R.id.site_name);
        card_view_address_text = (TextView) findViewById(R.id.card_view_address_text);
        card_view_tag_text = (TextView) findViewById(R.id.card_view_tag_text);
        card_view_phone_text = (TextView) findViewById(R.id.card_view_phone_text);
        card_view_url_text = (TextView) findViewById(R.id.card_view_url_text);
        card_view_sum_text = (TextView) findViewById(R.id.card_view_sum_text);
        card_view_details_text = (TextView) findViewById(R.id.card_view_details_text);

        // 북마크 여부
        retrofitAPI.getBookmarks(global.getEmail()).enqueue(new Callback<List<Bookmark>>() {
            @Override
            public void onResponse(Call<List<Bookmark>> call, Response<List<Bookmark>> response) {
                if(response.isSuccessful()){
                    List<Bookmark> list = response.body();
                    Log.d("GET_BOOKMARKS", "성공");
                    for(Bookmark bm : list){
                        //사용자의 북마크 리스트에 해당 장소가 있다면
                        if(bm.getTitle().equals(place_name)) {
                            flag = true;
                            // 하트 체크
                            card_view_heart_icon.setColorFilter(Color.parseColor("#FFD72626"));
                            Log.d("GET_BOOKMARKS", "flag : " + flag);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Bookmark>> call, Throwable t) {
                Log.d("GET_BOOKMARKS", "실패" + ", flag: " + flag);
            }
        });

        // NaverMapCallback OnMapReadyCallback
        OnMapReadyCallback callback = this;

        // retrofit 객체 생성 후 call 객체로 동기 통신

        HashMap<String, Object> input = new HashMap<>();
        input.put("name", place_name);

        // /api/v1/place로 응답 요청 (비동기)
        apiInterface.getPlaceByName(new PlaceRequestDto(place_name)).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (response.isSuccessful()) {
                    place = response.body();

                    ArrayList<Picture> list = new ArrayList<>();
                    for(int i = 0; i<place.getPicture().size(); i++){
                        list.add(place.getPicture().get(i));
                    }

                    viewPager2.setAdapter(new DetailsViewPagerAdapter(list));

                    site_name_text.setText(place.getName());
                    card_view_address_text.setText(place.getAddress());
                    card_view_phone_text.setText(place.getPhone());
                    card_view_tag_text.setText(tagStr(place.getTag()));
                    card_view_url_text.setText(place.getUrl());
                    card_view_sum_text.setText(place.getSum());
                    card_view_details_text.setText(place.getDetails());

                    // NaverMap Fragment 객체 생성 후 getMapAsync 함수로 OnMapReady 함수를 호출
                    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                    if (mapFragment == null) {
                        mapFragment = MapFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
                    }
                    mapFragment.getMapAsync(callback);
                }
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.d("DetailsActivity", t.toString());
            }
        });
        card_view_arrow_back_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        card_view_share_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, ApiClient.BASE_URL+"place/" + String.valueOf(place.getId()));
                startActivity(Intent.createChooser(intent, "친구에게 공유하기"));
            }
        });
        card_view_heart_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 북마크 추가 기능
                if(flag == false){

                    card_view_heart_icon.setColorFilter(Color.parseColor("#FFD72626"));

                    // Toast 커스텀
                    ((HomeActivity)HomeActivity.mContext).makeCustomToast("북마크 등록되었습니다", getApplicationContext());

                    Bookmark bookmark = new Bookmark(place_name, global.getEmail());
                    retrofitAPI.createBookmark(bookmark).enqueue(new Callback<Bookmark>() {
                        @Override
                        public void onResponse(Call<Bookmark> call, Response<Bookmark> response) {
                            if (response.isSuccessful()) {
                                Bookmark mark = response.body();
                                Log.d("CREATE_BOOKMARK", "성공");
                                Log.d("CREATE_BOOKMARK", mark.getTitle());
                            }
                        }

                        @Override
                        public void onFailure(Call<Bookmark> call, Throwable t) {
                            Log.d("CREATE_BOOKMARK", "존재하는 북마크");
                        }
                    });
                    flag = true;
                }
                else{  // 북마크 삭제 기능
                    card_view_heart_icon.setColorFilter(Color.parseColor("#FFFFFF"));

                    // Toast 커스텀
                    ((HomeActivity)HomeActivity.mContext).makeCustomToast("북마크 취소되었습니다", getApplicationContext());

                    retrofitAPI.deletedBookmark(global.getEmail(), place_name).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if(response.isSuccessful()){
                                String msg = response.body();
                                Log.d("DELETE_BOOKMARK", "성공");
                                Log.d("DELETE_BOOKMARK", msg);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("DELETE_BOOKMARK", "실패");
                        }
                    });
                    flag = false;
                }
            }
       });
        card_view_address_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + place.getLatitude()+","+place.getLongitude()+"?q="+place.getName()));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        card_view_phone_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+place.getPhone()));
                startActivity(intent);
            }
        });
        card_view_url_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getUrl()));
                intent.setPackage("com.android.chrome");
                startActivity(intent);
            }
        });
    }

    // 마커 생성 부분
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Marker marker = new Marker();

        // marker 찍는 부분
        marker.setPosition(new LatLng(place.getLatitude(), place.getLongitude()));
        marker.setMap(naverMap);

        // 지도 위치를 marker 위치로 조정하는 부분
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(place.getLatitude(), place.getLongitude()),
                10,
                0,
                0
        );
        naverMap.setCameraPosition(cameraPosition);

        // marker 클릭 시 네이버 지도로 넘어가는 부분은 이후에 구현할 예정
        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + place.getLatitude()+","+place.getLongitude()+"?q="+place.getName()));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
