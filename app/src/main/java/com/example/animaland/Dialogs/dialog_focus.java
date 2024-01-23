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
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;

public class dialog_focus extends Dialog {

    private Button start,stop,end;
    private EditText et_flag;
    public EditText et_studyTime;
    public TextView minute;
    public RadioGroup timingMode;
    public String text_plan,text_studyTime,text_breakTime;
    private View.OnClickListener listenerOfStart,listenerOfEndEarly,listenerOfRestart;
    private RadioGroup.OnCheckedChangeListener listenerOfMode;

    private int State=1;
    public static int Mode=1;
    public final static int STATE_START=1;//计时开始前界面
    public final static int STATE_STOP=2;//计时中界面
    public final static int MODE_BACKFORWARD=1;//倒计时模式
    public final static int MODE_FORWARD=2;//正计时模式

    public dialog_focus(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_focus setStart(View.OnClickListener listener) {
        this.listenerOfStart=listener;
        return this;
    }

    public dialog_focus setEndEarly(View.OnClickListener listener) {
        this.listenerOfEndEarly=listener;
        return this;
    }

    public dialog_focus setEnd(View.OnClickListener listener) {
        this.listenerOfRestart=listener;
        return this;
    }

    public dialog_focus setTimingMode(RadioGroup.OnCheckedChangeListener listener) {
        this.listenerOfMode=listener;
        return this;
    }

    public dialog_focus setState(int state) {
        State = state;
        return this;
    }

    public void setMode(int mode) {
        Mode = mode;
    }

    public int getState() {
        return State;
    }

    @Override//此方法在初始化弹窗时调用 只调用一次
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_startfoucing);//为弹窗绑定布局

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
        start=findViewById(R.id.focus_startOrstop);
        stop=findViewById(R.id.focus_stopTemply);
        end =findViewById(R.id.focus_end);
        et_flag=findViewById(R.id.focus_flag);
        et_studyTime=findViewById(R.id.focus_studyTime);
        timingMode=findViewById(R.id.focus_mode);
        minute=findViewById(R.id.focus_min);

        //设置监听
        start.setOnClickListener(listenerOfStart);
        stop.setOnClickListener(listenerOfEndEarly);
        end.setOnClickListener(listenerOfRestart);
        timingMode.setOnCheckedChangeListener(listenerOfMode);
    }

    @Override//此方法在显示弹窗时调用
    protected void onStart() {
        super.onStart();

        //根据State变量显示不同界面
        switch (State) {
            case STATE_START:
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);
                end.setVisibility(View.INVISIBLE);
                clearEditText();
                Editable(true);
                break;
            case STATE_STOP:
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                end.setVisibility(View.VISIBLE);
                Editable(false);
                break;
            default:
                break;
        }
    }

    //控制编辑框的编辑状态：若为不可编辑状态，点击后无键盘弹出等任何反应
    private void Editable(boolean isEdit){

        if(isEdit==true){
            et_flag.setFocusable(true);
            et_flag.setFocusableInTouchMode(true);
            et_studyTime.setFocusable(true);
            et_studyTime.setFocusableInTouchMode(true);
            timingMode.getChildAt(0).setClickable(true);
            timingMode.getChildAt(1).setClickable(true);
        }
        else
        {
            et_flag.setFocusable(false);
            et_flag.setFocusableInTouchMode(false);
            et_studyTime.setFocusable(false);
            et_studyTime.setFocusableInTouchMode(false);
            timingMode.getChildAt(0).setClickable(false);
            timingMode.getChildAt(1).setClickable(false);
        }
    }

    public void clearEditText(){
        et_flag.setText("");
        et_studyTime.setText("");
    }

    public boolean checkBeforeStart()
    {
       if(et_flag.getText().toString().equals(""))
           return false;
        if(Mode==MODE_BACKFORWARD&&et_studyTime.getText().toString().equals(""))
            return false;
        return true;
    }
}
