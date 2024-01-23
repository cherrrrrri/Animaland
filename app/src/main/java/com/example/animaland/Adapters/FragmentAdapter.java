package com.example.animaland.Adapters;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments=new ArrayList<>();
    private List<String> fragmentsTitles =new ArrayList<>();
    public FragmentAdapter(@NonNull FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }
    public FragmentAdapter(@NonNull FragmentManager fm,List<Fragment> fragments,List<String> fragmentTitles) {
        super(fm);
        this.fragments=fragments;
        this.fragmentsTitles=fragmentTitles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public void destroyItem(ViewGroup container, int position, Object object)
    {super.destroyItem(container, position, object);}

    public CharSequence getPageTitle(int position)
    {if(fragmentsTitles!=null)
        return fragmentsTitles.get(position);
    else
        return "";}
}


