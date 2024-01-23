package com.example.animaland.oral;

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

import androidx.annotation.NonNull;

import com.example.animaland.R;


public class dialog_enforcedExit extends Dialog {
    private Button exit;
    private View.OnClickListener listenerOfexit;

    public dialog_enforcedExit(@NonNull Context context) {
        super(context);
    }

    public dialog_enforcedExit(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_enforcedExit setExit(View.OnClickListener listener){
        this.listenerOfexit=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_enforcedexit);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);//dialog将占据屏幕的40%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        exit=findViewById(R.id.exit);
        exit.setOnClickListener(listenerOfexit);

    }

}
