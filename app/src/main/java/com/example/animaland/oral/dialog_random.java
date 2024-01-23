package com.example.animaland.oral;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;
import com.example.animaland.School.Language;


public class dialog_random extends Dialog {

    private Button match;
    private CheckedTextView tag_chat,tag_course;
    private RecyclerView recyclerView;
    private OralGridViewAdapter adapter;
    private View.OnClickListener listener;
    private boolean isChat=true;
    private Language language;
    private Language[] languageNames={Language.ENGLISH,Language.FRENCH,Language.JAPANESE,Language.OTHERS};

    public dialog_random(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.oral_dialog_random);

        //找到控件
        tag_chat=findViewById(R.id.tag_chat);
        tag_course=findViewById(R.id.tag_course);
        recyclerView=findViewById(R.id.rv_choose);
        match =findViewById(R.id.match);

        //设置recycleView
        GridLayoutManager glm = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(glm);
        adapter = new OralGridViewAdapter(getContext(),4);
        recyclerView.setAdapter(adapter);

        //设置监听
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChat=tag_chat.isChecked();

            }
        });
        match.setOnClickListener(listener);

        tag_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag_chat.toggle();
                tag_course.setChecked(tag_chat.isChecked()?false:true);
            }
        });

        tag_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag_course.toggle();
                tag_chat.setChecked(tag_course.isChecked()?false:true);
            }
        });


        adapter.setOnItemClickListener(new OralGridViewAdapter.onGridItemClickListener() {
            @Override
            public void onGridItemClick(int position) {
                setOthersUnChecked(glm,position);//将其他设为未选中，实现单选
                language=languageNames[position];
            }
        });


    }

    public void setOnMatchListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public boolean isChat() {
        return tag_chat.isChecked();
    }

    public Language chooseLanguage() {
        return language;
    }

    private void setOthersUnChecked(GridLayoutManager manager,int position){
        for(int i=0;i<4;i++){
            CheckedTextView checkedTextView =manager.findViewByPosition(i).findViewById(R.id.ctv);
            if(i!=position)
                checkedTextView.setChecked(false);
        }
    }
}
