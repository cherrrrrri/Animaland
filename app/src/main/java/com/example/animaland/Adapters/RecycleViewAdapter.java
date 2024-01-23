package com.example.animaland.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;
import com.example.animaland.School.Auditorium_student;
import com.example.animaland.School.Auditorium_teacher;
import com.example.animaland.School.talkModel;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>{
    private ArrayList<talkModel> talks;
    private Context context;


    public RecycleViewAdapter(Context context, ArrayList<talkModel> talks) {
        this.context=context;
        this.talks=talks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 根据不同的组件类型加载不同类型的布局文件
        switch (Auditorium_student.getPos()) {
            case 0:
                View view = LayoutInflater.from(this.context).inflate(R.layout.item5,parent,false);
                if(Auditorium_teacher.isRunning==1&&Auditorium_teacher.state==1){
                    View view4 = LayoutInflater.from(this.context).inflate(R.layout.item7,parent,false);
                    return new MyViewHolder(view4);}
                else if(Auditorium_teacher.isRunning==1&&Auditorium_teacher.state==2){
                    View view7 = LayoutInflater.from(this.context).inflate(R.layout.item8,parent,false);
                    return new MyViewHolder(view7);
                }
                else
                    return new MyViewHolder(view);
            case 1:
                View view2 = LayoutInflater.from(this.context).inflate(R.layout.item4,parent,false);
                return new MyViewHolder(view2);

            case 2:
                View view3 = LayoutInflater.from(this.context).inflate(R.layout.item6,parent,false);
                return new MyViewHolder(view3);

            case 3:
                View view4 = LayoutInflater.from(this.context).inflate(R.layout.items,parent,false);
                return new MyViewHolder(view4);

            case 4:
                View view5 = LayoutInflater.from(this.context).inflate(R.layout.item3,parent,false);
                return new MyViewHolder(view5);

            case 5:
                View view6 = LayoutInflater.from(this.context).inflate(R.layout.items2,parent,false);
                return new MyViewHolder(view6);

        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(talks.get(position).getTalk());
    }



    @Override
    public int getItemCount() {
        return this.talks.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);
        }
    }
}
