package com.example.animaland.oral;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;
import com.example.animaland.School.Language;

import java.util.ArrayList;
import java.util.List;

import static com.example.animaland.School.Student.tagNames;

public class OralGridViewAdapter extends RecyclerView.Adapter {


    private int num;
    private onGridItemClickListener monItemClickListener;
    private List<Language> checkedStates;
    public GirdItemViewHolder mViewHolder;

    public OralGridViewAdapter(Context context, int num) {
        this.num=num;
        checkedStates=new ArrayList<>();
    }

    public void setOnItemClickListener(onGridItemClickListener listener) {
        //设置一个监听
        this.monItemClickListener=listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mViewHolder= new GirdItemViewHolder(viewGroup.getContext(), viewGroup);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((GirdItemViewHolder) viewHolder).setData(i);
    }

    @Override
    public int getItemCount() {
        return num;
    }

    public List<Language> getCheckedStates(){
        return checkedStates;
    }

    public interface onGridItemClickListener {
        void onGridItemClick(int position);
    }

    public class GirdItemViewHolder extends RecyclerView.ViewHolder {

        public CheckedTextView ctv;
        private int mGridposition;
        private Language mLanguage;
        private Language[] languageNames={Language.ENGLISH,Language.FRENCH,Language.JAPANESE,Language.OTHERS};

        public GirdItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false));
            //找到控件
            ctv = itemView.findViewById(R.id.ctv);

            //设置监听
            ctv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctv.toggle();
                    mLanguage=languageNames[mGridposition];
                    checkedStates.set(mGridposition,ctv.isChecked()?mLanguage:null);//更新状态

                    if(monItemClickListener!=null)
                        monItemClickListener.onGridItemClick(mGridposition);
                }
            });

        }

        public void setData(int position){
            this.mGridposition =position;

            ctv.setText(tagNames[position+1]);
            checkedStates.add(null);
        }
    }
}
