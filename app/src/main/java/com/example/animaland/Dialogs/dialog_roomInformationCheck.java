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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;

import java.sql.SQLException;

public class dialog_roomInformationCheck extends Dialog {

    private EditText roomName,roomAnnouncement;
    private TextView roomID;
    private String str_ID,str_Name,str_Announcement;
    private ImageButton roomEdit;
    private boolean isEdited=false;//跟踪是否为编辑状态
    //private boolean isMaster=false;//跟踪是否为房主
    private DatabaseHelper db = new DatabaseHelper();
    private User user = new User();

    public dialog_roomInformationCheck(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    //设置“ID”
    public dialog_roomInformationCheck setRoomID(String roomID) {
        this.str_ID = roomID;
        return this;
    }

    //设置房间名字
    public dialog_roomInformationCheck setRoomName(String roomName) {
        this.str_Name = roomName;
        return this;
    }

    //设置房间公告
    public dialog_roomInformationCheck setRoomAnnouncement(String roomAnnouncement) {
        this.str_Announcement = roomAnnouncement;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_roomdatacheck);

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
        roomAnnouncement=findViewById(R.id.myRoomAnnoucement);
        roomID=findViewById(R.id.myRoomID);
        roomEdit=findViewById(R.id.check_editable);

        EditState(false);//初始化为不可编辑状态
        roomEdit.setVisibility(user.isMaster?View.VISIBLE:View.INVISIBLE);//根据用户身份显示“编辑”按钮
        roomName.setText(str_Name);
        roomAnnouncement.setText(str_Announcement);
        roomID.setText(str_ID);
        showHint();//根据用户身份显示提示文字

        //设置“编辑”按钮的监听事件
        roomEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdited=isEdited?false:true;//更新“编辑状态”
                EditState(isEdited);
                roomEdit.setImageResource(isEdited?R.drawable.save:R.drawable.edit);
                Toast.makeText(getContext(), isEdited?"修改完记得保存喔":"保存成功！", Toast.LENGTH_SHORT).show();

                Thread thread = new Thread(new Runnable() {//执行数据库操作
                    @Override
                    public void run() {
                        try {
                            db.updateRoomName(roomName.getText().toString());
                            db.updateRoomAnnouncement(roomAnnouncement.getText().toString());
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
                });
                thread.start();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        roomEdit.setVisibility(user.isMaster?View.VISIBLE:View.INVISIBLE);//根据用户身份显示“编辑”按钮
        showHint();//根据用户身份显示提示文字

        //设置“编辑”按钮的监听事件
        roomEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdited=isEdited?false:true;//更新“编辑状态”
                EditState(isEdited);
                roomEdit.setImageResource(isEdited?R.drawable.save:R.drawable.edit);
                Toast.makeText(getContext(), isEdited?"修改完记得保存喔":"保存成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void EditState(boolean isEdited){
        if(isEdited){
            roomName.setFocusable(true);
            roomName.setFocusableInTouchMode(true);
            roomAnnouncement.setFocusable(true);
            roomAnnouncement.setFocusableInTouchMode(true);
        }
        else{
            roomName.setFocusable(false);
            roomName.setFocusableInTouchMode(false);
            roomAnnouncement.setFocusable(false);
            roomAnnouncement.setFocusableInTouchMode(false);
        }
    }

    private void showHint(){
        //设置房间名称的内容
        if(roomName.getText().toString().equals("")){
            if(user.isMaster)
                roomName.setHint("请大师为此雅居题字赐名");
            else
                roomName.setHint("快让房主起个名");
        }
        //设置房间公告的内容
        if(roomAnnouncement.getText().toString().equals("")){
            if(user.isMaster)
                roomAnnouncement.setHint("皇上！上朝啦！快来定圣旨~");
            else
                roomAnnouncement.setHint("房虽无规，我心有规！");
        }
    }
}
