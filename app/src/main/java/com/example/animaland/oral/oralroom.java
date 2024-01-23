package com.example.animaland.oral;

public class oralroom {
    public oralUser user;//记录口语房主，房间ID与名称与房主ID及名称同步
    public boolean isCourse=true;//记录口语房间类型（社交语伴类/专业口语课类）
    public String roomIntroduction;//记录房间简介
    public String password;//记录房间密码
    public int roomMember=1;//记录课堂当前人数(达到两人时不在广场上显示)
    public int fee=0;//费用
    public int type=1;//类型 默认聊天
    public String id;
    public String studentId;
    public String teacherName;

}
