package com.example.proj1_30.fragmentclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proj1_30.Bookmark;
import com.example.proj1_30.R;

import java.util.List;

public class BookmarkAdapter extends BaseAdapter {
    Context mContext = null;
    List<Bookmark> mData = null;
    LayoutInflater mLayoutInflater = null;

    public BookmarkAdapter(Context context, List<Bookmark> data) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() { return mData.size(); }

    @Override
    public Object getItem(int i) { return mData.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View itemLayout = convertView;
        ViewHolder viewHolder = null;

        if(itemLayout == null) {
            itemLayout = mLayoutInflater.inflate(R.layout.bookmark_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.bookmarkTitle = (TextView)itemLayout.findViewById(R.id.bookmarkTitle);
            viewHolder.bookmarkTime = (TextView)itemLayout.findViewById(R.id.bookmarkTime);

            itemLayout.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder)itemLayout.getTag();

        viewHolder.bookmarkTitle.setText(mData.get(i).getTitle());
        viewHolder.bookmarkTime.setText(mData.get(i).getDateFormat());

        return itemLayout;
    }

    class ViewHolder {
        TextView bookmarkTitle;
        TextView bookmarkTime;
    }
}
