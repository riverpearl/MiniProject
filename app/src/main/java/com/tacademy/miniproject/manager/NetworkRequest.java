package com.tacademy.miniproject.manager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Tacademy on 2016-08-09.
 */
public abstract class NetworkRequest<T> implements Callback {

    public abstract Request getRequest();
    protected abstract T parse(ResponseBody body) throws IOException;

    NetworkManager.OnResultListener<T> rListener;
    public void setOnResultListener(NetworkManager.OnResultListener<T> listener) {
        rListener = listener;
    }

    Call call;
    void process(OkHttpClient client) {
        Request request = getRequest();
        call = client.newCall(request);
        call.enqueue(this); // 라이브러리를 보면 enqueue에서 parsing 해주도록 돼있음
    }

    T result;

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful())
            sendSuccess(parse(response.body()));
        else sendError(response.code(), response.message(), null);
    }

    private void sendSuccess(T result) {
        this.result = result;
        NetworkManager.getInstance().sendSuccess(this);
    }

    void sendSuccess() {
        if (rListener != null)
            rListener.onSuccess(this, result);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        sendError(-1, e.getMessage(), e);
    }

    int code;
    String errorMessage;
    Throwable exception;

    protected  void sendError(int code, String errorMessage, Throwable exception) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.exception = exception;
        NetworkManager.getInstance().sendFail(this);
    }

    void sendFail() {
        if (rListener != null)
            rListener.onFail(this, code, errorMessage, exception);
    }
}
