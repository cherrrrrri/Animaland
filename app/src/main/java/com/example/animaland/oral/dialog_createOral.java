package com.example.animaland.oral;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.example.animaland.R;


public class dialog_createOral extends Dialog {

    private ImageView basicData;
    private Button create;
    public CheckedTextView tag_chat,tag_course,pass_yes,pass_no,fee_yes,fee_no;
    public EditText passEdit,feeEdit;
    private LinearLayout ll_fee;
    private NumberPicker feePicker;
    private View.OnClickListener listener;
    private boolean isChat=true;
    private int fee;
    private String pass;

    public dialog_createOral(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.oral_dialog_create);

        //找到控件
        basicData=findViewById(R.id.basicData);
        tag_chat=findViewById(R.id.tag_chat);
        tag_course=findViewById(R.id.tag_course);
        pass_yes=findViewById(R.id.pass_yes);
        pass_no=findViewById(R.id.pass_no);
        passEdit=findViewById(R.id.password);
        fee_yes=findViewById(R.id.fee_yes);
        fee_no=findViewById(R.id.fee_no);
        feeEdit=findViewById(R.id.fee);
        create=findViewById(R.id.create);
        ll_fee=findViewById(R.id.ll_LanguageWantToLearn);

        //设置监听
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChat=tag_chat.isChecked();
                fee=getFee();
                pass=getPass();



            }
        });
        create.setOnClickListener(listener);

        tag_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag_chat.toggle();
                tag_course.setChecked(tag_chat.isChecked()?false:true);
                ll_fee.setVisibility(tag_chat.isChecked()?View.INVISIBLE:View.VISIBLE);
            }
        });

        tag_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag_course.toggle();
                tag_chat.setChecked(tag_course.isChecked()?false:true);
                ll_fee.setVisibility(tag_chat.isChecked()?View.INVISIBLE:View.VISIBLE);
            }
        });

        pass_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass_yes.toggle();
                pass_no.setChecked(pass_yes.isChecked()?false:true);
                passEdit.setVisibility(pass_yes.isChecked()?View.VISIBLE:View.INVISIBLE);
            }
        });

        pass_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass_no.toggle();
                pass_yes.setChecked(pass_no.isChecked()?false:true);
                passEdit.setVisibility(pass_yes.isChecked()?View.VISIBLE:View.INVISIBLE);
            }
        });

        fee_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fee_yes.toggle();
                fee_no.setChecked(fee_yes.isChecked()?false:true);
                feeEdit.setVisibility(fee_yes.isChecked()?View.VISIBLE:View.INVISIBLE);
            }
        });

        fee_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fee_no.toggle();
                fee_yes.setChecked(fee_no.isChecked()?false:true);
                feeEdit.setVisibility(fee_yes.isChecked()?View.VISIBLE:View.INVISIBLE);
            }
        });

        /*数字选择器
        //设置最大最小值
        feePicker.setMinValue(1);
        feePicker.setMaxValue(30);
        //设置默认的位置
        feePicker.setValue(15);
        feePicker.setTextColor(-6710887);
        feePicker.setWrapSelectorWheel(false);//设置循环滚动
        feePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//设置不可编辑

        feePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                fee=newValue;
            }
        });*/

    }

    public void setOnCreateListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public int getFee() {
        if(!isChat&&fee_yes.isChecked()&&!feeEdit.getText().toString().isEmpty())
            return Integer.parseInt(feeEdit.getText().toString());
        else
            return 0;
    }

    public String getPass() {
        if(pass_yes.isChecked()&&!passEdit.getText().toString().isEmpty())
            return passEdit.getText().toString();
        else
            return "";
    }

    public boolean isChat() {
        return isChat;
    }

    /*private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(numberPicker, new ColorDrawable(getContext().getResources().getColor(R.color.dark)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }*/


}
