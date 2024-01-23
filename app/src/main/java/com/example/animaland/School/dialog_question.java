package com.example.animaland.School;

import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.animaland.R;
//import com.example.myapplication.R;

//import meow.bottomnavigation.UtilsKt;

public class dialog_question extends Dialog {
    private EditText question_hint;
    private ImageView close;
    private Button send;
    private View.OnClickListener ListenerOfClose;
    private View.OnClickListener ListenerOfButton;

    public void clear()
    {
        question_hint.setText("");
    }

    public dialog_question setClose(View.OnClickListener listener) {
        this.ListenerOfClose=listener;
        return this;
    }

    public dialog_question setButton(View.OnClickListener listener) {
        this.ListenerOfButton=listener;
        return this;
    }

    public String getQuestion(){
        return question_hint.getText().toString();
    }
    public dialog_question(@NonNull Context context,int themeID) {
        super(context, themeID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.question);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);
        p.height=(int)(size.y*0.4);
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        question_hint=findViewById(R.id.question_hint);
        close=findViewById(R.id.close);
        send=findViewById(R.id.btn_send);
        close.setOnClickListener(ListenerOfClose);
        send.setOnClickListener(ListenerOfButton);
    }
}
