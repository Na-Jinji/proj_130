package com.example.proj1_30;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.Session;
import com.kakao.auth.api.AuthApi;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.api.UserApi;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 토큰 존재 여부 확인하기
                if(Session.getCurrentSession().checkAndImplicitOpen()){
                    // 카카오톡 로그인이 된 경우
                    Log.d("KAKAO_API", "로그인 된 상태");
                    SessionCallback callback = new SessionCallback();
                    callback.requestMe();
                    Log.d("KAKAO_API", "사용자 정보 가져오기 성공");

                    Intent isLogin = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(isLogin);
                    finish();
                }
                else{
                    Log.d("KAKAO_API", "로그인 안 된 상태");
                    // 카카오톡 로그인이 안 된 경우
                    Intent isNonLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(isNonLogin);
                    finish();
                }
            }
        }, 3000); // 3초 동안 인트로 실행
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
