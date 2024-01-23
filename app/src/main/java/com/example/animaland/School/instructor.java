package com.example.animaland.School;

public class instructor {
    public String name;//记录教师昵称
    public String introduction;//记录教师简介
    public boolean isIdentified;//记录教师认证状态

    public instructor(String name, String introduction, boolean isIdentified) {
        this.name = name;
        this.introduction = introduction;
        this.isIdentified = isIdentified;
    }
}
