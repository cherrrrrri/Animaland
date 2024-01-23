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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.example.animaland.R;

import java.sql.SQLException;

public class dialog_roomcreate extends Dialog implements View.OnClickListener{

    private Button roomCreate;
    public EditText roomPass;
    private RadioGroup radioPass;
    private RadioButton yesPass,noPass;
    private IonCreateRoomListener ListenerOfCreateButton;
    private RadioGroup.OnCheckedChangeListener ListenerOfRadioPass;
    public EditText roomName;
    //获取文本

    public dialog_roomcreate(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public  dialog_roomcreate setRoomCreate(IonCreateRoomListener listener) {
        this.ListenerOfCreateButton =listener;
        return this;
    }

    public dialog_roomcreate setRoomPass(boolean isDisplay) {
        this.roomPass.setVisibility(isDisplay?View.VISIBLE:View.INVISIBLE);
        return this;
    }

    public dialog_roomcreate setRadioPass(RadioGroup.OnCheckedChangeListener listener) {
        this.ListenerOfRadioPass=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_roomcreate);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.8);//dialog将占据屏幕的80%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        roomCreate=findViewById(R.id.room_createOver);//确认创建
        roomPass=findViewById(R.id.editRoomPassword);
        radioPass=findViewById(R.id.radio_pass);
        yesPass=findViewById(R.id.yesPass);
        noPass=findViewById(R.id.noPass);
        roomName=findViewById(R.id.newRoomName);



        //设置监听
        roomCreate.setOnClickListener(this);
        radioPass.setOnCheckedChangeListener(ListenerOfRadioPass);
    }

    @Override
    public void onClick(View view) {
        if(ListenerOfCreateButton !=null) {
            try {
                ListenerOfCreateButton.onCreateRoom(this);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public interface IonCreateRoomListener{
        void onCreateRoom(dialog_roomcreate roomcreate) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;
    }
}