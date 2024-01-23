package com.example.animaland;


import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jem.liquidswipe.LiquidSwipeViewPager;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

public class GuideStart extends AppCompatActivity implements CancelAdapt {


    final List<Fragment> startfragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_introductory);
        LiquidSwipeViewPager pager = (LiquidSwipeViewPager) findViewById(R.id.pager);
        startfragments.add(new Fragment(R.layout.liquid1));
        startfragments.add(new Fragment(R.layout.liquid2));
        startfragments.add(new Fragment(R.layout.liquid3));
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

    public void onStop() {
        super.onStop();
    }
}