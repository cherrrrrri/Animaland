package com.example.animaland.Adapters;

import android.view.View;
import android.view.ViewGroup;

import com.example.animaland.R;
import com.example.animaland.room.room;

import java.util.List;

public class MoreTypeBeanAdapter extends RecycleViewBaseAdapter {

    private final int TYPE_BOTTOM=0;
    private final int TYPE_TOP=1;
    private final int TYPE_CENTER=2;
    private final int TYPE_TREE1=3;
    private final int TYPE_TREE2=4;
    private List<room> mData;

    public MoreTypeBeanAdapter(List<room> roomData,int theme) {
        super(roomData,theme);
        mData=roomData;
    }


    @Override
    protected View getSubView(ViewGroup parent, int ViewType) {
        View view;
        switch (ViewType){
            case TYPE_BOTTOM:
                view=View.inflate(parent.getContext(),R.layout.item_room,null);
                return view;
            case TYPE_TOP:
                view=View.inflate(parent.getContext(),R.layout.item_room1,null);
                return view;
            case TYPE_CENTER:
                view=View.inflate(parent.getContext(),R.layout.item_room2,null);
                return view;
            case TYPE_TREE1:
                view=View.inflate(parent.getContext(),R.layout.item_room_tree1,null);
                return view;
            case TYPE_TREE2:
                view=View.inflate(parent.getContext(),R.layout.item_room_tree2,null);
                return view;
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        room r=mData.get(position);
        return r.type;
    }



}
