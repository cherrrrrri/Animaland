package com.example.animaland.School;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.animaland.Island;
import com.example.animaland.LoginAndSignUp.LoginTabFragment;
import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TeacherTabFragment extends TakePhotoFragment {

    private TextView identityText,courseTeach,coin;
    private EditText name,introduction;
    private ImageView identityIcon,edit;
    private Button back,exit;
    boolean isEdited=false;//记录是否为编辑状态
    public  int identity=UNCERTIFIED;//记录认证状态
    private static final int CERTIFIED=0;//已认证
    private static final int UNCERTIFIED=1;//未认证
    private static final int CERTIFIEING=2;//认证中
    private DatabaseHelper db = new DatabaseHelper();

    //拍照
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    //动态权限
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码

    //TakePhoto
    public static Uri imageUri;       //图片保存路径

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

            if(msg.what==0) {
                coin.setText((String)msg.obj);
            }else if(msg.what==1){
                name.setText((String)msg.obj);
            }else if(msg.what==2){
                courseTeach.setText((String)msg.obj);
            }else if(msg.what==3){
                introduction.setText((String)msg.obj);
            }else if(msg.what==5){
                if(identity==CERTIFIEING){
                    identity=CERTIFIED;//更新状态
                    identityText.setText("已认证");
                    identityIcon.setImageResource(R.drawable.save);//更改图标
                }
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.account_teacher,container,false);

        //找到控件
        coin=root.findViewById(R.id.coin);
        name=root.findViewById(R.id.name);
        introduction=root.findViewById(R.id.introduction);
        courseTeach=root.findViewById(R.id.courseTeach);
        identityText=root.findViewById(R.id.identityText);
        identityIcon=root.findViewById(R.id.identityIcon);
        back=root.findViewById(R.id.back_account);
        exit=root.findViewById(R.id.exit_account);
        edit=root.findViewById(R.id.edit_data);

        //拍照
        if (Build.VERSION.SDK_INT >= 23) {  //6.0才用动态权限
            //申请相关权限
            initPermission();
        }

        //设置文字

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //设置文字
                try {
                    Message msg1 = new Message();
                    msg1.what = 0;
                    msg1.obj = db.getCoin()+"";
                    handler.sendMessage(msg1);

                    Message msg2 = new Message();
                    msg2.what = 1;
                    msg2.obj = db.getName();
                    handler.sendMessage(msg2);

                    Message msg3 = new Message();
                    msg3.what = 2;
                    msg3.obj = db.getCourseTeach()+"";
                    handler.sendMessage(msg3);

                    Message msg4 = new Message();
                    msg4.what = 3;
                    msg4.obj =db.getIntro();
                    handler.sendMessage(msg4);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }

            }
        };
        cachedThreadPool.execute(runnable);
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        introduction.setFocusable(false);
        introduction.setFocusableInTouchMode(false);

        //设置编辑图标的监听事件
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdited){
                    //更新为不可编辑状态
                    if(name.getText().toString()==""){
                        Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.setName(name.getText().toString());
                                    db.setIntro(introduction.getText().toString());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (java.lang.InstantiationException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        cachedThreadPool.execute(runnable1);
                        isEdited=false;
                        name.setFocusable(false);
                        name.setFocusableInTouchMode(false);
                        introduction.setFocusable(false);
                        introduction.setFocusableInTouchMode(false);
                        edit.setImageResource(R.drawable.edit);
                    }
                }
                else{
                    //更新为可编辑状态
                    isEdited=true;
                    name.setFocusable(true);
                    name.setFocusableInTouchMode(true);
                    introduction.setFocusable(true);
                    introduction.setFocusableInTouchMode(true);
                    Toast.makeText(getContext(), "单击名字和简介即可更改", Toast.LENGTH_SHORT).show();
                    edit.setImageResource(R.drawable.save);
                }
            }
        });

        //设置认证图标的监听事件
        identityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(identity==UNCERTIFIED){
                    //出现“拍照/从相册选择”弹窗
                    dialog_photos dialog_photos=new dialog_photos(getContext(),R.style.dialogOfShowRoom);
                    dialog_photos.setCamera(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //打开相机
                            Toast.makeText(getActivity(), "打开相机", Toast.LENGTH_SHORT).show();
                            imageUri = getImageCropUri();
                            takePhoto.onPickFromCapture(imageUri);

                            identity=CERTIFIEING;//更新状态
                            identityText.setText("审核中");
                            identityIcon.setImageResource(R.drawable.down);//更改图标
                            dialog_photos.dismiss();
                        }
                    }).setAlbum(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //打开相册
                            Toast.makeText(getActivity(), "从相册选择", Toast.LENGTH_SHORT).show();
                            imageUri = getImageCropUri();
                            takePhoto.onPickFromGallery();
                          /*  Runnable runnable1 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        db.setQual(imageUri.toString());
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (java.lang.InstantiationException e) {
                                        e.printStackTrace();
                                    }

                                };
                            };
                            cachedThreadPool.execute(runnable1);*/
                            identity=CERTIFIEING;//更新状态
                            identityText.setText("审核中");
                            identityIcon.setImageResource(R.drawable.down);//更改图标
                            dialog_photos.dismiss();
                        }
                    }).setCancel(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //取消
                            dialog_photos.dismiss();
                        }
                    }).show();


                }else{
                    Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(db.hasQual()){
                                    handler.sendEmptyMessage(5);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    cachedThreadPool.execute(runnable1);

                }
            }
        });


        //设置返回的监听事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Island.class);
                startActivity(i);
            }
        });

        //设置退出的监听事件
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginTabFragment.class));
            }
        });

        return root;
    }


    //拍照

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    private void initPermission() {
        mPermissionList.clear();        //清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getActivity(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(getActivity(), permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        //     Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();
        String iconPath = result.getImage().getOriginalPath();

        //Toast显示图片路径
        //   Toast.makeText(this, "imagePath:" + iconPath, Toast.LENGTH_SHORT).show();
        //Google Glide库 用于加载图片资源
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    db.setQual(imageUri.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        };
        cachedThreadPool.execute(runnable1);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        //   Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        //     Toast.makeText(this,"取消",Toast.LENGTH_SHORT).show();
    }


    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, System.currentTimeMillis() + ".jpg");

        // File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
    public void onDestroyView() {
        super.onDestroyView();
    }

}
