package com.example.proj1_30.fragmentclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proj1_30.Data;
import com.example.proj1_30.DetailsActivity;
import com.example.proj1_30.R;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private TextView title;
    private ImageView image;

    private RelativeLayout layout;
    private Context context;

    Data data;

    ViewHolderPage(View itemView){
        super(itemView);
        title = itemView.findViewById(R.id.view_title);
        image = itemView.findViewById(R.id.reco_image);
        layout = itemView.findViewById(R.id.layout);
        context = itemView.getContext();
    }

    public void onBind(Data data){
        this.data = data;
        Glide.with(context).load(data.getImg_url()).into(image);

        title.setText(data.getTitle());
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("place_name", data.getTitle().replaceAll(String.valueOf('"'), ""));
                context.startActivity(intent);
            }
        });
    }
}
