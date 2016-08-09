package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.ResponseCode;
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
public class SignInRequest extends AbstractRequest<UserResult<User>> {

    Request request;

    public SignInRequest(Context context, String email, String password, String registrationId) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment("signin")
                .build();

        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("registrationId", registrationId)
                .build();

        request = new Request.Builder()
                .url(url)
                .post(body)
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
