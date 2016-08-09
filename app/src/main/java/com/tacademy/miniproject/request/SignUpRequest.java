package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Tacademy on 2016-08-09.
 */
public class SignUpRequest  extends AbstractRequest<UserResult<User>> {

    Request request;

    public SignUpRequest(Context context, String username, String password, String email, String registrationId) {
        HttpUrl.Builder builder = getBaseUrlBuilder()
                .addPathSegment("signup");

        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("email", email)
                .add("registrationId", registrationId)
                .build();

        request = new Request.Builder()
                .url(builder.build())
                .post(body)
                .tag(context)
                .build();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    protected Type getType() {
        return new TypeToken<UserResult<User>>(){}.getType();
    }
}
