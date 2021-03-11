package com.example.proj1_30;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypageEditActivity extends AppCompatActivity {
    private ImageView imgMyPageEdit;
    private TextView txtUserName, txtUserEmail;
    private EditText editUserAge;
    private RadioGroup rGroupUserSex;
    private RadioButton rdoMale, rdoFemale;
    private String strSex = "";
    private Integer intDwellings;

    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();
    private GlobalApplication global = GlobalApplication.getGlobalApplicationContext();
    private String[] koreaProvince;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_edit);

        // 이미지뷰 동그랗게
        imgMyPageEdit = (ImageView)findViewById(R.id.imgMyPageEdit);
        imgMyPageEdit.setBackground(new ShapeDrawable(new RoundRectShape(new float[] {100, 100, 100, 100, 100, 100, 100, 100}, null, new float[] {100, 100, 100, 100, 100, 100, 100, 100})));
        imgMyPageEdit.setClipToOutline(true);

        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);
        editUserAge = (EditText)findViewById(R.id.editUserAge);
        rdoFemale = (RadioButton)findViewById(R.id.rdoFemale);
        rdoMale = (RadioButton)findViewById(R.id.rdoMale);
        koreaProvince = getResources().getStringArray(R.array.korea_province);

        rGroupUserSex = (RadioGroup)findViewById(R.id.rGroupUserSex);
        rGroupUserSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rdoFemale)
                    strSex = "여자";
                else
                    strSex = "남자";
            }
        });

        // 사용자 프로필 초기화
        Intent intent = getIntent();
        txtUserName.setText(intent.getExtras().getString("userName"));
        txtUserEmail.setText(intent.getExtras().getString("userEmail"));
        editUserAge.setText(Integer.toString(intent.getIntExtra("userAge", 0)));

        strSex = intent.getExtras().getString("userSex");
        if(!strSex.equals("선택 안 함")) {
            if (strSex.equals("여자"))
                rdoFemale.setChecked(true);
            else
                rdoMale.setChecked(true);
        }

        intDwellings = intent.getExtras().getInt("userDwellings");

        // 사용자 프로필 이미지
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(global.getProfile().getProfileImageUrl());
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
            imgMyPageEdit.setImageBitmap(bm);
            Log.d("IMAGE", "성공");
        }catch(Exception e){
            Log.d("IMAGE", "실패");
            e.printStackTrace();
        }

        // 스피너 어댑터
        Spinner s = (Spinner)findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                intDwellings = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        s.setSelection(intDwellings);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelMyPageEdit: // 취소
                finish();
                break;
            case R.id.doneMyPageEdit: // 완료
                Integer userDwellings = intDwellings;
                int userAge = Integer.parseInt(editUserAge.getText().toString());

                Intent intent = new Intent();
                intent.putExtra("userAge", userAge);
                intent.putExtra("userDwellings", userDwellings);
                intent.putExtra("userSex", strSex);
                setResult(RESULT_OK, intent);

                // 서버 DB에 사용자 정보 변경 사항 저장
                UserInfo info = new UserInfo(strSex, userAge, koreaProvince[userDwellings]);
                Toast.makeText(getApplicationContext(), "프로필이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                retrofitAPI.updateUserInfo(global.getEmail(), info).enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if(response.isSuccessful()){
                            UserInfo res = response.body();
                            Log.d("UPDATE_USER", "성공");
                            Log.d("UPDATE_USER", res.getSex());
                            Log.d("UPDATE_USER", res.getResidence());
                            Log.d("UPDATE_USER", Integer.toString(res.getAge()));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.d("UPDATE_USER", "실패");
                    }
                });
                finish();
                break;
        }
    }
}