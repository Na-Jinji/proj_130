package com.example.proj1_30;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MypageEditActivity extends AppCompatActivity {
    private ImageView imgMyPageEdit;
    private EditText editUserName, editUserEmail, editUserAge, editUserDwelling;
    private RadioGroup rGroupUserSex;
    private String strSex = "";

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
        editUserDwelling = (EditText)findViewById(R.id.editUserDwelling);

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
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelMyPageEdit: // 취소
                finish();
                break;
            case R.id.doneMyPageEdit: // 완료
                String userName = editUserName.getText().toString();
                String userEmail = editUserEmail.getText().toString();
                String userDwellings = editUserDwelling.getText().toString();
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