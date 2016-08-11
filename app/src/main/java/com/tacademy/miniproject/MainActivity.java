package com.tacademy.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.login.LoginActivity;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.manager.PropertyManager;
import com.tacademy.miniproject.request.FriendListRequest;
import com.tacademy.miniproject.request.LogOutRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_friend)
    ListView friendsView;

    ArrayAdapter<User> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1);
        friendsView.setAdapter(mAdapter);

        FriendListRequest request = new FriendListRequest(this);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<List<User>>>() {
            @Override
            public void onSuccess(NetworkRequest<UserResult<List<User>>> request, UserResult<List<User>> result) {
                List<User> users = result.getResult();
                mAdapter.addAll(users);
            }

            @Override
            public void onFail(NetworkRequest<UserResult<List<User>>> request, int errorCode, String errorMessage, Throwable e) {
                Toast.makeText(MainActivity.this, "error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {

            LogOutRequest request = new LogOutRequest(this);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<String>>() {
                @Override
                public void onSuccess(NetworkRequest<UserResult<String>> request, UserResult<String> result) {
                    PropertyManager.getInstance().setEmail("");
                    PropertyManager.getInstance().setPassword("");
                    PropertyManager.getInstance().setRegId("");

                    // 지금까지 띄워놨던 activity들 다 스택에서 제거하고 로그인창 띄우기
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFail(NetworkRequest<UserResult<String>> request, int errorCode, String errorMessage, Throwable e) {
                    Toast.makeText(MainActivity.this, "error : " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
