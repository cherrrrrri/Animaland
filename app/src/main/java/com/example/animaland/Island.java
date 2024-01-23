package com.example.animaland;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animaland.LoginAndSignUp.LoginAndSignUp;
import com.example.animaland.School.School;
import com.example.animaland.chatgpt.GPTMainActivity;
import com.example.animaland.oral.Oral;
import com.example.animaland.tool.User;
import com.tencent.imsdk.v2.V2TIMManager;

public class Island extends AppCompatActivity {
    private ImageView back;
    private ImageView account;
    private Image3DView image1,image2,image3;
    private ImageView gpt;
    //gpt检测的按钮

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.island);
        back=findViewById(R.id.back3);
        account=findViewById(R.id.account);
        image1=findViewById(R.id.image1);//自习室
        image2=findViewById(R.id.image2);//大讲堂
        image3=findViewById(R.id.image3);//口语
        gpt=findViewById(R.id.chat_gpt);


        Intent intent1=new Intent(this, LoginAndSignUp.class);
        Intent intent2=new Intent(this,MyAccount.class);
        Intent intent3=new Intent(this,Map.class);
        Intent intent4=new Intent(this, School.class);
        Intent intent5=new Intent(this, Oral.class);
        Intent intent6=new Intent(this, GPTMainActivity.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
         public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent1);
                        V2TIMManager.getInstance().logout(null);
                        V2TIMManager.getInstance().unInitSDK();
                        Toast.makeText(Island.this,"退出登录",Toast.LENGTH_SHORT).show();
                        User user = new User();
                        user.tel="";
                    }

                }, 700);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent2);
                    }
                }, 700);
            }
        });
        gpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent6);
                    }
                }, 700);
            }


        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent3);
                    }
                }, 700);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent4);
                    }
                }, 700);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent5);
                    }
                }, 700);
            }
        });
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
