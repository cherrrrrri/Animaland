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

public class neckFragment extends Fragment {
    private ImageView necklock1;
    private ImageView necklock2;
    private ImageView neckStroke1;/*仅有被锁住的时候才会显示边框*/
    private ImageView neckStroke2;
    private TextView buy;
    private int chosen=0;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.neck_fragment, null);
        necklock1=view.findViewById(R.id.neck_lock1);
        necklock2=view.findViewById(R.id.neck_lock2);

        neckStroke1=view.findViewById(R.id.neck_stroke1);
        neckStroke2=view.findViewById(R.id.neck_stroke2);

        buy=view.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*需加判断金币数量够不够的代码和是否已经解锁*/
                Toast.makeText(getContext(),"解锁成功",Toast.LENGTH_SHORT).show();
                if(chosen==1){
                    necklock1.setVisibility(View.INVISIBLE);
                    neckStroke1.setVisibility(View.INVISIBLE);}
                if(chosen==2){
                    necklock2.setVisibility(View.INVISIBLE);
                    neckStroke2.setVisibility(View.INVISIBLE);}

            }
        });
        necklock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==1) {
                    neckStroke1.setVisibility(View.INVISIBLE);
                    chosen=5;/*5代表再次选中1*/
                }
                if(chosen==0||chosen==2) {
                    neckStroke1.setVisibility(View.VISIBLE);
                    neckStroke2.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(), "未解锁", Toast.LENGTH_SHORT).show();
                    chosen = 1;
                }
                if(chosen==5)
                    chosen=0;
            }
        });
        necklock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==2) {
                    neckStroke2.setVisibility(View.INVISIBLE);
                    chosen=6;/*6代表再次选中2*/
                }
                if(chosen==0||chosen==1) {
                    neckStroke2.setVisibility(View.VISIBLE);
                    neckStroke1.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(), "未解锁", Toast.LENGTH_SHORT).show();
                    chosen = 2;
                }
                if(chosen==6)
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
