package com.example.proj1_30.fragmentclass;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proj1_30.MypageEditActivity;
import com.example.proj1_30.R;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_MYPAGE_EDIT = 1025;

    private String mParam1;
    private String mParam2;

    private ImageView imgMyPage;
    private LinearLayout layoutMyPageEdit;
    private TextView txtMypageName, txtMypageSex, txtMypageAge, txtMypageDwellings, txtmypageEmail;

    private String userName, userEmail, userSex, userDwellings;
    private Integer userAge;

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

        // Textview
        txtMypageName = (TextView)view.findViewById(R.id.txtMypageName);
        txtMypageSex = (TextView)view.findViewById(R.id.txtMypageSex);
        txtMypageAge = (TextView)view.findViewById(R.id.txtMypageAge);
        txtMypageDwellings = (TextView)view.findViewById(R.id.txtMypageDwellings);
        txtmypageEmail = (TextView)view.findViewById(R.id.txtmypageEmail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutMyPageEdit:
                // MypageEditActivity 호출
                Intent intent = new Intent(getActivity(), MypageEditActivity.class);
                startActivityForResult(intent, REQUEST_MYPAGE_EDIT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_MYPAGE_EDIT) {
            if(resultCode != Activity.RESULT_OK)
                return;

            Log.d("AAAA", "ok");
            userName = data.getExtras().getString("userName");
            txtMypageName.setText(userName + " 님");

            userEmail = data.getExtras().getString("userEmail");
            txtmypageEmail.setText(userEmail);

            userAge = data.getExtras().getInt("userAge");
            txtMypageAge.setText(userAge.toString() + " 세");

            userSex = data.getExtras().getString("userSex");
            txtMypageSex.setText(userSex);

            userDwellings = data.getExtras().getString("userDwellings");
            txtMypageDwellings.setText(userDwellings);
        }
    }
}