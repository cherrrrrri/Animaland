package com.example.animaland.Dialogs;

import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_roomshow extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private TextView roomName,roomID,roomAnnouncement;
    private Button roomEnter;
    private ScrollView roomScrollView;
    private String Name,ID,Announcement,RoomEnter;
    private IonEnterRoomListener Listener;

    public dialog_roomshow(@NonNull Context context) {
        super(context);
    }

    public dialog_roomshow(@NonNull Context context, int themeID){
        super(context,themeID);
    }

    public dialog_roomshow setName(String name) {
        Name = name;
        return this;
    }

    public dialog_roomshow setID(String ID) {
        this.ID = ID;
        return this;
    }

    public dialog_roomshow setAnnouncement(String announcement) {
        Announcement = announcement;
        return this;
    }

    public dialog_roomshow setRoomEnter(String roomEnter,IonEnterRoomListener listener) {
        RoomEnter = roomEnter;
        Listener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_roomshow);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);//dialog将占据屏幕的60%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        roomName=findViewById(R.id.myRoomName);
        roomID=findViewById(R.id.myRoomID);
        roomAnnouncement=findViewById(R.id.myRoomAnnoucement);
        roomEnter=findViewById(R.id.enterRoom);
        roomScrollView=findViewById(R.id.scv_myRoomAnnouncement);

        //设置文本内容
        if (!TextUtils.isEmpty(Name)) {
            roomName.setText(Name);
        }
        if (!TextUtils.isEmpty(ID)) {
            roomID.setText(ID);
        }
        if (!TextUtils.isEmpty(Announcement)) {
            roomAnnouncement.setText(Announcement);
        }

        //设置监听
        roomEnter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(Listener!=null)
            Listener.onEnterRoom(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i)
        {
            case R.id.yesPass:

        }
    }

    public interface IonEnterRoomListener{
        void onEnterRoom(dialog_roomshow roomshow) ;
    }


}