package com.example.animaland.Dialogs;


import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_dictionary extends Dialog {
    public EditText dictionary_hint;
    private ImageView close;
    private TextView textView0;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private String str;
    private Spinner spinner1;
    private Spinner spinner2;

    public static void showInputWindow(EditText editText)
    {editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager=(InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText,0);
    }

    private TextView.OnEditorActionListener ListenerOfEditText;
    private View.OnClickListener ListenerOfClose;
    private AdapterView.OnItemSelectedListener ListenerOfSpinner1;
    private AdapterView.OnItemSelectedListener ListenerOfSpinner2;

    public dialog_dictionary setText(TextView.OnEditorActionListener listener) {
        this.ListenerOfEditText=listener;
        return this;
    }
    public dialog_dictionary setSpinner1(AdapterView.OnItemSelectedListener listener1)
    {this.ListenerOfSpinner1=listener1;
        return this;}
    public dialog_dictionary setSpinner2(AdapterView.OnItemSelectedListener listener2)
    {this.ListenerOfSpinner2=listener2;
        return this;}
    public dialog_dictionary setClose(View.OnClickListener listener) {
        this.ListenerOfClose=listener;

        return this;
    }
    public void clear()
    {
        dictionary_hint.setText("");
    }
    public void changeText0()
    {str=dictionary_hint.getText().toString();
        textView0.setText("查询单词："+str);}
    public void changeText1()
    {textView1.setText("发音音标：");}
    public void changeText2(String inter)
    {textView2.setText("翻译结果："+inter);}
    public void changeText3()
    {textView3.setText("例句：");}

    public dialog_dictionary(@NonNull Context context,int themeID) {
        super(context, themeID);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dictionary);

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
        dictionary_hint=findViewById(R.id.dictionary_hint);
        close=findViewById(R.id.close);
        textView1=findViewById(R.id.音标);
        textView2=findViewById(R.id.翻译);
        textView3=findViewById(R.id.例句);
        textView0=findViewById(R.id.单词);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);



        dictionary_hint.setOnEditorActionListener(ListenerOfEditText);
        close.setOnClickListener(ListenerOfClose);
        spinner1.setOnItemSelectedListener(ListenerOfSpinner1);
        spinner2.setOnItemSelectedListener(ListenerOfSpinner2);
    }
}
