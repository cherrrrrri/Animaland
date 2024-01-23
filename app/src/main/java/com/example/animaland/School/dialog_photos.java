package com.example.animaland.School;

import static android.view.Gravity.BOTTOM;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_photos extends Dialog {
    private Button camera,album,cancel;
    private View.OnClickListener listenerOfCamera,listenerOfAlbum,listenerOfCancel;

    public dialog_photos(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_photos setCamera(View.OnClickListener listener) {
        listenerOfCamera=listener;
        return this;
    }

    public dialog_photos setAlbum(View.OnClickListener listener) {
        listenerOfAlbum=listener;
        return this;
    }

    public dialog_photos setCancel(View.OnClickListener listener) {
        listenerOfCancel=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_photos);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.height=(int)(size.y*0.45);//dialog将占据屏幕的45%
        getWindow().setGravity(BOTTOM);//底部
        getWindow().setWindowAnimations(R.style.animStyle);//设置进出效果
        getWindow().setAttributes(p);

        //找到控件
        camera=findViewById(R.id.camera);
        album=findViewById(R.id.album);
        cancel=findViewById(R.id.cancel);

        //设置监听
        camera.setOnClickListener(listenerOfCamera);
        album.setOnClickListener(listenerOfAlbum);
        cancel.setOnClickListener(listenerOfCancel);

        //出现动画
        /*camera.animate().translationY((int)(-size.y*0.45)).setDuration(1000).start();
        album.animate().translationY((int)(-size.y*0.45)).setDuration(1000).start();
        cancel.animate().translationY((int)(-size.y*0.45)).setDuration(1000).start();
        */

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}