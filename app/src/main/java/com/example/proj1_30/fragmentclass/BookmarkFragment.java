package com.example.proj1_30.fragmentclass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proj1_30.Bookmark;
import com.example.proj1_30.DetailsActivity;
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

    private ListView listView;   // 북마크 리스트뷰
    private BookmarkAdapter bookmarkAdapter = null;  // 북마크 어댑터

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
    public void onStart() {
        super.onStart();

        // 액티비티 생명주기 : onCreate --> onStart --> onResume --> 액티비티 동작 중
        // 북마크 화면에서 DetailsActivity를 클릭 후 북마크를 취소한 뒤 뒤로가기를 누르면 해당 사항이 업데이트가 되어야 한다
        // 북마크 리스트 얻어오기
        retrofitAPI.getBookmarks(global.getEmail()).enqueue(new Callback<List<Bookmark>>() {
            @Override
            public void onResponse(Call<List<Bookmark>> call, Response<List<Bookmark>> response) {
                if (response.isSuccessful()) {
                    bookmarkList = response.body();
                    Log.d("GET_BOOKMARKS", "성공");
                    for(Bookmark bookmark : bookmarkList){
                        titleList.add(bookmark.getTitle());
                        //Log.d("GET_BOOKMARKS", bookmark.getCreation_date());
                    }
                    Log.d("SIZE_BOOKMARK", Integer.toString(titleList.size()));

                    // 리스트뷰에 북마크 리스트 set
                    bookmarkAdapter = new BookmarkAdapter(getContext(), bookmarkList);
                    listView.setAdapter(bookmarkAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Bookmark>> call, Throwable t) {
                Log.d("GET_BOOKMARKS", "실패");
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView)view.findViewById(R.id.bookmark_list);

        // 아이템 클릭 시 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // DetailsActivity 호출
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("place_name", bookmarkList.get(position).getTitle().replaceAll(String.valueOf('"'), ""));
                startActivity(intent);
            }
        });

        // 아이템 Long Click 이벤트
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                //builder.setTitle("주의사항");
                builder.setMessage(bookmarkList.get(position).getTitle() + "를 북마크에서 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 예 선택
                                removeItem(bookmarkList.get(position).getTitle());
                                bookmarkList.remove(position);           // 아이템 제거

                                listView.clearChoices();                // listView 선택 초기화
                                bookmarkAdapter.notifyDataSetChanged();  // listView 갱신
                            }
                        });
                builder.setNegativeButton("아니오", null);
                builder.show();

                return true;
            }
        });
    }

    // 서버에서 북마크 아이템 삭제
    public void removeItem(String place_name) {
        retrofitAPI.deletedBookmark(global.getEmail(), place_name).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String msg = response.body();
                    Log.d("DELETE_BOOKMARK", "성공");
                    Log.d("DELETE_BOOKMARK", msg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("DELETE_BOOKMARK", "실패");
            }
        });
    }
}
