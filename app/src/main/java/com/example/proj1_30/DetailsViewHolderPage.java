package com.example.proj1_30;

import com.example.proj1_30.table.Picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class DetailsViewHolderPage extends RecyclerView.ViewHolder {

    private ImageView image;

    Picture data;
    Bitmap bm;
    DetailsViewHolderPage(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.image);
    }

    public void onBind(Context context, Picture data){
        this.data = data;
        Glide.with(context).load(data.getUrl()).into(image);
    }

}
