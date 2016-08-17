package com.tacademy.miniproject.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tacademy.miniproject.R;
import com.tacademy.miniproject.data.ContentData;
import com.tacademy.miniproject.view.ContentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-16.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentViewHolder> {

    List<ContentData> items = new ArrayList<>();

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ContentData> l) {
        items.addAll(l);
        notifyDataSetChanged();
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_content, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        holder.setContent(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
