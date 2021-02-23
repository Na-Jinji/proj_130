package com.example.proj1_30;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private TextView title;
    private ImageView image;

    private RelativeLayout layout;

    Data data;
    Bitmap bm;

    ViewHolderPage(View itemView){
        super(itemView);
        title = itemView.findViewById(R.id.view_title);
        image = itemView.findViewById(R.id.reco_image);
        layout = itemView.findViewById(R.id.layout);
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
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}
