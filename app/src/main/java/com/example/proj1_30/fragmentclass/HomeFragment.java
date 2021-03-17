package com.example.proj1_30.fragmentclass;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proj1_30.Data;
import com.example.proj1_30.R;
import com.example.proj1_30.RetrofitAPI;
import com.example.proj1_30.RetrofitClient;
import com.example.proj1_30.api.ApiClient;
import com.example.proj1_30.api.ApiInterface;
import com.example.proj1_30.api.PlaceRequestDto;
import com.example.proj1_30.table.Place;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // layout
    public static final int MAX = 5;
    private TextView[] txtPlaces = new TextView[MAX];
    private ImageView search_icon, imgPlaces;
    private EditText editSearch;
    private TextView txtSubTitle;

    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private ViewPager2 viewPager2;
    private List<String> titleList;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Springboot RestAPI
        RetrofitAPI retrofitAPI = RetrofitClient.getApiService();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // 기타 view
        txtSubTitle = (TextView)root.findViewById(R.id.txtPlaces);
        imgPlaces = (ImageView)root.findViewById(R.id.imgPlaces);

        // TextView 추가
        txtPlaces[0] = (TextView)root.findViewById(R.id.place0);
        txtPlaces[1] = (TextView)root.findViewById(R.id.place1);
        txtPlaces[2] = (TextView)root.findViewById(R.id.place2);
        txtPlaces[3] = (TextView)root.findViewById(R.id.place3);
        txtPlaces[4] = (TextView)root.findViewById(R.id.place4);

        for(int i = 0; i < MAX; i++)
            txtPlaces[i].setOnClickListener(this);

        // 검색 아이콘
        search_icon = (ImageView)root.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPlace();
            }
        });

        // 검색창 EditText
        editSearch = (EditText)root.findViewById(R.id.editSearch);
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

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 추천지 5군데 페이지 설정
        int dpValue = 54;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);

        viewPager2 = view.findViewById(R.id.view_pager2);

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
    }

    @Override
    public void onResume() {
        super.onResume();

        // 검색기록 reset
        editSearch.setText("");
    }

    @Override
    public void onClick(View view) {
        TextView tmp = (TextView)view;
        String value = tmp.getText().toString();
        if(value.equals("") || value.length() <= 0)
            return;

        setPlacesVisible(false, null, value + "와 비슷한 추천 관광지");
        editSearch.setText(value);
        viewPager2.setVisibility(View.VISIBLE);
        setRecommend_list(value);
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

        for(int i = 0; i < txtPlaces.length; i++) {
            txtPlaces[i].setVisibility(visibility);

            txtPlaces[i].setText("");
            if(places != null && i < places.size())
                txtPlaces[i].setText(places.get(i));
        }
    }

    public void setRecommend_list(String item) {
        apiInterface.getRecommendedPlaceByName(new PlaceRequestDto(item)).enqueue(new Callback<List<Place>>(){
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if(response.isSuccessful()){
                    List<Place> place = response.body();

                    ArrayList<Data> list = new ArrayList<>();
                    for(int i = 0; i < place.size(); i++){
                        list.add(new Data(place.get(i).getPicture().get(0).getUrl(), place.get(i).getName()));
                    }

                    viewPager2.setAdapter(new ViewPagerAdapter(list));
                }
                else{
                    Log.d("POST TEST", "response 실패");
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.d("POST TEST", "실패");
                t.printStackTrace();
            }
        });
    }
}