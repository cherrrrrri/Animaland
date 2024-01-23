package com.example.animaland;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.animaland.Adapters.MyAccountAdapter;
import com.google.android.material.tabs.TabLayout;

public class MyAccount extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount);


        //找到控件
        tabLayout=findViewById(R.id.tl_MyAccount);
        viewPager=findViewById(R.id.vp_MyAccount);



        //设置页面标签
        tabLayout.addTab(tabLayout.newTab().setText("我是学生"));
        tabLayout.addTab(tabLayout.newTab().setText("我是老师"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final MyAccountAdapter accountAdapter=new MyAccountAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(accountAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("我是学生")) {

                }
                if(tab.getText().equals("我是老师")){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


    }

    public void onStop() {
        super.onStop();
    }
}