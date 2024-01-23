package com.example.animaland.oral;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;

import java.util.List;

public class OralRVAdapter extends RecyclerView.Adapter{

    private List<oralroom> oralRoomList;
    private onOralRoomClickListener onOralRoomClickListener;

    public OralRVAdapter(List<oralroom> oralRoomList) {
        this.oralRoomList = oralRoomList;
    }

    @NonNull
    @Override
    public oralRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_oralroom,null);
        return new oralRoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        oralRoomHolder oralRoomHolder= (OralRVAdapter.oralRoomHolder) holder;
        oralRoomHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return oralRoomList.size();
    }

    public interface onOralRoomClickListener{
        void onOralRoomClick(int position);
    }

    public void setOnOralRoomClickListener(onOralRoomClickListener listener){
        onOralRoomClickListener=listener;
    }

    private class oralRoomHolder extends RecyclerView.ViewHolder{

        private oralroom oralroom;
        private oralUser oraluser;
        private String mother="",good="",learn="";
        private ImageView photo;
        private TextView name,introduction,ID,tag,motherLanguage,LanguageGoodAt,LanguageWantToLearn;
        private Button enter;
        private LinearLayout ll_learn;
        private int mPosition;

        public oralRoomHolder(@NonNull View itemView) {
            super(itemView);

            //找到控件
            photo=itemView.findViewById(R.id.photo);
            name=itemView.findViewById(R.id.name);
            introduction=itemView.findViewById(R.id.introduction);
            ID=itemView.findViewById(R.id.ID);
            tag=itemView.findViewById(R.id.type);
            motherLanguage=itemView.findViewById(R.id.motherLanguage);
            LanguageGoodAt=itemView.findViewById(R.id.LanguageGoodAt);
            LanguageWantToLearn=itemView.findViewById(R.id.LanguageWantToLearn);
            enter=itemView.findViewById(R.id.enter);
            ll_learn=itemView.findViewById(R.id.ll_LanguageWantToLearn);

            //设置监听
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onOralRoomClickListener != null) {
                        onOralRoomClickListener.onOralRoomClick(mPosition);
                    }
                }
            });

        }

        private void setData(int position){
            mPosition=position;
            oralroom=oralRoomList.get(position);
            oraluser=oralroom.user;

            for(int i=0;i<oraluser.getMotherLanguage().length;i++)
                if(oraluser.getMotherLanguage()[i]!=null)
                    mother+=oraluser.getMotherLanguage()[i].getText()+" ";
            for(int i=0;i<oraluser.getLanguagesGoodAt().length;i++)
                if(oraluser.getLanguagesGoodAt()[i]!=null)
                    good+=oraluser.getLanguagesGoodAt()[i].getText()+" ";
            for(int i=0;i<oraluser.getLanguagesWantToLearn().length;i++)
                if(oraluser.getLanguagesWantToLearn()[i]!=null)
                    learn+=oraluser.getLanguagesWantToLearn()[i].getText()+" ";

            name.setText(oralroom.teacherName);
            introduction.setText(oralroom.roomIntroduction);
            ID.setText(oralroom.id);
            tag.setText(oralroom.isCourse?"课程":"聊天");
            motherLanguage.setText(mother);
            LanguageGoodAt.setText(good);
            LanguageWantToLearn.setText(learn);
            ll_learn.setVisibility(oralroom.isCourse?View.INVISIBLE:View.VISIBLE);

        }
    }

    public void setOralRoomList(List<oralroom> oralRoomList) {
        this.oralRoomList = oralRoomList;
    }

}
