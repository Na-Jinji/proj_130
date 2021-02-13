package com.example.proj1_30;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
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
                            Log.e("KAKAO_API", "사용자 아이디: " + result.getId());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if(kakaoAccount != null){

                                // 이메일
                                String email = kakaoAccount.getEmail();

                                if(email != null){
                                    Log.i("KAKAO_API", "email: " + email);
                                }else if(kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE){
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                }else{
                                    // 이메일 획득 불가
                                }

                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if(profile != null){
                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
                                }else if(kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE){
                                    // 동의 요청 후 프로필 정보 획득 가능
                                }else{
                                    // 프로필 획득 불가
                                }
                            }
                            startActivity(intent);
                        }
                    });
        }

    }

}
