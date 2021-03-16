package com.example.proj1_30;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.usermgmt.response.model.Profile;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    private String email;
    private Profile profile;
    private String sex;
    private int age;
    private String residence;

    public static GlobalApplication getGlobalApplicationContext(){
        if(instance == null){
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        // Kakao Sdk 초기화
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        instance = null;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public Profile getProfile(){
        return profile;
    }
    public void setProfile(Profile profile){
        this.profile = profile;
    }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getResidence() { return residence; }
    public void setResidence(String residence) { this.residence = residence; }

    public void init(){
        this.setEmail(null);
        this.setProfile(null);
        this.setAge(0);
        this.setSex(null);
        this.setResidence(null);
    }

    public class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig(){
            return new ISessionConfig(){

                // 로그인 시 인증 타입 지정
                @Override
                public AuthType[] getAuthTypes(){
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                // pause와 resume 시에 타이머를 설정 / CPU의 소모를 절약 할 지의 여부를 지정
                @Override
                public boolean isUsingWebviewTimer(){
                    return false;
                }


                @Override
                public boolean isSecureMode(){
                    return false;
                }

                // Kakao와 제휴 된 앱에서 사용되는 값
                @Override
                public ApprovalType getApprovalType(){
                    return ApprovalType.INDIVIDUAL;
                }

                // 로그인 웹뷰에서 email 입력 폼의 데이터를 저장할 지 여부를 지정
                @Override
                public boolean isSaveFormData(){
                    return true;
                }
            };
        }

        // Application이 가지고 있는 정보를 얻기 위한 인터페이스
        @Override
        public IApplicationConfig getApplicationConfig(){
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }



}