package com.example.animaland.School;

import static com.example.animaland.School.Student.tagNames;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;

import java.util.List;

public class LinerRecycleAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<List<classroom>> mList;
    int recycleHeight;
    GridRecycleAdapter mGridAdapter;
    private int mLinePosition;
    private onLineItemClickListener monMoreClickListener;
    private GridRecycleAdapter.onGridItemClickListener monClassroomClickListener;

    public LinerRecycleAdapter(Context context, List<List<classroom>> list,int recycleHeight) {
        this.mContext = context;
        this.mList = list;
        this.recycleHeight= recycleHeight;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinerItemViewHolder(mContext,viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LinerItemViewHolder vh = (LinerItemViewHolder) viewHolder;

        ViewGroup.LayoutParams layoutParams = vh.relativeLayout.getLayoutParams();

        /**
         * 点击tablayout最后一项时 如果类目不足一页，
         * 调整此item为recyclerView的高度
         */
        if (i == mList.size() - 1){
            layoutParams.height = recycleHeight;
        }else {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        vh.relativeLayout.setLayoutParams(layoutParams);

        /**
         * 类目的数量过少，高度不足recyclerView高度一半时会和tablayout造成bug
         * 可根据自己的实际情况进行调整
         * 这个方法有点low ，如有更好的办法，请替换

        if (mList.get(i)!= null && mList.get(i).size() > 3){
            vh.blockView.setVisibility(View.GONE);
        }else {
            vh.blockView.setVisibility(View.VISIBLE);
        }
         */

        vh.setData(i);
        vh.tag.setText(tagNames[i]);
        GridLayoutManager glm = new GridLayoutManager(mContext,3);
        vh.recyclerView.setLayoutManager(glm);
        mGridAdapter = new GridRecycleAdapter(mContext,mList.get(i));
        vh.recyclerView.setAdapter(mGridAdapter);
        if(monClassroomClickListener !=null)
            mGridAdapter.setOnItemClickListener(monClassroomClickListener);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface onLineItemClickListener {
        void onLineItemClick(int position);
    }

    public void setOnMoreClickListeners(onLineItemClickListener listener) {
        //设置监听
        this.monMoreClickListener =listener;
    }

    public void setOnClassroomClickListeners(GridRecycleAdapter.onGridItemClickListener listener){
        this.monClassroomClickListener =listener;
    }

    public class LinerItemViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;
        public TextView tag,more;
        public RecyclerView recyclerView;
        public View blockView;

        public LinerItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.item_classrooms, parent, false));

            //找到控件
            relativeLayout=itemView.findViewById(R.id.rl_classesWithTags);
            tag = itemView.findViewById(R.id.tag);
            more=itemView.findViewById(R.id.more);
            recyclerView = itemView.findViewById(R.id.rv_classesWithTag);
            blockView=itemView.findViewById(R.id.block_view);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monMoreClickListener != null) {
                        monMoreClickListener.onLineItemClick(mLinePosition);
                    }
                }
            });
        }

        private void setData(int position){
            mLinePosition =position;
        }
    }
}
