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

import com.example.proj1_30.Data;
import com.example.proj1_30.DetailsActivity;
import com.example.proj1_30.R;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private TextView title;
    private ImageView image;

    private RelativeLayout layout;
    private Context context;

    Data data;
    Bitmap bm;

    ViewHolderPage(View itemView){
        super(itemView);
        title = itemView.findViewById(R.id.view_title);
        image = itemView.findViewById(R.id.reco_image);
        layout = itemView.findViewById(R.id.layout);
        context = itemView.getContext();
    }

    public void onBind(Data data){
        this.data = data;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(data.getImg_url());
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
            title.setText(data.getTitle());
            image.setImageBitmap(bm);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("place_name", data.getTitle());
                    context.startActivity(intent);
                }
            });
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}
