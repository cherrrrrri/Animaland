package com.example.animaland.School;


import static android.view.Gravity.BOTTOM;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_cameraTop extends Dialog {

    private dialog_photos dialog;

    public dialog_cameraTop(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        dialog=new dialog_photos(getContext(),R.style.dialogOfShowRoom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    //    setContentView(R.layout.dialog_camera_top);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.45);//dialog将占据屏幕的45%
        p.height=(int)(size.y*0.2);//dialog将占据屏幕的45%
        getWindow().setGravity(BOTTOM);//底部
        //getWindow().setWindowAnimations(null);//设置进出效果
        getWindow().setAttributes(p);

        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismiss();
    }
}
