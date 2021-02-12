package com.example.proj1_30;

import com.example.proj1_30.api.ApiInterface;
import com.example.proj1_30.api.ApiClient;
import com.example.proj1_30.table.Place;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity{
    Place place;
    Long place_id;

    TextView card_view_address_text;
    TextView card_view_tag_text;
    TextView card_view_phone_text;
    TextView card_view_url_text;
    TextView card_view_sum_text;
    TextView card_view_details_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        Intent intent = getIntent();
        place_id = Long.parseLong(intent.getExtras().getString("place_id"));
        place_id = Long.valueOf(1);
        card_view_address_text = (TextView) findViewById(R.id.card_view_address_text);
        card_view_tag_text = (TextView) findViewById(R.id.card_view_tag_text);
        card_view_phone_text = (TextView) findViewById(R.id.card_view_phone_text);
        card_view_url_text = (TextView) findViewById(R.id.card_view_url_text);
        card_view_sum_text = (TextView) findViewById(R.id.card_view_sum_text);
        card_view_details_text = (TextView) findViewById(R.id.card_view_details_text);

        final Context context = this;

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Place> call_place = apiInterface.getPlace(place_id);
        call_place.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                place = response.body();

                ArrayList<String> data = new ArrayList<>();
                data.add(place.getPicture().get(0).getUrl());
                data.add(place.getPicture().get(1).getUrl());
                data.add(place.getPicture().get(2).getUrl());
                data.add(place.getPicture().get(3).getUrl());
                data.add(place.getPicture().get(4).getUrl());

                card_view_address_text.setText(place.getAddress());
                card_view_phone_text.setText(place.getPhone());
                card_view_tag_text.setText(tagStr(place.getTag()));
                card_view_url_text.setText(place.getUrl());
                card_view_sum_text.setText(place.getSum());
                card_view_details_text.setText(place.getDetails());

                Log.d("DetailsActivity", response.body().toString());
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.d("DetailsActivity", t.toString());
            }
        });
    }

    public String tagStr(String tag) {
        tag = "#" + tag.replace(" ", " #");
        return tag;
    }
}
