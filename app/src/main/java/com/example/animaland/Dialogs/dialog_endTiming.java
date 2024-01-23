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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_endTiming extends Dialog {

    private TextView endEarly,endAbandon,back;
    private View.OnClickListener listenerOfEarly,listenerOfAbandon,listenerOfBack;

    public dialog_endTiming(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_endTiming setEndEarly(View.OnClickListener listener) {
        this.listenerOfEarly=listener;
        return this;
    }

    public dialog_endTiming setEndAbandon(View.OnClickListener listener) {
        this.listenerOfAbandon=listener;
        return this;
    }

    public dialog_endTiming setBack(View.OnClickListener listener) {
        this.listenerOfBack=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_endtiming);//为弹窗绑定布局

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
        endEarly=findViewById(R.id.end_earlly);
        endAbandon=findViewById(R.id.end_abandon);
        back=findViewById(R.id.end_back);

        //根据计时模式显示文字
        switch (dialog_focus.Mode){
            case dialog_focus.MODE_BACKFORWARD:
                endEarly.setText("提前结束计时");
                break;
            case dialog_focus.MODE_FORWARD:
                endEarly.setText("结束本次计时");
                break;
            default:
                break;
        }

        //设置监听
        endEarly.setOnClickListener(listenerOfEarly);
        endAbandon.setOnClickListener(listenerOfAbandon);
        back.setOnClickListener(listenerOfBack);
    }
}
