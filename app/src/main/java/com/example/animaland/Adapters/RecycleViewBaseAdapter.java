package com.example.animaland.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.PassThroughButton;
import com.example.animaland.R;
import com.example.animaland.room.room;

import java.util.List;

public abstract class RecycleViewBaseAdapter extends RecyclerView.Adapter<RecycleViewBaseAdapter.InnerHolder> {

    private final List<room> mdata;
    private onItemClickListener monItemClickListener;
    protected int theme;
    public static final int THEME_TREEHOUSE=1;
    public static final int THEME_CAVE=2;
    public static final int THEME_SHELL=3;

    public RecycleViewBaseAdapter(List<room> roomData,int theme){
        this.mdata=roomData;
        this.theme=theme;
    }

    @NonNull
    @Override
    public RecycleViewBaseAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=getSubView(parent,viewType);
        return new InnerHolder(view,theme);
    }

    protected abstract View getSubView(ViewGroup parent,int ViewType);

    @Override
    public void onBindViewHolder(@NonNull RecycleViewBaseAdapter.InnerHolder holder, int position) {
        //绑定holder
        holder.setData(mdata.get(position),position);
    }

    @Override
    public int getItemCount() {
        if(mdata!=null)
            return mdata.size();
        return 0;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        //设置一个监听
        this.monItemClickListener=listener;
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private TextView idView;
        private TextView roomNameView;
        private TextView memberNumberView;
        private PassThroughButton imageButton;
        private int mposition;

        public InnerHolder(@NonNull View itemView,int theme) {
            super(itemView);

            //找到条目的控件
            idView= itemView.findViewById(R.id.room_id);//房间ID
            roomNameView= itemView.findViewById(R.id.roomshow_name);//房间名字
            memberNumberView= itemView.findViewById(R.id.room_memberNumber);//房间人数
            imageButton = itemView.findViewById(R.id.room_background);

            idView.bringToFront();
            roomNameView.bringToFront();
            memberNumberView.bringToFront();
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemClickListener != null) {
                        monItemClickListener.onItemClick(mposition);
                    }
                }
            });
        }//

        public void setData(room room,int position) {

            this.mposition=position;
            switch (theme){
                case THEME_TREEHOUSE:
                    switch(position%2){
                        case 0:
                            imageButton.setBackgroundResource(R.drawable.room1_1);
                            break;
                        case 1:
                            imageButton.setBackgroundResource(R.drawable.room1_2);
                            break;
                    }
                    break;
                case THEME_CAVE:
                    idView.setTextColor(-1120086);
                    roomNameView.setTextColor(-1120086);
                    memberNumberView.setTextColor(-1120086);
                    switch(position%3){
                        case 0:
                            imageButton.setBackgroundResource(R.drawable.room2_1);
                            break;
                        case 1:
                            imageButton.setBackgroundResource(R.drawable.room2_2);
                            break;
                        case 2:
                            imageButton.setBackgroundResource(R.drawable.room2_3);
                            break;
                    }
                    break;
                case THEME_SHELL:
                    switch(position%3){
                        case 0:
                            imageButton.setBackgroundResource(R.drawable.room3_1);
                            break;
                        case 1:
                            imageButton.setBackgroundResource(R.drawable.room3_2);
                            break;
                        case 2:
                            imageButton.setBackgroundResource(R.drawable.room3_3);
                            break;
                    }
                    break;
            }
            idView.setText("ID: "+room.id);
            roomNameView.setText(room.roomName+"");
            memberNumberView.setText(room.roomMember+"/4");
        }
    }
}