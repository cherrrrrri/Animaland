package com.example.animaland.room;
import com.example.animaland.Dialogs.dialog_roomshow;

public class room {
    public int type=0;//记录条目类型（0为房间，1为房间首页）

    public String id;//记录房间ID
    public String roomName;//记录房间名称
    public int roomMember;//记录房间当前人数
    public int icon=roomPictures.roomPics[0];//记录房间背景图片
    public String announce;
    public dialog_roomshow roomDialog;
}
