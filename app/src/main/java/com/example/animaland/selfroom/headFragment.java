package com.example.animaland.selfroom;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animaland.R;

public class headFragment extends Fragment {
    private ImageView headlock1;
    private ImageView headlock2;
    private ImageView headlock3;
    private ImageView headlock4;
    private ImageView headStroke1;/*仅有被锁住的时候才会显示边框*/
    private ImageView headStroke2;
    private ImageView headStroke3;
    private ImageView headStroke4;
    private TextView buy;
    private int chosen=0;
    private View.OnClickListener ListenerOfBuy;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.head_fragment, null);
        headlock1=view.findViewById(R.id.head_lock1);
        headlock2=view.findViewById(R.id.head_lock2);
        headlock3=view.findViewById(R.id.head_lock3);
        headlock4=view.findViewById(R.id.head_lock4);

        headStroke1=view.findViewById(R.id.head_stroke1);
        headStroke2=view.findViewById(R.id.head_stroke2);
        headStroke3=view.findViewById(R.id.head_stroke3);
        headStroke4=view.findViewById(R.id.head_stroke4);



        buy=view.findViewById(R.id.buy);

        buy.setOnClickListener(ListenerOfBuy);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*需加判断金币数量够不够的代码和是否已经解锁*/
                Toast.makeText(getContext(),"解锁成功",Toast.LENGTH_SHORT).show();
                if(chosen==1)
                { headlock1.setVisibility(View.INVISIBLE);
                    headStroke1.setVisibility(View.INVISIBLE);}
                if(chosen==2)
                {headlock2.setVisibility(View.INVISIBLE);
                    headStroke2.setVisibility(View.INVISIBLE);}
                if(chosen==3)
                { headlock3.setVisibility(View.INVISIBLE);
                    headStroke3.setVisibility(View.INVISIBLE);}
                if(chosen==4)
                { headlock4.setVisibility(View.INVISIBLE);
                    headStroke4.setVisibility(View.INVISIBLE);}
            }
        });

        headlock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==1) {
                    headStroke1.setVisibility(View.INVISIBLE);
                    chosen=5;/*5代表再次选中1*/
                }
                if(chosen==0||chosen==2||chosen==3||chosen==4) {
                    headStroke1.setVisibility(View.VISIBLE);
                    headStroke2.setVisibility(View.INVISIBLE);
                    headStroke3.setVisibility(View.INVISIBLE);
                    headStroke4.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "未解锁", Toast.LENGTH_SHORT).show();
                    chosen = 1;
                }
                if(chosen==5)
                    chosen=0;
            }
        });
        headlock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==2) {
                    headStroke2.setVisibility(View.INVISIBLE);
                    chosen=6;/*6代表再次选中2*/
                }
                if(chosen==0||chosen==1||chosen==3||chosen==4) {
                    headStroke2.setVisibility(View.VISIBLE);
                    headStroke1.setVisibility(View.INVISIBLE);
                    headStroke3.setVisibility(View.INVISIBLE);
                    headStroke4.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "未解锁", Toast.LENGTH_SHORT).show();
                    chosen = 2;
                }
                if(chosen==6)
                    chosen=0;
            }
        });
        headlock3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==3) {
                    headStroke3.setVisibility(View.INVISIBLE);
                    chosen=7;/*7代表再次选中3*/
                }
                if(chosen==0||chosen==1||chosen==2||chosen==4) {
                    headStroke3.setVisibility(View.VISIBLE);
                    headStroke2.setVisibility(View.INVISIBLE);
                    headStroke1.setVisibility(View.INVISIBLE);
                    headStroke4.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"未解锁",Toast.LENGTH_SHORT).show();
                    chosen=3;}
                if(chosen==7)
                    chosen=0;
            }
        });
        headlock4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==4) {
                    headStroke4.setVisibility(View.INVISIBLE);
                    chosen=8;/*8代表再次选中4*/
                }
                if(chosen==0||chosen==1||chosen==3||chosen==2) {
                    headStroke4.setVisibility(View.VISIBLE);
                    headStroke2.setVisibility(View.INVISIBLE);
                    headStroke3.setVisibility(View.INVISIBLE);
                    headStroke1.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"未解锁",Toast.LENGTH_SHORT).show();
                    chosen=4;}
                if(chosen==8)
                    chosen=0;
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}