package com.example.animaland.School;

import com.example.animaland.R;

public class classroom {
    public String id;//记录课堂房间ID
    public String roomName;//记录课堂名称
    public String roomIntroduction;//记录课堂简介
    public Language tag=Language.ENGLISH;//记录课堂类型,默认为英语
    public int roomMember;//记录课堂当前人数
    public String password;
    public int photo= R.drawable.classroom_cover1;//记录课堂封面
   // public instructor instructor=new instructor("一位岛民","不愿透露姓名的一位Animaland岛民",false);//记录课堂教师,默认
   public instructor instructor;
    public String cover;
}
