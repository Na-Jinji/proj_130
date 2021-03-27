package com.example.proj1_30.fragmentclass;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proj1_30.GlobalApplication;
import com.example.proj1_30.HomeActivity;
import com.example.proj1_30.LoginActivity;
import com.example.proj1_30.MypageEditActivity;
import com.example.proj1_30.R;
import com.example.proj1_30.RetrofitAPI;
import com.example.proj1_30.RetrofitClient;
import com.example.proj1_30.UserInfo;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Thread.sleep;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_MYPAGE_EDIT = 1025;

    private String mParam1;
    private String mParam2;

    private ImageView imgMyPage;
    private LinearLayout layoutMyPageEdit;
    private TextView txtMypageName, txtMypageSex, txtMypageAge, txtMypageDwellings, txtmypageEmail;

    private String userName, userEmail, userSex;
    private Integer userAge, userDwellings;
    private String[] koreaProvince;

    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();
    private GlobalApplication global = GlobalApplication.getGlobalApplicationContext();
    private Bitmap bm;
    private LinearLayout layoutLogout;

    public MypageFragment() {
    }

    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
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

        koreaProvince = getResources().getStringArray(R.array.korea_province);

        // 사용자 프로필 - 서버 DB에서 읽어오기
        if (global.getProfile() == null)
            userName = "없음";
        else
            userName = global.getProfile().getNickname();
        if (global.getEmail() == null)
            userEmail = "없음";
        else
            userEmail = global.getEmail();

        if (global.getSex() == null)
            userSex = "선택 안 함";
        else
            userSex = global.getSex();

        if (global.getAge() == 0)
            userAge = 0;
        else
            userAge = global.getAge();

        if (global.getResidence() == null)
            userDwellings = 0;
        else {
            for (int i = 0; i < koreaProvince.length; i++) {
                if (koreaProvince[i].equals(global.getResidence()))
                    userDwellings = i;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mypage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // imgView 둥글게
        imgMyPage = (ImageView)view.findViewById(R.id.imgMyPage);
        imgMyPage.setBackground(new ShapeDrawable(new RoundRectShape(new float[] {100, 100, 100, 100, 100, 100, 100, 100}, null, new float[] {100, 100, 100, 100, 100, 100, 100, 100})));
        imgMyPage.setClipToOutline(true);

        layoutMyPageEdit = (LinearLayout)view.findViewById(R.id.layoutMyPageEdit);
        layoutMyPageEdit.setOnClickListener(this);

        // 로그아웃 버튼 onClickListener 상속
        layoutLogout = (LinearLayout)view.findViewById(R.id.layoutLogout);
        layoutLogout.setOnClickListener(this);

        // Textview
        txtMypageName = (TextView)view.findViewById(R.id.txtMypageName);
        txtMypageName.setText(userName + " 님");

        txtMypageSex = (TextView)view.findViewById(R.id.txtMypageSex);
        txtMypageSex.setText(userSex);

        txtMypageAge = (TextView)view.findViewById(R.id.txtMypageAge);
        txtMypageAge.setText(userAge.toString() + " 세");

        txtMypageDwellings = (TextView)view.findViewById(R.id.txtMypageDwellings);
        txtMypageDwellings.setText(koreaProvince[userDwellings]);

        txtmypageEmail = (TextView)view.findViewById(R.id.txtmypageEmail);
        txtmypageEmail.setText(userEmail);

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
            imgMyPage.setImageBitmap(bm);
            Log.d("IMAGE", "성공");
        }catch(Exception e){
            Log.d("IMAGE", "실패");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutMyPageEdit:
                // MypageEditActivity 호출
                Intent intent = new Intent(getActivity(), MypageEditActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userAge", userAge);
                intent.putExtra("userSex", userSex);
                intent.putExtra("userDwellings", userDwellings);
                startActivityForResult(intent, REQUEST_MYPAGE_EDIT);
                break;
            case R.id.layoutLogout: {

                ((HomeActivity)HomeActivity.mContext).makeCustomToast("로그아웃 되었습니다", getContext());
                // 카카오 로그아웃

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.d("KAKAO_API", "로그아웃 완료");
                        global.init();
                        Intent login = new Intent(getActivity(), LoginActivity.class);
                        startActivity(login);
                    }
                });

                // 카카오 연결끊기
                /*
                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback(){

                    @Override
                    public void onSuccess(Long result) {
                        Log.d("KAKAO_API", "연결 끊기 성공. id : " + result);
                        global.init();
                        Intent login = new Intent(getActivity(), LoginActivity.class);
                        startActivity(login);
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음 : " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult){
                        Log.e("KAKAO_API", "연결 끊기 실패 : " + errorResult);
                    }
                });
                */
               break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_MYPAGE_EDIT) {
            if(resultCode != Activity.RESULT_OK)
                return;

            userAge = data.getExtras().getInt("userAge");
            txtMypageAge.setText(userAge.toString() + " 세");

            userSex = data.getExtras().getString("userSex");
            txtMypageSex.setText(userSex);

            userDwellings = data.getExtras().getInt("userDwellings");
            txtMypageDwellings.setText(koreaProvince[userDwellings]);
        }
    }
}