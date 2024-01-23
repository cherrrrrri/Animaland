package com.example.animaland.School;


import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_classroomInformationCheck extends Dialog {

    private classroom mClassroom;
    private instructor mInstructor;
    private TextView roomID,roomName,roomIntroduction,roomMember,teacherName,teacherIntroduction;
    private ImageView teacherPhoto;
    private ImageButton enterArrows;
    private LinearLayout roomEnter;
    private boolean isInClassroom;//跟踪是否在课堂中，在则显示“进入”按钮
    private View.OnClickListener listener;//进入按钮的监听器

    public dialog_classroomInformationCheck(@NonNull Context context, int themeResId,boolean isInClassroom,classroom classroom) {
        super(context, themeResId);
        this.isInClassroom=isInClassroom;
        this.mClassroom=classroom;
        this.mInstructor=classroom.instructor;
    }

    //设置课堂
    public dialog_classroomInformationCheck setClassroom(classroom classroom) {
        this.mClassroom=classroom;
        return this;
    }

    //设置老师
    public dialog_classroomInformationCheck setInstructor(instructor instructor) {
        this.mInstructor=instructor;
        return this;
    }

    //设置课堂人数
    public dialog_classroomInformationCheck setRoomMember(int member) {
        this.mClassroom.roomMember=member;
        return this;
    }

    //设置进入按钮的监听
    public dialog_classroomInformationCheck setOnEnterListener(View.OnClickListener listener) {
        this.listener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.student_dialog_classroomdatacheck);

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
        teacherPhoto=findViewById(R.id.classroom_teacher_photo);
        teacherName=findViewById(R.id.classroom_teacher);
        teacherIntroduction=findViewById(R.id.classroom_teacher_introduction);
        roomName=findViewById(R.id.classroom_name);
        roomIntroduction =findViewById(R.id.classroom_introduction);
        roomID=findViewById(R.id.myRoomID);
        roomMember=findViewById(R.id.RoomMember);
        roomEnter =findViewById(R.id.ll_enter);
        enterArrows=findViewById(R.id.enter);

        //为控件赋值
        teacherName.setText(mClassroom.instructor.name);
        teacherIntroduction.setText(mClassroom.instructor.introduction);
        teacherPhoto.setImageResource(mClassroom.instructor.isIdentified?R.drawable.teacher_identified:R.drawable.teacher);
        roomName.setText(mClassroom.roomName);
        roomIntroduction.setText(mClassroom.roomIntroduction);
        roomID.setText(mClassroom.id);
        roomMember.setText(mClassroom.roomMember+"人正在观看中，加入他们");
        roomEnter.setVisibility(isInClassroom?View.INVISIBLE:View.VISIBLE);//根据场景判定是否显示“进入”按钮

        //设置监听
        roomEnter.setOnClickListener(listener);
        enterArrows.setOnClickListener(listener);
    }
}