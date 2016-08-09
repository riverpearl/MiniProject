package com.tacademy.miniproject.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tacademy.miniproject.MainActivity;
import com.tacademy.miniproject.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SignInFragment())
                    .commit();
        }

    }

    public void changeSignUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignUpFragment())
                .commit();
    }

    public void moveMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
