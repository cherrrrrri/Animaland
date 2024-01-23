package com.example.animaland.School;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.Dialogs.dialog_roomeditpass;
import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class Student extends AppCompatActivity{

    /**
     我自定义了一个classroom类和instructor类，classroom类包含instructor类
     课堂的数据在initData()里设置
     课堂的点击事件在最下面initListener()里设置
     */

    private EditText searchBox;
    private ImageView back,clear,search,calendar;
    private VerticalTabLayout tabLayout;
    private RecyclerView recyclerView;
    private ArrayList<classroom> wholeClassroomList;
    private ArrayList<List<classroom>> classroomLists;
    private LinerRecycleAdapter mLinerAdapter;
    private LinearLayoutManager llm;
    private int currentPosition = 0;
    private int recyclerHeight;
    private VerticalTabLayout.OnTabSelectedListener tabSelectedListener;

    public static String tagNames[]={"热门","英语","法语","日语","其他"}; /**分类标签（如果要增加的话Language类也要同步修改）**/
    private int numOfClassrooms;//记录课堂数量，假设有10个
    private DatabaseHelper db = new DatabaseHelper();

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){
                initData();//初始化课堂数据
                initView();//初始化布局
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_student);


        //找到控件
        back=findViewById(R.id.back);
        searchBox =findViewById(R.id.searchBox);
        clear =findViewById(R.id.clear);
        search =findViewById(R.id.search);
        calendar=findViewById(R.id.calendar);
        tabLayout = findViewById(R.id.tl_classTags);
        recyclerView=findViewById(R.id.rv_classes);

        wholeClassroomList=new ArrayList<>();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    numOfClassrooms=db.getLivingRoomNumber();
                    //传进wholeClassroomList
                    for(int i = 0;i<numOfClassrooms;i++) {
                        wholeClassroomList.add(db.getWholeClassroom().get(i));
                    }
                    handler.sendEmptyMessage(0);
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

        //initData();//初始化课堂数据


        //返回按钮的监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Student.this,School.class);
                startActivity(intent);
            }
        });

        //搜索框键盘确定按钮的监听
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_SEARCH) {
                            //TODO:搜索框键盘上"搜索"键的监听
                            /**
                             下面是一个房间信息展示的弹窗，在构造函数中传入实例化的classroom和instructor就可以把信息传过去，
                             也可以通过setClassroom(),setInstructor()函数传入课堂和老师的信息
                             **/
                            for(int j = 0;j<numOfClassrooms;j++){
                                if(searchBox.getText().toString().equals(wholeClassroomList.get(j).id)) {//找到房间
                                    final classroom room =  wholeClassroomList.get(j);
                                    dialog_classroomInformationCheck dialog = new dialog_classroomInformationCheck(Student.this, R.style.dialogOfShowRoom, false, room);
                                    dialog.setOnEnterListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //TODO:进入房间的监听
                                            String id = room.id;
                                            if (room.password != null) {//有密码
                                                //出现输入密码弹窗
                                                dialog_roomeditpass dialog = new dialog_roomeditpass(Student.this, R.style.dialogOfShowRoom);
                                                dialog.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                                    @Override
                                                    public void onEnterRoom(dialog_roomeditpass roomeditpass) {
                                                        //TODO:跳转至直播间（学生端）
                                                        Intent intent = new Intent(Student.this, Auditorium_student.class);
                                                        startActivity(intent);
                                                        Runnable runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    db.enterLivingRoom(id);
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
                                                }).show();
                                            } else {//没密码
                                                Intent intent = new Intent(Student.this, Auditorium_student.class);
                                                startActivity(intent);
                                                Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            db.enterLivingRoom(id);
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
                                    }).show();
                                    break;
                                }else if(i==numOfClassrooms-1&&(!searchBox.getText().toString().equals(wholeClassroomList.get(i).id)))
                                    Toast.makeText(getBaseContext(),"暂无该房间",Toast.LENGTH_SHORT).show();
                            }
                        }
                return false;
            }
        });

        //放大镜按钮的监听
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:“搜索”按钮的监听（同上）
                /**
                 我自定义了一个classroom类和instructor类，classroom类有instructor类的数据成员
                 下面是一个房间信息展示的弹窗，在构造函数中传入实例化的classroom就可以把信息传过去
                 也可以通过setClassroom(),setInstructor()函数传入课堂和老师的信息
                 **/
                for(int i = 0;i<numOfClassrooms;i++) {
                    if (searchBox.getText().toString().equals(wholeClassroomList.get(i).id)) {//找到房间
                        final classroom room = wholeClassroomList.get(i);
                        dialog_classroomInformationCheck dialog = new dialog_classroomInformationCheck(Student.this, R.style.dialogOfShowRoom, false, room);
                        dialog.setOnEnterListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO:进入房间的监听

                                String id = room.id;
                                if (room.password != null) {//有密码
                                    //出现输入密码弹窗
                                    dialog_roomeditpass dialog = new dialog_roomeditpass(Student.this, R.style.dialogOfShowRoom);
                                    dialog.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                        @Override
                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {
                                            //TODO:跳转至直播间（学生端）
                                            Intent intent = new Intent(Student.this, Auditorium_student.class);
                                            startActivity(intent);
                                            Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        db.enterLivingRoom(id);
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
                                    }).show();
                                } else {//没密码
                                    Intent intent = new Intent(Student.this, Auditorium_student.class);
                                    startActivity(intent);
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                db.enterLivingRoom(id);
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

                        }).show();
                        break;
                    }else if((i==numOfClassrooms-1)&&(!searchBox.getText().toString().equals(wholeClassroomList.get(i).id)))
                    Toast.makeText(getBaseContext(),"暂无该房间",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //清空按钮的监听
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空搜索框
                searchBox.setText("");
            }
        });

        //日历按钮的监听
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:日历的点击事件
                Toast.makeText(Student.this, "预约功能，敬请期待~", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initData() {

        classroomLists=new ArrayList<>();
        int numOfTags=5;//记录分类标签数量

        //按照分类标签数量生成subLists并设置标签
        for(int i=0;i<numOfTags;i++){
            ArrayList<classroom> subClassroomList=new ArrayList<>();
            classroomLists.add(subClassroomList);
            tabLayout.addTab(new QTabView(getBaseContext()).setTitle(
                    new QTabView.TabTitle.Builder().setContent(tagNames[i]).build()));
        }



        //为“热门”subList划分课堂（此处随便划分前三个课堂进去）
        for(int i=0;i<3;i++){
            classroomLists.get(0).add(wholeClassroomList.get(i));
        }

        //根据语言将课堂划分到不同的subList中
        for(int i=0;i<numOfClassrooms;i++){
            classroom room=wholeClassroomList.get(i);
            switch (room.tag){
                case ENGLISH:
                    classroomLists.get(Language.ENGLISH.getCode()).add(room);
                    break;
                case FRENCH:
                    classroomLists.get(Language.FRENCH.getCode()).add(room);
                    break;
                case JAPANESE:
                    classroomLists.get(Language.JAPANESE.getCode()).add(room);
                    break;
                case OTHERS:
                    classroomLists.get(Language.OTHERS.getCode()).add(room);
                    break;
                default:
                    break;
            }
        }

    }

    private ImageView getImageView(int outerPosition,int innerPosition){
        View OuterView = llm.findViewByPosition(outerPosition);
        RelativeLayout outer_rl = (RelativeLayout)OuterView; //获取布局中任意控件对象
        RecyclerView recyclerView= outer_rl.findViewById(R.id.rv_classesWithTag);

        View InnerView=recyclerView.getLayoutManager().findViewByPosition(innerPosition);
        RelativeLayout inner_rl = (RelativeLayout)InnerView; //获取布局中任意控件对象
        return inner_rl.findViewById(R.id.classroom_cover);
    }


    private void initView() {
        llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerHeight = recyclerView.getHeight();
        mLinerAdapter = new LinerRecycleAdapter(getBaseContext(),classroomLists,recyclerHeight);
        recyclerView.setAdapter(mLinerAdapter);
        initListener();

        /** ↑↑↑上面四行是去掉了post外框后的代码，运行后暂无出现错误
         *getHeight 需延时获取，有网络请求时可响应结束后同步获取，否则异步获取
         recyclerView.post(new Runnable() {
        @Override
        public void run() {
        recyclerHeight = recyclerView.getHeight();
        mLinerAdapter = new LinerRecycleAdapter(getBaseContext(),classroomLists,recyclerHeight);
        recyclerView.setAdapter(mLinerAdapter);
        initListener();
        }
        });
         */

        tabSelectedListener = new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                if (currentPosition != position){
                    llm.scrollToPositionWithOffset(position,0);
                    currentPosition = position;
                }
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        };

        tabSelectedListener = new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                if (currentPosition != position){
                    llm.scrollToPositionWithOffset(position,0);
                    currentPosition = position;
                }
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        };


        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (int i= 0; i < llm.getItemCount();i++){
                    if (null != llm.findViewByPosition(i)){
                        if (llm.findViewByPosition(i).getTop() < recyclerHeight / 2 && llm.findViewByPosition(i).getBottom() > recyclerHeight / 2){
                            if (tabLayout.getSelectedTabPosition() != i){
                                currentPosition = i;
                                tabLayout.setTabSelected(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0){
                    tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                }else if (newState == 3){
                    tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                }else {
                    tabLayout.addOnTabSelectedListener(tabSelectedListener);
                }
            }
        };

        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void initListener(){
        //设置监听
        mLinerAdapter.setOnClassroomClickListeners(new GridRecycleAdapter.onGridItemClickListener() {
            @Override
            public void onGridItemClick(int position) {
                if (classroomLists.get(currentPosition).get(position).password != null) {//有密码
                    //出现输入密码弹窗
                    dialog_roomeditpass dialog = new dialog_roomeditpass(Student.this, R.style.dialogOfShowRoom);
                    dialog.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                        @Override
                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {
                            //TODO:跳转至直播间（学生端）
                            Intent intent = new Intent(Student.this, Auditorium_student.class);
                            startActivity(intent);
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        db.enterLivingRoom(classroomLists.get(currentPosition).get(position).id);
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
                    }).show();
                } else {//没密码
                    Intent intent = new Intent(Student.this, Auditorium_student.class);
                    startActivity(intent);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                db.enterLivingRoom(classroomLists.get(currentPosition).get(position).id);
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
        });

        //"查看更多“的点击事件
        mLinerAdapter.setOnMoreClickListeners(new LinerRecycleAdapter.onLineItemClickListener() {
            @Override
            public void onLineItemClick(int position) {
            }
        });
    }

    public void onStop() {
        super.onStop();
    }
}


