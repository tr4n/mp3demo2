package com.example.mp3demo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public abstract class BaseRecyclerViewAdapter<V extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<V> {
    private final Context mContext;

    protected BaseRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }
    protected Context getContext(){
        return mContext;
    }
}
