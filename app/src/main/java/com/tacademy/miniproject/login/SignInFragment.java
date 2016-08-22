package com.tacademy.miniproject.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tacademy.miniproject.R;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.data.FacebookUser;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.manager.PropertyManager;
import com.tacademy.miniproject.request.FacebookLoginRequest;
import com.tacademy.miniproject.request.SignInRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.edit_email)
    EditText emailView;

    @BindView(R.id.edit_password)
    EditText passwordView;

    @BindView(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        PropertyManager.getInstance().setFacebookId("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                processAfterFacebookLogin();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void processAfterFacebookLogin() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            String token = accessToken.getToken();
            String regid = PropertyManager.getInstance().getRegId();
            FacebookLoginRequest request = new FacebookLoginRequest(getContext(), token, regid);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<Object>>() {
                @Override
                public void onSuccess(NetworkRequest<UserResult<Object>> request, UserResult<Object> result) {
                    if (result.getCode() == 1) {
                        String facebookId = accessToken.getUserId();
                        PropertyManager.getInstance().setFacebookId(facebookId);
                        ((LoginActivity)getActivity()).moveMainActivity();
                    }
                    else if (result.getCode() == 3) {
                        FacebookUser user = (FacebookUser)result.getResult();
                        ((LoginActivity)getActivity()).changeFacebookSignUp(user);
                    }
                }

                @Override
                public void onFail(NetworkRequest<UserResult<Object>> request, int errorCode, String errorMessage, Throwable e) {
                    Toast.makeText(getContext(), "login fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_login)
    public void onLogin(View view) {
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        String regid = PropertyManager.getInstance().getRegId();
        SignInRequest request = new SignInRequest(getContext(), email, password, regid);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<User>>() {
            @Override
            public void onSuccess(NetworkRequest<UserResult<User>> request, UserResult<User> result) {
                User user = result.getResult();
                PropertyManager.getInstance().setEmail(email);
                PropertyManager.getInstance().setPassword(password);
                PropertyManager.getInstance().setRegId("id");
                Toast.makeText(getContext(), "user id : " + user.getId(), Toast.LENGTH_SHORT).show();
                ((LoginActivity)getActivity()).moveMainActivity();
            }

            @Override
            public void onFail(NetworkRequest<UserResult<User>> request, int errorCode, String errorMessage, Throwable e) {
                Toast.makeText(getContext(), "error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_signup)
    public void onSignUp() {
        ((LoginActivity)getActivity()).changeSignUp();
    }
}
