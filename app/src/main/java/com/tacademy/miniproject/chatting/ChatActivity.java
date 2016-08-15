package com.tacademy.miniproject.chatting;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.tacademy.miniproject.R;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.data.ChatContract;
import com.tacademy.miniproject.manager.DBManager;
import com.tacademy.miniproject.widget.ChatAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    public final static String EXTRA_USER = "users";

    @BindView(R.id.rv_list)
    RecyclerView listView;

    @BindView(R.id.group_type)
    RadioGroup typeView;

    @BindView(R.id.edit_input)
    EditText inputView;

    ChatAdapter cAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        user = (User)getIntent().getSerializableExtra(EXTRA_USER);

        cAdapter = new ChatAdapter();
        listView.setAdapter(cAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.btn_send)
    public void onSend(View view) {
        String message = inputView.getText().toString();
        int type = ChatContract.ChatMessage.TYPE_SEND;

        switch (typeView.getCheckedRadioButtonId()) {
            case R.id.radio_send :
                type = ChatContract.ChatMessage.TYPE_SEND;
                break;
            case R.id.radio_receive :
                type = ChatContract.ChatMessage.TYPE_RECEIVE;
                break;
        }

        DBManager.getInstance().addMessage(user, type, message);
        updateMessage();
    }

    private void updateMessage() {
        Cursor c = DBManager.getInstance().getChatMessage(user);
        cAdapter.changeCursor(c);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cAdapter.changeCursor(null);
    }
}
