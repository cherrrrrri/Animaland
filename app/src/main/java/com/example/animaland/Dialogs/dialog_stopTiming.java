package com.example.animaland.Dialogs;

import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.animaland.R;

import org.junit.FixMethodOrder;

import java.util.concurrent.Callable;

public class dialog_stopTiming extends Dialog {

    private Button goon;
    private View.OnClickListener listener;
    public static Chronometer fiveMin;
    public static boolean isOver=false;
    public dialog_stopTiming(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_stopTiming setGoon(View.OnClickListener listener) {
        this.listener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_stoptiming);//为弹窗绑定布局
        fiveMin=(Chronometer) findViewById(R.id.FiveMin);
        fiveMin.setBase(SystemClock.elapsedRealtime()+5*60000);
        fiveMin.start();

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.5);//dialog将占据屏幕的30%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);
        //找到控件
        goon=findViewById(R.id.focus_goon);
        fiveMin.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                fiveMin.setText(fiveMin.getText().toString().substring(1));
                if (SystemClock.elapsedRealtime()-fiveMin.getBase()>=0){
                    //delete();
                    fiveMin.stop();
                    isOver=true;
                }
            }
        });
        //设置监听
        goon.setOnClickListener(listener);
    }

}
