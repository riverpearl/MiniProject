package com.tacademy.miniproject.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tacademy.miniproject.R;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.data.FacebookUser;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.manager.PropertyManager;
import com.tacademy.miniproject.request.FacebookSignUpReqeust;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookSignUpFragment extends Fragment {

    @BindView(R.id.edit_username)
    EditText nameView;

    @BindView(R.id.edit_email)
    EditText emailView;

    FacebookUser user;

    public static final String ARG_FACEBOOK_USER = "facebookUser";

    public static FacebookSignUpFragment newInstance(FacebookUser user) {
        FacebookSignUpFragment f = new FacebookSignUpFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_FACEBOOK_USER, user);
        f.setArguments(b);
        return f;
    }

    public FacebookSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            user = (FacebookUser)getArguments().getSerializable(ARG_FACEBOOK_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facebook_sign_up, container, false);
        ButterKnife.bind(this, view);
        nameView.setText(user.getName());
        emailView.setText(user.getEmail());
        return view;
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUp(View view) {
        String username = nameView.getText().toString();
        String email = emailView.getText().toString();
        
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email)) {
            FacebookSignUpReqeust request = new FacebookSignUpReqeust(getContext(), username, email);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<User>>() {
                @Override
                public void onSuccess(NetworkRequest<UserResult<User>> request, UserResult<User> result) {
                    PropertyManager.getInstance().setFacebookId(user.getId());
                    ((LoginActivity)getActivity()).moveMainActivity();
                }

                @Override
                public void onFail(NetworkRequest<UserResult<User>> request, int errorCode, String errorMessage, Throwable e) {
                    Toast.makeText(getContext(), "sign up fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
