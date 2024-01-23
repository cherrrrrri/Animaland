package com.example.animaland.tool;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animaland.LoginAndSignUp.LoginAndSignUp;
import com.example.animaland.R;

public class LiquidFragment extends Fragment {
    private Button enterLand;

    public LiquidFragment(@LayoutRes int contentLayoutId){
        super(contentLayoutId);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view= (ViewGroup) inflater.inflate(R.layout.liquid3,container,false);
        enterLand=view.findViewById(R.id.btn_enterLand);

        enterLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转至登录页面
                Intent intent=new Intent(getActivity(), LoginAndSignUp.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
