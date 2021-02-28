package com.example.proj1_30.fragmentclass;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proj1_30.Data;
import com.example.proj1_30.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewHolderPage> {
    private ArrayList<Data> listData;

    public ViewPagerAdapter(ArrayList<Data> data){
        this.listData = data;
    }

    @Override
    public ViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType){
        /*Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);*/
        return new ViewHolderPage(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolderPage holder, int position){
        if(holder instanceof ViewHolderPage){
            ViewHolderPage viewHolder = (ViewHolderPage) holder;
            viewHolder.onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount(){
        Log.d("AAAA", String.valueOf(listData.size()));
        return listData.size();
    }
}
