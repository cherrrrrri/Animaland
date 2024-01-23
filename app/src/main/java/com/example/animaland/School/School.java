package com.example.animaland.School;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.Island;
import com.example.animaland.PassThroughButton;
import com.example.animaland.R;

public class School extends AppCompatActivity {

    private PassThroughButton desk,chair;
    private ImageView back;

    private static boolean first_school=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_map);

        //找到控件
        desk=findViewById(R.id.teacher_desk);
        chair=findViewById(R.id.student_chair);
        back=findViewById(R.id.back);


        if(first_school==false) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.school_guide))
                    .addGuidePage(GuidePage.newInstance().addHighLight(chair))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.school_guide2))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.school_guide3))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.school_guide4))
                    .addGuidePage(GuidePage.newInstance().addHighLight(desk))
                    .show();
            first_school=true;
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(School.this, Island.class);
                startActivity(intent);
            }
        });

        desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(School.this, Teacher.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(School.this, desk, getString(R.string.desk));
                startActivity(intent, optionsCompat.toBundle());
            }
        });

        chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(School.this, Student_chair.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(School.this, chair,"chair");
                startActivity(intent, optionsCompat.toBundle());
            }
        });
    }

    public void onStop() {
        super.onStop();
    }
}