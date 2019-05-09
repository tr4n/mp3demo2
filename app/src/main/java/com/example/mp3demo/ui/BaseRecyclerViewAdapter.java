package com.example.mp3demo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private List<T> mItems;

    protected BaseRecyclerViewAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    protected T getItem(int position) {
        return (position >= 0 && position < getItemCount()) ? mItems.get(position) : null;
    }

    public void updateData(List<T> newItems) {
        mItems.clear();
        mItems.addAll(newItems);
        notifyDataSetChanged();
    }
}
