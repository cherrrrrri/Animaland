package com.example.animaland.oral;

import com.example.animaland.School.Language;
import com.example.animaland.tool.User;

public class oralUser extends User {

    public static int HANGOUT=0;//游玩中
    public static int WAITING=1;//等待中
    public static int CHATTING=2;//交流中

    public int photo;//TODO:更换为虚拟动物形象
    public static String introduction;

    private Language[] motherLanguage;
    private Language[] LanguagesGoodAt;
    private Language[] LanguagesWantToLearn;

    public int eng = 0;
    public int fra = 0;
    public int jan = 0;
    public int other = 0;

    public String str_mother="";
    public String str_good="";
    public String str_learn="";
    private int state;//记录状态


    public oralUser(int photo, Language[] l1, Language[] l2, Language[] l3, int state) {
        this.photo = photo;
        motherLanguage = l1;
        LanguagesGoodAt = l2;
        LanguagesWantToLearn = l3;
        this.state = state;

        /*for(int i=0;i<l1.length;i++)
            str_mother +=(l1[i].getText()+" ");
        for(int i=0;i<l2.length;i++)
            str_good +=(l2[i].getText()+" ");
        for(int i=0;i<l3.length;i++)
            str_learn +=(l3[i].getText()+" ");*/

    }

    /*public oralUser(int photo, String motherLanguage, String languagesGoodAt, String languagesWantToLearn, int state) {
        this.photo = photo;
        this.str_mother = motherLanguage;
        this.str_good = languagesGoodAt;
        this.str_learn = languagesWantToLearn;
        this.state = state;
    }*/

    public String getMotherString() {
        for(int i=0;i<motherLanguage.length;i++)
                str_mother +=(motherLanguage[i].getText()+" ");
        return str_mother;
    }

    public String getGoodString() {
        for(int i=0;i<LanguagesGoodAt.length;i++)
            str_good +=(LanguagesGoodAt[i].getText()+" ");
        return str_good;
    }

    public String getLearnString() {
        for(int i=0;i<LanguagesWantToLearn.length;i++)
            str_learn +=(LanguagesWantToLearn[i].getText()+" ");
        return str_learn;
    }

    public void setLanguagesGoodAt(Language[] languagesGoodAt) {
        LanguagesGoodAt = languagesGoodAt;
    }

    public void setLanguagesWantToLearn(Language[] languagesWantToLearn) {
        LanguagesWantToLearn = languagesWantToLearn;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Language[] getMotherLanguage() {
        return motherLanguage;
    }

    public Language[] getLanguagesGoodAt() {
        return LanguagesGoodAt;
    }

    public Language[] getLanguagesWantToLearn() {
        return LanguagesWantToLearn;
    }

    public int getState() {
        return state;
    }
}
