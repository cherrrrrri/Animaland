package com.example.animaland.SeatFolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;

import java.util.List;

public class RecyclerFlagAdapter extends RecyclerView.Adapter<RecyclerFlagAdapter.InnerHolder> {

    private List<flag> mFlagData;
    private onItemCheckedChangeListener mlistenerOfCheckbox;
    private onItemDeleteListener mlistenerOfDelete;
    private boolean isEdited=false;

    //public RecyclerFlagAdapter(){}

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_todolist,null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.setData(dialog_todo.mFlagList.get(position),position);
    }

    @Override
    public int getItemCount() {
        if(dialog_todo.mFlagList!=null)
            return dialog_todo.mFlagList.size();
        return 0;
    }

    public void setOnCheckedChangeListener(onItemCheckedChangeListener listener) {
        //设置一个监听
        this.mlistenerOfCheckbox =listener;
    }

    public void setOnItemDeleteListener(onItemDeleteListener listener){
        this.mlistenerOfDelete=listener;
    }

    public interface onItemCheckedChangeListener{
        void onItemCheckedChange(int position);
    }

    public interface onItemDeleteListener{
        void onItemDelete(int position);
    }

    public class InnerHolder extends RecyclerView.ViewHolder{

        public CheckBox checkBox;
        private EditText editText;
        private int mposition;
        private ImageView editButton,delete;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);

            //找到控件
            checkBox=itemView.findViewById(R.id.itemTodo_check);
            editText=itemView.findViewById(R.id.itemTodo_plan);
            editButton=itemView.findViewById(R.id.itemTodo_editable);
            delete=itemView.findViewById(R.id.itemTodo_delete);

            makeEdited(isEdited);
            //设置复选框的监听事件
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editText.getText().toString().equals("")){//若目标暂未填写，不允许打钩
                        checkBox.setChecked(false);
                        Toast.makeText(itemView.getContext(), "请先填写小目标~", Toast.LENGTH_SHORT).show();
                    }
                    else{//目标已经填写
                        dialog_todo.mFlagList.get(mposition).state=checkBox.isChecked()?true:false;
                        if(mlistenerOfCheckbox !=null)
                            mlistenerOfCheckbox.onItemCheckedChange(mposition);
                    }
                }
            });

            //设置“编辑”按钮的监听事件
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isEdited=isEdited==true?false:true;
                    makeEdited(isEdited);
                    if(!isEdited){
                        Toast.makeText(view.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        dialog_todo.mFlagList.get(mposition).flagContent=editText.getText().toString();
                    }
                }
            });

            //设置“删除”按钮的监听事件
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistenerOfDelete != null) {
                        mlistenerOfDelete.onItemDelete(mposition);
                    }
                }
            });


        }

        private void makeEdited(boolean isEdited){
            if(isEdited){

                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            }
            else{

                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
            }
        }

        public void setData(flag flag,int position) {
            //设置数据 初始化为空 所以不设置
            this.mposition=position;
            editText.setText(dialog_todo.mFlagList.get(position).flagContent);
            checkBox.setChecked(dialog_todo.mFlagList.get(position).state?true:false);
        }
    }
}
