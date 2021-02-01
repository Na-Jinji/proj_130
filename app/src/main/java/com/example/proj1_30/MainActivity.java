package com.example.proj1_30;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView search_icon, imgPlaces;
    EditText editSearch;
    TextView txtSubTitle;
    RelativeLayout layout;

    TextView[] txtPlaces = new TextView[5];
    ArrayList<String> titleList;
    static int MAX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Title 파일 읽기
        titleList = readTitleFile();

        layout = (RelativeLayout)findViewById(R.id.layout);
        search_icon = (ImageView)findViewById(R.id.search_icon);
        editSearch = (EditText)findViewById(R.id.editSearch);
        txtSubTitle = (TextView)findViewById(R.id.txtPlaces);
        imgPlaces = (ImageView)findViewById(R.id.imgPlaces);

        // 텍스트뷰 추가
        txtPlaces[0] = (TextView)findViewById(R.id.place0);
        txtPlaces[1] = (TextView)findViewById(R.id.place1);
        txtPlaces[2] = (TextView)findViewById(R.id.place2);
        txtPlaces[3] = (TextView)findViewById(R.id.place3);
        txtPlaces[4] = (TextView)findViewById(R.id.place4);

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPlace();
            }
        });

        editSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_UP) { // 엔터키
                    checkPlace();
                    return true;
                }

                return false;
            }
        });

        // focus 확인
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if(isFocus) {
                    Log.d("AAAA", "isFocus - True");
                    layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.discover_sc));
                }
            }
        });
    }

    public ArrayList<String> readTitleFile() {
        ArrayList<String> list = new ArrayList<>();

        try {
            InputStream in = getResources().openRawResource(R.raw.title);

            if(in != null) {
                InputStreamReader stream = new InputStreamReader(in, "utf-8");
                BufferedReader buffer = new BufferedReader(stream);

                String read;
                while((read = buffer.readLine()) != null) {
                    list.add(read);
                    // Log.d("AAAA", read);
                }
                in.close();
            }
        } catch (Exception e) {
            Log.e("AAAA", e.getMessage());
            return null;
        }

        return list;
    }

    public void checkPlace() {
        String txt = editSearch.getText().toString();
        if (txt.length() <= 0 || txt.equals("")) {
            setPlacesVisible(false, null);
            return;
        }
        txt = txt.trim();

        // 비슷한 이름 장소 가져오기
        ArrayList<String> similarPlaces = getSimilarPlaces(txt);
        if(similarPlaces == null) {
            setPlacesVisible(false, null);
            return;
        }

        // 장소 보여주기
        setPlacesVisible(true, similarPlaces);
    }

    public ArrayList<String> getSimilarPlaces(String place) {
        ArrayList<String> value = new ArrayList<>();

        // 비슷한 관광지 받아오기
        for(String name : titleList) {
            if(name.contains(place))
                value.add(name);
        }
        return value;
    }

    public void setPlacesVisible(Boolean flag, ArrayList<String> places) {
        int visibility = View.INVISIBLE;
        if(flag) {
            visibility = View.VISIBLE;
        }

        txtSubTitle.setVisibility(visibility);
        imgPlaces.setVisibility(visibility);

        // Log.d("AAAA","setPlaces");
        for(int i = 0; i < txtPlaces.length; i++) {
            txtPlaces[i].setVisibility(visibility);

            txtPlaces[i].setText("");
            if(places != null && i < places.size())
                txtPlaces[i].setText(places.get(i));
        }
    }

    public void onClick(View view) {
        TextView tmp = (TextView)view;
        String value = tmp.getText().toString();

        // 추천지 보여준다
    }
}