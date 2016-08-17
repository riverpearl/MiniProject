package com.tacademy.miniproject.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.data.ContentData;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Tacademy on 2016-08-16.
 */
public class UploadRequest extends AbstractRequest<UserResult<ContentData>> {
    Request request;
    MediaType jpeg = MediaType.parse("image/jpeg");

    public UploadRequest(Context context, String text, File file) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment("upload")
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", text);

        if (file != null) {
            builder.addFormDataPart("myFile", file.getName(), RequestBody.create(jpeg, file));
        }

        RequestBody body = builder.build();

        request = new Request.Builder()
                .url(url)
                .post(body)
                .tag(context)
                .build();
    }

    @Override
    protected Type getType() {
        return new TypeToken<UserResult<ContentData>>(){}.getType();
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
