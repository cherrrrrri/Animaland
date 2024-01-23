package com.example.animaland;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.room.TreeHouseStudyRoom;
import com.example.animaland.room.UndergroundStudyRoom;

import org.yaml.snakeyaml.nodes.Tag;

public class Map extends Activity {

    private PassThroughButton undergroundStudyRoom,treeHouse,shellRoom,below,school;
    private Button mBack;
    private Tag t;
    private ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tab_fragment);

        undergroundStudyRoom=findViewById(R.id.room_cave);
        treeHouse=findViewById(R.id.room_treehouse);
        shellRoom=findViewById(R.id.room_shell);
        below=findViewById(R.id.room_below);
        school=findViewById(R.id.room_school);
        mBack=findViewById(R.id.back);
        //logout=findViewById(R.id.logout);

        setListeners();

        SharedPreferences preferences = getSharedPreferences("count6", Context.MODE_PRIVATE);
        boolean count6= preferences.getBoolean("count6",true);
        if(count6==true) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)//最终版本需要删掉！！
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.mainland_guide))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.mainland_guide0))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.mainland_guide1))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.mainland_guide2))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.mainland_guide3))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.mainland_guide4))
                    .show();
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("count6",false);
            editor.commit();
        }

        /**below.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Toast.makeText(Map.this, "敬请期待", Toast.LENGTH_SHORT).show();
        }
        });**/

    }

    public void setListeners(){
        onClick onClick=new onClick();
        undergroundStudyRoom.setOnClickListener(onClick);
        treeHouse.setOnClickListener(onClick);
        shellRoom.setOnClickListener(onClick);
        mBack.setOnClickListener(onClick);
        //    logout.setOnClickListener(onClick);
    }

    private class onClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent=null;
            switch(view.getId()){
                case R.id.room_cave:
                    //跳转到地底自习室页面
                    intent=new Intent(Map.this, UndergroundStudyRoom.class);
                    break;
                case R.id.room_treehouse:
                    //跳转到树屋自习室页面
                    intent=new Intent(Map.this, TreeHouseStudyRoom.class);
                    break;
                case R.id.room_shell:
                    //跳转到贝壳自习室页面
                    intent=new Intent(Map.this, Guide_shell.class);
                    break;
                case R.id.back:
                    //跳转到轮播图
                    intent=new Intent(Map.this, Island.class);
                    break;
             /*   case R.id.logout:
                    User u = new User();
                    u.setTel("");
                    Toast.makeText(Map.this,"已经退出登录",Toast.LENGTH_SHORT).show();
                    intent=new Intent(Map.this,MainActivity.class);
                    break;*/

            }
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }
}