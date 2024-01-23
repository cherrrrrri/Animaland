package com.example.animaland.SeatFolder;

import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;

import java.util.ArrayList;
import java.util.List;

public class dialog_todo extends Dialog {

    public static List<flag> mFlagList;
    private RecyclerView mRecycleView;
    private RecyclerFlagAdapter madapter;
    private TextView progress;
    private Button addFlag;
    private View.OnClickListener listenerOfEditable;
    private ScrollView scvOfTodoList;
    public dialog_todo(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog_todo setEditable(View.OnClickListener listener){
        this.listenerOfEditable=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_todolist);
        mFlagList=new ArrayList<>();

        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);//dialog将占据屏幕的60%
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

        //找到控件
        mRecycleView=findViewById(R.id.todo_list);
        progress=findViewById(R.id.todo_progress);
        addFlag=findViewById(R.id.todo_addFlag);
        scvOfTodoList=findViewById(R.id.scv_todolist);

        progress.setText("开始今天的计划吧！");//初始化进度
        initData();//初始化TODOlist
        showList(true,false);//展示TODOlist

        //设置“添加”按钮的监听事件
        addFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag newFlag=new flag();
                newFlag.background=flagPictures.flagPics[0];
                mFlagList.add(newFlag);
                showList(true,false);//展示TODOlist
                if(getEditedProgress()==0)
                    progress.setText("开始今天的计划吧！");
                else
                    progress.setText("进度： "+ getFinishedProgress()+"/"+mFlagList.size());

                //定位到最底端（新添加的目标处）
                mRecycleView.scrollToPosition(mFlagList.size()-1);
            }
        });


    }//OnCreate的末尾

    private void initData() {
        for(int i=0;i<3;i++){
            flag newFlag=new flag();
            newFlag.background=flagPictures.flagPics[0];
            mFlagList.add(newFlag);
        }
    }

    private void initListener() {
            //设置复选框的监听事件
            madapter.setOnCheckedChangeListener(new RecyclerFlagAdapter.onItemCheckedChangeListener() {
                @Override
                public void onItemCheckedChange(int position) {
                    progress.setText("进度： "+ getFinishedProgress()+"/"+mFlagList.size());
                    if(mFlagList.get(position).state){
                        if(getFinishedProgress()!=mFlagList.size())
                            Toast.makeText(getContext(),"离目标又近了一步！冲冲冲！", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(),"Cheers!!!计划已经完成啦！休息一下吧~", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //设置删除的监听事件
            madapter.setOnItemDeleteListener(new RecyclerFlagAdapter.onItemDeleteListener() {
                @Override
                public void onItemDelete(int position) {
                    mFlagList.remove(position);
                    showList(true,false);
                    progress.setText("进度： "+ getFinishedProgress()+"/"+mFlagList.size());
                    if(mFlagList.size()==0||getEditedProgress()==0)
                        progress.setText("开始今天的计划吧！");
                    if(getFinishedProgress()==mFlagList.size()&&mFlagList.size()!=0)
                        Toast.makeText(getContext(),"Cheers!!!计划已经完成啦！休息一下吧~", Toast.LENGTH_SHORT).show();

                    //定位到最顶端
                    scvOfTodoList.post(new Runnable() {
                        @Override
                        public void run() {
                            scvOfTodoList.fullScroll(View.FOCUS_UP);
                        }
                    });
                }
            });
    }

    private void showList(boolean isVertical, boolean isReverse) {
        //设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        //StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);

        //设置水平还是垂直
        layoutManager.setOrientation(isVertical?LinearLayoutManager.VERTICAL:LinearLayoutManager.HORIZONTAL);

        //设置正向还是反向
        layoutManager.setReverseLayout(isReverse);
        mRecycleView.setLayoutManager(layoutManager);

        //创建适配器Adapter
        madapter=new RecyclerFlagAdapter();

        //设置适配器
        mRecycleView.setAdapter(madapter);

        initListener();

    }

    private int getFinishedProgress(){
        int finished=0;
        for(int i=0;i<mFlagList.size();i++)
            if(mFlagList.get(i).state==true)
                finished++;
        return finished;
    }

    private int getEditedProgress(){
        int edited=0;
        for(int i=0;i<mFlagList.size();i++)
            if(mFlagList.get(i).flagContent!="")
                edited++;
        return edited;
    }

}
