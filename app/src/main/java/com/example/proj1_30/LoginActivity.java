package com.example.proj1_30;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    private Button btn_custom_login;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        Log.e("getKeyHash", ""+getKeyHash(LoginActivity.this));

        sessionCallback = new SessionCallback(LoginActivity.this);

        btn_custom_login = (Button) findViewById(R.id.btn_kakao_login);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        btn_custom_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
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

    public void redirectHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
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
