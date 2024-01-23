package com.example.animaland.School;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;

import java.util.List;

public class GridRecycleAdapter extends RecyclerView.Adapter {

    Context context;
    List<classroom> list;
    private onGridItemClickListener monItemClickListener;

    public GridRecycleAdapter(Context context,List<classroom> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<classroom> list){
        this.list = list;
    }

    public void setOnItemClickListener(onGridItemClickListener listener) {
        //设置一个监听
        this.monItemClickListener=listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GirdItemViewHolder(context,viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        GirdItemViewHolder vh = (GirdItemViewHolder) viewHolder;
        ((GirdItemViewHolder) viewHolder).setData(list.get(i), i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onGridItemClickListener {
        void onGridItemClick(int position);
    }

    public class GirdItemViewHolder extends RecyclerView.ViewHolder {

        public TextView roomName,member,teacherName,roomIntroduction;
        public ImageView roomCover,teacherPhoto;
        private int mGridposition;

        public GirdItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.item_classroom, parent, false));
            //找到控件
            roomName = itemView.findViewById(R.id.classroom_name);
            member=itemView.findViewById(R.id.classroom_member);
            teacherName=itemView.findViewById(R.id.classroom_teacher);
            roomIntroduction=itemView.findViewById(R.id.classroom_introduction);
            roomCover=itemView.findViewById(R.id.classroom_cover);
            teacherPhoto=itemView.findViewById(R.id.classroom_teacher_photo);

            //设置跑马灯效果
            roomName.requestFocus();

            //设置监听
            roomCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemClickListener != null) {
                        monItemClickListener.onGridItemClick(mGridposition);
                    }
                }
            });

        }

        public void setData(classroom classroom,int position){
            this.mGridposition =position;

            roomName.setText(classroom.roomName);
            member.setText(classroom.roomMember+"");
            roomIntroduction.setText(classroom.roomIntroduction);
            teacherName.setText(classroom.instructor.name);
            roomCover.setImageResource(classroom.photo);
            teacherPhoto.setImageResource(classroom.instructor.isIdentified?R.drawable.teacher_identified:R.drawable.teacher);
        }
    }
}
