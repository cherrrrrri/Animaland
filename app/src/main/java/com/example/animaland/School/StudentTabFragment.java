package com.example.animaland.School;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animaland.Island;
import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;

import java.sql.SQLException;


public class StudentTabFragment extends Fragment {

    private TextView courseBuy,coin;
    private EditText name;
    private ImageView edit;
    private Button back,exit;
    boolean isEdited=false;//记录是否为编辑状态
    private DatabaseHelper db = new DatabaseHelper();

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0) {
                coin.setText((String)msg.obj);
            }else if(msg.what==1){
                name.setText((String)msg.obj);
            }else if(msg.what==2) {
                courseBuy.setText((String)msg.obj);
            }


        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.account_student,container,false);


        //找到控件
        coin=root.findViewById(R.id.coin);
        name=root.findViewById(R.id.name);
        courseBuy=root.findViewById(R.id.courseBuy);
        back=root.findViewById(R.id.back_account);
        exit=root.findViewById(R.id.exit_account);
        edit=root.findViewById(R.id.edit_data);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //设置文字
                try {
                    Message msg1 = new Message();
                    msg1.what = 0;
                    msg1.obj = db.getCoin()+"";
                    handler.sendMessage(msg1);

                    Message msg2 = new Message();
                    msg2.what = 1;
                    msg2.obj = db.getName();
                    handler.sendMessage(msg2);

                    Message msg3 = new Message();
                    msg3.what = 2;
                    msg3.obj = db.getCourseBuy()+"";
                    handler.sendMessage(msg3);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }

            }
        };
        cachedThreadPool.execute(runnable);
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);


        //设置编辑图标的监听事件
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdited){
                    //更新为不可编辑状态
                    if(name.getText().toString()==""){
                        Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.setName(name.getText().toString());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (java.lang.InstantiationException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        cachedThreadPool.execute(runnable1);
                        isEdited=false;
                        name.setFocusable(false);
                        name.setFocusableInTouchMode(false);
                        edit.setImageResource(R.drawable.edit);
                    }
                }
                else{
                    //更新为可编辑状态
                    isEdited=true;
                    name.setFocusable(true);
                    name.setFocusableInTouchMode(true);
                    Toast.makeText(getContext(), "单击名字即可更改", Toast.LENGTH_SHORT).show();
                    edit.setImageResource(R.drawable.save);
                }
            }
        });


        //设置返回的监听事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Island.class);
                startActivity(i);
            }
        });

        //设置退出的监听事件
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }


    public void onDestroyView() {

        super.onDestroyView();
    }
}
