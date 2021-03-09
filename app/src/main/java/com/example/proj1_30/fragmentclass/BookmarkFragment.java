package com.example.proj1_30.fragmentclass;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proj1_30.Bookmark;
import com.example.proj1_30.GlobalApplication;
import com.example.proj1_30.R;
import com.example.proj1_30.RetrofitAPI;
import com.example.proj1_30.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView listView;

    private GlobalApplication global = GlobalApplication.getGlobalApplicationContext();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();
    // 북마크 리스트
    private List<Bookmark> bookmarkList;
    // 북마크 title 리스트
    private List<String> titleList = new ArrayList<String>();
    static String[] test = {"test1", "test2", "test3"};

    public BookmarkFragment() {
    }

    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        retrofitAPI.getBookmarks(global.getEmail()).enqueue(new Callback<List<Bookmark>>() {
            @Override
            public void onResponse(Call<List<Bookmark>> call, Response<List<Bookmark>> response) {
                if (response.isSuccessful()) {
                    bookmarkList = response.body();
                    Log.d("GET_BOOKMARKS", "성공");
                    Log.d("GET_BOOKMARKS", bookmarkList.get(0).getTitle());
                    for(Bookmark bookmark : bookmarkList){
                        titleList.add(bookmark.getTitle());
                    }
                    Log.d("SIZE_BOOKMARK", Integer.toString(titleList.size()));

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titleList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Bookmark>> call, Throwable t) {
                Log.d("GET_BOOKMARKS", "실패");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        listView = (ListView) view.findViewById(R.id.bookmark_list);
        return view;
    }

}
