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
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_roomeditpass extends Dialog implements View.OnClickListener {
    public EditText editPass;
    private Button enterWithPass;
    private IonEnterRoomListener Listener;

    public dialog_roomeditpass setEnterWithPass(IonEnterRoomListener listener) {
        this.Listener=listener;
        return this;
    }

    public dialog_roomeditpass(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_roomeditpass);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.4);//dialog将占据屏幕的40%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        editPass=findViewById(R.id.room_editPass);
        enterWithPass=findViewById(R.id.room_enterWithPass);

        //设置监听
        enterWithPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(Listener!=null)
            Listener.onEnterRoom(this);
    }

    public interface IonEnterRoomListener{
        void onEnterRoom(dialog_roomeditpass roomeditpass);
    }


}
