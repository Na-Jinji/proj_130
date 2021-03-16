package com.example.proj1_30;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proj1_30.fragmentclass.BookmarkFragment;
import com.example.proj1_30.fragmentclass.HomeFragment;
import com.example.proj1_30.fragmentclass.MypageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView navView;

    // Fragment
    private BookmarkFragment bookmarkFragment;
    private HomeFragment homeFragment;
    private MypageFragment mypageFragment;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;

        // Bottom Navigation Bar
        navView = findViewById(R.id.bottomNav);

        // Fragment 초기화
        bookmarkFragment = new BookmarkFragment();
        homeFragment = new HomeFragment();
        mypageFragment = new MypageFragment();

        // Transaction 추가
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment, homeFragment).commit();

        // 홈 버튼
        navView.getMenu().getItem(1).setChecked(true);

        // Navigation Button 클릭 시 리스너
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bn_bookmark:
                        replaceFragment(bookmarkFragment);
                        break;
                    case R.id.bn_home:
                        replaceFragment(homeFragment);
                        break;
                    case R.id.bn_mypage:
                        replaceFragment(mypageFragment);
                        break;
                }
                return true;
            }
        });
    }

    // Navigation 메뉴마다 Fragment 바꾸기
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment).commit();
    }

    // 커스텀 토스트 생성하기
    public void makeCustomToast(String str, Context context) {
        LayoutInflater inflater = getLayoutInflater();
        View toastDesign = inflater.inflate(R.layout.toast_design, (ViewGroup)findViewById(R.id.toast_design_root));

        TextView txt = toastDesign.findViewById(R.id.txtToast);
        txt.setText(str);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastDesign);
        toast.show();
    }
}