package com.example.proj1_30;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proj1_30.fragmentclass.BookmarkFragment;
import com.example.proj1_30.fragmentclass.HomeFragment;
import com.example.proj1_30.fragmentclass.MypageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navView;

    // Fragment
    BookmarkFragment bookmarkFragment;
    HomeFragment homeFragment;
    MypageFragment mypageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
}