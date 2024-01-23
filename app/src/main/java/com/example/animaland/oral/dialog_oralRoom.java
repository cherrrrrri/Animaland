package com.example.animaland.oral;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;


public class dialog_oralRoom extends Dialog {

    private oralroom oralroom;
    private oralUser oraluser;
    private String mother="",good="",learn="";
    private ImageView photo;
    private TextView name,introduction,ID,tag,motherLanguage,LanguageGoodAt,LanguageWantToLearn;
    private Button enter;
    private LinearLayout ll_learn;
    private View.OnClickListener listener;

    public dialog_oralRoom(@NonNull Context context, int themeResId,oralroom room) {
        super(context, themeResId);
        this.oralroom=room;
        this.oraluser=room.user;
    }
    public dialog_oralRoom(@NonNull Context context, int themeResId) {
        super(context, themeResId);
     //   this.oralroom=room;
       // this.oraluser=room.user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.item_oralroom);

        //找到控件
        photo=findViewById(R.id.photo);
        name=findViewById(R.id.name);
        introduction=findViewById(R.id.introduction);
        ID=findViewById(R.id.ID);
        tag=findViewById(R.id.type);
        motherLanguage=findViewById(R.id.motherLanguage);
        LanguageGoodAt=findViewById(R.id.LanguageGoodAt);
        LanguageWantToLearn=findViewById(R.id.LanguageWantToLearn);
        enter=findViewById(R.id.enter);
        ll_learn=findViewById(R.id.ll_LanguageWantToLearn);

        //设置数据
        setData();

        //设置监听
        enter.setOnClickListener(listener);

    }

    public void setOnEnterListener(View.OnClickListener listener){
        this.listener=listener;
    }

    private void setData(){

        for(int i=0;i<oraluser.getMotherLanguage().length;i++)
            if(oraluser.getMotherLanguage()[i]!=null)
                mother+=oraluser.getMotherLanguage()[i].getText()+" ";
        for(int i=0;i<oraluser.getLanguagesGoodAt().length;i++)
            if(oraluser.getLanguagesGoodAt()[i]!=null)
                good+=oraluser.getLanguagesGoodAt()[i].getText()+" ";
        for(int i=0;i<oraluser.getLanguagesWantToLearn().length;i++)
            if(oraluser.getLanguagesWantToLearn()[i]!=null)
                learn+=oraluser.getLanguagesWantToLearn()[i].getText()+" ";

        name.setText(oralroom.teacherName);
        introduction.setText(oralroom.roomIntroduction);
        ID.setText(oralroom.id);
        tag.setText(oralroom.isCourse?"课程":"聊天");
        motherLanguage.setText(mother);
        LanguageGoodAt.setText(good);
        LanguageWantToLearn.setText(learn);
        ll_learn.setVisibility(oralroom.isCourse?View.INVISIBLE:View.VISIBLE);
    }

}
