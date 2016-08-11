package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;

import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by Tacademy on 2016-08-10.
 */
public class LogOutRequest extends AbstractRequest<UserResult<String>> {

    Request request;

    public LogOutRequest(Context context) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment("logout")
                .build();

        request = new Request.Builder()
                .url(url)
                .tag(context)
                .build();
    }

    @Override
    protected Type getType() {
        return new TypeToken<UserResult<String>>(){}.getType();
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
