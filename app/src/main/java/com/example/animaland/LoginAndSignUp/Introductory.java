package com.example.animaland.LoginAndSignUp;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.animaland.R;
import com.example.animaland.tool.LiquidFragment;
import com.jem.liquidswipe.LiquidSwipeViewPager;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;


public class Introductory extends AppCompatActivity implements CancelAdapt {

    ImageView  logo, bg;
    TextView name;
    LottieAnimationView lottieAnimationView;
    final List<Fragment> startfragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_introductory);

        //找到控件
        name=findViewById(R.id.app_name);
        logo = findViewById(R.id.app_logo);
        bg = findViewById(R.id.app_bg);
        lottieAnimationView = findViewById(R.id.lottie);
        //enterLand=findViewById(R.id.btn_enterLand);
        //lottieAnimationView2=findViewById(R.id.lottie2);

        /*lottieAnimationView.setAnimation(R.raw.animal);
        lottieAnimationView.playAnimation();*/

        name.animate().translationY(2200).setDuration(1000).setStartDelay(2000);
        logo.animate().translationY(2200).setDuration(1000).setStartDelay(2000);
        bg.animate().translationY(-3500).setDuration(1000).setStartDelay(2000);
        lottieAnimationView.animate().translationY(2200).setDuration(2000).setStartDelay(2000);

        //液体过渡
        LiquidSwipeViewPager pager =  findViewById(R.id.pager);
        startfragments.add(new Fragment(R.layout.liquid1));
        startfragments.add(new Fragment(R.layout.liquid2));
        startfragments.add(new LiquidFragment(R.layout.liquid3));
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return startfragments.get(position);
            }

            @Override
            public int getCount() {
                return startfragments.size();
            }
        });

    }
}