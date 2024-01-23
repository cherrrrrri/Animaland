package com.example.animaland.School;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.animaland.PassThroughButton;
import com.example.animaland.R;

public class Student_chair extends AppCompatActivity {

    private ImageView back;
    private PassThroughButton chair,ipad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_student_chair);

        //找到控件
        back=findViewById(R.id.back);
        chair=findViewById(R.id.student_chair);
        ipad=findViewById(R.id.student_ipad);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Student_chair.this,School.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Student_chair.this,chair, "chair");
                startActivity(intent, optionsCompat.toBundle());
            }
        });

        ipad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Student_chair.this,Student.class);
                startActivity(intent);
            }
        });
    }

    public void onStop() {
        super.onStop();
    }
}