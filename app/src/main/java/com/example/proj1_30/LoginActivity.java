package com.example.proj1_30;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback = new SessionCallback();
    private Button btn_custom_login;
    private Button btn_custom_logout;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        Log.e("getKeyHash", ""+getKeyHash(LoginActivity.this));

        btn_custom_login = (Button) findViewById(R.id.btn_kakao_login);
        btn_custom_logout = (Button) findViewById(R.id.btn_kakao_login_out);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        btn_custom_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });

        btn_custom_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserManagement.getInstance()
                        .requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Log.d("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                            }

                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Log.d("KAKAO_API", "연결 끊기 실패: " + errorResult);

                            }
                            @Override
                            public void onSuccess(Long result) {
                                //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                Log.d("KAKAO_API", "연결 끊기 성공. id: " + result);
                                Toast.makeText(LoginActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);
                            }
                        });

            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    public class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened(){
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception){
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe(){
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        // 세션 오픈 실패. 세션이 삭제된 경우,
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        // 사용자 정보 요청 실패
                        @Override
                        public void onFailure(ErrorResult errorResult){
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        // 사용자정보 요청에 성공한 경우,
                        @Override
                        public void onSuccess(MeV2Response result) {
                            Log.d("KAKAO_API", "사용자 아이디: " + result.getId());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if(kakaoAccount != null){

                                // 이메일
                                String email = kakaoAccount.getEmail();

                                if(email != null){
                                    Log.d("KAKAO_API", "email: " + email);
                                    ((GlobalApplication)LoginActivity.this.getApplication()).setEmail(email);
                                }else if(kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE){
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                    Log.d("KAKAO_API", "선택적");
                                }else{
                                    // 이메일 획득 불가
                                    Log.d("KAKAO_API", "이메일 획득 불가");
                                }

                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if(profile != null){
                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
                                    ((GlobalApplication)LoginActivity.this.getApplication()).setProfile(profile);
                                }else if(kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE){
                                    // 동의 요청 후 프로필 정보 획득 가능
                                    Log.d("KAKAO_API", "선택적");
                                }else{
                                    // 프로필 획득 불가
                                    Log.d("KAKAO_API", "프로필 획득 불가");
                                }
                            }
                            startActivity(intent);
                        }
                    });
        }

    }

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
