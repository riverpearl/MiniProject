package com.tacademy.miniproject.widget;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tacademy.miniproject.R;
import com.tacademy.miniproject.data.ChatContract;
import com.tacademy.miniproject.view.ReceiveViewHolder;
import com.tacademy.miniproject.view.SendViewHolder;

/**
 * Created by Jinju on 2016-08-14.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Cursor cursor;

    public void changeCursor(Cursor c) {
        if (cursor != null)
            cursor.close();

        cursor = c;
        notifyDataSetChanged();
    }

    private final static int VIEW_TYPE_SEND = 1;
    private final static int VIEW_TYPE_RECEIVE = 2;

    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        int type = cursor.getInt(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_TYPE));

        switch (type) {
            case ChatContract.ChatMessage.TYPE_SEND :
                return VIEW_TYPE_SEND;
            case ChatContract.ChatMessage.TYPE_RECEIVE :
                return VIEW_TYPE_RECEIVE;
        }

        throw new IllegalArgumentException("invalid type");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SEND :
                View sendView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_send, parent, false);
                return new SendViewHolder(sendView);
            case VIEW_TYPE_RECEIVE :
                View receiveView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_receive, parent, false);
                return new ReceiveViewHolder(receiveView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        //int type = cursor.getInt(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_TYPE));
        String message = cursor.getString(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_MESSAGE));

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SEND :
                SendViewHolder svh = (SendViewHolder)holder;
                svh.setMessage(message);
                break;
            case VIEW_TYPE_RECEIVE :
                ReceiveViewHolder rvh = (ReceiveViewHolder)holder;
                rvh.setMessage(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }
}
