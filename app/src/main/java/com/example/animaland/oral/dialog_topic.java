package com.example.animaland.oral;


import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dialog_topic extends Dialog {

    //private ImageView refresh;
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<News> mNewsList = new ArrayList<>();

    private View.OnClickListener ListenerOfRefresh;
    public dialog_topic setRefresh(View.OnClickListener listener) {
        this.ListenerOfRefresh=listener;
        return this;
    }

    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){
                WindowManager m=getWindow().getWindowManager();
                Display d=m.getDefaultDisplay();
                WindowManager.LayoutParams p=getWindow().getAttributes();
                Point size=new Point();
                d.getSize(size);
                p.width=(int)(size.x*0.6);
                p.height=(int)(size.y*0.8);
                getWindow().setGravity(CENTER);//居中
                getWindow().setAttributes(p);

            }
        }
    };

    private DatabaseHelper db = new DatabaseHelper();

    public dialog_topic(@NonNull Context context, int themeID) {
        super(context, themeID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.topic);



        //找到控件
      //  refresh=findViewById(R.id.refresh);
        mRecyclerView = findViewById(R.id.recycleView);

//生成topic数组
      //  Runnable runnable = new Runnable() {
               Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    for(int i = 0;i<10;i++){
                        mNewsList.add(db.getTopic().get(i));
                     //   System.out.println(mNewsList.get(i).content);
                    }

                  //  System.out.println("传入数据");
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
      //  cachedThreadPool.execute(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        // 构造一些数据 可以从雅思口语题库里找

     //   System.out.println("显示");
        //设置dialog显示位置
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);
        p.height=(int)(size.y*0.8);
        getWindow().setGravity(CENTER);//居中
        getWindow().setAttributes(p);

   /*     refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("刷新");
                        try {
                            for(int i = 0;i<10;i++){
                                mNewsList.clear();
                                mNewsList.add(db.getTopic().get(i));
                              //  System.out.println(mNewsList.get(i).content);
                            }
                            handler.sendEmptyMessage(0);
                            System.out.println("传入数据");
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
        });*/

    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.recy_topic, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            News news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            holder.mTitleContent.setText(news.content);
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        TextView mTitleContent;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView);
            mTitleContent = itemView.findViewById(R.id.textView2);
        }
    }

}
