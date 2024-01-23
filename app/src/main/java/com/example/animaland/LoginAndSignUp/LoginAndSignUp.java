package com.example.animaland.LoginAndSignUp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.animaland.R;
import com.google.android.material.tabs.TabLayout;

import me.jessyan.autosize.internal.CancelAdapt;

public class LoginAndSignUp extends AppCompatActivity implements CancelAdapt {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ImageView background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_loginandsignup);

        //找到控件
        tabLayout=findViewById(R.id.tl_FocusAndTodoList);
        viewPager=findViewById(R.id.vp_LoginAndSignUp);
        background=findViewById(R.id.bg_LoginAndSignUp);

        //设置页面滑动
        tabLayout.addTab(tabLayout.newTab().setText("登录"));//增添页面标签
        tabLayout.addTab(tabLayout.newTab().setText("注册"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdaper adapter=new LoginAdaper(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("登录")) {
                    LoginTabFragment.anim();
                }
                if(tab.getText().equals("注册")){
                    SignUpTabFragment.anim();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
