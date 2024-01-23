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

public class ornamentFragment extends Fragment {
    private ImageView ornamentlock1;
    private ImageView ornamentlock2;
    private ImageView ornamentStroke1;/*仅有被锁住的时候才会显示边框*/
    private ImageView ornamentStroke2;
    private TextView buy;
    private int chosen=0;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.ornament_fragment, null);
        ornamentlock1=view.findViewById(R.id.ornament_lock1);
        ornamentlock2=view.findViewById(R.id.ornament_lock2);

        ornamentStroke1=view.findViewById(R.id.ornament_stroke1);
        ornamentStroke2=view.findViewById(R.id.ornament_stroke2);

        buy=view.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*需加判断金币数量够不够的代码和是否已经解锁*/
                Toast.makeText(getContext(),"解锁成功",Toast.LENGTH_SHORT).show();
                if(chosen==1){
                    ornamentlock1.setVisibility(View.INVISIBLE);
                    ornamentStroke1.setVisibility(View.INVISIBLE);}
                if(chosen==2){
                    ornamentlock2.setVisibility(View.INVISIBLE);
                    ornamentStroke2.setVisibility(View.INVISIBLE);}

            }
        });
        ornamentlock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==1) {
                    ornamentStroke1.setVisibility(View.INVISIBLE);
                    chosen=5;/*5代表再次选中1*/
                }
                if(chosen==0||chosen==2) {
                    ornamentStroke1.setVisibility(View.VISIBLE);
                    ornamentStroke2.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(), "未解锁", Toast.LENGTH_SHORT).show();
                    chosen = 1;
                }
                if(chosen==5)
                    chosen=0;
            }
        });
        ornamentlock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen==2) {
                    ornamentStroke2.setVisibility(View.INVISIBLE);
                    chosen=6;/*6代表再次选中2*/
                }
                if(chosen==0||chosen==1) {
                    ornamentStroke2.setVisibility(View.VISIBLE);
                    ornamentStroke1.setVisibility(View.INVISIBLE);

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
