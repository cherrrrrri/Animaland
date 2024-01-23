package com.example.animaland.tool;

import android.app.Application;

public class User extends Application {
    public static String tel;//用户手机号

    public static String roomId;

    public static String name;

    public static String roomName;

    private static int seat;

    public static boolean isMaster = false;

    private static int head;//头饰编号

    private static int neck;//颈饰编号

    private static int ornament;//摆件

    private static int animal;//动物形象

    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    public String getRoomName(){
        return roomName;
    }


    public void setName(String n){
        this.name = n;
    }

    public String getName(){
        return name;
    }
    public void setTel(String s){
        this.tel = s;
    }

    public String getTel(){
        return this.tel;
    }

    public void setRoomId(String id){this.roomId = id;}

    public String getRoomId(){return roomId;}

    public void setSeat(int seat){this.seat = seat;}

    public int getSeat(){return seat;}

    //设置“房主”变量
    public void setMaster(boolean master) {
        isMaster = master;
    }

    public void setHead(int head){
        this.head=head;
    }

    public void setNeck(int neck){
        this.neck = neck;
    }

    public void setOrnament(int ornament){
        this.ornament = ornament;
    }

    public void setAnimal(int animal){
        this.animal = animal;
    }

    public int getHead(){
        return head;
    }

    public int getNeck(){
        return neck;
    }

    public int getOrnament(){
        return ornament;
    }

    public int getAnimal(){
        return animal;
    }
}
