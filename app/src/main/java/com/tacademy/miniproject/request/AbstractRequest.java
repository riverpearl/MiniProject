package com.tacademy.miniproject.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.ResponseCode;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.manager.NetworkRequest;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;

/**
 * Created by Tacademy on 2016-08-09.
 */
public abstract class AbstractRequest<T> extends NetworkRequest<T> {
    protected HttpUrl.Builder getBaseUrlBuilder() {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("https");
        builder.host("pearlsminiweb.appspot.com");
        return builder;
    }

    @Override
    protected T parse(ResponseBody body) throws IOException {
        String text = body.string();
        Gson gson = new Gson();
        ResponseCode code = gson.fromJson(text, ResponseCode.class);

        switch (code.getCode()) {
            case 1 :
                T result = gson.fromJson(text, getType());
                return result;
            case 2 :
            default :
                Type type = new TypeToken<UserResult<String>>(){}.getType();
                UserResult<String> fail = gson.fromJson(text, type);
                throw new IOException(fail.getResult());
        }
    }

    protected abstract Type getType();
}
