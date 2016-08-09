package com.tacademy.miniproject.autodata;

public class UserResult<T> {
    private T result;
    private int code;

    public T getResult() {
        return this.result;
    }

    public int getCode() {
        return this.code;
    }
}
