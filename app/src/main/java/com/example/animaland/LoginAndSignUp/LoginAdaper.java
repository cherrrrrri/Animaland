package com.example.animaland.LoginAndSignUp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LoginAdaper extends FragmentPagerAdapter {

    private Context context;
    private LoginTabFragment loginTabFragment;
    private SignUpTabFragment signUpTabFragment;
    private ViewPager.OnPageChangeListener listener;
    public int totalTabs;
    public LoginAdaper(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                loginTabFragment=new LoginTabFragment();
                return loginTabFragment;
            case 1:
                signUpTabFragment=new SignUpTabFragment();
                return signUpTabFragment;
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
                return "登录";
            case 1:
                signUpTabFragment=new SignUpTabFragment();
                return "注册";
            default:
                return null;
        }
    }
}