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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;


public class dialog_download extends Dialog {
    private Button yes;
    private View.OnClickListener listenerOfYes,listenerOfHint;
    private TextView upload_hint;

    public dialog_download(@NonNull Context context) {
        super(context);
    }

    public dialog_download(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_download setYes(View.OnClickListener listener) {
        this.listenerOfYes=listener;
        return this;
    }

    public dialog_download setHint(View.OnClickListener listener) {
        this.listenerOfHint=listener;
        return this;
    }
    public void setHint(String s) {
        upload_hint.setText(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_download);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);//dialog将占据屏幕的40%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        yes=findViewById(R.id.yes);
        upload_hint=findViewById(R.id.upload_hint);

        //设置监听
        yes.setOnClickListener(listenerOfYes);
        upload_hint.setOnClickListener(listenerOfHint);
    }

}
