package com.example.animaland;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.room.ShellStudyRoom;

public class Guide_shell extends AppCompatActivity {
    private Button guide;
    private Button mBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buding_beach);
        guide=findViewById(R.id.guide_shell);
        mBack=findViewById(R.id.room_back);

        SharedPreferences preferences = getSharedPreferences("count1", Context.MODE_PRIVATE);
        boolean count1 = preferences.getBoolean("count1",true);
        if(count1==true) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)//最终版本需要删掉！！
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(guide)
                            .setLayoutRes(R.layout.shell_guide))
                    .show();
        }

        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("count1",false);
        editor.commit();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Guide_shell.this, Map.class);
                startActivity(intent);
            }
        });

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Guide_shell.this, ShellStudyRoom.class);
                startActivity(intent);
            }
        });
    }

    public void onStop() {
        super.onStop();
    }
}
