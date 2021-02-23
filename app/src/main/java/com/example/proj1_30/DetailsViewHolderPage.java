package com.example.proj1_30;

import com.example.proj1_30.table.Picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.IntToDoubleFunction;

public class DetailsViewHolderPage extends RecyclerView.ViewHolder {

    private ImageView image;

    Picture data;
    Bitmap bm;
    DetailsViewHolderPage(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.image);
    }

    public void onBind(Picture data){
        this.data = data;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(data.getUrl());
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

        try{
            mThread.join();
            image.setImageBitmap(bm);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

    }

}
