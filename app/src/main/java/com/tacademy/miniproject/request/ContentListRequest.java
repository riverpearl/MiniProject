package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.data.ContentData;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by Tacademy on 2016-08-16.
 */
public class ContentListRequest extends AbstractRequest<UserResult<List<ContentData>>> {
    Request request;

    public ContentListRequest(Context context) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment("contents")
                .build();

        request = new Request.Builder()
                .url(url)
                .build();
    }

    @Override
    protected Type getType() {
        return new TypeToken<UserResult<List<ContentData>>>(){}.getType();
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
