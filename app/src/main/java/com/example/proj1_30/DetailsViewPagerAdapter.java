package com.example.proj1_30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proj1_30.table.Picture;

import java.util.ArrayList;

public class DetailsViewPagerAdapter extends RecyclerView.Adapter<DetailsViewHolderPage> {

    private ArrayList<Picture> listData;

    public DetailsViewPagerAdapter(ArrayList<Picture> data){
        this.listData = data;
    }

    @Override
    public DetailsViewHolderPage onCreateViewHolder(ViewGroup parent,int viewType){
        return new DetailsViewHolderPage(LayoutInflater.from(parent.getContext()).inflate(R.layout.details_view_pager, parent, false));
    }

    @Override
    public void onBindViewHolder(DetailsViewHolderPage holder, int position){
        if(holder instanceof DetailsViewHolderPage){
            DetailsViewHolderPage viewHolder = (DetailsViewHolderPage) holder;
            viewHolder.onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount(){
        return listData.size();
    }
}
