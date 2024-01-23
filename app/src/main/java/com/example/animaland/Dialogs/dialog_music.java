package com.example.animaland.Dialogs;


import static android.view.Gravity.CENTER;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.animaland.R;


public class dialog_music extends Dialog {
    ObjectAnimator mMusicAnimation1;
    ObjectAnimator mMusicAnimation2;
    ObjectAnimator mMusicAnimation3;
    ObjectAnimator mMusicAnimation4;
    private ImageView i11,i22,i33,i44,i1,i2,i3,i4,close;

    int firstPlay1=1;
    int firstPlay2=1;
    int firstPlay3=1;
    int firstPlay4=1;
    int isPlaying1=0;
    int isPlaying2=0;
    int isPlaying3=0;
    int isPlaying4=0;

    public int getState1(){return isPlaying1;}
    public int getState2(){return isPlaying2;}
    public int getState3(){return isPlaying3;}
    public int getState4(){return isPlaying4;}


    private View.OnClickListener ListenerOfi11,ListenerOfi22,ListenerOfi33,ListenerOfi44,ListenerOfClose;

    public dialog_music(@NonNull Context context,int themeID) {
        super(context, themeID);
    }
    public dialog_music setClose(View.OnClickListener listener) {
        this.ListenerOfClose=listener;
        return this;
    }

    public dialog_music setI11(View.OnClickListener listener) {
        this.ListenerOfi11=listener;
        return this;
    }

    public dialog_music setI22(View.OnClickListener listener) {
        this.ListenerOfi22=listener;
        return this;
    }
    public dialog_music setI33(View.OnClickListener listener) {
        this.ListenerOfi33=listener;
        return this;
    }
    public dialog_music setI44(View.OnClickListener listener) {
        this.ListenerOfi44=listener;
        return this;
    }

    public void change_image1(){
        isPlaying1=1;
        i11.setImageResource(R.drawable.ic_baseline_stop_24);
        if(firstPlay1==1)
            animation1(1000);
        else
            mMusicAnimation1.resume();
        if(isPlaying2==1)
            change_image4();
        if(isPlaying3==1)
            change_image6();
        if(isPlaying4==1)
            change_image8();

        firstPlay1=0;

    }
    public void change_image2(){

        i11.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        mMusicAnimation1.pause();
        isPlaying1=0;
    }
    public void change_image4(){

        i22.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        isPlaying2=0;
        mMusicAnimation2.pause();
    }

    public void change_image3(){
        i22.setImageResource(R.drawable.ic_baseline_stop_24);
        isPlaying2=1;
        if(firstPlay2==1)
            animation2(1000);
        else
            mMusicAnimation2.resume();
        if(isPlaying1==1)
            change_image2();
        if(isPlaying3==1)
            change_image6();
        if(isPlaying4==1)
            change_image8();
        firstPlay2=0;

    }
    public void change_image6(){

        i33.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        isPlaying3=0;
        mMusicAnimation3.pause();
    }

    public void change_image5(){
        i33.setImageResource(R.drawable.ic_baseline_stop_24);
        isPlaying3=1;
        if(firstPlay3==1)
            animation3(1000);
        else
            mMusicAnimation3.resume();
        if(isPlaying2==1)
            change_image4();
        if(isPlaying1==1)
            change_image2();
        if(isPlaying4==1)
            change_image8();
        firstPlay3=0;

    }
    public void change_image8(){

        i44.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        isPlaying4=0;
        mMusicAnimation4.pause();
    }

    public void change_image7(){
        i44.setImageResource(R.drawable.ic_baseline_stop_24);
        isPlaying4=1;
        if(firstPlay4==1)
            animation4(1000);
        else
            mMusicAnimation4.resume();
        if(isPlaying2==1)
            change_image4();
        if(isPlaying3==1)
            change_image6();
        if(isPlaying1==1)
            change_image2();
        firstPlay4=0;

    }

    public void animation1(int mDuration) {

        mMusicAnimation1 =ObjectAnimator.ofFloat(i1, "rotation", 0f,360f);

        mMusicAnimation1.setDuration(5000);

        mMusicAnimation1.setInterpolator(new LinearInterpolator());//not stop

        mMusicAnimation1.setRepeatCount(-1);//set repeat time forever


        mMusicAnimation1.start();
    }
    public void animation2(int mDuration) {

        mMusicAnimation2 =ObjectAnimator.ofFloat(i2, "rotation", 0f,360f);

        mMusicAnimation2.setDuration(5000);

        mMusicAnimation2.setInterpolator(new LinearInterpolator());//not stop

        mMusicAnimation2.setRepeatCount(-1);//set repeat time forever


        mMusicAnimation2.start();
    }

    public void animation3(int mDuration) {

        mMusicAnimation3 =ObjectAnimator.ofFloat(i3, "rotation", 0f,360f);

        mMusicAnimation3.setDuration(5000);

        mMusicAnimation3.setInterpolator(new LinearInterpolator());//not stop

        mMusicAnimation3.setRepeatCount(-1);//set repeat time forever


        mMusicAnimation3.start();
    }
    public void animation4(int mDuration) {

        mMusicAnimation4 =ObjectAnimator.ofFloat(i4, "rotation", 0f,360f);

        mMusicAnimation4.setDuration(5000);

        mMusicAnimation4.setInterpolator(new LinearInterpolator());//not stop

        mMusicAnimation4.setRepeatCount(-1);//set repeat time forever


        mMusicAnimation4.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.music);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.7);//dialog将占据屏幕的70%
        p.height=(int)(size.y*0.7);
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        i11=findViewById(R.id.i11);
        i22=findViewById(R.id.i22);
        i33=findViewById(R.id.i33);
        i44=findViewById(R.id.i44);
        i1=findViewById(R.id.i1);
        i2=findViewById(R.id.i2);
        i3=findViewById(R.id.i3);
        i4=findViewById(R.id.i4);
        close=findViewById(R.id.close);


        //设置监听
        i11.setOnClickListener(ListenerOfi11);
        i22.setOnClickListener(ListenerOfi22);
        i33.setOnClickListener(ListenerOfi33);
        i44.setOnClickListener(ListenerOfi44);
        close.setOnClickListener(ListenerOfClose);

        ;

    }
}
