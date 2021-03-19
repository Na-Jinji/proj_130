package com.example.proj1_30;

import com.example.proj1_30.table.Picture;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class DetailsViewHolderPage extends RecyclerView.ViewHolder {

    private ImageView image;
    private Context context;

    Picture data;

    public DetailsViewHolderPage(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.image);
        context = itemView.getContext();
    }

    public void onBind(Picture data){
        this.data = data;
        Glide.with(context).load(data.getUrl()).into(image);
    }

}
