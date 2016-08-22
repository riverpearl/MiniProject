package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.data.FacebookUser;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;

import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Tacademy on 2016-08-22.
 */
public class FacebookLoginRequest extends AbstractRequest<UserResult<Object>> {

    Request request;

    public FacebookLoginRequest(Context context, String token, String regId) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment("facebooksignin")
                .build();

        RequestBody body = new FormBody.Builder()
                .add("access_token", token)
                .add("registrationId", regId)
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
    protected Type getType(int code) {
        if (code == 3)
            return new TypeToken<UserResult<FacebookUser>>(){}.getType();

        return null;
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
