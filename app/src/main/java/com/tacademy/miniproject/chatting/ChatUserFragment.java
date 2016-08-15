package com.tacademy.miniproject.chatting;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.tacademy.miniproject.R;
import com.tacademy.miniproject.data.ChatContract;
import com.tacademy.miniproject.manager.DBManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatUserFragment extends Fragment {

    @BindView(R.id.listView)
    ListView listView;

    SimpleCursorAdapter cAdapter;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] from = { ChatContract.ChatUser.COLUMN_NAME, ChatContract.ChatUser.COLUMN_EMAIL, ChatContract.ChatMessage.COLUMN_MESSAGE };
        int[] to = { R.id.text_name, R.id.text_email, R.id.text_last_message };
        cAdapter = new SimpleCursorAdapter(getContext(), R.layout.view_chat_user, null, from, to, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_user, container, false);
        ButterKnife.bind(this, view);
        listView.setAdapter(cAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Cursor c = DBManager.getInstance().getChatUser();
        cAdapter.changeCursor(c);
    }

    @Override
    public void onStop() {
        super.onStop();
        cAdapter.changeCursor(null);
    }
}
