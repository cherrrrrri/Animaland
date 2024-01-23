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


public class dialog_roomexit extends Dialog {
    private Button yes,no;
    private View.OnClickListener listenerOfYes,listenerOfNo;

    public dialog_roomexit(@NonNull Context context) {
        super(context);
    }

    public dialog_roomexit(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_roomexit setYes(View.OnClickListener listener) {
        this.listenerOfYes=listener;
        return this;
    }

    public dialog_roomexit setNo(View.OnClickListener listener) {
        this.listenerOfNo=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_roomexit);

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
        yes=findViewById(R.id.room_exitYes);
        no=findViewById(R.id.room_exitNo);

        //设置监听
        yes.setOnClickListener(listenerOfYes);
        no.setOnClickListener(listenerOfNo);
    }

}
