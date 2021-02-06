package com.example.proj1_30;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private TextView title;
    private RelativeLayout layout;

    Data data;

    ViewHolderPage(View itemView){
        super(itemView);
        title = itemView.findViewById(R.id.view_title);
        layout = itemView.findViewById(R.id.layout);
    }

    public void onBind(Data data){
        this.data = data;

        title.setText(data.getTitle());
        layout.setBackgroundResource(data.getColor());
    }

}
