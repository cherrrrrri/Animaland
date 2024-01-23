package com.example.animaland.School;

import static com.example.animaland.School.dialog_giveclasses.roomPhoto;
import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.animaland.PassThroughButton;
import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.tencent.imsdk.v2.V2TIMGroupListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Teacher extends TakePhotoActivity {

    private ImageView back;
    private PassThroughButton desk,giveClasses,timetable,photo;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    public User user;

    String ROOMID;//房间id（唯一）
    String ROOMNAME;//房间名称
    //两个和Auditorium_student的ROOMID和ROOMNAME是一样的。

    //动态权限
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码

    private DatabaseHelper db= new DatabaseHelper();


    public static Uri imageUri;       //图片保存路径

    public Teacher() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }

        return takePhoto;
    }

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){
                Glide.with(Teacher.this).load((String)msg.obj).into(roomPhoto);
            }else if(msg.what==1){
            }else if(msg.what==2){
                createRoom_chat();
                createRoom_answer();
                createRoom_question();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_teacher);
        if (Build.VERSION.SDK_INT >= 23) {  //6.0才用动态权限
            //申请相关权限
            initPermission();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //动态权限请求
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    private void initPermission() {
        mPermissionList.clear();        //清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //找到控件
        giveClasses=findViewById(R.id.desk_giveClasses);
        timetable=findViewById(R.id.desk_timetable);
        photo=findViewById(R.id.desk_photo);
        back=findViewById(R.id.back);
        desk=findViewById(R.id.teacher_desk);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Teacher.this,School.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Teacher.this, desk, getString(R.string.desk));
                startActivity(intent, optionsCompat.toBundle());
            }
        });


        giveClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_giveclasses dialog_giveclasses=new dialog_giveclasses(Teacher.this,R.style.dialogOfShowRoom);
                dialog_giveclasses.setRoomCreate(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        //“开始直播”的点击事件
                        Boolean name = false;
                        Boolean pass = false;

                        //房间名
                        if(dialog_giveclasses.roomName.getText().toString()!=null)
                            name = true;
                        else
                            Toast.makeText(Teacher.this, "房间名为空", Toast.LENGTH_SHORT).show();

                        //密码
                        if(dialog_giveclasses.radioPass.getCheckedRadioButtonId()==-1)
                            Toast.makeText(Teacher.this, "密码为空", Toast.LENGTH_SHORT).show();
                        else if(dialog_giveclasses.radioPass.getCheckedRadioButtonId()==0) {//有密码
                            if(dialog_giveclasses.roomPass.getText().toString()==null)
                                Toast.makeText(Teacher.this, "密码为空", Toast.LENGTH_SHORT).show();
                            else
                                pass = true;
                        }else{//无密码
                            pass = true;
                        }

                        if(name && pass) {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("CreatLivingRoom");
                                        if(dialog_giveclasses.radioPass.getCheckedRadioButtonId()==0) {//有密码

                                            db.creatLivingRoom(dialog_giveclasses.roomName.getText().toString(), dialog_giveclasses.roomPass.getText().toString(),
                                                    dialog_giveclasses.language.getText().toString().trim(), dialog_giveclasses.roomIntroduction.getText().toString());
                                            if(imageUri!=null)
                                                db.setCover(imageUri.toString());
                                        }else{//无密码
                                            db.creatLivingRoom(dialog_giveclasses.roomName.getText().toString(), null,
                                                    dialog_giveclasses.language.getText().toString().trim(), dialog_giveclasses.roomIntroduction.getText().toString());
                                            if(imageUri!=null)
                                                db.setCover(imageUri.toString());
                                        }

                                        System.out.println("从数据库出来了");

                                        ROOMID=user.roomId;//房间id（唯一）
                                        System.out.println("ROOMID:"+ROOMID+" roomId:"+user.roomId);
                                        ROOMNAME=user.roomName;//房间名称
                                        System.out.println(ROOMID+" " +ROOMNAME);
                                        handler.sendEmptyMessage(2);

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            cachedThreadPool.execute(runnable);
                        }
                    }
                }).setPhotoUpload(new dialog_giveclasses.IonUploadPhotoListener() {
                    //“上传封面”的点击事件
                    @Override
                    public void onUploadPhoto(com.example.animaland.School.dialog_giveclasses giveclasses) {
                        dialog_photos dialog_photos=new dialog_photos(Teacher.this,R.style.dialogOfShowRoom);
                        dialog_photos.setCamera(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //打开相机
                                imageUri = getImageCropUri();
                                takePhoto.onPickFromCapture(imageUri);
                                dialog_photos.dismiss();
                                Runnable runnable =new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            db.setCover(imageUri.toString());
                                            handler.sendEmptyMessage(1);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                cachedThreadPool.execute(runnable);

                            }
                        }).setAlbum(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //打开相册
                                imageUri = getImageCropUri();
                                takePhoto.onPickFromGallery();
                                dialog_photos.dismiss();

                            }
                        }).setCancel(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //取消
                                dialog_photos.dismiss();
                            }
                        }).show();
                    }
                }).show();

            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Teacher.this, "预约功能 敬请期待", Toast.LENGTH_SHORT).show();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Teacher.this, "历史记录功能 敬请期待", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 创建一个房间
     */
    private void createRoom_chat(){
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_AVCHATROOM, ROOMID+"chat", ROOMNAME, new V2TIMValueCallback<String>() {
            @Override
            public void onSuccess(String s) {
                // 创建群组成功
                Log.i("腾讯云即时通信IM","成功创建房间");
                startActivity(new Intent(Teacher.this,Auditorium_teacher.class));
            }

            @Override
            public void onError(int code, String desc) {
                // 创建群组失败
                Log.e("腾讯云即时通信IM","创建房间失败"+code+","+desc);
                Toast.makeText(Teacher.this,"创建房间失败",Toast.LENGTH_SHORT).show();
            }
        });
        // 监听群组创建通知
        V2TIMManager.getInstance().addGroupListener(new V2TIMGroupListener() {
            @Override
            public void onGroupCreated(String groupID) {
                // 群创建回调，groupID 为新创建群组的 ID
            }
        });
    }

    private void createRoom_question(){
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_MEETING, ROOMID+"question", ROOMNAME, new V2TIMValueCallback<String>() {
            @Override
            public void onSuccess(String s) {
                // 创建群组成功
                Log.i("腾讯云即时通信IM","成功创建房间");
            }

            @Override
            public void onError(int code, String desc) {
                // 创建群组失败
                Log.e("腾讯云即时通信IM","创建房间失败"+code+","+desc);
            }
        });
        // 监听群组创建通知
        V2TIMManager.getInstance().addGroupListener(new V2TIMGroupListener() {
            @Override
            public void onGroupCreated(String groupID) {
                // 群创建回调，groupID 为新创建群组的 ID
            }
        });
    }

    private void createRoom_answer(){
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_MEETING, ROOMID+"answer", ROOMNAME, new V2TIMValueCallback<String>() {
            @Override
            public void onSuccess(String s) {
                // 创建群组成功
                Log.i("腾讯云即时通信IM","成功创建房间");
            }

            @Override
            public void onError(int code, String desc) {
                // 创建群组失败
                Log.e("腾讯云即时通信IM","创建房间失败"+code+","+desc);
            }
        });
        // 监听群组创建通知
        V2TIMManager.getInstance().addGroupListener(new V2TIMGroupListener() {
            @Override
            public void onGroupCreated(String groupID) {
                // 群创建回调，groupID 为新创建群组的 ID
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        String iconPath = result.getImage().getOriginalPath();

        //Toast显示图片路径

        //Google Glide库 用于加载图片资源
        Glide.with(this).load(iconPath).into(roomPhoto);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();

    }


    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, System.currentTimeMillis() + ".jpg");

        // File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }
}