package com.example.animaland.School;


import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;

import com.example.animaland.R;


public class dialog_questionNum extends Dialog {

    EditText number;
    String str;
    Button btn;



    public dialog_questionNum(@NonNull Context context, int themeID) {
        super(context, themeID);
    }


    private View.OnClickListener ListenerOfButton;

    public dialog_questionNum setBtn(View.OnClickListener listener) {
        this.ListenerOfButton=listener;
        return this;
    }
    public void getEditText()
    {
        str = number.getText().toString();

    }
    public String getText()
    {
        return str;

    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.question_num);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.8);
        p.height=(int)(size.y*0.9);
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        number=findViewById(R.id.number);
        btn=findViewById(R.id.btn);
        btn.setOnClickListener(ListenerOfButton);




    }
}
