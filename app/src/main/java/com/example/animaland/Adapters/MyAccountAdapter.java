package com.example.animaland.Adapters;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.animaland.School.StudentTabFragment;
import com.example.animaland.School.TeacherTabFragment;


public class MyAccountAdapter extends FragmentPagerAdapter {

    private Context context;
    private TeacherTabFragment teacherTabFragment;
    private StudentTabFragment studentTabFragment;
    public int totalTabs;

    public MyAccountAdapter(@NonNull FragmentManager fm,Context context,int totalTabs) {
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                studentTabFragment=new StudentTabFragment();
                return studentTabFragment;
            case 1:
                teacherTabFragment=new TeacherTabFragment();
                return teacherTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "我是学生";
            case 1:
                return "我是老师";
            default:
                return null;
        }
    }
}
