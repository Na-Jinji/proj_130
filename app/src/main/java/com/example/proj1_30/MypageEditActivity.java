package com.example.proj1_30;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MypageEditActivity extends AppCompatActivity {
    private ImageView imgMyPageEdit;
    private EditText editUserName, editUserEmail, editUserAge;
    private RadioGroup rGroupUserSex;
    private RadioButton rdoMale, rdoFemale;
    private String strSex = "";
    private String strDwellings = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_edit);

        // 이미지뷰 동그랗게
        imgMyPageEdit = (ImageView)findViewById(R.id.imgMyPageEdit);
        imgMyPageEdit.setBackground(new ShapeDrawable(new RoundRectShape(new float[] {100, 100, 100, 100, 100, 100, 100, 100}, null, new float[] {100, 100, 100, 100, 100, 100, 100, 100})));
        imgMyPageEdit.setClipToOutline(true);

        editUserName = (EditText)findViewById(R.id.editUserName);
        editUserEmail = (EditText)findViewById(R.id.editUserEmail);
        editUserAge = (EditText)findViewById(R.id.editUserAge);
        rdoFemale = (RadioButton)findViewById(R.id.rdoFemale);
        rdoMale = (RadioButton)findViewById(R.id.rdoMale);

        rGroupUserSex = (RadioGroup)findViewById(R.id.rGroupUserSex);
        rGroupUserSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rdoFemale)
                    strSex = "여자";
                else
                    strSex = "남자";
            }
        });

        // 사용자 프로필 초기화
        Intent intent = getIntent();
        editUserName.setText(intent.getExtras().getString("userName"));
        editUserEmail.setText(intent.getExtras().getString("userEmail"));
        //editUserDwelling.setText(intent.getExtras().getString("userDwellings"));
        editUserAge.setText(Integer.toString(intent.getIntExtra("userAge", 0)));

        strSex = intent.getExtras().getString("userSex");
        if(strSex.equals("여자"))
            rdoFemale.setChecked(true);
        else
            rdoMale.setChecked(true);

        // 스피너 어댑터
        Spinner s = (Spinner)findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                strDwellings = (String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelMyPageEdit: // 취소
                finish();
                break;
            case R.id.doneMyPageEdit: // 완료
                String userName = editUserName.getText().toString();
                String userEmail = editUserEmail.getText().toString();
                String userDwellings = strDwellings;
                int userAge = Integer.parseInt(editUserAge.getText().toString());

                Intent intent = new Intent();
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userAge", userAge);
                intent.putExtra("userDwellings", userDwellings);
                intent.putExtra("userSex", strSex);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}