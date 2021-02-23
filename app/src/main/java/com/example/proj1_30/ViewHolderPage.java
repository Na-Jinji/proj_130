package com.example.proj1_30;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private TextView title;
    private RelativeLayout layout;
    private Context context;
    Data data;

    ViewHolderPage(View itemView){
        super(itemView);
        title = itemView.findViewById(R.id.view_title);
        layout = itemView.findViewById(R.id.layout);
        context = itemView.getContext();
    }

    public void onBind(Data data){
        this.data = data;

        title.setText(data.getTitle());
        layout.setBackgroundResource(data.getColor());

        // layout 클릭 시, DetailsActivity로 이동
        // intent에 들어갈 값 -> place의 이름
        layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("place_name", data.getTitle());
                context.startActivity(intent);
            }
        });
    }

}
