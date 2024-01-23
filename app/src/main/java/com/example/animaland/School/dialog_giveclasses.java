package com.example.animaland.School;

import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;

public class dialog_giveclasses extends Dialog implements View.OnClickListener{

    public Button roomCreate;
    public static ImageView roomPhoto;
    public TextView language;
    private PopupWindow mpop;
    public EditText roomName,roomPass,roomLanguage,roomIntroduction;
    public RadioGroup radioPass;
    private View.OnClickListener ListenerOfCreateButton;
    private IonUploadPhotoListener ListenerOfUploadPhoto;
    private Language Language= com.example.animaland.School.Language.ENGLISH;//记录用户所选择的语言
    private DatabaseHelper db = new DatabaseHelper();

    public dialog_giveclasses(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public  dialog_giveclasses setRoomCreate(View.OnClickListener listener) {
        this.ListenerOfCreateButton =listener;
        return this;
    }


    public  dialog_giveclasses setPhotoUpload(IonUploadPhotoListener listener) {
        this.ListenerOfUploadPhoto =listener;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.teacher_dialog_giveclasses);

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.8);//dialog将占据屏幕的80%
        getWindow().setGravity(CENTER);//底部
        getWindow().setAttributes(p);

        //找到控件
        roomName=findViewById(R.id.newClassroom_Name);
        radioPass=findViewById(R.id.radio_pass);
        roomPass=findViewById(R.id.editClassroomPassword);
        language=findViewById(R.id.classroom_chooseLanguage);
        roomLanguage=findViewById(R.id.newClassroom_language);
        roomIntroduction=findViewById(R.id.newClassroom_introduction);
        roomPhoto=findViewById(R.id.classroom_photo);
        roomCreate=findViewById(R.id.room_createOver);


        //是否输入密码的监听事件
        radioPass.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.yesPass:
                        roomPass.post(new Runnable() {
                            @Override
                            public void run() {
                                roomPass.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case R.id.noPass:
                        roomPass.post(new Runnable() {
                            @Override
                            public void run() {
                                roomPass.setVisibility(View.INVISIBLE);
                            }
                        });
                        break;
                }
            }
        });

        //”选择语言“的监听事件
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v=getLayoutInflater().inflate(R.layout.teacher_folder_language,null);

                //设置下拉列表
                mpop=new PopupWindow(v,language.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                mpop.setFocusable(true);
                mpop.setOutsideTouchable(true);
                mpop.showAsDropDown(language);

                //设置列表元素
                Button English=v.findViewById(R.id.camera);
                Button French=v.findViewById(R.id.album);
                Button Japanese=v.findViewById(R.id.cancel);
                Button Others=v.findViewById(R.id.lan_others);

                //点击事件
                English.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Language= com.example.animaland.School.Language.ENGLISH;
                        roomLanguage.setVisibility(View.INVISIBLE);
                        language.setText("英 语");
                        mpop.dismiss();
                    }
                });

                French.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Language= com.example.animaland.School.Language.FRENCH;
                        roomLanguage.setVisibility(View.INVISIBLE);
                        language.setText("法 语");
                        mpop.dismiss();
                    }
                });

                Japanese.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Language= com.example.animaland.School.Language.JAPANESE;
                        roomLanguage.setVisibility(View.INVISIBLE);
                        language.setText("日 语");
                        mpop.dismiss();
                    }
                });

                Others.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Language= com.example.animaland.School.Language.OTHERS;
                        roomLanguage.setVisibility(View.VISIBLE);
                        language.setText("其 他");
                        mpop.dismiss();
                    }
                });

            }
        });


        //“上传封面”的监听事件
        /*roomPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_photos dialog_photos=new dialog_photos(getContext(),R.style.dialogOfShowRoom);
                dialog_photos.show();
            }
        });*/

        roomCreate.setOnClickListener(ListenerOfCreateButton);
        roomPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(ListenerOfUploadPhoto!=null)
            ListenerOfUploadPhoto.onUploadPhoto(this);
    }

    public interface IonUploadPhotoListener{
        void onUploadPhoto(dialog_giveclasses dialog_giveclasses);
    }
}