package com.tacademy.miniproject.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tacademy.miniproject.R;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.manager.PropertyManager;
import com.tacademy.miniproject.request.SignUpRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.edit_name)
    EditText nameView;

    @BindView(R.id.edit_email)
    EditText emailView;

    @BindView(R.id.edit_password)
    EditText passwordView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_signup)
    public void onSignUp() {
        String name = nameView.getText().toString();
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        SignUpRequest request = new SignUpRequest(getContext(), name, password, email, "id");
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<User>>() {
            @Override
            public void onSuccess(NetworkRequest<UserResult<User>> request, UserResult<User> result) {
                User user = result.getResult();
                PropertyManager.getInstance().setEmail(email);
                PropertyManager.getInstance().setPassword(password);
                PropertyManager.getInstance().setRegId("id");
                Toast.makeText(getContext(), "user id :" + user.getId(), Toast.LENGTH_SHORT).show();
                ((LoginActivity)getActivity()).moveMainActivity();
            }

            @Override
            public void onFail(NetworkRequest<UserResult<User>> request, int errorCode, String errorMessage, Throwable e) {
                Toast.makeText(getContext(), "message : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
