package com.tacademy.miniproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.chatting.ChatActivity;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.request.FriendListRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.listView)
    ListView listView;

    ArrayAdapter<User> uAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uAdapter = new ArrayAdapter<User>(getContext(), android.R.layout.simple_list_item_1);

        FriendListRequest request = new FriendListRequest(getContext());
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<List<User>>>() {
            @Override
            public void onSuccess(NetworkRequest<UserResult<List<User>>> request, UserResult<List<User>> result) {
                List<User> users = result.getResult();
                uAdapter.addAll(users);
            }

            @Override
            public void onFail(NetworkRequest<UserResult<List<User>>> request, int errorCode, String errorMessage, Throwable e) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        listView.setAdapter(uAdapter);
        return view;
    }

    @OnItemClick(R.id.listView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        User user = (User)listView.getItemAtPosition(position);
        intent.putExtra(ChatActivity.EXTRA_USER, user);
        startActivity(intent);
    }

}
