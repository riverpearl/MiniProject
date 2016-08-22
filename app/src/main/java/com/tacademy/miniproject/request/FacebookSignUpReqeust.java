package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;

import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Tacademy on 2016-08-22.
 */
public class FacebookSignUpReqeust extends AbstractRequest<UserResult<User>> {

    Request request;

    public FacebookSignUpReqeust(Context context, String username, String email) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment("facebooksignup")
                .build();

        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("email", email)
                .build();

        request = new Request.Builder()
                .url(url)
                .post(body)
                .tag(context)
                .build();
    }

    @Override
    protected Type getType() {
        return new TypeToken<UserResult<User>>(){}.getType();
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
