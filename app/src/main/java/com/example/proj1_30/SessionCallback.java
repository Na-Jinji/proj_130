package com.example.proj1_30;

import android.content.Context;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionCallback implements ISessionCallback {
    private Context mContext;
    private static RetrofitAPI retrofitAPI = RetrofitClient.getApiService();
    private UserInfo info;
    private static GlobalApplication global = GlobalApplication.getGlobalApplicationContext();

    public SessionCallback(Context context){
        this.mContext = context;
    }

    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened(){
        requestMe();
        // HomeActivity로 이동
        ((LoginActivity)mContext).redirectHomeActivity();
    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception){
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
    }

    // 사용자 정보 요청
    public void requestMe() {
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {
                    // 세션 오픈 실패. 세션이 삭제된 경우,
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    // 사용자 정보 요청 실패
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                    }

                    // 사용자정보 요청에 성공한 경우,
                    @Override
                    public void onSuccess(MeV2Response result) {
                        Log.d("KAKAO_API", "사용자 아이디: " + result.getId());


                        UserAccount kakaoAccount = result.getKakaoAccount();
                        if (kakaoAccount != null) {

                            // 이메일
                            String email = kakaoAccount.getEmail();

                            if (email != null) {
                                Log.d("KAKAO_API", "email: " + email);
                                //((GlobalApplication)LoginActivity.this.getApplication()).setEmail(email);
                                global.setEmail(email);
                            } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                // 동의 요청 후 이메일 획득 가능
                                // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                Log.d("KAKAO_API", "선택적");
                            } else {
                                // 이메일 획득 불가
                                Log.d("KAKAO_API", "이메일 획득 불가");
                            }

                            // 프로필(nickname, profile image, thumbnail image)
                            Profile profile = kakaoAccount.getProfile();

                            if (profile != null) {
                                Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
                                //((GlobalApplication)LoginActivity.this.getApplication()).setProfile(profile);
                                global.setProfile(profile);
                            } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                // 동의 요청 후 프로필 정보 획득 가능
                                Log.d("KAKAO_API", "선택적");
                            } else {
                                // 프로필 획득 불가
                                Log.d("KAKAO_API", "프로필 획득 불가");
                            }

                            info = new UserInfo(profile.getNickname(), email);
                        }

                        Log.d("USER", "getUserInfo() 전");

                        // 유저 생성
                        retrofitAPI.createUserInfo(info).enqueue(new Callback<UserInfo>() {

                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if (response.isSuccessful()) {
                                    UserInfo user = response.body();
                                    Log.d("CREATE_USER", "성공");
                                    Log.d("CREATE_USER", user.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                Log.d("CREATE_USER", "존재하는 회원");
                                t.printStackTrace();
                            }
                        });

                        // 유저 정보 가져오기
                        retrofitAPI.getUserInfo(info.getEmail()).enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if (response.isSuccessful()) {
                                    UserInfo user = response.body();
                                    Log.d("GET_USER", "성공");
                                    Log.d("GET_USER", user.getUsr_name());
                                    global.setSex(user.getSex());
                                    global.setAge(user.getAge());
                                    global.setResidence(user.getResidence());
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                Log.d("GET_USER", "실패");
                            }
                        });
                    }
                });
    }// requestMe 메소드

}